package hluwa.dex.format;

import hluwa.dex.base.Item;
import hluwa.dex.type.uLeb128;

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
	public void parseData() {
		this.method_id = this.cursor.nextuLeb128();
		this.access_flags = this.cursor.nextuLeb128();
		this.code_off = this.cursor.nextuLeb128();
		if (code_off.toInt() != 0) {
			this.code = new Code_Item(this.cursor.getData(), code_off.toInt());
		}
	}

}
