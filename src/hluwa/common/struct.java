package hluwa.common;

public abstract class struct {
    protected int file_off;
    protected ByteCursor cursor;

    public struct(byte data[], int off)
    {
        this.file_off = off;
        cursor = new ByteCursor(data);
        if(off < 0 || off > data.length)
        {
            return;
        }
        cursor.move(file_off);
        parseData();
    }
    public abstract void parseData();
    public abstract int getLength();
    public int getFileOff() {
        return file_off;
    }
}
