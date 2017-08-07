import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

public class fix {
	public static void main(String args[]) {
		fix("C:\\Users\\huluwa\\Desktop\\classes.dex");
	}
	 public static void fix(String file) {  
	        byte[] fBytes = readFile(file);  
	        fix(fBytes);  
	        saveFile(fBytes, file);  
	    }  
	  
	    private static byte[] readFile(String file) {  
	        FileInputStream fis = null;  
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        try {  
	            fis = new FileInputStream(file);  
	            if (fis != null && bos != null) {  
	                int len = -1;  
	                byte[] buf = new byte[512];  
	                while ((len = fis.read(buf)) != -1) {  
	                    bos.write(buf, 0, len);  
	                    bos.flush();  
	                }  
	            }  
	  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (fis != null) {  
	                try {  
	                    fis.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	        byte[] fBytes = bos.toByteArray();  
	        if (bos != null) {  
	            try {  
	                bos.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return fBytes;  
	    }  
	  
	    private static void saveFile(byte[] fBytes, String file) {  
	        FileOutputStream fos = null;  
	        try {  
	            fos = new FileOutputStream(file);  
	            fos.write(fBytes);  
	            fos.flush();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (fos != null) {  
	                try {  
	                    fos.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	    }  
	  
	    private static void fix(byte[] fBytes) {  
	        fixSha1Signature(fBytes);  
	        fixChecksum(fBytes);  
	    }  
	  
	    private static void fixSha1Signature(byte[] fBytes) {  
	        MessageDigest sha1 = null;  
	        try {  
	            sha1 = MessageDigest.getInstance("SHA1");  
	            sha1.update(fBytes, 32, fBytes.length - 32);  
	            byte[] hashBytes = sha1.digest();  
	            for (int i = 0; i < hashBytes.length; i++) {  
	                fBytes[12 + i] = hashBytes[i];  
	            }  
	        } catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	    private static void fixChecksum(byte[] fBytes) {  
	        Adler32 al = new Adler32();  
	        al.update(fBytes, 12, fBytes.length - 12);  
	        int sum = (int) al.getValue();  
	        byte[] result = new byte[4];  
	        result[0] = (byte) sum;  
	        result[1] = (byte) (sum >> 8);  
	        result[2] = (byte) (sum >> 16);  
	        result[3] = (byte) (sum >> 24);  
	        for (int i = 0; i < result.length; i++) {  
	            fBytes[8 + i] = result[i];  
	        }  
	    }  
}
