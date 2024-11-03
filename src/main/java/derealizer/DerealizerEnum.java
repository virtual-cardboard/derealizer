package derealizer;

public interface DerealizerEnum {

	Class<? extends Serializable> objClass();

	Class<? extends DerealizerEnum> derealizerEnum();

}
