package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Struct;
import hluwa.common.struct;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ResStringPool  extends struct {
    public ResStringPool_Header stringPool_header;
    public ArrayList<Integer> string_offs;
    public ArrayList<Integer> style_offs;
    public ArrayList<ResStringPool_Item> strings;
//    public ArrayList<ResStringPool_Item> styles = new ArrayList<ResStringPool_Item>();

    public ResStringPool(byte[] data, int off) {
        super(data, off);
    }


    @Override
    public void parseData() {
        stringPool_header = new ResStringPool_Header(this.cursor.getData(),this.cursor.getPos());
        string_offs = new ArrayList<Integer>();
        style_offs  = new ArrayList<Integer>();
        strings = new ArrayList<ResStringPool_Item>();
        this.cursor.belowMove(stringPool_header.getLength());
        for(int i = 0; i < stringPool_header.string_count; i++)
        {
            string_offs.add(this.cursor.nextInt());
        }
        for(int i = 0; i < stringPool_header.style_count; i++)
        {
            style_offs.add(this.cursor.nextInt());
        }
        int begin = this.getFileOff() + stringPool_header.stringsStart;
        for(int off : string_offs)
        {
            ResStringPool_Item item = new ResStringPool_Item(this.cursor.getData(),begin + off,stringPool_header.flag);
            strings.add(item);
        }
//        styles is can't parse ,direct skip
//        begin = this.getFileOff() + stringPool_header.stylesStart;
//        for(int off : style_offs)
//        {
//            ResStringPool_Item item = new ResStringPool_Item(this.cursor.getData(),begin + off);
//            styles.add(item);
//        }
    }

    @Override
    public int getLength() {
        return stringPool_header.chunk_header.Chunk_Size;
    }
}
