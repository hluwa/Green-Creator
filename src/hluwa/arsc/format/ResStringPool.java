package hluwa.arsc.format;

import hluwa.common.struct;

import java.util.ArrayList;

public class ResStringPool  extends struct {
    ResStringPool_Header stringPool_header;
    ArrayList<Integer> string_offs = new ArrayList<Integer>();
    ArrayList<ResStringPool_Item> strings = new ArrayList<ResStringPool_Item>();

    public ResStringPool(byte[] data, int off) {
        super(data, off);
    }


    @Override
    public void parseData() {

        stringPool_header = new ResStringPool_Header(this.cursor.getData(),this.cursor.getPos());
        this.cursor.belowMove(stringPool_header.getLength());
        for(int i = 0; i < stringPool_header.string_count; i++)
        {
            string_offs.add(this.cursor.nextInt());
        }
        int begin = this.cursor.getPos();
        for(int off : string_offs)
        {
            ResStringPool_Item item = new ResStringPool_Item(this.cursor.getData(),this.cursor.getPos());
            strings.add(item);
            this.cursor.belowMove(item.getLength());
        }
    }

    @Override
    public int getLength() {
        return 0;
    }
}
