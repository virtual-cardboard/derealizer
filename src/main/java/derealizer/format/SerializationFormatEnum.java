package derealizer.format;

public interface SerializationFormatEnum {

	public SerializationFormat format();

	public Class<? extends Serializable> pojoClass();

}
