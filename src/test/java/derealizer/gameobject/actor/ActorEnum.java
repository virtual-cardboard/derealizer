package derealizer.gameobject.actor;

import derealizer.Serializer;
import derealizer.format.Derealizable;
import derealizer.format.DerealizerEnum;

public enum ActorEnum implements DerealizerEnum {

	NOMAD(Nomad.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends Serializer> serializer;

	ActorEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	ActorEnum(Class<? extends Derealizable> objClass, Class<? extends Serializer> serializer) {
		this.objClass = objClass;
		this.serializer = serializer;
	}

	@Override
	public Class<? extends Derealizable> objClass() {
		return objClass;
	}

	@Override
	public Class<? extends Serializer> serializer() {
		return serializer;
	}
}
