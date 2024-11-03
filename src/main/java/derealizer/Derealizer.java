package derealizer;

import static java.lang.reflect.Modifier.isAbstract;

import derealizer.annotation.Derealizable;

/**
 * Serializes and deserializes subclasses.
 */
public final class Derealizer {

	/**
	 * Serializes the object.
	 *
	 * @param obj The object to serialize.
	 * @param enumClass
	 * @return The serialized bytes.
	 */
	public static byte[] serialize(Serializable obj, Class<? extends DerealizerEnum> enumClass) {
		SerializationWriter writer = new SerializationWriter();
		recursiveWrite(obj, writer, enumClass);
		return writer.toByteArray();
	}

	/**
	 * Deserializes the object.
	 *
	 * @param bytes The bytes to deserialize.
	 * @param enumClass
	 * @return The deserialized object.
	 */
	public static Serializable deserialize(byte[] bytes, Class<? extends DerealizerEnum> enumClass) {
		return recursiveRead(new SerializationReader(bytes), enumClass);
	}

	/**
	 * Gets the first class in the hierarchy above that has a {@link Derealizable} annotation.
	 */
	public static Class<?> getDerealizableHeirarchyClass(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.isAnnotationPresent(Derealizable.class)) {
			return clazz;
		}
		return getDerealizableHeirarchyClass(clazz.getSuperclass());
	}

	/**
	 * Recursively writes the object to the writer.
	 *
	 * @param obj The object to write.
	 * @param writer
	 * @param enumClass
	 */
	public static void recursiveWrite(Serializable obj, SerializationWriter writer, Class<? extends DerealizerEnum> enumClass) {
		DerealizerEnum[] enumConstants = enumClass.getEnumConstants();
		for (int i = 0; i < enumConstants.length; i++) {
			DerealizerEnum enumVal = enumConstants[i];
			if (!enumVal.objClass().isAssignableFrom(obj.getClass())) {
				continue;
			}
			// Calculate the minimum number of bits needed to represent values.length
			int numBitsToWrite = 32 - Integer.numberOfLeadingZeros(enumClass.getEnumConstants().length);
			writer.consume(i, numBitsToWrite);
			// Get the Deserializer from cache
			if (enumVal.derealizerEnum() == null) {
				// obj is not abstract
				obj.write(writer);
			} else {
				// obj is abstract
				Class<? extends DerealizerEnum> childEnumClass = enumVal.derealizerEnum();
				recursiveWrite(obj, writer, childEnumClass);
			}
			return;
		}
	}

	/**
	 * Recursively reads the object from the reader.
	 *
	 * @param reader
	 * @param enumClass
	 * @return
	 */
	public static Serializable recursiveRead(SerializationReader reader, Class<? extends DerealizerEnum> enumClass) {
		DerealizerEnum[] values = enumClass.getEnumConstants();
		// Calculate the minimum number of bits needed to represent values.length
		int numBitsToRead = 32 - Integer.numberOfLeadingZeros(values.length);
		DerealizerEnum enumValue = values[reader.readIntN(numBitsToRead)];
		Class<? extends DerealizerEnum> childEnumClass = enumValue.derealizerEnum();
		if (isAbstract(enumValue.objClass().getModifiers())) {
			return recursiveRead(reader, childEnumClass);
		} else {
			try {
				Serializable instance = enumValue.objClass().getConstructor().newInstance();
				instance.read(reader);
				return instance;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
