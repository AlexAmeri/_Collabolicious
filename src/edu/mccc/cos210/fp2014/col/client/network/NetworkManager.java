package edu.mccc.cos210.fp2014.col.client.network;
import edu.mccc.cos210.fp2014.col.client.objects.PointModel;
import edu.mccc.cos210.fp2014.col.client.objects.TextboxModel;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.network.EncryptorQueue;
import edu.mccc.cos210.fp2014.col.client.network.DecryptorQueue;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import javax.swing.Timer;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.KeyGenerator;
import java.security.PublicKey;
import java.io.FileInputStream;
import java.io.File;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
/**
* Handles all network actions from the client to the server
*/
public class NetworkManager {
	private ConcurrentLinkedQueue<String> sendQueue;
	private ConcurrentLinkedQueue<String> recieveQueue;
	private WhiteboardModel whiteboard;
	private EncryptorQueue sessionSendQueue;
	private DecryptorQueue sessionRecieveQueue;
	private EncryptorQueue housekeepingSendQueue;
	private DecryptorQueue housekeepingReceiveQueue;
	private BufferedReader in;
	private PrintWriter out;
	private BufferedReader sessionIn;
	private PrintWriter sessionOut;
	private RecieveThread reciever;
	private Socket housekeepingSocket;
	private int count = 0;
	private String username;
	private String password;
	private KeyPair keypair;
	private KeyPairGenerator keyGen;
	private KeyPair keyPair;
	private PublicKey publicKey;
	private PublicKey serversPublicKey;
	private SecretKey secretKey;
	private PrivateKey privateKey;
	private boolean recieveThreadAllowedToRun = true;
	private final String SERVER_ADDRESS = "24.0.251.221";
	public NetworkManager() {
		generateKeyPair();
		generateBlowfishKey();
		sendQueue = new ConcurrentLinkedQueue<String>();
		recieveQueue = new ConcurrentLinkedQueue<String>();
		try {
			this.sessionSendQueue = new EncryptorQueue(getServersPublKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.sessionRecieveQueue = new DecryptorQueue(this.privateKey);
	}
	public NetworkManager(String username, String password) {
		this.username = username;
		this.password = password;
		generateKeyPair();
		generateBlowfishKey();
		sendQueue = new ConcurrentLinkedQueue<String>();
		recieveQueue = new ConcurrentLinkedQueue<String>();
		try {
			this.sessionSendQueue = new EncryptorQueue(getServersPublKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.sessionRecieveQueue = new DecryptorQueue(this.privateKey);
	}
	public NetworkManager(WhiteboardModel whiteboard) throws IOException {
		this.whiteboard = whiteboard;
		generateKeyPair();
		generateBlowfishKey();
		sendQueue = new ConcurrentLinkedQueue<String>();
		recieveQueue = new ConcurrentLinkedQueue<String>();
		try {
			this.sessionSendQueue = new EncryptorQueue(getServersPublKey());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		this.sessionRecieveQueue = new DecryptorQueue(this.privateKey);
	}
	public PublicKey getServersPublKey() 
			throws Exception {
		File filePublicKey = new File(getClass().getResource("../security/ServerPublic.key").toURI());
		FileInputStream fis = new FileInputStream(filePublicKey);
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		this.serversPublicKey = publicKey;
		return publicKey;
	}
	private void generateBlowfishKey() {
		try {
			KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");
			SecretKey secretKey = keygenerator.generateKey();
			this.secretKey = secretKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void generateKeyPair() {
		//Create private fields for a p
		try { 
		 	this.keyGen = KeyPairGenerator.getInstance("RSA");
  	  	 	this.keyGen.initialize(1024);
  	  	 	this.keyPair = keyGen.genKeyPair();
   	  	 	this.privateKey = keyPair.getPrivate();
       	 	this.publicKey = keyPair.getPublic();
       	 } catch (Exception e) {
       	 	e.printStackTrace();
       	 }	
	}
	public void setWhiteboardModel(WhiteboardModel w) {
		this.whiteboard = w;
	}
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return this.username;
	}
	/**
	 * Allows user to register
	 * @throws IOException 
	 */
	public boolean sendRegisterMessage(
				String uName,String pWord
			) throws Exception {
		this.housekeepingSocket = new Socket(SERVER_ADDRESS, 3001);
		this.in = new BufferedReader(
			new InputStreamReader(this.housekeepingSocket.getInputStream())
		);
		this.out = new PrintWriter(housekeepingSocket.getOutputStream(), true);
		housekeepingSendQueue = new EncryptorQueue(getServersPublKey());
		housekeepingSendQueue.add(new String("1/" + uName + "/" + pWord + "/ / /" 
						+ getHexString(this.publicKey.getEncoded()) + "/ /"
						));
		String message = housekeepingSendQueue.poll();
		System.out.println(message);
		this.out.println(message);
		String response = null;
		while (response == null) {
			response = this.in.readLine();
		}
		housekeepingReceiveQueue = new DecryptorQueue(privateKey);
		housekeepingReceiveQueue.add(response);
		String s = housekeepingReceiveQueue.poll();
		this.housekeepingSocket.close();
		return (!s.equals("2"));
	}
	/**
	 * Logs user out of their session
	 */
	public String sendLogoutMessage() throws Exception {
		this.housekeepingSocket = new Socket(SERVER_ADDRESS, 3001);
		this.in = new BufferedReader(
			new InputStreamReader(this.housekeepingSocket.getInputStream())
		);
		this.out = new PrintWriter(
			this.housekeepingSocket.getOutputStream(),true
		);
		housekeepingSendQueue = new EncryptorQueue(getServersPublKey());
		String response = null;
		housekeepingSendQueue.add(new String("3/" + this.username + "/" + " / / / / /"));
		String message = housekeepingSendQueue.poll();
		this.out.println(message);
		while (response == null) {
			response = this.in.readLine();
		}
		housekeepingReceiveQueue = new DecryptorQueue(privateKey);
		housekeepingReceiveQueue.add(response);
		response = housekeepingReceiveQueue.poll();
		this.housekeepingSocket.close();
		return response;
	}
	/**
	 * Logs the user in
	 * @throws IOException 
	 */
	public String sendLoginMessage() throws Exception {
		this.housekeepingSocket = new Socket(SERVER_ADDRESS, 3001);
		this.in = new BufferedReader(
			new InputStreamReader(this.housekeepingSocket.getInputStream())
		);
		this.out = new PrintWriter(housekeepingSocket.getOutputStream(), true);
		String message;
		System.out.println(this.publicKey);
		housekeepingSendQueue = new EncryptorQueue(getServersPublKey());
		housekeepingSendQueue.add(new String("0/" + this.username + "/" + this.password + "/ / /" 
								+ getHexString(this.publicKey.getEncoded()) + "/"
								+ getHexString(this.secretKey.getEncoded()) + "/")
								);
		message = housekeepingSendQueue.poll();
		this.out.println(message);
		String response = null;
		while (response == null) {
			response = this.in.readLine();
		}
		housekeepingReceiveQueue = new DecryptorQueue(privateKey);
		housekeepingReceiveQueue.add(response);
		response = housekeepingReceiveQueue.poll();
		this.housekeepingSocket.close();
		return response;
	}
	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	private byte[] hexConvert(String hex) {
		int length = hex.length();
		byte[] finalFileData = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
		 	finalFileData[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
							+ Character.digit(hex.charAt(i+1), 16)
							);
		}
		return finalFileData;
	}
	public String sendCreateWhiteboardMessage(
				String whiteboardName
			) throws Exception {
		this.housekeepingSocket = new Socket(SERVER_ADDRESS, 3001);
		in = new BufferedReader(
			new InputStreamReader(this.housekeepingSocket.getInputStream())
		);
		this.out = new PrintWriter(housekeepingSocket.getOutputStream(), true);
		String message;
		housekeepingSendQueue = new EncryptorQueue(getServersPublKey());
		housekeepingSendQueue.add(new String("5/" + this.username + "/" + " /" + whiteboardName + "/ / / /"));
		message = housekeepingSendQueue.poll();
		this.out.println(message);
		String response = null;
		while (response == null) {
			response = this.in.readLine();
		}
		housekeepingReceiveQueue = new DecryptorQueue(privateKey);
		housekeepingReceiveQueue.add(response);
		response = housekeepingReceiveQueue.poll();
		this.housekeepingSocket.close();
		return response;
	}
	public String sendGetWhiteboardDataMessage(
				String whiteboardName
			) throws Exception {
		this.housekeepingSocket = new Socket(SERVER_ADDRESS, 3002);
		this.in = new BufferedReader(
			new InputStreamReader(housekeepingSocket.getInputStream())
		);
		this.out = new PrintWriter(
			this.housekeepingSocket.getOutputStream(), true
		);
		Cipher cipher = Cipher.getInstance("Blowfish");
		String message = new String("8/" + username + "/" + " /" + whiteboardName + "/ / / /");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);	
    	byte[] encrypted = cipher.doFinal(message.getBytes());
		String messageF = this.username + "+" + getHexString(encrypted);
		this.out.println(messageF);
		System.out.println("Request sent");
		String response = null;
		while (response == null) {
			response = this.in.readLine();
		}
		cipher.init(Cipher.DECRYPT_MODE, secretKey);	
    	byte[] decrypted = cipher.doFinal(hexConvert(response));
		String answer = new String(decrypted);
		this.housekeepingSocket.close();
		return answer;
	}
	public String sendShareWhiteboardMessage(
				String whiteboardName, String targetsName
			) throws Exception {
		String userName = this.username;
		housekeepingSocket = new Socket(SERVER_ADDRESS, 3001);
		in = new BufferedReader(
			new InputStreamReader(housekeepingSocket.getInputStream())
		);
		out = new PrintWriter(housekeepingSocket.getOutputStream(), true);
		String response = null;
		String message;
		housekeepingSendQueue = new EncryptorQueue(getServersPublKey());
		message = "6/" + this.username + "/" + " /" + whiteboardName + "/";
		message += targetsName + "/ / /";
		housekeepingSendQueue.add(message);
		message = housekeepingSendQueue.poll();
		this.out.println(message);
		while (response == null) {
			response = this.in.readLine();
		}
		housekeepingReceiveQueue = new DecryptorQueue(privateKey);
		housekeepingReceiveQueue.add(response);
		response = housekeepingReceiveQueue.poll();
		this.housekeepingSocket.close();
		return response;
	}
	public boolean sendCheckIfUserExistsMessage(String targetsName) throws Exception {
		this.housekeepingSocket = new Socket(SERVER_ADDRESS, 3001);
		this.in = new BufferedReader(
			new InputStreamReader(this.housekeepingSocket.getInputStream())
		);
		this.out = new PrintWriter(housekeepingSocket.getOutputStream(), true);
		String message;
		housekeepingSendQueue = new EncryptorQueue(getServersPublKey());
		message = "10/" + this.username + "/ / /" + targetsName + "/ / /";
		housekeepingSendQueue.add(message);
		message = housekeepingSendQueue.poll();
		this.out.println(message);
		String response = null;
		while (response == null) {
			response = this.in.readLine();
		}
		housekeepingReceiveQueue = new DecryptorQueue(privateKey);
		housekeepingReceiveQueue.add(response);
		response = housekeepingReceiveQueue.poll();
		this.housekeepingSocket.close();
		return response.equals("12");
	}
	public void parseData(String s) {
		if (!s.equals("*")) {
			this.whiteboard.updateData(
				s.substring(whiteboard.getName().length() + 1, s.length())
			);
		}
	}
	/**
	 * Retrieves all the data from the server pretaining to the logged in user
	 * When the timer calls it
	 */
	public void getDataFromServer() {		
		String messageToInterpret = "";
		synchronized (recieveQueue) {
			for (int i = 0; i < recieveQueue.size(); i++) {
				String s = recieveQueue.poll();
				parseData(s);
				messageToInterpret += s;
			}
		}
		count += 1;
	}
	/**
	 * Sends update data whenever this method
	 * gets called by the WhiteboardModel, should accept
	 * a String which describes the whiteboard object that
	 * was just made (its getInfo() method)
	 */
	public void sendData(String s) {
		synchronized(this.sendQueue) {
			this.sendQueue.add(s);
		}
	}
	public void startRecieveThread() throws IOException {
		Timer repaintCaller = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (recieveThreadAllowedToRun && whiteboard != null) {
					try {
						reciever = new RecieveThread();
					} catch (Exception e) {
						e.printStackTrace(); 
					}
					reciever.start();
					getDataFromServer();
				}
			}
		});
		repaintCaller.start();
	}
	public void setRecieveThreadStatus(boolean isRunning) {
		this.recieveThreadAllowedToRun = isRunning;
	}
	private boolean verifySignature(byte[] data, byte[] signature)
			throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
        	md.update(password.getBytes());
       	 	byte byteData[] = md.digest();
        	byte[] decryptedDigest = RSAdecrypt(signature);
        	String a = getHexString(byteData);
        	String b = getHexString(decryptedDigest);
        	return a.equals(b);
	}
	private byte[] generateSignature(byte[] dataToSign)
			throws Exception {
	    	MessageDigest md = MessageDigest.getInstance("SHA-256");
        	md.update(password.getBytes());
        	byte byteData[] = md.digest();
        	return RSAencrypt(byteData, this.privateKey);
	}
	private byte[] RSAdecrypt(byte[] data) 
			throws Exception {		  
        	Cipher cipher = Cipher.getInstance("RSA");
        	cipher.init(Cipher.DECRYPT_MODE, this.serversPublicKey);
        	byte[] encryptedData = data;
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
	   	return returnTo;
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
	private byte[] RSAencrypt(byte[] text, PrivateKey privateKey) 
			throws Exception {	   
   		Cipher cipher = Cipher.getInstance("RSA");
   		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
      		byte[] textToBytes = text;
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
	/**
	 * This thread sends the users' updates,
	 * and in response gets updates
	 * from other users. This thread is used exclusively for session data.
	 */
	private class RecieveThread extends Thread {
		private BufferedReader in;
		private PrintWriter out;
		private BufferedReader sessionIn;
		private PrintWriter sessionOut;
		private Socket sessionTalker;
		private Cipher cipher;
		private final int SESSION_PORT = 3002;
		/**
		 * The constructor which initializes the cipher
		 */
		public RecieveThread() throws Exception {	
			 this.cipher = Cipher.getInstance("Blowfish");
		}
		/**
		 * A new thread gets called by the timer every 0.1 seconds.
		 * In the thread, everything on the sendQueue gets sent to the server,
		 * and the reply (which contains updates made by other users) gets
		 * parsed.
		 */
		@SuppressWarnings("finally")
		@Override
		public void run() {
			try {
				String myUpdates = "";
				String part1;
				part1 = "9/" + username + "/ /" + whiteboard.getName() + "/ /";
				String part2 = "/";
				synchronized (NetworkManager.this.sendQueue) {
					if (NetworkManager.this.sendQueue.isEmpty()) {
						myUpdates += "*";
					} else {
						System.out.println("not empty");
						while (!NetworkManager.this.sendQueue.isEmpty()) {
							myUpdates += NetworkManager.this.sendQueue.poll();
						}
						System.out.println(myUpdates + " end updates");
					}
				}
				String finalMessage = part1 + myUpdates + part2;
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);	
    				byte[] encrypted = cipher.doFinal(finalMessage.getBytes()); 
				String finalM = username + "+" + getHexString(encrypted);
				this.sessionTalker = new Socket(SERVER_ADDRESS, SESSION_PORT);
				this.sessionIn = new BufferedReader(
					new InputStreamReader(this.sessionTalker.getInputStream())
				);
				this.sessionOut = new PrintWriter(
					this.sessionTalker.getOutputStream(), true
				);
				System.out.println(finalM);
				sessionOut.println(finalM);
				try {
					String otherUsersUpdates = null;
					while (otherUsersUpdates == null) {
						otherUsersUpdates = this.sessionIn.readLine();
					}
					cipher.init(Cipher.DECRYPT_MODE, secretKey);	
    				byte[] decrypted = cipher.doFinal(hexConvert(otherUsersUpdates)); 
					synchronized (NetworkManager.this.recieveQueue) {
						NetworkManager.this.recieveQueue.add(
							new String(decrypted)
						);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					this.sessionTalker.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
}
