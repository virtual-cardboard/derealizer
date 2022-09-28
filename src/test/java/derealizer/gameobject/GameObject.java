package derealizer.gameobject;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.Derealizable;

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

	public abstract GameObjectEnum derealizerEnum();

	public long id() {
		return id;
	}

}
