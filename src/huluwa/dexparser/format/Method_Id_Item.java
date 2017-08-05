package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonSameItemLengthException;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;

public class Method_Id_Item extends Item {
	private static String itemName = "Method_Id";
	private static int length = 8;

	public short class_id;
	public short proto_id;
	public int name_id;

	public Method_Id_Item(byte[] data, int off) throws NonSameItemLengthException, QueryNextDataException {
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
		// TODO Auto-generated method stub
		class_id = cursor.nextShort();
		proto_id = cursor.nextShort();
		name_id = cursor.nextInt();
	}
}
