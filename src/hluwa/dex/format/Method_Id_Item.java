package hluwa.dex.format;

import hluwa.dex.Exception.NonSameItemLengthException;
import hluwa.common.Exception.QueryNextDataException;
import hluwa.dex.base.Item;

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


	@Override
	public void parseData() {
		// TODO Auto-generated method stub
		class_id = cursor.nextShort();
		proto_id = cursor.nextShort();
		name_id = cursor.nextInt();
	}
}
