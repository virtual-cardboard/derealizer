package derealizer.gameobject;

import derealizer.Derealizable;
import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public abstract class GameObject implements Derealizable {

	private long id;

	public GameObject() {
	}

	public GameObject(long id) {
		this.id = id;
	}

	@Override
	public void read(SerializationReader reader) {
		id = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(id);
	}

	public long id() {
		return id;
	}

}
