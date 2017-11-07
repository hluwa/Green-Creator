package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonSameItemLengthException;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;

public class Type_Id_Item extends Item {
	private static String itemName = "Type_Id";
	private static int length = 4;

	public int id;

	public Type_Id_Item(byte[] data, int off) throws NonSameItemLengthException {
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
		this.id = cursor.nextInt();
	}
}
