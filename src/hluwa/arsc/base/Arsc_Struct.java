package hluwa.arsc.base;

import hluwa.arsc.format.ResChunk_Header;
import hluwa.common.struct;

public abstract class Arsc_Struct extends struct {
    public ResChunk_Header chunk_header;
    public Arsc_Struct(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        chunk_header = new ResChunk_Header(this.cursor.getData(),this.cursor.getPos());
        this.cursor.belowMove(chunk_header.getLength());
    }

    @Override
    public int getLength() {
        return chunk_header.getLength();
    }
}
