package hluwa.arsc;

import hluwa.arsc.format.*;
import hluwa.common.ByteCursor;
import hluwa.dex.format.DexFile;

import java.io.File;
import java.util.ArrayList;

public class ARSCParser {
    public static ArscFile ParseArsc(byte[] body)
    {
        ByteCursor cursor = new ByteCursor(body);
        return ParseArsc(cursor);
    }
    public static ArscFile ParseArsc(ByteCursor cursor)
    {

        ArscFile file = new ArscFile();
        ResTable_Header header = new ResTable_Header(cursor.getData(),0);
        ResStringPool stringPool = null;
        ArrayList<ResPackage> packages = new ArrayList<ResPackage>();
        int cur = header.chunk_header.Chunk_Head_Size;
        int end = cursor.getLength();
        while(cur < end)
        {
            ResChunk_Header chunk_header = new ResChunk_Header(cursor.getData(),cur);
            switch(chunk_header.Chunk_Type)
            {
                case ResChunk_Header.RES_STRING_POOL_TYPE:
                    stringPool = new ResStringPool(cursor.getData(),cur);
                    break;
                case ResChunk_Header.RES_TABLE_PACKAGE_TYPE:
                    packages.add(new ResPackage(cursor.getData(),cur));
                    break;
            }
            cur += chunk_header.Chunk_Size;
        }
        file.setTable_header(header);
        file.setStringPool(stringPool);
        file.setPackages(packages);
        return file;
    }
    public static ArscFile ParseArsc(File file)
    {
        ByteCursor cursor = new ByteCursor(file);
        return ParseArsc(cursor);
    }
}
