package derealizer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;
import java.util.List;

public class SerializationWriter {

	private final List<Byte> bytes = new ArrayList<>();
	private int bitIndex = 0;

	private void writeByteInternal(byte val) {
		if (bitIndex == 0) {
			bytes.add(val);
		} else {
			// Read the bits from the current byte
			byte current = bytes.get(bytes.size() - 1);
			// OR the bits from the next byte
			current |= (byte) ((val & 0xFF) >>> bitIndex);
			bytes.set(bytes.size() - 1, current);
			bytes.add((byte) (val << (8 - bitIndex) & 0xFF));
		}
	}

	// TODO Use a more efficient algorithm (write chunks of bits at a time, instead of 1 at a time)
	public void consume(int val, int n) {
		if (n > 31) {
			throw new IllegalArgumentException("Cannot write more than 31 bits.");
		}
		for (int i = 0; i < n; i++) {
			if (bitIndex == 0) {
				bytes.add((byte) 0);
			}
			// get current bit from val
			int bit = (byte) ((val >> (n - i - 1)) & 1);
			// write current bit at the correct bit index of the current byte
			byte currentByte = bytes.get(bytes.size() - 1);
			currentByte |= bit << (7 - bitIndex);
			bytes.set(bytes.size() - 1, currentByte);
			bitIndex = (bitIndex + 1) % 8;
		}
	}

	public SerializationWriter consume(long val) {
		writeByteInternal((byte) ((val >> 56) & 0xFF));
		writeByteInternal((byte) ((val >> 48) & 0xFF));
		writeByteInternal((byte) ((val >> 40) & 0xFF));
		writeByteInternal((byte) ((val >> 32) & 0xFF));
		writeByteInternal((byte) ((val >> 24) & 0xFF));
		writeByteInternal((byte) ((val >> 16) & 0xFF));
		writeByteInternal((byte) ((val >> 8) & 0xFF));
		writeByteInternal((byte) (val & 0xFF));
		return this;
	}

	public SerializationWriter consume(int val) {
		writeByteInternal((byte) ((val >> 24) & 0xFF));
		writeByteInternal((byte) ((val >> 16) & 0xFF));
		writeByteInternal((byte) ((val >> 8) & 0xFF));
		writeByteInternal((byte) (val & 0xFF));
		return this;
	}

	public SerializationWriter consume(short val) {
		writeByteInternal((byte) ((val >> 8) & 0xFF));
		writeByteInternal((byte) (val & 0xFF));
		return this;
	}

	public SerializationWriter consume(byte val) {
		writeByteInternal(val);
		return this;
	}

	public SerializationWriter consume(boolean val) {
		consume(val ? 1 : 0, 1);
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
		consume((short) (numBytes - Short.MAX_VALUE));
		for (byte x : b) {
			writeByteInternal(x);
		}
		return this;
	}

	public List<Byte> getBytes() {
		return bytes;
	}

	public byte[] toByteArray() {
		byte[] arr = new byte[bytes.size()];
		int i = 0;
		for (byte b : bytes) {
			arr[i++] = b;
		}
		return arr;
	}

}
