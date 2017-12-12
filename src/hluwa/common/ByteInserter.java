package hluwa.common;

import java.util.ArrayList;

import hluwa.dex.type.uLeb128;
import hluwa.dex.type.TypeCast;

public class ByteInserter extends ByteCursor {
	ArrayList<Byte> instBuf = new ArrayList<Byte>();

	int tag = TypeCast.ENDIAN_CONSTANT;

	public ByteInserter() {
		super();
	};

	public ByteInserter(byte data[]) {
		super(data);
		int pos = 0;
		while (pos < data.length) {
			instBuf.add(data[pos++]);
		}
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void instInt(int val) {
		instData(new TypeCast(val, tag).toBytes());
	}

	public void instShort(short val) {
		instData(new TypeCast(val, tag).toBytes());
	}

	public void instLeb128(uLeb128 val) {
		instData(new TypeCast(val).toBytes());
	}

	public void instData(byte[] data) {
		int pos = 0;
		while (pos < data.length) {
			instByte(data[pos++]);
		}
	}

	public void instByte(byte b) {
		instBuf.add(this.position++, b);
	}

	public void flush() {
		this.data = new byte[instBuf.size()];
		for (int i = 0; i < instBuf.size(); i++) {
			this.data[i] = instBuf.get(i);
		}
	}

	public void reset() {
		instBuf.clear();
		int pos = 0;
		while (pos < data.length) {
			instBuf.add(data[pos++]);
		}
	}
}
