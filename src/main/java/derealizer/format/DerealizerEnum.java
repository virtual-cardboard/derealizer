package derealizer.format;

import derealizer.Deserializer;

public interface DerealizerEnum {

	public Class<? extends Derealizable> objClass();

	Class<? extends Deserializer> deserializer();

}
