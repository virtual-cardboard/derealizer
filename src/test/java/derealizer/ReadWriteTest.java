package derealizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ReadWriteTest {

	@Test
	void testBooleans() {
		SerializationWriter writer = new SerializationWriter();
		boolean[] booleans = { false, true, true, false, true, false, false, true, false };

		writer.consume(1337);
		for (boolean b : booleans) {
			writer.consume(b);
		}

		SerializationReader reader = new SerializationReader(writer.toByteArray());

		assertEquals(1337, reader.readInt());
		for (boolean b : booleans) {
			assertEquals(b, reader.readBoolean());
		}
	}

	@Test
	void testPrimitives() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337)
				.consume(true)
				.consume((short) 3005)
				.consume((short) -4124)
				.consume(-467896789L)
				.consume(false)
				.consume(678926789674L)
				.consume(Integer.MAX_VALUE)
				.consume(Integer.MIN_VALUE)
				.consume((byte) -75);
		byte[] bytes = writer.toByteArray();
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(1337, reader.readInt());
		assertTrue(reader.readBoolean());
		assertEquals((short) 3005, reader.readShort());
		assertEquals((short) -4124, reader.readShort());
		assertEquals(-467896789L, reader.readLong());
		assertFalse(reader.readBoolean());
		assertEquals(678926789674L, reader.readLong());
		assertEquals(Integer.MAX_VALUE, reader.readInt());
		assertEquals(Integer.MIN_VALUE, reader.readInt());
	}

	@Test
	void testStrings() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337).consume(true)
				.consume(false)
				.consume("2254767542`~{:}:+_~<>?DHS")
				.consume(true)
				.consume(false)
				.consume("5yaeot79dgas15][.2314124.12'53;");
		byte[] bytes = writer.toByteArray();
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(1337, reader.readInt());
		assertTrue(reader.readBoolean());
		assertFalse(reader.readBoolean());
		assertEquals("2254767542`~{:}:+_~<>?DHS", reader.readStringUtf8());
		assertTrue(reader.readBoolean());
		assertFalse(reader.readBoolean());
		assertEquals("5yaeot79dgas15][.2314124.12'53;", reader.readStringUtf8());
	}

}