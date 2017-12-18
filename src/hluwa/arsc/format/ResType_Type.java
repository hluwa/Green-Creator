package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Type;

import java.util.*;

public class ResType_Type  extends Arsc_Type {
    public int entriesStart;
    public ResType_Config config;
    public ArrayList<Integer> entryOffsets;
    public ArrayList<ResType_MapEntry> mapEntries;
    public Map<ResType_Entry,ResType_Value> resEntrys;

    public ResType_Type(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        super.parseData();
        entryOffsets = new ArrayList<Integer>();
        resEntrys = new LinkedHashMap<ResType_Entry, ResType_Value>();
        mapEntries = new ArrayList<>();
        entriesStart = this.cursor.nextInt();
        config = new ResType_Config(this.cursor.getData(),this.cursor.getPos());
        this.cursor.belowMove(config.getLength());
        for(int i = 0; i < this.entryCount; i++)
        {
            entryOffsets.add(this.cursor.nextInt());
        }
        for(int i : entryOffsets)
        {

            ResType_Entry entry = new ResType_Entry(this.cursor.getData(),this.getFileOff() + this.entriesStart + i);
            if((entry.flags & 0x0001) != 0)
            {
                ResType_MapEntry mapEntry = new ResType_MapEntry(this.cursor.getData(),this.getFileOff() + this.entriesStart + i);
                mapEntries.add(mapEntry);
            }
            else
            {
                ResType_Value value = new ResType_Value(this.cursor.getData(),entry.getFileOff() + entry.getLength());
                resEntrys.put(entry,value);
            }

        }

    }

    @Override
    public int getLength() {
        int entrysLength = 0;
        for(ResType_Entry entry : resEntrys.keySet())
        {
            entrysLength += entry.getLength();
            entrysLength += resEntrys.get(entry).getLength();
        }
        return super.getLength() + 4 + config.getLength() + entryOffsets.size() * 4 + entrysLength;
    }
}
