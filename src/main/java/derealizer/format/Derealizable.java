package derealizer.format;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public interface Derealizable {

	public default byte[] serialize() {
		SerializationWriter writer = new SerializationWriter();
		write(writer);
		return writer.toByteArray();
	}

	public abstract void read(SerializationReader reader);

	public abstract void write(SerializationWriter writer);

	public abstract SerializationFormatEnum formatEnum();

}
