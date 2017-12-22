package hluwa.dex.format;

import hluwa.dex.base.Item;
import hluwa.dex.type.uLeb128;

public class String_Data_Item extends Item {
	private static String itemName = "String_Data";

	public uLeb128 length;
	public byte body[];

	public String_Data_Item(byte[] data, int off){
		super(data, off);
	}

	public int getLength() {
		return length.getLength() + body.length ;
	}

	public String getName() {
		return itemName;
	}

	@Override
	public void parseData(){
		length = cursor.nextuLeb128();
		body = cursor.nextString();
//		System.out.println("[Parser] new String[" + this.getFileOff() + ":" + length.toInt() + "] :" + toString());
	}

	@Override
	public String toString() {
		String res = new String(this.body);
		if(res.endsWith("\0") && res.length() >= 2)
		{
			res = new String(res.getBytes(),0,res.length()-1);
		}
		return res;
	}
}
