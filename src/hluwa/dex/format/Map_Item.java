package hluwa.dex.format;

import hluwa.dex.base.Item;

public class Map_Item extends Item {
	public static final short STRING_ID_MAP = 1;
	public static final short TYPE_ID_MAP = 2;
	public static final short PROTO_ID_MAP = 3;
	public static final short FIELD_ID_MAP = 4;
	public static final short METHOD_ID_MAP = 5;
	public static final short CLASS_DEF_MAP = 6;
	public static final short STRING_DATA_MAP = 8194;
	public static final short TYPE_LIST_MAP = 4097;

	public Map_Item(byte[] data, int off) {
		super(data, off);
	}

	public static int length = 12;

	public short type;
	public short unused = 0;
	public int size;
	public int offset;

	public int getLength() {
		return length;
	}
	@Override
	public void parseData(){
		this.type = cursor.nextShort();
		this.unused = cursor.nextShort();
		this.size = cursor.nextInt();
		this.offset = cursor.nextInt();

	}

}
