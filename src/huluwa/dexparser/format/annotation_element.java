package huluwa.dexparser.format;

import huluwa.dexparser.base.Item;
import huluwa.dexparser.type.uLeb128;

public class annotation_element extends Item {

	public uLeb128 name_idx;
	public encoded_value value;
	
	public annotation_element(byte[] data, int off) {
		super(data, off);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void parseData() {
		name_idx = this.cursor.nextuLeb128();
		value = new encoded_value(this.cursor.getBytes(),this.cursor.getPos());
	}

	@Override
	public int getLength() {
		return name_idx.getLength() + value.getLength();
	}

}
