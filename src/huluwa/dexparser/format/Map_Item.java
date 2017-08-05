package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonSameItemLengthException;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;

public class Map_Item extends Item {
	public static final int STRING_ID_MAP = 1;
	public static final int TYPE_ID_MAP = 2;
	public static final int PROTO_ID_MAP = 3;
	public static final int FIELD_ID_MAP = 4;
	public static final int METHOD_ID_MAP = 5;
	public static final int CLASS_DEF_MAP = 6;
	public static final int STRING_DATA_MAP = 8194;
	public static final int TYPE_LIST_MAP = 4097;

	public Map_Item(byte[] data, int off) throws NonSameItemLengthException {
		super(data, off);
	}

	public static String itemName = "Map";
	public static int length = 12;

	public short type;
	public short unused = 0;
	public int size;
	public int offset;

	public int getLength() {
		return length;
	}

	public String getName() {
		return itemName;
	}

	@Override
	public void parseData() throws QueryNextDataException {
		this.type = cursor.nextShort();
		this.unused = cursor.nextShort();
		this.size = cursor.nextInt();
		this.offset = cursor.nextInt();

	}

}
