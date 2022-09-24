package derealizer.format;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public interface Derealizable {

	default byte[] serialize() {
		SerializationWriter writer = new SerializationWriter();
		write(writer);
		return writer.toByteArray();
	}

	void read(SerializationReader reader);

	void write(SerializationWriter writer);

	DerealizerEnum derealizerEnum();

}
