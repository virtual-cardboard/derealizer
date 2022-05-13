package derealizer.datatype;

import static derealizer.datatype.DataTypeType.POJO;

import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationPojo;

public class PojoDataType extends SerializationDataType {

	private final SerializationFormatEnum<? extends SerializationPojo> pojoFormatEnum;

	protected PojoDataType(SerializationFormatEnum<? extends SerializationPojo> pojoFormatEnum) {
		super(POJO);
		this.pojoFormatEnum = pojoFormatEnum;
	}

	public SerializationFormatEnum<? extends SerializationPojo> pojoFormatEnum() {
		return pojoFormatEnum;
	}

}
