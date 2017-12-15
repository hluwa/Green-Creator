package hluwa.arsc.format;

import java.util.ArrayList;

public class ArscFile {
    ResTable_Header table_header;
    ResStringPool stringPool;
    ArrayList<ResPackage> packages = new ArrayList<ResPackage>();

    public ResTable_Header getTable_header() {
        return table_header;
    }

    public void setTable_header(ResTable_Header table_header) {
        this.table_header = table_header;
    }

    public ResStringPool getStringPool() {
        return stringPool;
    }

    public void setStringPool(ResStringPool stringPool) {
        this.stringPool = stringPool;
    }

    public ArrayList<ResPackage> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<ResPackage> packages) {
        this.packages = packages;
    }
}
