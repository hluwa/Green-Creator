package hluwa.dex.Exception;

@SuppressWarnings("serial")
public class NonStandardLeb128Exception extends Exception {

	public NonStandardLeb128Exception(byte end, int length) {
		super("end byte is 0x" + Integer.toHexString(end) + ", bin is " + Integer.toBinaryString(end) + ",lenght = "
				+ length);
	}
}
