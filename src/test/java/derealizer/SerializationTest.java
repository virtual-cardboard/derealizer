package derealizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class SerializationTest {

	@Test
	void testBitPacking1() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337).consume(false).consume((byte) -75);
		byte[] bytes = writer.toByteArray();
		System.out.println("SerializationTest.testBitPacking1");
		System.out.println("bytes = " + Arrays.toString(bytes));
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(1337, reader.readInt());
		assertFalse(reader.readBoolean());
		assertEquals(-75, reader.readByte());
	}

	@Test
	void testBitPacking2() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337).consume(true).consume((byte) -75);
		byte[] bytes = writer.toByteArray();
		System.out.println("SerializationTest.testBitPacking2");
		System.out.println("bytes = " + Arrays.toString(bytes));
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(1337, reader.readInt());
		assertTrue(reader.readBoolean());
		assertEquals(-75, reader.readByte());
	}

	@Test
	void testBitPacking3() {
		SerializationWriter writer = new SerializationWriter();
		writer.consume(1337).consume(true).consume((byte) -75).consume(false).consume("Bubbb");
		byte[] bytes = writer.toByteArray();
		System.out.println("SerializationTest.testBitPacking3");
		System.out.println("bytes = " + Arrays.toString(bytes));
		SerializationReader reader = new SerializationReader(bytes);
		assertEquals(1337, reader.readInt());
		assertTrue(reader.readBoolean());
		assertEquals(-75, reader.readByte());
		assertFalse(reader.readBoolean());
		assertEquals("Bubbb", reader.readStringUtf8());
	}

}