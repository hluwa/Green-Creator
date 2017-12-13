package hluwa.arsc.format;

import hluwa.common.struct;

public class ResTable_Header extends struct {
    ResChunk_Header chunk_header;
    int packageCount;

    public ResTable_Header(byte[] data, int off) {
        super(data, off);
    }


    @Override
    public void parseData() {
        chunk_header = new ResChunk_Header(this.cursor.getData(),this.cursor.getPos());
        this.cursor.belowMove(chunk_header.getLength());
        packageCount = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return chunk_header.getLength() + 4;
    }
}
