package derealizer.datatype;

import static derealizer.datatype.DataTypeType.POJO;

import derealizer.format.DerealizerEnum;

public class ObjDataType extends SerializationDataType {

	private final DerealizerEnum objDerealizerEnum;

	protected ObjDataType(DerealizerEnum objDerealizerEnum) {
		super(POJO);
		this.objDerealizerEnum = objDerealizerEnum;
	}

	public DerealizerEnum objDerealizerEnum() {
		return objDerealizerEnum;
	}

}
