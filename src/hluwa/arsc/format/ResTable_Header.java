package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Struct;
import hluwa.common.struct;

public class ResTable_Header extends Arsc_Struct {
    public int packageCount;

    public ResTable_Header(byte[] data, int off) {
        super(data, off);
    }


    @Override
    public void parseData() {
        super.parseData();
        packageCount = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return super.getLength() + 4;
    }
}
