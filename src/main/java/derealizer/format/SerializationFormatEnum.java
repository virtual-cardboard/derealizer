package derealizer.format;

public interface SerializationFormatEnum<T extends SerializationPojo> {

	public SerializationFormat format();

	public Class<? extends T> pojoClass();

	public Class<? extends T> superClass();

}