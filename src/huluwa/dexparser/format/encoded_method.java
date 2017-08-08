package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;
import huluwa.dexparser.type.uLeb128;

public class encoded_method extends Item {

	public static final String itemName = "encoded_method";
	public uLeb128 method_id;
	public int real_id;
	public uLeb128 access_flags;
	public uLeb128 code_off;

	public Code_Item code;

	public encoded_method(byte[] data, int off) {
		super(data, off);
	}

	@Override
	public int getLength() {
		return method_id.getLength() + access_flags.getLength() + code_off.getLength();
	}

	@Override
	public String getName() {
		return itemName;
	}

	@Override
	public void parseData() throws QueryNextDataException {
		try {
			this.method_id = this.cursor.nextLeb128();
			this.access_flags = this.cursor.nextLeb128();
			this.code_off = this.cursor.nextLeb128();
		} catch (NonStandardLeb128Exception e) {
			System.out.println("[*E]" + getName() + ":" + e.getMessage());
		}
		if (code_off.toInt() != 0) {
			this.code = new Code_Item(this.cursor.getBytes(), code_off.toInt());
		}
	}

}
