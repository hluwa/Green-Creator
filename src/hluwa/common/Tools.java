package hluwa.common;

public class Tools {

	public static boolean isASCII(byte[] name) {
		int off = 0;
		while (off < name.length) {
			if ((name[off] < 48 && name[off] > 122) && name[off] != 0) {
				break;
			}
			off++;
		}
		return off != name.length ? false : true;
	}
}
