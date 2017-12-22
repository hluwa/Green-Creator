package hluwa.arsc.format;

import hluwa.common.ByteCursor;
import hluwa.common.struct;
import hluwa.dex.type.TypeCast;

public class ResStringPool_Item extends struct {
    public ResStringPool_Item(byte[] data, int off,int flags) {
        super(data, off);
        this.flags = flags;
    }

    private static final int UTF8_FLAG = 1 << 8;
    public int size;
    public byte[] body;
    public int flags;


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
        if((flags & UTF8_FLAG) == UTF8_FLAG)
        {
            body = this.cursor.nextString();
        }
        else
        {
            body = this.cursor.nextData(size * 2);
        }
    }

    @Override
    public String toString() {
        if((flags & UTF8_FLAG) == UTF8_FLAG)
        {
            return new String(body).trim();
        }
        else
        {
            return unicode2String(body);
        }
    }

    public static String unicode2String(byte[] unicode) {
        StringBuffer string = new StringBuffer();
        ByteCursor cursor = new ByteCursor(unicode);
        for (int i = 0; i < unicode.length / 2; i++) {
            string.append((char)cursor.nextShort());
        }
        return string.toString();
    }

    @Override
    public int getLength() {
        return 2 + body.length;
    }
}
