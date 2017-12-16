package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Type;

import java.util.ArrayList;

public class ResType_Spec extends Arsc_Type {
    public ArrayList<Integer> entrys;

    public ResType_Spec(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        super.parseData();
        entrys = new ArrayList<Integer>();
        for(int i = 0; i < this.entryCount; i++)
        {
            entrys.add(this.cursor.nextInt());
        }
    }

    @Override
    public int getLength() {
        return super.getLength() + entrys.size() * 4;
    }
}
