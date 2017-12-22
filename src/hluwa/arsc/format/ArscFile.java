package hluwa.arsc.format;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArscFile {
    public ResTable_Header table_header;
    public ResStringPool stringPool;
    public ArrayList<ResPackage> packages;


    public String[] getAllString()
    {
        ArrayList<String> strings = new ArrayList<String>();
        for(ResStringPool_Item item : stringPool.strings)
        {
            strings.add(item.toString());
        }
        for(ResPackage resPackage : packages)
        {
            for(ResStringPool_Item item : resPackage.keyStrings.strings)
            {
                strings.add(item.toString());
            }
            for(ResStringPool_Item item : resPackage.typeStrings.strings)
            {
                strings.add(item.toString());
            }
        }

        return strings.toArray(new String[]{});
    }

    public String getStringById(int id)
    {
        String result = "";
        int packageId = id >> 24 & 0xFF;
        int typeId = id >> 16 & 0xFF;
        int entryId = id & 0xFFFF;
        for(ResPackage resPackage : packages)
        {
            if(resPackage.package_id == packageId)
            {
                int num = 0;
                for(ResType_Type type : resPackage.typeTypes)
                {
                    if(type.id != typeId)
                    {
                        continue;
                    }
                    for(ResType_MapEntry mapEntry : type.mapEntries)
                    {
                        for(ResType_Map map : mapEntry.maps)
                        {
                            if(num == entryId)
                            {
                                if(map.value.dataType == 1)
                                {
                                    return getStringById(map.value.data);
                                }
                               return this.stringPool.strings.get(map.value.data).toString();
                            }
                            num++;
                        }
                    }
                    for(ResType_Value value : type.resEntrys.values())
                    {
                        if(num == entryId)
                        {
                            if(value.dataType == 1)
                            {
                                return getStringById(value.data);
                            }
                            return this.stringPool.strings.get(value.data).toString();
                        }
                        num++;
                    }
                }
            }
        }
        return result;
    }
}
