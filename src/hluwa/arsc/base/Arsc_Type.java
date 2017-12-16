package hluwa.arsc.base;

import hluwa.common.struct;

public class Arsc_Type extends Arsc_Struct {
    public byte id;
    public byte res0;
    public short res1;
    public int entryCount;
    public Arsc_Type(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        super.parseData();
        this.id = this.cursor.nextByte();
        this.res0 = this.cursor.nextByte();
        this.res1 = this.cursor.nextShort();
        this.entryCount = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return super.getLength() + 8;
    }
}
