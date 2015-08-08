package madmarcos;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import org.json.JSONArray;
import org.json.JSONObject;

public class CryptoStuff {
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static String hashSha256(String plainPW) {
		//calc and return sha 256 hash of plainPW
		if(plainPW == null || plainPW.length() < 1)
			return null;
		String h = null;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(plainPW.getBytes(Charset.forName("UTF-8")));
			byte[] hash = md.digest();

			h = bytesToHex(hash);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return h;
	}
	
	public static String addCheckSum(JSONArray arr){ //creates and appends checksum object
		if (arr == null) {
			throw new IllegalArgumentException("array is null");
		}
		JSONArray array = arr;
		String hash = CryptoStuff.hashSha256(arr.toString()); //creates checksum value
		JSONObject o = new JSONObject();
		o.put("checksum", hash);
		array.put(o); //adds checksum object to array
		return array.toString(); //returns string
	}
	
	public static String addCheckSum(JSONArray arr, String salt){ //creates and appends checksum object with salt
		if (arr == null) {
			throw new IllegalArgumentException("array is null");
		}
		JSONArray array = arr;
		String hash = CryptoStuff.hashSha256(arr.toString()+salt); //creates and appends checksum object with salt
		JSONObject o = new JSONObject();
		o.put("checksum", hash); 
		array.put(o); //creates and appends checksum object
		return array.toString(); //returns string
	}
	
	public static JSONObject getJSONObjectFromKey(JSONArray a, String key) { //from professor's code
		if(a == null)
			throw new IllegalArgumentException("JSONArray a cannot be null!");
		if(key == null)
			throw new IllegalArgumentException("String key cannot be null!");
		for(int i = 0; i < a.length(); i++) {
			Object o = a.get(i); 
			if(o instanceof JSONObject) {
				JSONObject jo = (JSONObject) o;
				if(jo.has(key))
					return jo;
			}
			
		}
		//fall through: object with key not found
		return null;
	}
}
