package hluwa.dex.format;

import java.util.ArrayList;

import hluwa.dex.base.Item;
import hluwa.dex.base.debug_opcode;
import hluwa.dex.type.uLeb128;
import hluwa.dex.type.uLeb128p1;

public class debug_info_item extends Item {

	public uLeb128 line_start;
	public uLeb128 parameters_size;
	public uLeb128p1[] parameter_names;
	public ArrayList<debug_opcode> opcodes;
	
	public debug_info_item(byte[] data, int off) {
		super(data, off);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseData() {
		line_start = this.cursor.nextuLeb128();
		parameters_size = this.cursor.nextuLeb128();
		if(parameters_size.toInt() != 0) 
		{
			parameter_names = new uLeb128p1[parameters_size.toInt()];
			for(int i = 0; i <parameter_names.length;i++) 
			{
				parameter_names[i] = this.cursor.nextuLeb128p1();
			}
		}
		int i = 0;
		opcodes = new ArrayList<debug_opcode>();
		debug_opcode op;
		do {
			byte b = this.cursor.nextByte();
			op = (b & 0x0FF) >=0x0a ? debug_opcode.DBG_UNKNOW : debug_opcode.values()[b];
			opcodes.add(op) ;
	    } while (op != debug_opcode.DBG_END_SEQUENCE);
	}

	@Override
	public int getLength() {
		int len = line_start.getLength() + parameters_size.getLength();
		if(parameter_names != null) 
		{
			for(int i =0 ;i < parameter_names.length;i++) 
			{
				len += parameter_names[i].getLength();
			}
		}
		len += opcodes.size();
		return len;
	}

}
