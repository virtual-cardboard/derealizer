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
			val = (byte) (bytes[index] << bitIndex & 0xFF);
			// Add the bits from the next byte
			val |= (byte) ((bytes[index + 1] & 0xFF) >>> 8 - bitIndex);
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
			val |= (bytes[index] & (1 << (7 - bitIndex))) >>> (7 - bitIndex - i);
			bitIndex++;
			if (bitIndex == 8) {
				bitIndex = 0;
				index++;
			}
		}
		return val;
	}

	public long readLong() {
		return ((long) readByteInternal() << 56)
				| ((long) (readByteInternal() & 0xFF) << 48)
				| ((long) (readByteInternal() & 0xFF) << 40)
				| ((long) (readByteInternal() & 0xFF) << 32)
				| ((long) (readByteInternal() & 0xFF) << 24)
				| ((readByteInternal() & 0xFF) << 16)
				| ((readByteInternal() & 0xFF) << 8)
				| (readByteInternal() & 0xFF);
	}

	public int readInt() {
		return ((readByteInternal()) << 24)
				| ((readByteInternal() & 0xFF) << 16)
				| ((readByteInternal() & 0xFF) << 8)
				| (readByteInternal() & 0xFF);
	}

	public short readShort() {
		return (short) (((readByteInternal() & 0xFF) << 8)
				| (readByteInternal() & 0xFF));
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
		int numBytes = readShort() - Short.MIN_VALUE; // readShort() + 32768
		byte[] strBytes = new byte[numBytes];
		for (int i = 0; i < numBytes; i++) {
			strBytes[i] = readByte();
		}
		return new String(strBytes, UTF_8);
	}

	public int bytesRemaining() {
		return bytes.length - index;
	}

}
