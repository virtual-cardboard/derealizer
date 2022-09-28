package derealizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import derealizer.gameobject.GameObjectEnum;
import derealizer.gameobject.actor.ActorEnum;
import derealizer.gameobject.actor.Nomad;
import org.junit.jupiter.api.Test;

class AbstractClassSerializationTest {

	@Test
	public void testGameObject() {
		Nomad actor = new Nomad(-253674653L, 25, "Warrior");
		byte[] bytes = Derealizer.serialize(actor, GameObjectEnum.class);
		Nomad actor2 = (Nomad) Derealizer.deserialize(bytes, GameObjectEnum.class);
		assertEquals(actor.id(), actor2.id());
		assertEquals(actor.health(), actor2.health());
		assertEquals(actor.className(), actor2.className());
	}

	@Test
	public void testActor() {
		Nomad actor = new Nomad(2624503483275L, 246954, "Monk");
		byte[] bytes = Derealizer.serialize(actor, ActorEnum.class);
		Nomad actor2 = (Nomad) Derealizer.deserialize(bytes, ActorEnum.class);
		assertEquals(actor.id(), actor2.id());
		assertEquals(actor.health(), actor2.health());
		assertEquals(actor.className(), actor2.className());
	}

	@Test
	public void testSerialize() {
		Nomad actor = new Nomad(-6653785126793L, 13, "Magus");
		byte[] bytes1 = Derealizer.serialize(actor, ActorEnum.class);
		byte[] bytes2 = Derealizer.serialize(actor, GameObjectEnum.class);
		assertFalse(Arrays.equals(bytes1, bytes2));
	}

}
