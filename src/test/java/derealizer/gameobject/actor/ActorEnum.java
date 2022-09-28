package derealizer.gameobject.actor;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;

public enum ActorEnum implements DerealizerEnum {

	NOMAD(Nomad.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	ActorEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	ActorEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
		this.objClass = objClass;
		this.derealizerEnum = derealizerEnum;
	}

	@Override
	public Class<? extends Derealizable> objClass() {
		return objClass;
	}

	@Override
	public Class<? extends DerealizerEnum> derealizerEnum() {
		return derealizerEnum;
	}
}
