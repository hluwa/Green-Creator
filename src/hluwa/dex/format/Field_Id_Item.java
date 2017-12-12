package hluwa.dex.format;

import hluwa.dex.base.Item;

public class Field_Id_Item extends Item {
	private static String itemName = "Field_Id";
	private static int length = 8;

	public short class_id;
	public short type_id;
	public int name_id;

	public Field_Id_Item(byte[] data, int off) {
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
		class_id = cursor.nextShort();
		type_id = cursor.nextShort();
		name_id = cursor.nextInt();
	}
}
