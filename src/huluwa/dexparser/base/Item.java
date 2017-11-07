package huluwa.dexparser.base;

import huluwa.dexparser.tool.ByteCursor;
import huluwa.dexparser.type.TypeCast;

public abstract class Item {
	protected int tag = TypeCast.ENDIAN_CONSTANT;
	protected int file_off;
	protected ByteCursor cursor;

	public Item(byte data[], int off) {
		this.file_off = off;
		cursor = new ByteCursor(data);
		cursor.move(file_off);
		parseData();
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getFileOff() {
		return file_off;
	}
	
	
	public abstract void parseData();
	public abstract int getLength();
	
}
