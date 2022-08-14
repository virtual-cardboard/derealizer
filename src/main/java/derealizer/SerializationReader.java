package derealizer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SerializationReader {

	private final byte[] bytes;
	private int index = 0;
	private int bitIndex = 0;

	public SerializationReader(byte[] bytes) {
		this.bytes = bytes;
	}

	private byte readByteInternal() {
		byte val;
		if (bitIndex == 0) {
			val = bytes[index];
		} else {
			// Read the bits from the current byte
			val = (byte) ((bytes[index] >> bitIndex) & 0xFF);
			// Add the bits from the next byte
			val |= (bytes[index + 1] << (8 - bitIndex));
		}
		index++;
		return val;
	}

	private int readNBitsInternal(int n) {
		if (n > 31) {
			throw new IllegalArgumentException("Cannot read more than 31 bits.");
		}
		int val = 0;
		for (int i = 0; i < n; i++) {
			val |= (bytes[index] & (1 << (7 - bitIndex))) >> (7 - bitIndex - i);
			bitIndex++;
			if (bitIndex == 8) {
				bitIndex = 0;
				index++;
			}
		}
		return val;
	}

	public long readLong() {
		long val = ((long) bytes[index] << 56)
				| ((long) (bytes[index + 1] & 0xFF) << 48)
				| ((long) (bytes[index + 2] & 0xFF) << 40)
				| ((long) (bytes[index + 3] & 0xFF) << 32)
				| ((long) (bytes[index + 4] & 0xFF) << 24)
				| ((bytes[index + 5] & 0xFF) << 16)
				| ((bytes[index + 6] & 0xFF) << 8)
				| (bytes[index + 7] & 0xFF);
		index += 8;
		return val;
	}

	public int readInt() {
		int val = ((bytes[index]) << 24)
				| ((bytes[index + 1] & 0xFF) << 16)
				| ((bytes[index + 2] & 0xFF) << 8)
				| (bytes[index + 3] & 0xFF);
		index += 4;
		return val;
	}

	public short readShort() {
		short val = (short) (((bytes[index] & 0xFF) << 8)
				| (bytes[index + 1] & 0xFF));
		index += 2;
		return val;
	}

	public byte readByte() {
		return readByteInternal();
	}

	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	public boolean readBoolean() {
		return (readNBitsInternal(1) & 1) == 1;
	}

	public String readStringUtf8() {
		int numBytes = (short) (((bytes[index] & 0xFF) << 8) // TODO make this an unsigned short
				| (0xFF & bytes[index + 1] & 0xFF));
		index += 2;
		String val = new String(bytes, index, numBytes, UTF_8);
		index += numBytes;
		return val;
	}

	public int bytesRemaining() {
		return bytes.length - index;
	}

}
