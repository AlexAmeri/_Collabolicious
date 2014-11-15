package edu.mccc.cos210.fp2014.col.server.users;
import edu.mccc.cos210.fp2014.col.server.whiteboard.Whiteboard;
import edu.mccc.cos210.fp2014.col.server.whiteboard.WhiteboardManager;
import edu.mccc.cos210.fp2014.col.server.ServerMasterManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class UserManager {
	private WhiteboardManager whiteboardManager;
	private ServerSocket listener;
	private ServerSocket sessionListener;
	private final int LISTENER_PORT = 3001;
	private final int SESSION_PORT = 3002;
	private ConcurrentHashMap<String, User> userDataBase;
	private ConcurrentHashMap<String, String> passwordDataBase;	
	private PrivateKey privateKey;
	public UserManager() {
		userDataBase = new ConcurrentHashMap<String, User>();
		passwordDataBase = new ConcurrentHashMap<String, String>();
		try {
			this.privateKey = getServersPrivKey();
		} catch (Exception e) {
		}
	}
	/**
	 * Used to start the socket after everything has been set up at startup time
	 * @throws IOException 
	 */
	public void initiate() throws IOException {
		try {
			listener = new ServerSocket(LISTENER_PORT);
			while (true) {
				new LoginListenerThread(listener.accept(), true).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			listener.close();
		}
	}
	public void initiateSessionPort() throws IOException {
		try {
			sessionListener = new ServerSocket(SESSION_PORT);
			System.out.println("listening on port 3002");
			while (true) {
				new LoginListenerThread(sessionListener.accept(), false).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			listener.close();
		}
	}
	/**
	*Used to check if the login credentials provided by a Client are valid
	*/
	public synchronized  boolean isLoginValid(String userName, String password) {
		boolean returnValue = false;
		if (passwordDataBase.containsKey(userName)) {
			if (password.equals(passwordDataBase.get(userName))) {
				returnValue = true;
			}
		}
		return returnValue;
	}
	/**
	* Used to set the 'Logged In' status of a particular user to "true"
	* and will also initialize this user's public and secret keys 
	*/
	public synchronized String LoginUser(String username,
					     String password, 
					     String publicKeyAsHex, 
					     String secretKeyAsHex) {
		String reply = "";
		byte[] pk = hexConvert(publicKeyAsHex);
		byte[] sk = hexConvert(secretKeyAsHex);
		System.out.println(getHexString(sk));
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pk));
			SecretKeySpec secretKey =  new SecretKeySpec(sk, "Blowfish");
			if (isLoginValid(username, password)) {
				userDataBase.get(username).setLoggedIn(true);
				userDataBase.get(username).setPublicKey(publicKey);
				userDataBase.get(username).setSecretKey(secretKey);
				System.out.println(userDataBase.get(username).getPublicKeyAsString());
				reply = userDataBase.get(username).getViewableWhiteboards();			
			} else {
				reply = "0";
			}		
		} catch (Exception e) {
			e.printStackTrace();
			reply = "0";
		}
		return reply;
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
	/**
	*Used to set the 'Logged In' status of a particular user to "false"
	*/
	public synchronized String LogoutUser(String username) {
		String reply = "";
		userDataBase.get(username).setLoggedIn(false);
		reply = "5";
		return reply;
	}
	/**
	*Used when registering a new User to see if the Username/Password
	*combination they want is available.
	*/
	public synchronized boolean CheckUsernamePasswordAvailable(String username, String password) {
		return !(passwordDataBase.containsKey(username) || passwordDataBase.containsValue(password));
	}
	/**
	 * Used to register a new user.
	 */
	public synchronized String RegisterNewUser(String username, String password, String publicKeyAsHex) {
		String reply = "";
		byte[] pk = hexConvert(publicKeyAsHex);
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pk));
			if (CheckUsernamePasswordAvailable(username, password)) {
				User newUser = new User(username, password);
				userDataBase.put(username, newUser);
				passwordDataBase.put(username, password);
				userDataBase.get(username).setPublicKey(publicKey);
				reply = "3";
			} else {
			reply = "2";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reply = "2";
		}	
		return reply;
	}
	/**
	 * Used to send a user their status
	 * i.e. the whiteboards they have
	 * access to
	 */
	private synchronized String sendUserStatus(String userName) {
		User u = userDataBase.get(userName);
		return u.getViewableWhiteboards();
	}
	public void setWhiteboardManager(WhiteboardManager w) {
		this.whiteboardManager = w;
	}
	public ConcurrentHashMap<String, User> getUsers() {
		return this.userDataBase;
	}
	/**
	 * Called when the user wants to create a new whiteboard
	 */
	private synchronized String createNewWhiteboard(String userName, String whiteboardName) {
			String returnValue = "";
			try {
				this.whiteboardManager.createWhiteBoard(userName, whiteboardName);
				this.userDataBase.get(userName).addViewableWhiteboard(
									this.whiteboardManager.getWhiteboard(
										userName,
										whiteboardName)
									);
				returnValue = "6";
			} catch (Exception e) {
				returnValue = "7";
			}
			return returnValue;
	}
	/**
	 * Used when the user wants to share a whiteboard with somebody else
	 */
	private synchronized String shareWhiteboard(String ownersUsername, String targetsUsername, String whiteboardName) {
			String returnValue = "";
			Whiteboard w = this.whiteboardManager.getWhiteboard(ownersUsername, whiteboardName);
			if (userDataBase.containsKey(targetsUsername)) {
				User targetUser = userDataBase.get(targetsUsername);
				if(!targetUser.getWhiteboards().containsValue(w)) {
					targetUser.addViewableWhiteboard(w);
					w.addUser(targetUser);
					returnValue = "*";
				}
			} else {
				returnValue = "9";
			}
			return returnValue;
	}
	private synchronized String uploadWhiteboard(String userName, String whiteboardName, String data) {
			String returnValue = "";
			try {
				this.whiteboardManager.uploadWhiteBoard(userName, whiteboardName, data);
				this.userDataBase.get(userName).addViewableWhiteboard(
									this.whiteboardManager.getWhiteboard(
										 userName,
										 whiteboardName)
									);
				returnValue = "10";
			} catch (Exception e) {
				returnValue = "11";
			}
			return returnValue;
	}
	private synchronized String checkIfUserExists(String targetUserName) {
		boolean exists = this.userDataBase.containsKey(targetUserName);
		String returnVal = "13";
		if (exists) {
			returnVal = "12";
		}
		return returnVal;
	}
	/**
	 * Used to interpret a message sent by a user
	 */
	private String parseMessage(String message) throws Exception {
		//System.out.println(message);
		String[] data = message.split("/");
		int intention = Integer.parseInt(data[0]);
		String messageUsername = data[1];
		String messagePassword = data[2];
		String whiteboardName = data[3];
		String shareesUsername = data[4];
		String whiteboardData = data[5];
		String secretKey = data[6];
		String returnValue = "";
		switch (intention) {
			case 0:
				returnValue = LoginUser(messageUsername, messagePassword, whiteboardData, secretKey);
				break;
			case 1:
				returnValue = RegisterNewUser(messageUsername, messagePassword, whiteboardData);
				break;
			case 2:
				returnValue = " ";
				break;
			case 3:
				returnValue = LogoutUser(messageUsername);
				break;
			case 4:
				returnValue = sendUserStatus(messageUsername);
				break;
			case 5:
				returnValue = createNewWhiteboard(messageUsername, whiteboardName);
				break;
			case 6:
				returnValue = shareWhiteboard(messageUsername, shareesUsername, whiteboardName);
				break;
			case 7:
				returnValue = uploadWhiteboard(messageUsername, whiteboardName, whiteboardData);
				break;
			case 8:
				returnValue = getWhiteboardData(messageUsername, whiteboardName);
				break;
			case 9:
				returnValue = postWhiteboardData(messageUsername, whiteboardName, whiteboardData);
				break;
			case 10:
				returnValue = checkIfUserExists(shareesUsername);
				break;
		}
		PublicKey replyKey = this.userDataBase.get(messageUsername).getPublicKey();
		String toReturn = encrypt(returnValue, replyKey);
		return toReturn;
     }
     private String postWhiteboardData(String messageUsername, String whiteboardName, String whiteboardData) {
		String usersPost = whiteboardData;
		String whiteboardToUpdate = whiteboardName;
		String theUsername = messageUsername;
		User theUser = this.userDataBase.get(theUsername);
		String[] whiteboardInfo = whiteboardName.split("@");
		Whiteboard w = this.whiteboardManager.getWhiteboard(whiteboardInfo[1], whiteboardInfo[0]);
		//System.out.println(whiteboardData + " step 1");
		w.update(whiteboardData, theUsername);
		String reply = theUser.getUpdateData(whiteboardName);
		return reply;
     }
     private String getWhiteboardData(String messageUsername,String whiteboardName) {
	User theUser = this.userDataBase.get(messageUsername);
	return theUser.getWhiteboardData(whiteboardName);
     }
     private String parseMessageBlowfish(String username, String m) throws Exception {
	SecretKey usersKey = this.userDataBase.get(username).getSecretKey();
	System.out.println(getHexString(usersKey.getEncoded()));
    	Cipher cipher = Cipher.getInstance("Blowfish");	
    	cipher.init(Cipher.DECRYPT_MODE, usersKey);	
    	byte[] decrypted = cipher.doFinal(hexConvert(m));
    	String message = new String(decrypted);
    	System.out.println(message);
    	String[] data = message.split("/");
	int intention = Integer.parseInt(data[0]);
	String messageUsername = data[1];
	String whiteboardName = data[3];
	String whiteboardData = data[5];
	String returnValue = "";
	switch (intention) {
		case 8:
			returnValue = getWhiteboardData(messageUsername, whiteboardName);
			break;
		case 9:
			returnValue = postWhiteboardData(messageUsername, whiteboardName, whiteboardData);
			break;
	}
	cipher.init(Cipher.ENCRYPT_MODE, usersKey);	
    	byte[] encrypted = cipher.doFinal(returnValue.getBytes());
    	String reply = getHexString(encrypted);
    	return reply;
     }
    /**
     * Every time a new user connects, a new
     * thread is created to handle the user's
     * requests.
     */
    private class LoginListenerThread extends Thread {
	private Socket socket;
	private boolean usingRSA;
	public LoginListenerThread(Socket socket, boolean useRSA){
		this.socket = socket;
		this.usingRSA = useRSA;
	}
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            	String input = null;
	           	while(input == null) {
					input = in.readLine();
	           	}
	            	String replyMessage = "";
	            	if (usingRSA) {
	            		input = decrypt(input, privateKey);	
				replyMessage = parseMessage(input);
			} else {
				System.out.println(input);
				String[] message = input.split("[+]");
				String user = message[0];
				input = message[1];
				replyMessage = parseMessageBlowfish(user, input);
			}
			out.println(replyMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	}
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
        System.out.println(new String(returnTo));
	    return new String(returnTo);
    } 
    private String encrypt(String text, PublicKey publicKey) 
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
      	return getHexString(toReturn);
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
    private PrivateKey getServersPrivKey() 
			throws Exception {
	File filePrivateKey = new File(getClass().getResource("../security/ServerPrivate.key").toURI());
	FileInputStream fis = new FileInputStream(filePrivateKey);
	byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
	fis.read(encodedPrivateKey);
	fis.close();
	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
	PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
	return privateKey;
    }
}