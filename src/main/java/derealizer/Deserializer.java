package derealizer;

import static java.lang.reflect.Modifier.isAbstract;

import java.util.HashMap;
import java.util.Map;

import derealizer.format.Derealizable;
import derealizer.format.SerializationFormatEnum;

public interface Deserializer {

	public static final Map<Class<? extends Derealizable>, Deserializer> deserializerCache = new HashMap<>();

	static void addToCache(Class<? extends SerializationFormatEnum> formatEnum) {
		for (SerializationFormatEnum val : formatEnum.getEnumConstants()) {
			Class<? extends Derealizable> objClass = val.objClass();
			if (isAbstract(objClass.getModifiers())) {
				Class<? extends Deserializer> deserializer = val.deserializer();
				try {
					deserializerCache.put(objClass, deserializer.getConstructor().newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	Derealizable deserialize(SerializationReader reader);

	default Derealizable recursiveDeserialize(SerializationReader reader, Class<? extends SerializationFormatEnum> enumClass) {
		SerializationFormatEnum[] values = enumClass.getEnumConstants();
		// Calculate the minimum number of bits needed to represent values.length
		int numBitsToRead = 32 - Integer.numberOfLeadingZeros(values.length);
		Class<? extends Derealizable> objClass = values[reader.readIntN(numBitsToRead)].objClass();
		if (isAbstract(objClass.getModifiers())) {
			// Get the Deserializer from cache
			return Deserializer.deserializerCache.get(objClass).deserialize(reader);
		} else {
			try {
				Derealizable instance = objClass.getConstructor().newInstance();
				instance.read(reader);
				return instance;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
