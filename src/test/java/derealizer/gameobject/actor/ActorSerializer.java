package derealizer.gameobject.actor;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.Serializer;
import derealizer.format.Derealizable;

public class ActorSerializer implements Serializer {

	@Override
	public Derealizable read(SerializationReader reader) {
		return recursiveRead(reader, ActorEnum.class);
	}

	@Override
	public void write(Derealizable obj, SerializationWriter writer) {
		recursiveWrite(obj, writer, ActorEnum.class);
	}

}
