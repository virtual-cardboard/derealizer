package derealizer.datatype;

import static derealizer.datatype.DataTypeType.POJO;

import derealizer.format.SerializationPojo;

public class PojoDataType extends SerializationDataType {

	private final Class<? extends SerializationPojo> pojoClass;

	protected PojoDataType(Class<? extends SerializationPojo> pojoClass) {
		super(POJO);
		this.pojoClass = pojoClass;
	}

	public Class<? extends SerializationPojo> pojoClass() {
		return pojoClass;
	}

}
