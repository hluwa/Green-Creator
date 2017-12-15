package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Struct;
import hluwa.common.struct;

public class ResStringPool_Header extends Arsc_Struct {

    public static final int
            // If set, the string index is sorted by the string values (based on strcmp16()).
            SORTED_FLAG = 1<<0,
            // String pool is encoded in UTF-8
            UTF8_FLAG = 1<<8;

    int string_count;
    int style_count;
    int flag;
    int stringsStart;
    int stylesStart;

    public ResStringPool_Header(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        super.parseData();
        string_count = this.cursor.nextInt();
        style_count = this.cursor.nextInt();
        flag = this.cursor.nextInt();
        stringsStart = this.cursor.nextInt();
        stylesStart = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return super.getLength() + 20;
    }
}
