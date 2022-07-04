package derealizer.datatype;

import derealizer.format.SerializationFormatEnum;

public class SerializationDataType {

	public static final SerializationDataType LONG = new SerializationDataType(DataTypeType.LONG);
	public static final SerializationDataType INT = new SerializationDataType(DataTypeType.INT);
	public static final SerializationDataType SHORT = new SerializationDataType(DataTypeType.SHORT);
	public static final SerializationDataType BYTE = new SerializationDataType(DataTypeType.BYTE);
	public static final SerializationDataType BOOLEAN = new SerializationDataType(DataTypeType.BOOLEAN);
	public static final SerializationDataType DOUBLE = new SerializationDataType(DataTypeType.DOUBLE);
	public static final SerializationDataType FLOAT = new SerializationDataType(DataTypeType.FLOAT);
	public static final SerializationDataType STRING_UTF8 = new SerializationDataType(DataTypeType.STRING_UTF8);

	public final DataTypeType type;

	protected SerializationDataType(DataTypeType type) {
		this.type = type;
	}

	public static RepeatedDataType repeated(SerializationDataType type) {
		return new RepeatedDataType(type);
	}

	public static OptionalDataType optional(SerializationDataType type) {
		return new OptionalDataType(type);
	}

	public static PojoDataType pojo(SerializationFormatEnum pojoFormatEnum) {
		return new PojoDataType(pojoFormatEnum);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SerializationDataType)) return false;
		return type == ((SerializationDataType) o).type;
	}

}
