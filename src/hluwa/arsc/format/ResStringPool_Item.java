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
        byte hval = this.cursor.nextByte();
        byte lval  = this.cursor.nextByte();
        if((hval & 0x80) != 0) {
            size = (((hval & 0x7F) << 8)) | lval;
        } else {
            size = hval;
        }
//        this.size = new TypeCast(this.cursor.nextData(2)).toLeb128().toInt();
        body = this.cursor.nextString();
    }

    @Override
    public String toString() {
        return new String(body).trim();
    }

    @Override
    public int getLength() {
        return 2 + body.length;
    }
}
