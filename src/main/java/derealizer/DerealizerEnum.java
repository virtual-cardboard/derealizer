package derealizer;

public interface DerealizerEnum {

	Class<? extends Derealizable> objClass();

	Class<? extends DerealizerEnum> derealizerEnum();

}
