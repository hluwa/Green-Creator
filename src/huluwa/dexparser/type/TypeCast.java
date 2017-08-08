package huluwa.dexparser.type;

public class TypeCast {
	public static final int ENDIAN_CONSTANT = 0x12345678;
	public static final int REVERSE_ENDIAN_CONSTANT = 0x78563412;

	private byte[] data;
	private int tag;

	private int ival;
	private short sval;
	private uLeb128 lval;

	public TypeCast(byte data[]) {
		this(data, ENDIAN_CONSTANT);
	}

	public TypeCast(byte data[], int tag) {
		this.data = data;
		this.tag = tag;
	}

	public TypeCast(int ival) {
		this(ival, ENDIAN_CONSTANT);
	}

	public TypeCast(int ival, int tag) {
		this.data = new byte[4];
		if (tag == ENDIAN_CONSTANT) {
			data[3] = (byte) ((ival >> 24) & 0xFF);
			data[2] = (byte) ((ival >> 16) & 0xFF);
			data[1] = (byte) ((ival >> 8) & 0xFF);
			data[0] = (byte) (ival & 0xFF);
		} else {
			data[0] = (byte) ((ival >> 24) & 0xFF);
			data[1] = (byte) ((ival >> 16) & 0xFF);
			data[2] = (byte) ((ival >> 8) & 0xFF);
			data[3] = (byte) (ival & 0xFF);
		}
		this.ival = ival;
	}

	public TypeCast(short sval) {
		this(sval, ENDIAN_CONSTANT);
	}

	public TypeCast(short sval, int tag) {
		this.data = new byte[2];
		if (tag == ENDIAN_CONSTANT) {
			data[1] = (byte) ((sval >> 8) & 0xFF);
			data[0] = (byte) (sval & 0xFF);
		} else {
			data[0] = (byte) ((sval >> 8) & 0xFF);
			data[1] = (byte) (sval & 0xFF);
		}
		this.sval = sval;
	}

	public TypeCast(uLeb128 lval) {
		this.data = lval.getData();
		this.lval = lval;
	}

	public int toInt() {
		if (this.ival != 0) {
			return this.ival;
		}
		if (this.tag == ENDIAN_CONSTANT) {
			this.ival = this.data[0] & 0xFF | (this.data[1] & 0xFF) << 8 | (this.data[2] & 0xFF) << 16
					| (this.data[3] & 0xFF) << 24;
		} else {
			this.ival = this.data[3] & 0xFF | (this.data[2] & 0xFF) << 8 | (this.data[1] & 0xFF) << 16
					| (this.data[0] & 0xFF) << 24;
		}
		return this.ival;
	}

	public short toShort() {
		if (this.sval != 0) {
			return this.sval;
		}
		if (this.tag == ENDIAN_CONSTANT) {
			this.sval = (short) (this.data[0] & 0xFF | (this.data[1] & 0xFF) << 8);
		} else {
			this.sval = (short) (this.data[1] & 0xFF | (this.data[0] & 0xFF) << 8);
		}
		return this.sval;
	}

	public uLeb128 toLeb128() {
		if (this.lval != null) {
			return this.lval;
		}
		return uLeb128.Create(toInt());
	}

	public byte[] toBytes() {
		return this.data;
	}
}
