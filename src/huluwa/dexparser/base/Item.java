package huluwa.dexparser.base;

import huluwa.dexparser.Exception.CursorMoveException;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.tool.ByteCursor;
import huluwa.dexparser.type.TypeCast;

public abstract class Item {
	protected int tag = TypeCast.ENDIAN_CONSTANT;
	protected int file_off;
	protected ByteCursor cursor;

	public Item(byte data[], int off) {
		this.file_off = off;
		cursor = new ByteCursor(data);
		try {
			cursor.move(file_off);
			parseData();
		} catch (CursorMoveException e1) {
			System.out.println("[*E]" + getName() + ":" + e1.getMessage());
		} catch (QueryNextDataException e) {
			System.out.println("[*E]" + getName() + ":" + e.getMessage());
		}
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public abstract void parseData() throws QueryNextDataException;

	public abstract int getLength();

	public abstract String getName();

	public int getFileOff() {
		return file_off;
	}
}
