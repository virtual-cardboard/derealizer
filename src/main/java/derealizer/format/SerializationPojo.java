package derealizer.format;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public interface SerializationPojo {

	public static <T extends SerializationPojo> T createPojo(Class<T> pojoClass) {
		try {
			return pojoClass.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public default byte[] serialize() {
		SerializationWriter writer = new SerializationWriter();
		write(writer);
		return writer.toByteArray();
	}

	public abstract void read(SerializationReader reader);

	public abstract void write(SerializationWriter writer);

}
