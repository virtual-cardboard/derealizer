package derealizer.gameobject.actor;

import derealizer.Serializable;
import derealizer.DerealizerEnum;

public enum ActorEnum implements DerealizerEnum {

	NOMAD(Nomad.class),
	;

	private final Class<? extends Serializable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	ActorEnum(Class<? extends Serializable> objClass) {
		this(objClass, null);
	}

	ActorEnum(Class<? extends Serializable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
		this.objClass = objClass;
		this.derealizerEnum = derealizerEnum;
	}

	@Override
	public Class<? extends Serializable> objClass() {
		return objClass;
	}

	@Override
	public Class<? extends DerealizerEnum> derealizerEnum() {
		return derealizerEnum;
	}
}
