package derealizer.gameobject;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.Serializer;
import derealizer.format.Derealizable;

public class GameObjectSerializer implements Serializer {

	@Override
	public Derealizable read(SerializationReader reader) {
		return recursiveRead(reader, GameObjectEnum.class);
	}

	@Override
	public void write(Derealizable obj, SerializationWriter writer) {
		recursiveWrite(obj, writer, GameObjectEnum.class);
	}

}
