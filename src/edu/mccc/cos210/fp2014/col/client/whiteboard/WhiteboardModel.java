package edu.mccc.cos210.fp2014.col.client.whiteboard;

import edu.mccc.cos210.fp2014.col.client.objects.TextboxModel;
import edu.mccc.cos210.fp2014.col.client.objects.ImageModel;
import edu.mccc.cos210.fp2014.col.client.objects.LinkModel;
import edu.mccc.cos210.fp2014.col.client.objects.FileModel;
import edu.mccc.cos210.fp2014.col.client.objects.WebpageModel;
import edu.mccc.cos210.fp2014.col.client.objects.ShapeModel;
import edu.mccc.cos210.fp2014.col.client.objects.Shape;
import edu.mccc.cos210.fp2014.col.client.objects.PointModel;
import edu.mccc.cos210.fp2014.col.client.tools.Tools;
import edu.mccc.cos210.fp2014.col.client.network.NetworkManager;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardPanel;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import java.io.File;
import java.io.IOException;

public class WhiteboardModel {
	private String name;
	private ArrayList<PointModel> points = new ArrayList<PointModel>(); 
    	private ArrayList<TextboxModel> textboxes = new ArrayList<TextboxModel>();
    	private ArrayList<ImageModel> images = new ArrayList<ImageModel>(); 
    	private ArrayList<WebpageModel> webpages = new ArrayList<WebpageModel>();
	private ArrayList<FileModel> files = new ArrayList<FileModel>();
	private ArrayList<ShapeModel> shapes = new ArrayList<ShapeModel>();
	private ArrayList<LinkModel> links = new ArrayList<LinkModel>();
	private ArrayList<String> users = new ArrayList<String>();
    	private Tools selectedTool = Tools.NONE;
	private NetworkManager network;
	private WhiteboardPanel observer;
	public boolean networked = false;

	public WhiteboardModel(String name) {
		this.name = name;
	}
	
	public WhiteboardModel(String name, NetworkManager networkManager) {
		this.name = name;
		this.network = networkManager;
		this.network.setWhiteboardModel(this);
	}

	public String getName() {
		return this.name;
	}
	
	public void setNetworkManager(NetworkManager n) {
		this.network = n;
	}

	public void addShape(ShapeModel shapeModel) {
		this.shapes.add(shapeModel);
		if (this.network != null) {
			network.sendData(shapeModel.getInfo());
		}
		shapeModel.setObserver(this);
	}
	
	public ArrayList<String> getUsers() {
		return this.users;
	}
	
	public void addUser(String user) {
		this.users.add(user);
	}

	public ArrayList<ShapeModel> getShapes() {
		return this.shapes;
	}

	public void addPoint(PointModel pointModel) {
		this.points.add(pointModel);
		if (this.network != null) {
			network.sendData(pointModel.getInfo());
		}
	}

	public ArrayList<PointModel> getPoints() {
		return this.points;
	}

	public void addTextbox(TextboxModel textModel) {
		this.textboxes.add(textModel);
		if (this.network != null) {
			network.sendData(textModel.getInfo());
		}
		textModel.setObserver(this);
	}

	public ArrayList<TextboxModel> getTextboxes() {
		return this.textboxes;
	}

	public void addImage(ImageModel imageModel) {
		this.images.add(imageModel);
		if (this.network != null) {
			network.sendData(imageModel.getNetworkSharingData());
		}
		imageModel.setObserver(this);
	}

	public ArrayList<ImageModel> getImages() {
		return this.images;
	}

	public void addWebpage(WebpageModel webpageModel) {
		this.webpages.add(webpageModel);
		if (this.network != null) {
			network.sendData(webpageModel.getInfo());
		}
		webpageModel.setObserver(this);
	}

	public ArrayList<WebpageModel> getWebpages() {
		return this.webpages;
	}
	
	public void addFile(FileModel fileModel) {
		this.files.add(fileModel);
		if (this.network != null) {
			network.sendData(fileModel.getNetworkSharingData());
		}
		fileModel.setObserver(this);
	}

	public ArrayList<FileModel> getFiles() {
		return this.files;
	}

