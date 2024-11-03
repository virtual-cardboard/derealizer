package derealizer.annotation.inheritance;

public class SubClass extends MiddleClass {

	private String subField;

	public SubClass(String superField, String subField) {
		super(superField);
		this.subField = subField;
	}

	public String subField() {
		return subField;
	}

}
