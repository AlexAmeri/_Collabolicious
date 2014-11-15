package edu.mccc.cos210.fp2014.col.client;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardView;
import edu.mccc.cos210.fp2014.col.client.user.LoginView;
import edu.mccc.cos210.fp2014.col.client.whiteboardSelection.WhiteboardSelectionView;
import edu.mccc.cos210.fp2014.col.client.whiteboardSelection.WhiteboardCreateView;
import edu.mccc.cos210.fp2014.col.client.network.NetworkManager;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;
public class CollaboliciousFrame extends JFrame{
	private ArrayList<WhiteboardModel> whiteboardModels 
		= new ArrayList<WhiteboardModel>();
	private ArrayList<WhiteboardModel> onlineWhiteboardModels 
		= new ArrayList<WhiteboardModel>();
	private NetworkManager networkManager;
	private boolean onlineMode = false;
	private static final long serialVersionUID = 1L;
	public CollaboliciousFrame() {

		try {
			UIManager.setLookAndFeel("com.sun.javax.swing.plaf.basic");
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	    	System.out.println(e);
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
		
		Dimension screenSize = new Dimension(500, 500);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(
			JFrame.EXIT_ON_CLOSE
		);
		this.setResizable(true);
		this.setPreferredSize(screenSize);
		this.pack();
		this.setBackground(Color.WHITE);
		this.setLocationRelativeTo(null);
		this.networkManager = new NetworkManager();
		for(int i = 0; i < 10; i++){
			WhiteboardModel testingModel = 
				new WhiteboardModel("Whiteboard" + i);
			this.whiteboardModels.add(testingModel);
		}
		showLoginView();
	}

	public void showLoginView() {
		clear();
		LoginView loginView = new LoginView(this);
	}

	public void showWhiteboardView(
		WhiteboardModel whiteboard) {
		clear();
		WhiteboardView whiteboardView = 
			new WhiteboardView(
				this, 
				whiteboard
			);
		if (whiteboard.networked) {
			try {
				setOnlineMode(true);
				whiteboard.setNetworkManager(getNetworkManager());
				whiteboard.
					getNetworkManager().
						setWhiteboardModel(whiteboard);
			    String data =
				whiteboard.
					getNetworkManager().
						sendGetWhiteboardDataMessage(
							whiteboard.getName()
						);
				whiteboard.
					setObserver(
						whiteboardView.getPanel()
					);
				whiteboard.setData(data);
				whiteboard.
					getNetworkManager().startRecieveThread();
			} catch (Exception e) {}
		} else {
			setOnlineMode(false);
		}
	}
	
	public void showNewWhiteboardView() {
		clear();
		WhiteboardCreateView whiteboardCreate = 
			new WhiteboardCreateView(this);
	}

	public void showWhiteboardSelectionView() {
		clear();
		onlineMode = false;
		WhiteboardModel[] models = 
			new WhiteboardModel[
				this.whiteboardModels.size()
			];
		models = this.whiteboardModels.toArray(models);
		WhiteboardSelectionView whiteboardSelection = 
			new WhiteboardSelectionView(this, models);
	}
	
	public void showOnlineWhiteboardSelectionView() {
		clear();
		onlineMode = true;
		WhiteboardModel[] models = 
			new WhiteboardModel[
				this.onlineWhiteboardModels.size()
			];
		models = this.onlineWhiteboardModels.toArray(models);
		WhiteboardSelectionView whiteboardSelection = 
			new WhiteboardSelectionView(this, models);	
	}
	
	public boolean getOnlineMode() {
		return this.onlineMode;
	}
	
	public void setOnlineMode (boolean b) {
		this.onlineMode = b;
	}
	
	public ArrayList<WhiteboardModel> getOnlineWhiteboardModels() {
		return this.onlineWhiteboardModels;
	}
	
	public ArrayList<WhiteboardModel> getOfflineWhiteboardModels() {
		return this.whiteboardModels;
	}
	
	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public void exit() {
		System.exit(0);
	}

	public void clear() {
		this.getContentPane().removeAll();
		this.getContentPane().repaint();
	}

}