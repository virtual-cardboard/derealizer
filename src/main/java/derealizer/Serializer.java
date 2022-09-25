package derealizer;

import static java.lang.reflect.Modifier.isAbstract;

import java.util.HashMap;
import java.util.Map;

import derealizer.format.Derealizable;
import derealizer.format.DerealizerEnum;

public interface Serializer {

	public static final Map<Class<? extends Serializer>, Serializer> serializerCache = new HashMap<>();

	static void addToCache(Class<? extends DerealizerEnum> formatEnum) {
		for (DerealizerEnum val : formatEnum.getEnumConstants()) {
			Class<? extends Serializer> serializerClass = val.serializer();
			if (serializerClass != null) {
				Class<? extends Serializer> serializer = val.serializer();
				try {
					serializerCache.put(serializerClass, serializer.getConstructor().newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	default Derealizable deserialize(byte[] bytes) {
		return read(new SerializationReader(bytes));
	}

	Derealizable read(SerializationReader reader);

	default Derealizable recursiveRead(SerializationReader reader, Class<? extends DerealizerEnum> enumClass) {
		DerealizerEnum[] values = enumClass.getEnumConstants();
		// Calculate the minimum number of bits needed to represent values.length
		int numBitsToRead = 32 - Integer.numberOfLeadingZeros(values.length);
		DerealizerEnum enumValue = values[reader.readIntN(numBitsToRead)];
		Class<? extends Serializer> serializerClass = enumValue.serializer();
		if (isAbstract(enumValue.objClass().getModifiers())) {
			// Get the Deserializer from cache
			Serializer serializer = serializerCache.get(serializerClass);
			if (serializer == null) {
				addToCache(enumClass);
				serializer = serializerCache.get(serializerClass);
			}
			return serializer.read(reader);
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

	default byte[] serialize(Derealizable obj) {
		SerializationWriter writer = new SerializationWriter();
		write(obj, writer);
		return writer.toByteArray();
	}

	void write(Derealizable obj, SerializationWriter writer);

	default void recursiveWrite(Derealizable obj, SerializationWriter writer, Class<? extends DerealizerEnum> enumClass) {
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
			if (enumVal.serializer() == null) {
				// obj is not abstract
				obj.write(writer);
			} else {
				// obj is abstract
				Serializer serializer = serializerCache.get(enumVal.serializer());
				if (serializer == null) {
					addToCache(enumClass);
					serializer = serializerCache.get(enumVal.serializer());
				}
				serializer.write(obj, writer);
			}
			return;
		}
	}

}
