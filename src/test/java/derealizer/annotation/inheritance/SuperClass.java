package derealizer.annotation.inheritance;

import derealizer.annotation.Derealizable;

@Derealizable
public class SuperClass extends IrrelevantSuperClass {

	private String superField;

	public SuperClass(String superField) {
		this.superField = superField;
	}

	public String superField() {
		return superField;
	}

}
