package derealizer;

import static java.lang.reflect.Modifier.isAbstract;

/**
 * Serializes and deserializes subclasses.
 */
public final class Derealizer {

	public static Derealizable deserialize(byte[] bytes, Class<? extends DerealizerEnum> enumClass) {
		return recursiveRead(new SerializationReader(bytes), enumClass);
	}

	public static Derealizable recursiveRead(SerializationReader reader, Class<? extends DerealizerEnum> enumClass) {
		DerealizerEnum[] values = enumClass.getEnumConstants();
		// Calculate the minimum number of bits needed to represent values.length
		int numBitsToRead = 32 - Integer.numberOfLeadingZeros(values.length);
		DerealizerEnum enumValue = values[reader.readIntN(numBitsToRead)];
		Class<? extends DerealizerEnum> childEnumClass = enumValue.derealizerEnum();
		if (isAbstract(enumValue.objClass().getModifiers())) {
			return recursiveRead(reader, childEnumClass);
		} else {
			try {
				Derealizable instance = enumValue.objClass().getConstructor().newInstance();
				instance.read(reader);
				return instance;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static byte[] serialize(Derealizable obj, Class<? extends DerealizerEnum> enumClass) {
		SerializationWriter writer = new SerializationWriter();
		recursiveWrite(obj, writer, enumClass);
		return writer.toByteArray();
	}

	public static void recursiveWrite(Derealizable obj, SerializationWriter writer, Class<? extends DerealizerEnum> enumClass) {
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

}
