package hluwa.dex.type;

public class TypeCast {
	public static final int ENDIAN_CONSTANT = 0x12345678;
	public static final int REVERSE_ENDIAN_CONSTANT = 0x78563412;

	private byte[] data;
	private int tag;

	private int ival;
	private short sval;
	private uLeb128 lval;
	private uLeb128p1 lp1val;

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
			if(this.data.length >= 1) 
			{
				this.ival = this.data[0] & 0xFF;
				if(this.data.length >= 2)
				{
					this.ival |=  (this.data[1] & 0xFF) << 8;
					if(this.data.length >= 3)
					{
						this.ival |=  (this.data[2] & 0xFF) << 16;
						if(this.data.length >= 4)
						{
							this.ival |=  (this.data[3] & 0xFF) << 24;
						}
					}
					
				}
			}
			else 
			{
				this.ival = 0;
			}
		} else {
			if(this.data.length == 1) 
			{
				this.ival = (this.data[0] & 0xFF) << 24;
			}
			else if(this.data.length == 2) 
			{
				this.ival = (this.data[0] & 0xFF) << 24 | (this.data[1] & 0xFF) << 16;
			}
			else if(this.data.length == 3) 
			{
				this.ival = (this.data[0] & 0xFF) << 24 | (this.data[1] & 0xFF) << 16 | (this.data[2] & 0xFF) << 8;
			}
			else if(this.data.length >= 4) 
			{

				this.ival = (this.data[0] & 0xFF) << 24 | (this.data[1] & 0xFF) << 16 | (this.data[2] & 0xFF) << 8 | (this.data[3] & 0xFF);
			}
			else 
			{
				this.ival = 0;
			}
		}
		return this.ival;
	}

	public short toShort() {
		if (this.sval != 0) {
			return this.sval;
		}
		if (this.tag == ENDIAN_CONSTANT) {
			if(this.data.length >= 1) 
			{
				this.sval = (short) (this.data[0] & 0xFF);
				if(this.data.length >= 2) 
				{
					this.sval |= (this.data[1] & 0xFF) << 8;
				}
			}
			else 
			{
				this.sval = 0;
			}
		} else {
			if(this.data.length == 1) 
			{
				this.sval = (short) (this.data[0] & 0xFF << 8);
			}
			else if(this.data.length >= 2) 
			{
				this.sval = (short) (this.data[1] & 0xFF | (this.data[0] & 0xFF) << 8);
			}
			else 
			{
				this.sval = 0;
			}
			
		}
		return this.sval;
	}

	public uLeb128p1 toLeb128p1() {
		if (this.lp1val != null) {
			return this.lp1val;
		}
		return new uLeb128p1(this.data);
//		return uLeb128.Create(toInt());
	}
	
	public uLeb128 toLeb128() {
		if (this.lval != null) {
			return this.lval;
		}
		return new uLeb128(this.data);
//		return uLeb128.Create(toInt());
	}

	public byte[] toBytes() {
		return this.data;
	}
}
