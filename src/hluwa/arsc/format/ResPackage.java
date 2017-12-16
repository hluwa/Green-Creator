package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Struct;
import hluwa.common.struct;

import java.util.ArrayList;

public class ResPackage extends Arsc_Struct {
    public int package_id;
    public byte[] package_name;
    public int typeStringOffset;
    public int lastPublicType;
    public int keyStringsOffset;
    public int lastPublicKey;
    public int lastIdOffset;
    public ResStringPool typeStrings;
    public ResStringPool keyStrings;

    public ArrayList<ResType_Spec> typeSpecs;
    public ArrayList<ResType_Type> typeTypes;
    public ResPackage(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        super.parseData();
        typeSpecs = new ArrayList<ResType_Spec>();
        typeTypes = new ArrayList<ResType_Type>();
        this.package_id = this.cursor.nextInt();
        this.package_name = this.cursor.nextData(256);
        this.typeStringOffset = this.cursor.nextInt();
        this.lastPublicType = this.cursor.nextInt();
        this.keyStringsOffset = this.cursor.nextInt();
        this.lastPublicKey = this.cursor.nextInt();
        this.lastIdOffset = this.cursor.nextInt();
        this.typeStrings = new ResStringPool(this.cursor.getData(),this.getFileOff() + this.typeStringOffset);
        this.keyStrings = new ResStringPool(this.cursor.getData(),this.getFileOff() + this.keyStringsOffset);
        int cur = this.getFileOff() + this.chunk_header.Chunk_Head_Size;
        int end = this.getFileOff() + this.chunk_header.Chunk_Size;
        while(cur < end)
        {
            ResChunk_Header header = new ResChunk_Header(this.cursor.getData(),cur);
            switch(header.Chunk_Type)
            {
                case ResChunk_Header.RES_TASHBLE_TYPE_TYPE:
                    typeTypes.add(new ResType_Type(this.cursor.getData(),cur));
                    break;
                case ResChunk_Header.RES_TABLE_TYPE_SPEC_TYPE:
                    typeSpecs.add(new ResType_Spec(this.cursor.getData(),cur));
                    break;
            }
            cur += header.Chunk_Size;
        }

    }

    @Override
    public int getLength() {
        return super.chunk_header.Chunk_Size;
    }
}
