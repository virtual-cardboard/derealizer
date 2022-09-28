package derealizer;

public interface Derealizable {

	default byte[] serialize() {
		SerializationWriter writer = new SerializationWriter();
		write(writer);
		return writer.toByteArray();
	}

	void read(SerializationReader reader);

	void write(SerializationWriter writer);

}
