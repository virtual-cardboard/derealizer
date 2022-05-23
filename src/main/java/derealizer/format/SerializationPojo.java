package derealizer.format;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public interface SerializationPojo<T extends SerializationFormatEnum> {

	public default byte[] serialize() {
		SerializationWriter writer = new SerializationWriter();
		write(writer);
		return writer.toByteArray();
	}

	public abstract void read(SerializationReader reader);

	public abstract void write(SerializationWriter writer);

	public abstract T formatEnum();

}
