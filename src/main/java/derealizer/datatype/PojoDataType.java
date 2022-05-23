package derealizer.datatype;

import static derealizer.datatype.DataTypeType.POJO;

import derealizer.format.SerializationFormatEnum;

public class PojoDataType extends SerializationDataType {

	private final SerializationFormatEnum pojoFormatEnum;

	protected PojoDataType(SerializationFormatEnum pojoFormatEnum) {
		super(POJO);
		this.pojoFormatEnum = pojoFormatEnum;
	}

	public SerializationFormatEnum pojoFormatEnum() {
		return pojoFormatEnum;
	}

}
