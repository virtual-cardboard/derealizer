package derealizer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import derealizer.gameobject.GameObjectSerializer;
import derealizer.gameobject.actor.ActorSerializer;
import derealizer.gameobject.actor.Nomad;
import org.junit.jupiter.api.Test;

class SerializerTest {

	@Test
	public void testGameObject() {
		Nomad actor = new Nomad(-253674653L, 25, "Warrior");
		byte[] bytes = new GameObjectSerializer().serialize(actor);
		Nomad actor2 = (Nomad) new GameObjectSerializer().deserialize(bytes);
		assertEquals(actor.id(), actor2.id());
		assertEquals(actor.health(), actor2.health());
		assertEquals(actor.className(), actor2.className());
	}

	@Test
	public void testActor() {
		Nomad actor = new Nomad(2624503483275L, 246954, "Monk");
		byte[] bytes = new ActorSerializer().serialize(actor);
		Nomad actor2 = (Nomad) new ActorSerializer().deserialize(bytes);
		assertEquals(actor.id(), actor2.id());
		assertEquals(actor.health(), actor2.health());
		assertEquals(actor.className(), actor2.className());
	}

}
