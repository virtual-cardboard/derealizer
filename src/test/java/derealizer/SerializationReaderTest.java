package derealizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SerializationReaderTest {

	@Test
	void testBitPacking1() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337).consume(true).consume((byte) -7);
		byte[] bytes = writer.toByteArray();
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(reader.readInt(), 1337);
		assertTrue(reader.readBoolean());
		assertEquals(reader.readByte(), -7);
	}

	@Test
	void testBitPacking2() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337).consume(true).consume((byte) -7);
		byte[] bytes = writer.toByteArray();
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(reader.readInt(), 1337);
		assertTrue(reader.readBoolean());
		assertEquals(reader.readByte(), -7);
	}

}