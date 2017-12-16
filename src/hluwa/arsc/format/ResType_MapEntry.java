package hluwa.arsc.format;

import hluwa.common.struct;

import java.util.ArrayList;

public class ResType_MapEntry extends struct {
    public ResType_Entry entry;
    public int parentRef;
    public int count;
    public ArrayList<ResType_Map> maps;
    public ResType_MapEntry(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        maps = new ArrayList<ResType_Map>();
        this.entry = new ResType_Entry(this.cursor.getData(),this.cursor.getPos());
        this.cursor.belowMove(entry.getLength());
        this.parentRef = this.cursor.nextInt();
        this.count = this.cursor.nextInt();
        for(int i = 0; i < count; i++)
        {
            ResType_Map map = new ResType_Map(this.cursor.getData(),this.cursor.getPos());
            maps.add(map);
            this.cursor.belowMove(map.getLength());
        }
    }

    @Override
    public int getLength() {
        int mapLength = 0;
        for(ResType_Map m : maps)
        {
            mapLength += m.getLength();
        }
        return entry.getLength() + 8 + mapLength;
    }
}
