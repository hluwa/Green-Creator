package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonSameItemLengthException;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;

public class String_Id_Item extends Item {
	public static String itemName = "String_Id";
	public static int length = 4;

	public int data_off;

	public String_Id_Item(byte[] data, int off) throws NonSameItemLengthException {
		super(data, off);
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return itemName;
	}

	@Override
	public void parseData() throws QueryNextDataException {
		this.data_off = cursor.nextInt();
	}
}
