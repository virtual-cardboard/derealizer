package derealizer;

import static java.lang.Math.max;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SerializationReader {

	private final byte[] bytes;
	private int index = 0;
	private int bitIndex = 0;

	public SerializationReader(byte[] bytes) {
		this.bytes = bytes;
	}

	public long readLong() {
		return ((long) readByte() << 56)
				| ((long) (readByte() & 0xFF) << 48)
				| ((long) (readByte() & 0xFF) << 40)
				| ((long) (readByte() & 0xFF) << 32)
				| ((long) (readByte() & 0xFF) << 24)
				| ((readByte() & 0xFF) << 16)
				| ((readByte() & 0xFF) << 8)
				| (readByte() & 0xFF);
	}

	public int readInt() {
		return ((readByte()) << 24)
				| ((readByte() & 0xFF) << 16)
				| ((readByte() & 0xFF) << 8)
				| (readByte() & 0xFF);
	}

	public int readIntN(int n) {
		if (n > 31) {
			throw new IllegalArgumentException("Cannot read more than 31 bits.");
		}
		int val = 0;
		int bitsRemaining = n;
		while (bitsRemaining > 0) {
			if (bitIndex + bitsRemaining >= 8) {
				val |= ((bytes[index] << bitIndex) & 0xFF) >> bitIndex << (bitsRemaining - (8 - bitIndex));
				bitsRemaining = max(bitsRemaining - 8 + bitIndex, 0);
				bitIndex = 0;
				index++;
			} else {
				int thePartWeWant = ((bytes[index] << bitIndex) & 0xFF) >> bitIndex >> (8 - bitIndex - bitsRemaining);
				val |= thePartWeWant;
				bitIndex += bitsRemaining;
				bitsRemaining = 0;
			}
		}
		return val;
	}

	public short readShort() {
		return (short) (((readByte() & 0xFF) << 8)
				| (readByte() & 0xFF));
	}

	public byte readByte() {
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

	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	public boolean readBoolean() {
		return (readIntN(1) & 1) == 1;
	}

	public String readStringUtf8() {
		int numBytes = readShort() + Short.MAX_VALUE; // readShort() + 32768
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
