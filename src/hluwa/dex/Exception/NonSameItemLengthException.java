package hluwa.dex.Exception;

@SuppressWarnings("serial")
public class NonSameItemLengthException extends Exception {
	public NonSameItemLengthException(String itemname, int itemlen, int datalen) {
		super("Can't parse this data to " + itemname + "item, itemlen = " + itemlen + ",but datalen = " + datalen);
	}
}
