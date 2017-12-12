package hluwa.dex.format;

import hluwa.dex.base.Item;
import hluwa.dex.type.TypeCast;

public class Type_List_Item extends Item {
	private static String itemName = "Type_List";
	public int size;
	public short type_ids[];

	public Type_List_Item(byte[] data, int off){
		super(data, off);

	}
	

	public int getLength() {
		return 4 + size * 2;
	}

	public String getName() {
		return itemName;
	}

	@Override
	public void parseData() {
		size = cursor.nextInt();
		type_ids = new short[size];
		int i = 0;
		while (i < size) {
			type_ids[i] = new TypeCast(cursor.nextShort()).toShort();
			i++;
		}
	}
}
