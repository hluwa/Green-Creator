package huluwa.dexparser.format;

import huluwa.dexparser.Exception.NonStandardLeb128Exception;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;
import huluwa.dexparser.type.uLeb128;

public class encoded_field extends Item {
	public static final String itemName = "encoded_field";
	uLeb128 field_id;
	int real_id;
	uLeb128 access_flags;

	public encoded_field(byte[] data, int off) {
		super(data, off);
	}

	@Override
	public int getLength() {
		return field_id.getLength() + access_flags.getLength();
	}

	@Override
	public void parseData() {
		this.field_id = this.cursor.nextLeb128();
		this.access_flags = this.cursor.nextLeb128();
	}

}
