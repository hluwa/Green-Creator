package huluwa.dexparser.format;

import huluwa.dexparser.base.Item;
import huluwa.dexparser.base.value_type;

public class encoded_value extends Item {

	public encoded_value(byte[] data, int off) {
		super(data, off);
		// TODO Auto-generated constructor stub
	}

	public value_type type;
	public byte arg;
	public byte[] value;
	@Override
	public void parseData() {
		byte b = this.cursor.nextByte();
		type = value_type.values()[b & 0x1f];
		arg = (byte) (b >> 5);
		if(type == value_type.VALUE_ARRAY) 
		{
			encoded_array arr = new encoded_array(this.cursor.getBytes(),this.cursor.getPos());
			value = this.cursor.nextData(arr.getLength());
		}
		if(type == value_type.VALUE_ANNOTATION) 
		{
			encoded_annotation arr = new encoded_annotation(this.cursor.getBytes(),this.cursor.getPos());
			value = this.cursor.nextData(arr.getLength());
		}
		if(type == value_type.VALUE_NULL) 
		{
			value = new byte[0];
		}
		if(type == value_type.VALUE_BOOLEAN) 
		{
			value = new byte[0];
		}
		value = this.cursor.nextData(arg + 1);
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 1 + value.length;
	}

}
