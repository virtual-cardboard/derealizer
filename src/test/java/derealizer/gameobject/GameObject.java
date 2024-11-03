package derealizer.gameobject;

import derealizer.Serializable;
import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public abstract class GameObject implements Serializable {

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
