package derealizer.gameobject.actor;

import derealizer.Derealizable;
import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.gameobject.GameObject;

public abstract class Actor extends GameObject implements Derealizable {

	private int health;

	public Actor() {
	}

	public Actor(long id, int health) {
		super(id);
		this.health = health;
	}

	public Actor(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
		health = reader.readInt();
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
		writer.consume(health);
	}

	public int health() {
		return health;
	}

}
