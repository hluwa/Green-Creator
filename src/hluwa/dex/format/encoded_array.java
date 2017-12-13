package hluwa.dex.format;

import hluwa.dex.base.Item;
import hluwa.dex.type.uLeb128;

public class encoded_array extends Item {
	public encoded_array(byte[] data, int off) {
		super(data, off);
		// TODO Auto-generated constructor stub
	}

	public uLeb128 size;
	public encoded_value[] values;
	
	
	@Override
	public void parseData() {
		size = this.cursor.nextuLeb128();
		values = new encoded_value[size.toInt()];
		for(int i = 0 ;i < values.length; i++) 
		{
			values[i] = new encoded_value(this.cursor.getData(),this.cursor.getPos());
			this.cursor.belowMove(values[i].getLength());
		}
	}

	@Override
	public int getLength() {
		int len = 0;
		for(int i = 0;i < values.length ; i++) 
		{
			len += values[i].getLength();
		}
		return size.getLength() + len;
	}

}
