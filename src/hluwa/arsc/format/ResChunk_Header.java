package hluwa.arsc.format;

import hluwa.common.struct;

public class ResChunk_Header extends struct {
    public static final short
            RES_NULL_TYPE               = 0x0000,
            RES_STRING_POOL_TYPE        = 0x0001,
            RES_TABLE_TYPE              = 0x0002,
            RES_XML_TYPE                = 0x0003,
            RES_XML_FIRST_CHUNK_TYPE    = 0x0100,
            RES_XML_START_NAMESPACE_TYPE= 0x0100,
            RES_XML_END_NAMESPACE_TYPE  = 0x0101,
            RES_XML_START_ELEMENT_TYPE  = 0x0102,
            RES_XML_END_ELEMENT_TYPE    = 0x0103,
            RES_XML_CDATA_TYPE          = 0x0104,
            RES_XML_LAST_CHUNK_TYPE     = 0x017f,
            RES_XML_RESOURCE_MAP_TYPE   = 0x0180,
            RES_TABLE_PACKAGE_TYPE      = 0x0200,
            RES_TASHBLE_TYPE_TYPE       = 0x0201,
            RES_TABLE_TYPE_SPEC_TYPE    = 0x0202;

    public ResChunk_Header(byte[] data, int off) {
        super(data, off);
    }


    public short Chunk_Type;
    public short Chunk_Head_Size;
    public int Chunk_Size;

    @Override
    public void parseData() {
        Chunk_Type = this.cursor.nextShort();
        Chunk_Head_Size = this.cursor.nextShort();
        Chunk_Size = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return 8;
    }
}
