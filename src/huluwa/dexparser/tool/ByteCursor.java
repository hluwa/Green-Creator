package huluwa.dexparser.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		this.length = data.length;
	}
	
	public ByteCursor(File file) 
	{
		InputStream in;
		try {
			in = new FileInputStream(file);
			byte[] data = new byte[in.available()];
			in.read(data);
			in.close();
			this.data = data;
			this.length = data.length;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void move(int pos){
		// if(pos >= length){
		// throw new CursorMoveException(length,pos);
		// }
		position = pos;
	}

	public void aboveMove(int index) {
		int i = position - index;
		if (i < 0) {
			position = 0;
			return;
		}
		position = i;
	}

	public void belowMove(int index) {
		int i = position + index;
		if (i > length) {
			position = length;
			return;
		}
		position = i;
	}

	public byte getByte(int index) {
		return data[index];
	}

	public int getLength() {
		return length;
	}

	public byte nextByte() {
		return data[position++];
	}

	public int nextInt(){
		byte buf[] = nextData(4);
		return new TypeCast(buf).toInt();
	}

	public short nextShort(){
		byte buf[] = nextData(2);
		return new TypeCast(buf).toShort();
	}

	public uLeb128 nextLeb128(){
		int i = 0;
		while (getByte(this.position + i) >> 7 != 0) {
			i++;
			if (this.position + i >= length || i >= 5) {
				return null;
			}
		}
		byte d[] = nextData(i + 1);
		return new TypeCast(d).toLeb128();
	}

	public byte[] nextString(){
		int length = 0;
		while (getByte(this.position + length) != '\0') {
			length++;
		}
		byte b[] = nextData(++length);
		return b;
	}

	public byte[] nextData(int size){
		if (size == 0) {
			return new byte[0];
		}
		int i = position + size;
		if (i > length) {
			return null;
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
