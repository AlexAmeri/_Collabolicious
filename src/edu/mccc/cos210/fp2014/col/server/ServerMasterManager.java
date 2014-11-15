package edu.mccc.cos210.fp2014.col.server;
import edu.mccc.cos210.fp2014.col.server.whiteboard.WhiteboardManager;
import edu.mccc.cos210.fp2014.col.server.users.UserManager;
import java.io.IOException;
import java.lang.Thread;
import java.lang.Runnable; 

public class ServerMasterManager {
	/**
	 * Used to create the 
	 * UserManager and the
	 * WhiteboardManager at startup,
	 * and start the networking threads
	 */
	public static void main(String[] sa) {
		final UserManager userManager = new UserManager();
		WhiteboardManager whiteboardManager = new WhiteboardManager();
		userManager.setWhiteboardManager(whiteboardManager);
		whiteboardManager.setUserManager(userManager);
		try {
			Thread sessionThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						userManager.initiateSessionPort();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			sessionThread.start();
			userManager.initiate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
