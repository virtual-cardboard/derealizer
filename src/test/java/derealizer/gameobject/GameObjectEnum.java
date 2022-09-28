package derealizer.gameobject;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;
import derealizer.gameobject.actor.Actor;
import derealizer.gameobject.actor.ActorEnum;

public enum GameObjectEnum implements DerealizerEnum {

	ACTOR(Actor.class, ActorEnum.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	GameObjectEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	GameObjectEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
