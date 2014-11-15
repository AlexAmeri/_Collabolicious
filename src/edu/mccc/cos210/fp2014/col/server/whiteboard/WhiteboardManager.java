package edu.mccc.cos210.fp2014.col.server.whiteboard;
import edu.mccc.cos210.fp2014.col.server.users.UserManager;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
public class WhiteboardManager extends java.lang.Object {
    private UserManager userManager;
    private ConcurrentHashMap<String, Whiteboard> whiteboardDatabase;   
    public WhiteboardManager() {
	whiteboardDatabase = new ConcurrentHashMap<String, Whiteboard>();
    }
    /* Methods */
    public String createWhiteBoard(String ownersName, String whiteboardName) {
	Whiteboard newWhiteboard = new Whiteboard(whiteboardName + "@" + ownersName);
	newWhiteboard.addUser(this.userManager.getUsers().get(ownersName));
	this.whiteboardDatabase.put(whiteboardName + "@" + ownersName, newWhiteboard);
	return "12";
    }
    public void uploadWhiteBoard(String ownersName, String whiteboardName, String data) throws Exception {
    	try {
    		Whiteboard newWhiteboard = new Whiteboard(whiteboardName 
								+ "@" 
								+ ownersName,
								data
								);
    		newWhiteboard.addUser(this.userManager.getUsers().get(ownersName));
    		this.whiteboardDatabase.put(whiteboardName 
						+ "@" 
						+ ownersName,
						newWhiteboard
						);
    	} catch (Exception e) {
    		throw new Exception();
    	}
    }	
    public Whiteboard getWhiteboard(String ownersName, String whiteboardName) {
	return this.whiteboardDatabase.get(whiteboardName + "@" + ownersName);
    }	
    public void setUserManager(UserManager u) {
	this.userManager = u;
    }
	
}
