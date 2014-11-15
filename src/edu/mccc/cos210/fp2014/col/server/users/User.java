package edu.mccc.cos210.fp2014.col.server.users;
import edu.mccc.cos210.fp2014.col.server.whiteboard.Whiteboard;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.security.*;
import java.security.spec.*;

public class User {
	private String userName;
	private String password;
	private boolean loggedIn;
	private PublicKey publicKey;
	private SecretKeySpec secretKey;
	private List<String> viewableWhiteboards = new LinkedList<String>();
	private ConcurrentHashMap<String, Whiteboard> whiteboardFetch = new ConcurrentHashMap<String, Whiteboard>();
	private HashMap<Whiteboard, Queue<String>> whiteboardUpdates = new HashMap<Whiteboard, Queue<String>>();
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public void setSecretKey(SecretKeySpec secretKey) {
		this.secretKey = secretKey;
	}
	public SecretKeySpec getSecretKey() {
		return this.secretKey;
	}
	public String getPublicKeyAsString() {
		return this.publicKey.toString();
	}
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	public void addViewableWhiteboard(Whiteboard w) {
		this.viewableWhiteboards.add(w.getName());
		this.whiteboardFetch.put(w.getName(), w);
		Queue<String> newDataQueue = new LinkedList<String>();
		this.whiteboardUpdates.put(w, newDataQueue);
	}
	/**
	 * Returns a list of the names of all whiteboards this user has access to.
	 */
	public String getViewableWhiteboards() {
		String reply = "";
		for (int i =0; i < this.viewableWhiteboards.size(); i++) {
			reply += this.viewableWhiteboards.get(i) + "|";
			}
		return reply;
	}
	public ConcurrentHashMap<String, Whiteboard> getWhiteboards() {
		return this.whiteboardFetch;
	}
	/** 
	 * returns if a user is logged in or not
	 */
	public boolean isLoggedIn() {
		return this.loggedIn;
	}
	/**
	 * Sets if somebody was logged in
	 */
	public void setLoggedIn(boolean loggedInStatus) {
		this.loggedIn = loggedInStatus;
	}
	public String getWhiteboardData(String whiteboardName) {
		Whiteboard w = this.whiteboardFetch.get(whiteboardName);
		String whiteboardData = w.getInfo();
		this.whiteboardUpdates.get(w).clear();
		return whiteboardData;
	}
	public String getUpdateData(String whiteboardName) {
		Whiteboard w = this.whiteboardFetch.get(whiteboardName);
		String reply = "";
		Queue<String> data = this.whiteboardUpdates.get(w);
		if(!data.isEmpty()) {
			reply += w.getName() + "|";
			while(!data.isEmpty()) {
				reply += data.poll();
			}
		} else {
			reply += "*";
		}
		return reply;
	}
	/**
	* Called by Whiteboard to add an update to this user's update queue
	*/
	public void addUpdateMessage(Whiteboard w, String message) {
		whiteboardUpdates.get(w).add(message);
	}
}