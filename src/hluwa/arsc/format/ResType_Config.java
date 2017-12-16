package hluwa.arsc.format;

import hluwa.arsc.base.Arsc_Struct;
import hluwa.common.struct;

public class ResType_Config extends struct {
    public int size;
    public int imsi;
    public int locale;
    public int screenType;
    public int input;
    public int screenSize;
    public int version;
    public int screenConfig;
    public int screenSizeDp;
    public byte[] localScript;
    public byte[] localeVariant;
//    int screenConfig2;

    public ResType_Config(byte[] data, int off) {
        super(data, off);
    }

    @Override
    public void parseData() {
        this.size = this.cursor.nextInt();
        this.imsi = this.cursor.nextInt();
        this.locale = this.cursor.nextInt();
        this.screenType = this.cursor.nextInt();
        this.input = this.cursor.nextInt();
        this.screenSize = this.cursor.nextInt();
        this.version = this.cursor.nextInt();
        this.screenConfig = this.cursor.nextInt();
        this.screenSizeDp = this.cursor.nextInt();
        this.localScript = this.cursor.nextData(4);
        this.localeVariant = this.cursor.nextData(8);
//        this.screenConfig2 = this.cursor.nextInt();
    }

    @Override
    public int getLength() {
        return this.size;
    }
}
