package huluwa.dexparser.tool;

import huluwa.dexparser.Exception.CursorMoveException;
import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.type.uLeb128;
import huluwa.dexparser.type.TypeCast;

public class ByteCursor {
	protected byte data[];
	protected int position = 0;
	protected int length = -1;

	public ByteCursor() {
		super();
	};

	public ByteCursor(byte data[]) {
		this.data = data;
		getLength();
	}

	public void move(int pos) throws CursorMoveException {
		// if(pos >= length){
		// throw new CursorMoveException(length,pos);
		// }
		position = pos;
	}

	public void aboveMove(int index) throws CursorMoveException {
		int i = position - index;
		if (i < 0) {
			throw new CursorMoveException(length, i);
		}
		position = i;
	}

	public void belowMove(int index) throws CursorMoveException {
		int i = position + index;
		if (i > length) {
			throw new CursorMoveException(length, i);
		}
		position = i;
	}

	public byte getByte(int index) {
		return data[index];
	}

	public int getLength() {
		if (this.length == -1) {
			this.length = data.length;
		}
		return length;
	}

	public byte nextByte() {
		return data[position++];
	}

	public int nextInt() throws QueryNextDataException {
		byte buf[] = nextData(4);
		return new TypeCast(buf).toInt();
	}

	public short nextShort() throws QueryNextDataException {
		byte buf[] = nextData(2);
		return new TypeCast(buf).toShort();
	}

	public uLeb128 nextLeb128() throws NonStandardLeb128Exception, QueryNextDataException {
		int i = 0;
		while (getByte(this.position + i) >> 7 != 0) {
			i++;
			if (this.position + i >= length || i >= 5) {
				throw new NonStandardLeb128Exception(data[this.position + i - 1], i);
			}
		}
		byte d[] = nextData(i + 1);
		return new TypeCast(d).toLeb128();
	}

	public byte[] nextString() throws QueryNextDataException {
		int length = 0;
		while (getByte(this.position + length) != '\0') {
			length++;
		}
		byte b[] = nextData(++length);
		return b;
	}

	public byte[] nextData(int size) throws QueryNextDataException {
		if (size == 0) {
			return null;
		}
		int i = position + size;
		if (i > length) {
			throw new QueryNextDataException(length, i);
		}
		byte d[] = new byte[size];
		System.arraycopy(this.data, this.position, d, 0, size);
		this.position += size;
		return d;
	}

	public int getPos() {
		return this.position;
	}

	public byte[] getBytes() {
		return data;
	}
}
