package hluwa.arsc.format;

import hluwa.common.struct;

public class ResType_Value extends struct {
    short size;
    byte res0;
    byte dataType;
    int data;
    public ResType_Value(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        this.size = this.cursor.nextShort();
        this.res0 = this.cursor.nextByte();
        this.dataType = this.cursor.nextByte();
        this.data = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return this.size;
    }
}
