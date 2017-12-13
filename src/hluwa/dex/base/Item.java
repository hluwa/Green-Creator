package hluwa.dex.base;

import hluwa.common.ByteCursor;
import hluwa.common.struct;
import hluwa.dex.type.TypeCast;

public abstract class Item extends struct {
	protected int tag = TypeCast.ENDIAN_CONSTANT;



	public Item(byte data[], int off) {
		super(data,off);
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
}
