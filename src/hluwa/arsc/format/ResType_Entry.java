package hluwa.arsc.format;

import hluwa.common.struct;

public class ResType_Entry extends struct {
    short size;
    short flags;
    int ketRef;
    public ResType_Entry(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        size = this.cursor.nextShort();
        flags = this.cursor.nextShort();
        ketRef = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return this.size;
    }
}
