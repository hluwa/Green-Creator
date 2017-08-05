package huluwa.dexparser.format;

import huluwa.dexparser.Exception.CursorMoveException;
import huluwa.dexparser.Exception.QueryNextDataException;
import huluwa.dexparser.base.Item;
import huluwa.dexparser.base.OpCode;

public class insns_item extends Item {
	private static final String itemName = "insns_item";

	public byte[] data;
	public OpCode opcode;

	public insns_item(byte[] data, int off) {
		super(data, off);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	@Override
	public String getName() {
		return itemName;
	}

	@Override
	public void parseData() throws QueryNextDataException {
		int b = this.cursor.nextByte() & 0xFF;

		opcode = OpCode.values()[b];
		try {
			this.cursor.aboveMove(1);
		} catch (CursorMoveException e) {
			System.out.println("[*E]" + getName() + ":" + e.getMessage());
		}
		data = this.cursor.nextData(opcode.length);
	}
}
