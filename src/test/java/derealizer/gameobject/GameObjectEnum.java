package derealizer.gameobject;

import derealizer.Serializable;
import derealizer.DerealizerEnum;
import derealizer.gameobject.actor.Actor;
import derealizer.gameobject.actor.ActorEnum;

public enum GameObjectEnum implements DerealizerEnum {

	ACTOR(Actor.class, ActorEnum.class),
	;

	private final Class<? extends Serializable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	GameObjectEnum(Class<? extends Serializable> objClass) {
		this(objClass, null);
	}

	GameObjectEnum(Class<? extends Serializable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
