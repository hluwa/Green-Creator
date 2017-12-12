package hluwa.dex.format;

import hluwa.dex.Exception.NonSameItemLengthException;
import hluwa.dex.base.Item;

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
	public void parseData() {
		this.data_off = cursor.nextInt();
	}
}