	public void addLink(LinkModel linkModel){
		this.links.add(linkModel);
		if (this.network != null) {
			network.sendData(linkModel.getInfo());
			try {
				for (int i = 0; i < this.users.size(); i++) {
					String shareName = linkModel.getContent().getName().split("[@]")[0];
					network.sendShareWhiteboardMessage(shareName, users.get(i));
				}
			} catch (Exception e) {
			}
		}
	}

	public void clearSelectedTool() {
		this.selectedTool = Tools.NONE;
	}

	public void setSelectedTool(Tools selectedTool) {
		this.selectedTool = selectedTool;
	}

	public Tools getSelectedTool() {
		return this.selectedTool;
	}

	public void setObserver(WhiteboardPanel observer) {
		this.observer = observer;
	}
	
	public WhiteboardPanel getObserver() {
		return this.observer;
	}
	
	public NetworkManager getNetworkManager() {
		return this.network;
	}
   	/**
	* Called by NetworkManager when a user first opens a whiteboard,
	* used to load up-to-date data from the server to this local model
	* (currently broken, working on this)
	*/	
	public void setData(String data) {
		this.textboxes.clear();
		this.points.clear();
		this.webpages.clear();
		this.images.clear();
		this.files.clear();
		this.links.clear();
		if(!data.equals("*")) {
			parseData(data.substring(1, data.length()));
		}
	}
	/**
	* Called by NetworkManager whenever updates are 
	* recieved from the Server regarding a whiteboard
	*/
	public void updateData(String data) {
		if(!data.equals("*")) {
			parseData(data);
		}
	}	
	/**
	* Used to parse messages from the NetworkManager
	*/
	private void parseData(String data) {
		String[] components = data.split("}");
		for(int i = 0; i < components.length; i++) {
			if(components[i] != null) {
				String[] params = components[i].split("[|]");
				switch (params[0].charAt(0)) {
					case 'P':
						drawPoint(params);
						break;
					case 'T':
						drawTextbox(params);
						break;
					case 'I':
						drawWeblink(params);
						break;
					case 'F':
						drawImage(params);
						break;
					case 'Q':
						drawFile(params);
						break;
					case 'L':
						drawShape(params);
						break;
					case 'E':
						String[] info = components[i].split("[`]");
						editObjectOnTheWhiteboard(
									info[1],
									info[2],
									false 
								);
						break;
					case 'u':
						addUser(params[1]);
						break;
					case 'l':
						drawLink(params);
						break;
				}
			}
		}
	}
	/**
	* This method is called when a new file has been uploaded
	* by another user
	* and must be drawn on the whiteboard.
	*/
	private void drawShape(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		int width = Integer.parseInt(params[3]);
		int height = Integer.parseInt(params[4]);
		int r = Integer.parseInt(params[5]);
		int g = Integer.parseInt(params[6]);
		int b = Integer.parseInt(params[7]);
		String shape = params[8];
		ShapeModel s = new ShapeModel(
			x, 
			y, 
			(Shape.valueOf(shape.toUpperCase())),
			new Color(r, g, b), 
			new Dimension(width, height)
		);
		shapes.add(s);
		s.setObserver(this);
		getObserver().addShape(s);

	}
	private void drawFile(String[] params) {
		 int x = Integer.parseInt(params[1]);
		 int y = Integer.parseInt(params[2]);
		 String title = params[4];
		 String hexData = params[3];
		 //turn the hex data into a file 
		 int length = hexData.length();
		 byte[] finalFileData = new byte[length / 2];
		 for (int i = 0; i < length; i += 2) {
		 	finalFileData[i / 2] = (byte) ((Character.digit(hexData.charAt(i), 16) << 4)
							+ Character.digit(hexData.charAt(i+1), 16)
							);
		 }
		 try {
			FileModel newFile = new FileModel(finalFileData, x, y);
			newFile.setFileSize(new Dimension(100, 100));
			newFile.setTitle(title);
			newFile.setPicture();
			this.files.add(newFile);
			System.out.println(title);
			System.out.println("File made and added");
			getObserver().addFile(newFile);
			System.out.println("File added to whiteboard");
		} catch (Exception e) {
		}
	}
	/**
	* This method is called when a new image has been uploaded by another user
	* and must be drawn on the whiteboard.
	*/
	private void drawImage(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		int width = Integer.parseInt(params[3]);
		int height = Integer.parseInt(params[4]);
		String title = params[5];
		String hexData = params[6];
		//turn the hex data into a buffered image
		int length = hexData.length();
		byte[] finalImageData = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			finalImageData[i / 2] = (byte) ((Character.digit(hexData.charAt(i), 16) << 4)
							+ Character.digit(hexData.charAt(i+1), 16)
							);
		}
		InputStream in = new ByteArrayInputStream(finalImageData);
		try{
			BufferedImage theImage = ImageIO.read(in);
			//Now create an image model
			ImageModel im = new ImageModel(theImage, 100, 100, x, y);
			im.setBounds(x, y, 100, 100);
			im.setTitle(title);
			this.images.add(im);
			im.setObserver(this);
			getObserver().addImage(im);
		} catch (IOException e) {
		}
	}
	/**
	 * Will add a point to the model and notify the observing WhiteboardPanel
	 */
	private void drawPoint(String[] params) {
		int x1 = Integer.parseInt(params[1]);
		int y1 = Integer.parseInt(params[2]);
		int x2 = Integer.parseInt(params[3]);
		int y2 = Integer.parseInt(params[4]);
		int stroke = Integer.parseInt(params[5]);
		int R = Integer.parseInt(params[6]);
		int G = Integer.parseInt(params[7]);
		int B = Integer.parseInt(params[8]);
		Line2D line = new Line2D.Double(x1, y1, x2, y2);
		Color c = new Color(R, G,  B);
		PointModel p = new PointModel(line, stroke, c);
		this.points.add(p);
		getObserver().drawPoint();
		System.out.println("Point added: " +p.getInfo());
	}
	/**
	 * Will add a textbox to the model and notify the observing WhiteboardPanel
	 */
	private void drawTextbox(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		String text = params[3];
		String fontName = params[4];
		int fonttype = Integer.parseInt(params[5]);
		int fontsize = Integer.parseInt(params[10]);
		int tR = Integer.parseInt(params[7]);
		int tG = Integer.parseInt(params[8]);
		int tB = Integer.parseInt(params[9]);
		Font font = new Font(fontName, fonttype, fontsize);
		Color c = new Color(tR, tG, tB);
		TextboxModel t = new TextboxModel(x, y, text, font, c, fontsize);
		t.setFontSize(fontsize);
		t.setObserver(this);
		this.textboxes.add(t);
		getObserver().addTextbox(t);
	}

	private void drawWeblink(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		String url = params[3];
		int urlSize = Integer.parseInt(params[4]);
		WebpageModel w = new WebpageModel(url);
		w.setLocation(x, y);
		this.webpages.add(w);
		getObserver().addWeblink(w);
	}
	
	private void drawLink(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		String modelName = params[3];
		WhiteboardModel w = new WhiteboardModel(modelName, getNetworkManager());
		LinkModel link = new LinkModel(w);
		link.setLocation(x, y);
		this.links.add(link);
		getObserver().addWhiteboardLink(link);
	}

  	/**
	 * Called whenever a textbox currently 
	 * on the whiteboard must be edited. 
	 */
	public void editObjectOnTheWhiteboard(String before,
					      String after, 
					      boolean isThisMessageComingFromWhiteboardView) {
		if (!isThisMessageComingFromWhiteboardView) {
			String[] beforeMessage = before.split("[|]");
			String[] afterMessage = after.split("[|]");
			char indicator = beforeMessage[0].charAt(0);
			switch (indicator) {
				case 'T':
					editTextbox(before, after, beforeMessage, afterMessage);
					break;
				case 'I':
					editWeblink(before, after, beforeMessage, afterMessage);
					break;
				case 'F':
					editImage(before, after, beforeMessage, afterMessage);
					break;
				case 'L':
					editShape(before, after, beforeMessage, afterMessage);
					break;
				case 'Q':
					editFile(before, after, beforeMessage, afterMessage);
					break;
			}
		}
		else {
			before = before.replaceAll("}", "`");
			after = after.replaceAll("}", "`");
			String message = "E|`" + before + after + "}";
			System.out.println(message);
			if (getNetworkManager() != null) {
				getNetworkManager().sendData(message);
			}
		}
	}
	 /**
	 * Called whenever a File Icon currently on
	 * the whiteboard must be MOVED OR RESIZED ONLY.
	 */
	private void editFile(String before,
			      String after, 
			      String[] beforeMessage, 
			      String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.files.size(); i++) {
			potentialMatch = this.files.get(i).getData().replaceAll("}", "");
			System.out.println("before is: " + before);						
			System.out.println("potential match is: " + potentialMatch);
			if(potentialMatch.equals(before)) {
				searchIndex = i;
				break;
			}
		}
		if(searchIndex != -1) {
			FileModel editThis = this.files.get(searchIndex);
			int x = Integer.parseInt(afterMessage[1]);
			int y = Integer.parseInt(afterMessage[2]);
			int width = Integer.parseInt(afterMessage[3]);
			int height = Integer.parseInt(afterMessage[4]);
			String title = afterMessage[5];
			if (afterMessage.length == 6) {
				FileModel editedFile = new FileModel(
								editThis.getContents(), 
								width, 
								height, 
								x, 
								y
							 );
				editedFile.setTitle(title);
				getObserver().editFile(editThis, editedFile);
				this.files.set(searchIndex, editedFile);
			} else {
				String hexData = afterMessage[6];
				//turn the hex data into a buffered image
				int length = hexData.length();
				byte[] finalFileData = new byte[length / 2];
				for (int i = 0; i < length; i += 2) {
					finalFileData[i / 2] = (byte) ((Character.digit(hexData.charAt(i), 16) << 4)
									+ Character.digit(hexData.charAt(i+1), 16)
									);
				}
				FileModel editedFile = new FileModel(finalFileData, width, height, x, y);
				editedFile.setTitle(title);
				getObserver().editFile(editThis, editedFile);
				this.files.set(searchIndex, editedFile);
			}
		}
	}
    	/**
	 * Called whenever an Image currently on the 
	 * whiteboard must be MOVED OR RESIZED ONLY.
	 */
	private void editImage(String before,
			       String after,
			       String[] beforeMessage, 
			       String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.images.size(); i++) {
			potentialMatch = this.images.get(i).getData().replaceAll("}", "");
			System.out.println("before is: " + before);						
			System.out.println("potential match is: " + potentialMatch);
			if(potentialMatch.equals(before)) {
				searchIndex = i;
				break;
			}
		}
		if(searchIndex != -1) {
			ImageModel editThis = this.images.get(searchIndex);
			int x = Integer.parseInt(afterMessage[1]);
			int y = Integer.parseInt(afterMessage[2]);
			int width = Integer.parseInt(afterMessage[3]);
			int height = Integer.parseInt(afterMessage[4]);
			String title = afterMessage[5];
			if (afterMessage.length == 6) {
				ImageModel editedImage = new ImageModel(editThis.getContents(),
									width,
									height,
									x,
									y);
				getObserver().editImage(editThis, editedImage);
				editedImage.setSize(width, height);
				images.set(searchIndex, editedImage);
			} else {
				String hexData = afterMessage[6];
				//turn the hex data into a buffered image
				int length = hexData.length();
				byte[] finalImageData = new byte[length / 2];
				for (int i = 0; i < length; i += 2) {
					finalImageData[i / 2] = (byte) ((Character.digit(hexData.charAt(i), 16) << 4)
									+ Character.digit(hexData.charAt(i+1), 16));
				}
				InputStream in = new ByteArrayInputStream(finalImageData);
				try{
					BufferedImage theImage = ImageIO.read(in);
					//Now create an image model
					ImageModel editedImage = new ImageModel(theImage,
										width, 
										height, 
										x, 
										y);
					editedImage.setBounds(x, y, width, height);
					editedImage.setTitle(title);
					getObserver().editImage(editThis, editedImage);
					getObserver().revalidate();
					getObserver().repaint();
					images.set(searchIndex, editedImage);
				} catch (IOException e) {
				}
			}
		}
	}
	/**
	 * Called whenever a textbox currently
     	 * on the whiteboard must be edited. 
	 */
	private void editTextbox(String before,
				 String after, 
				 String[] beforeMessage, 
				 String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.textboxes.size(); i++) {
			potentialMatch = this.textboxes.get(i).getInfo().replaceAll("}", "");
			System.out.println("before is: " + before);						
			System.out.println("potential match is: " + potentialMatch);
			if(potentialMatch.equals(before)) {
				searchIndex = i;
				break;
			}
		}
		if(searchIndex != -1) {
			TextboxModel editThis = this.textboxes.get(searchIndex);
			int x = Integer.parseInt(afterMessage[1]);
			int y = Integer.parseInt(afterMessage[2]);
			String text = afterMessage[3];
			String fontName = afterMessage[4];
			int fonttype = Integer.parseInt(afterMessage[5]);
			int fontsize = Integer.parseInt(afterMessage[6]);
			int tR = Integer.parseInt(afterMessage[7]);
			int tG = Integer.parseInt(afterMessage[8]);
			int tB = Integer.parseInt(afterMessage[9]);
			Font font = new Font(fontName, fonttype, fontsize);
			Color c = new Color(tR, tG, tB);
			TextboxModel editedTextbox = new TextboxModel(x, y, text, font, c, fontsize);
			editedTextbox.setFontSize(fontsize);
			getObserver().editTextbox(editThis, editedTextbox);
			editedTextbox.setObserver(this);
			this.textboxes.set(searchIndex, editedTextbox);
		}
	}
	/**
	 * Called whenever a textbox currently 
	 * on the whiteboard must be edited.
	 */
	private void editWeblink(String before,
				 String after, 
				 String[] beforeMessage,
				 String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.webpages.size(); i++) {
			potentialMatch = this.webpages.get(i).getInfo().replaceAll("}", "");
			System.out.println("before is: " + before);						
			System.out.println("potential match is: " + potentialMatch);
			if(potentialMatch.equals(before)) {
				searchIndex = i;
				break;
			}
		}
		if(searchIndex != -1) {
			WebpageModel editThis = this.webpages.get(searchIndex);
			int x = Integer.parseInt(afterMessage[1]);
			int y = Integer.parseInt(afterMessage[2]);
			String url = afterMessage[3];
			int urlSize = Integer.parseInt(afterMessage[4]);
			WebpageModel editedWebModel = new WebpageModel(url);
			editedWebModel.setLocation(x, y);
			getObserver().editWeblink(editThis, editedWebModel);
			editedWebModel.setObserver(this);
			this.webpages.set(searchIndex, editedWebModel);
		}
	}
	private void editShape(String before,
			       String after, 
			       String[] beforeMessage,
			       String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.shapes.size(); i++) {
			potentialMatch = this.shapes.get(i).getInfo().replaceAll("}", "");
			System.out.println("before is: " + before);						
			System.out.println("potential match is: " + potentialMatch);
			if(potentialMatch.equals(before)) {
				searchIndex = i;
				break;
			}
		}
		if(searchIndex != -1) {
			ShapeModel editThis = this.shapes.get(searchIndex);
			int x = Integer.parseInt(afterMessage[1]);
			int y = Integer.parseInt(afterMessage[2]);
			int height = Integer.parseInt(afterMessage[3]);
			int width = Integer.parseInt(afterMessage[4]);
			int r = Integer.parseInt(afterMessage[5]);
			int g = Integer.parseInt(afterMessage[6]);
			int b = Integer.parseInt(afterMessage[7]);	
			String shape = afterMessage[8];
			ShapeModel editedShapemodel = new ShapeModel(
								x,
								y,
								(Shape.valueOf(shape.toUpperCase())),
								new Color(r, g, b), 
								new Dimension(width, height)							
						      );
			getObserver().editShape(editThis, editedShapemodel);
			editedShapemodel.setObserver(this);
			this.shapes.set(searchIndex, editedShapemodel);
		}
	}
	public String getInfo() {
		String s = "*";
		for (int i = 0; i < this.points.size(); i++) {
			s += this.points.get(i).getInfo();
		}
		for (int i = 0; i < this.textboxes.size(); i++) {
			s += this.textboxes.get(i).getInfo();
		}
		for (int i = 0; i < this.webpages.size(); i++) {
			s += this.webpages.get(i).getInfo();
		}
		for (int i = 0; i < this.shapes.size(); i++) {
			s += this.shapes.get(i).getInfo();
		}
		for (int i = 0; i < this.images.size(); i++) {
			s += this.images.get(i).getNetworkSharingData();
		}
		for (int i = 0; i < this.files.size(); i++) {
			s += this.files.get(i).getNetworkSharingData();
		}
		return s;
	}
}