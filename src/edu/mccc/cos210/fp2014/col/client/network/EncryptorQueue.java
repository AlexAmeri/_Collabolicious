package edu.mccc.cos210.fp2014.col.client.network;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.BadPaddingException;
import java.util.concurrent.ConcurrentLinkedQueue;
public class EncryptorQueue {
    	private PublicKey publicKey;	
    	private ConcurrentLinkedQueue<String> queue;
	public EncryptorQueue(PublicKey publicKey) {
		queue = new ConcurrentLinkedQueue<String>();
		this.publicKey = publicKey;
	}
	public boolean add(String s) {
		boolean returnVal = false;	
		try {
			returnVal = queue.add(getHexString(encrypt(s, this.publicKey)));	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return returnVal;
	}
	public String poll() {
		return queue.poll();
	}
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	public ConcurrentLinkedQueue<String> getQueue() {
		return queue;
	}
	private byte[] encrypt(String text, PublicKey publicKey) 
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, 
			NoSuchAlgorithmException, NoSuchPaddingException {	   
   		Cipher cipher = Cipher.getInstance("RSA");
   		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      		byte[] textToBytes = text.getBytes();
      		byte[] interm = new byte[100];
      		byte[] toReturn = new byte[0];
      		byte[] arr;
      		byte[] cipherData;
      		int a = textToBytes.length - (textToBytes.length % interm.length);
      		int i = 0;
      		while (i != a) {
      			for (int x = 0; x < interm.length; x++) {
        	      		interm[x] = textToBytes[i];
            	  		i++;
         	 	}
         		cipherData = cipher.doFinal(interm);
          		toReturn = append(toReturn, cipherData);
      		}
      		arr = new byte[textToBytes.length - a];	
      		int r = 0;
      		for (int b = a; b < textToBytes.length; b++) {
       			arr[r] = textToBytes[b];
         		r++;
      		}
      		cipherData = cipher.doFinal(arr);
      		toReturn = append(toReturn, cipherData);
      		return toReturn;
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
    	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}                
}