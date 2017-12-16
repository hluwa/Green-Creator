package hluwa.arsc.format;

import hluwa.common.struct;
import hluwa.dex.type.TypeCast;

public class ResStringPool_Item extends struct {
    public ResStringPool_Item(byte[] data, int off) {
        super(data, off);
    }
    public int size;
    public byte[] body;


    @Override
    public void parseData() {
        this.size = new TypeCast(this.cursor.nextData(2)).toLeb128().toInt();
        body = this.cursor.nextString();
    }

    @Override
    public String toString() {
        return new String(body);
    }

    @Override
    public int getLength() {
        return 2 + body.length;
    }
}
