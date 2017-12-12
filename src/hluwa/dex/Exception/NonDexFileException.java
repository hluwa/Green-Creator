package hluwa.dex.Exception;

@SuppressWarnings("serial")
public class NonDexFileException extends Exception {

	public NonDexFileException(String filePath) {
		super("Not Found DEX_MAGIC in file: " + filePath);
	}

}
