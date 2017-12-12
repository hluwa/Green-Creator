package hluwa.dex.format;

import hluwa.dex.Exception.NonSameItemLengthException;
import hluwa.common.Exception.QueryNextDataException;
import hluwa.dex.base.Item;

public class Proto_Id_Item extends Item {
	private static String itemName = "Proto_Id";
	private static int length = 12;

	public int shorty_id;
	public int return_type_id;
	public int params_off;

	public Proto_Id_Item(byte[] data, int off) throws NonSameItemLengthException, QueryNextDataException {
		super(data, off);
	}

	public int getLength() {
		return length;
	}

	@Override
	public void parseData() {
		shorty_id = cursor.nextInt();
		return_type_id = cursor.nextInt();
		params_off = cursor.nextInt();
	}

}
