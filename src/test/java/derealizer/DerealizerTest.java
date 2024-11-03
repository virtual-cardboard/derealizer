package derealizer;

import static derealizer.Derealizer.getDerealizableHeirarchyClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import derealizer.annotation.inheritance.IrrelevantSuperClass;
import derealizer.annotation.inheritance.MiddleClass;
import derealizer.annotation.inheritance.SubClass;
import derealizer.annotation.inheritance.SuperClass;
import org.junit.jupiter.api.Test;

public class DerealizerTest {

	@Test
	void testGetDerealizableSuperclass () {
		assertNotEquals(MiddleClass.class, getDerealizableHeirarchyClass(SubClass.class));
		assertEquals(SuperClass.class, getDerealizableHeirarchyClass(SubClass.class));
		assertNotEquals(IrrelevantSuperClass.class, getDerealizableHeirarchyClass(SubClass.class));

		assertEquals(SuperClass.class, getDerealizableHeirarchyClass(MiddleClass.class));
		assertNull(getDerealizableHeirarchyClass(IrrelevantSuperClass.class));
	}

}
