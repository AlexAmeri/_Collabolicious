package edu.mccc.cos210.fp2014.col.client.network;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.BadPaddingException;
import java.util.concurrent.ConcurrentLinkedQueue;
public class DecryptorQueue {
	private PrivateKey privateKey;	
    	private ConcurrentLinkedQueue<String> queue;
	public DecryptorQueue(PrivateKey privateKey) {
		queue = new ConcurrentLinkedQueue<String>();
		this.privateKey = privateKey;
	}
	public String poll() {
		String string = "";
		String s = queue.poll();
		try {
			string = decrypt(s, this.privateKey);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return string;
	}
	public void add(String s) {
		queue.add(s);
	}
	public ConcurrentLinkedQueue<String> getQueue() {
		return queue;
	}
	public int size() {
		return queue.size();
	}
	private String decrypt(String data, PrivateKey privateKey) 
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, 
			NoSuchAlgorithmException, NoSuchPaddingException {		  
        	Cipher cipher = Cipher.getInstance("RSA");
       		cipher.init(Cipher.DECRYPT_MODE, privateKey);
        	byte[] encryptedData = hexConvert(data);
        	byte[] interm = new byte[128];
        	byte[] returnTo = new byte[0];
        	byte[] cipherData;
        	int a = encryptedData.length - (encryptedData.length % interm.length);
        	int i = 0;
        	while (i != a) {
      	    		for (int x = 0; x < interm.length; x++) {
                		interm[x] = encryptedData[i];
                		i++;
            		}
            		cipherData = cipher.doFinal(interm);
            		returnTo = append(returnTo, cipherData); 
        	} 
	    	return new String(returnTo);
    	}  
    	private byte[] append(byte[] pre, byte[] post) {
    		byte[] toReturn = new byte[pre.length + post.length];
        	for (int i = 0; i < pre.length; i++) {
            		toReturn[i] = pre[i];
        	}
        	for(int i = 0; i < post.length; i++) {
            		toReturn[i + pre.length] = post[i];
        	}
        	return toReturn;
    	}
    	private byte[] hexConvert(String hexData) {
		int length = hexData.length();
		byte[] finalFileData = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
		 	finalFileData[i / 2] = (byte) ((Character.digit(hexData.charAt(i), 16) << 4)
							+ Character.digit(hexData.charAt(i+1), 16)
							);
		}
		return finalFileData;
	}
}