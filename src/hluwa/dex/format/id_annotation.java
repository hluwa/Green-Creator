package hluwa.dex.format;

import hluwa.dex.base.Item;

public class id_annotation extends Item {

	public int field_idx;
	public int annotations_off;
	
	public id_annotation(byte[] data, int off) {
		super(data, off);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseData() {
		this.field_idx = this.cursor.nextInt();
		this.annotations_off = this.cursor.nextInt();
	}

	@Override
	public int getLength() {
		return 8;
	}

}
