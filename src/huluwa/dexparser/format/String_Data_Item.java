package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;
import huluwa.dexparser.type.uLeb128;

public class String_Data_Item extends Item {
	private static String itemName = "String_Data";

	public uLeb128 length;
	public byte body[];

	public String_Data_Item(byte[] data, int off) throws QueryNextDataException, NonStandardLeb128Exception {
		super(data, off);
	}

	public int getLength() {
		return length.getLength() + body.length;
	}

	public String getName() {
		return itemName;
	}

	@Override
	public void parseData() throws QueryNextDataException {
		try {
			length = cursor.nextLeb128();
		} catch (NonStandardLeb128Exception e) {
			// TODO Auto-generated catch block
			System.out.println("[*E]" + getName() + ":" + e.getMessage());
		}
		body = cursor.nextString();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new String(body);
	}
}
