package hluwa.arsc.format;

import hluwa.common.struct;

public class ResType_Map extends struct {
    public int name;
    public ResType_Value value;
    public ResType_Map(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        this.name = this.cursor.nextInt();
        this.value = new ResType_Value(this.cursor.getData(),this.cursor.getPos());
        this.cursor.belowMove(value.getLength());
    }

    @Override
    public int getLength() {
        return  4 + this.value.getLength();
    }
}
