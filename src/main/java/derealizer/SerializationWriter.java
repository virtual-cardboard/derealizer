package derealizer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayDeque;
import java.util.Queue;

public class SerializationWriter {

	private final Queue<Byte> bytes = new ArrayDeque<>();

	public SerializationWriter consume(long val) {
		bytes.add((byte) ((val >> 56) & 0xFF));
		bytes.add((byte) ((val >> 48) & 0xFF));
		bytes.add((byte) ((val >> 40) & 0xFF));
		bytes.add((byte) ((val >> 32) & 0xFF));
		bytes.add((byte) ((val >> 24) & 0xFF));
		bytes.add((byte) ((val >> 16) & 0xFF));
		bytes.add((byte) ((val >> 8) & 0xFF));
		bytes.add((byte) (val & 0xFF));
		return this;
	}

	public SerializationWriter consume(int val) {
		bytes.add((byte) ((val >> 24) & 0xFF));
		bytes.add((byte) ((val >> 16) & 0xFF));
		bytes.add((byte) ((val >> 8) & 0xFF));
		bytes.add((byte) (val & 0xFF));
		return this;
	}

	public SerializationWriter consume(short val) {
		bytes.add((byte) ((val >> 8) & 0xFF));
		bytes.add((byte) (val & 0xFF));
		return this;
	}

	public SerializationWriter consume(byte val) {
		bytes.add(val);
		return this;
	}

	public SerializationWriter consume(boolean val) {
		bytes.add((byte) (val ? 1 : 0));
		return this;
	}

	public SerializationWriter consume(double val) {
		return consume(Double.doubleToLongBits(val));
	}

	public SerializationWriter consume(float val) {
		return consume(Float.floatToIntBits(val));
	}

	public SerializationWriter consume(String val) {
		byte[] b = val.getBytes(UTF_8);
		short numBytes = (short) b.length;
		bytes.add((byte) ((numBytes >> 8) & 0xFF));
		bytes.add((byte) (numBytes & 0xFF));
		for (byte x : b) {
			bytes.add(x);
		}
		return this;
	}

	public Queue<Byte> getBytes() {
		return bytes;
	}

	public byte[] toByteArray() {
		byte[] arr = new byte[bytes.size()];
		int i = 0;
		for (Byte b : bytes) {
			arr[i++] = b;
		}
		return arr;
	}

}
