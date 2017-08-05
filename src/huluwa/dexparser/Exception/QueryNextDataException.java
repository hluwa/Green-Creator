package huluwa.dexparser.Exception;

@SuppressWarnings("serial")
public class QueryNextDataException extends Exception {
	public QueryNextDataException(int length, int size) {
		super("data length = " + length + ", want query size = " + size);
	}
}
