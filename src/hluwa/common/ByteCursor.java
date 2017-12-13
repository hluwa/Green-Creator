package hluwa.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import hluwa.dex.format.encoded_value;
import hluwa.dex.type.uLeb128;
import hluwa.dex.type.uLeb128p1;
import hluwa.dex.type.TypeCast;

public class ByteCursor {
	protected byte data[];
	protected int position = 0;

	public ByteCursor() {
		super();
	};

	public ByteCursor(byte data[]) {
		this.data = data;
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
		if (i > data.length) {
			position = data.length;
			return;
		}
		position = i;
	}

	public byte getByte(int index) {
		return data[index];
	}

	public int getLength() {
		return data.length;
	}

	public encoded_value nextEncoeded_value()
	{
		encoded_value value = new encoded_value(this.getData(),this.position);
		this.belowMove(value.getLength());
		return value;
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

	public uLeb128 nextuLeb128(){
		int i = 0;
		while (getByte(this.position + i) >> 7 != 0) {
			i++;
			if (this.position + i >= data.length || i >= 5) {
				return null;
			}
		}
		byte d[] = nextData(i + 1);
		return new TypeCast(d).toLeb128();
	}
	public uLeb128p1 nextuLeb128p1(){
		int i = 0;
		while (getByte(this.position + i) >> 7 != 0) {
			i++;
			if (this.position + i >= data.length || i >= 5) {
				return null;
			}
		}
		byte d[] = nextData(i + 1);
		return new TypeCast(d).toLeb128p1();
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
		if (i > data.length) {
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

	public byte[] getData() {
		return data;
	}
}
