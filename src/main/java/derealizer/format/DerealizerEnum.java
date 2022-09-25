package derealizer.format;

import derealizer.Serializer;

public interface DerealizerEnum {

	public Class<? extends Derealizable> objClass();

	Class<? extends Serializer> serializer();

}
