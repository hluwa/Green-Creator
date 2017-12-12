package hluwa.common.Exception;

@SuppressWarnings("serial")
public class CursorMoveException extends Exception {
	public CursorMoveException(int size, int pos) {
		super("data size = " + size + ", want move to pos = " + pos);
	}
}
