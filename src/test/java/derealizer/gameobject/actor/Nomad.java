package derealizer.gameobject.actor;

import derealizer.Derealizable;
import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public class Nomad extends Actor implements Derealizable {

	private String className;

	public Nomad() {
	}

	public Nomad(long id, int health, String className) {
		super(id, health);
		this.className = className;
	}

	public Nomad(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
		className = reader.readStringUtf8();
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
		writer.consume(className);
	}

	public String className() {
		return className;
	}

}
