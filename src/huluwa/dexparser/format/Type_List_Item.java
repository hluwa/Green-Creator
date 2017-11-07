package huluwa.dexparser.format;

import java.io.UnsupportedEncodingException;

import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;
import huluwa.dexparser.type.TypeCast;

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
		if (size % 2 != 0) {
			size++;
		}
		type_ids = new short[size];
		int i = 0;
		while (i < size) {
			type_ids[i] = new TypeCast(cursor.nextShort()).toShort();
			i++;
		}
	}
}
