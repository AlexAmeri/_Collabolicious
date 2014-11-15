package edu.mccc.cos210.fp2014.col.server.whiteboard;
import edu.mccc.cos210.fp2014.col.server.objects.ImageModel;
import edu.mccc.cos210.fp2014.col.server.objects.PointModel;
import edu.mccc.cos210.fp2014.col.server.objects.TextboxModel;
import edu.mccc.cos210.fp2014.col.server.objects.WebpageModel;
import edu.mccc.cos210.fp2014.col.server.objects.ShapeModel;
import edu.mccc.cos210.fp2014.col.server.objects.Shape;
import edu.mccc.cos210.fp2014.col.server.objects.FileModel;
import edu.mccc.cos210.fp2014.col.server.users.User;
import edu.mccc.cos210.fp2014.col.server.users.UserManager;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
public class Whiteboard {
	private String name;
	private ArrayList<PointModel> points = new ArrayList<PointModel>(); 
    	private ArrayList<TextboxModel> textboxes = new ArrayList<TextboxModel>();
    	private ArrayList<ImageModel> images = new ArrayList<ImageModel>(); 
    	private ArrayList<WebpageModel> webpages = new ArrayList<WebpageModel>();
	private ArrayList<ShapeModel> sshapes = new ArrayList<ShapeModel>();
	private ArrayList<FileModel> files = new ArrayList<FileModel>();
    	private ArrayList<User> myUsers = new ArrayList<User>();
	public Whiteboard(String name) {
		this.name = name;
	}	
	public Whiteboard(String name, String data) {
		this.name = name;
		setData(data);
	}
	private void setData(String data) {
		parseData(data);
	}
	public void addUser(User u) {
		this.myUsers.add(u);
	}
	public String getName() {
		return this.name;
	}
	public void addPoint(PointModel markerLine) {
		points.add(markerLine);
		// Add logic to clean up the additions
	}
	public ArrayList<PointModel> getPoints() {
		return points;
	}
	public void addTextbox(TextboxModel textboxDrawing) {
		textboxes.add(textboxDrawing);
	}
	public ArrayList<TextboxModel> getTextboxes() {
		return textboxes;
	}
	public void addImage(ImageModel imageModel) {
		images.add(imageModel);
	}
	public ArrayList<ImageModel> getImages() {
		return images;
	}
	public void addWebpage(WebpageModel webpageModel) {
		webpages.add(webpageModel);
	}
	public ArrayList<WebpageModel> getWebpages() {
		return webpages;
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
		for (int i = 0; i < this.sshapes.size(); i++) {
			s += this.sshapes.get(i).getInfo();
		}
		for (int i = 0; i < this.images.size(); i++) {
			s += this.images.get(i).getNetworkSharingData();
		}
		for (int i = 0; i < this.files.size(); i++) {
			s += this.files.get(i).getNetworkSharingData();
		}
		return s;
	}
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
		//System.out.println(p.getInfo());
	}
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
		sshapes.add(s);
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
			newFile.setTitle(title);
			this.files.add(newFile);
			System.out.println(title);
		} catch (Exception e) {
		}
	}
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
		try {
			BufferedImage theImage = ImageIO.read(in);
			//Now create an image model
			ImageModel im = new ImageModel(theImage, x, y);
			im.setBounds(x, y, 100, 100);
			im.setTitle(title);
			this.images.add(im);
			System.out.println("Image added!");
		} catch (IOException e) {
		}
	}
	private void drawTextbox(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		String text = params[3];
		String fontName = params[4];
		int fonttype = Integer.parseInt(params[5]);
		int fontsize = Integer.parseInt(params[6]);
		int tR = Integer.parseInt(params[7]);
		int tG = Integer.parseInt(params[8]);
		int tB = Integer.parseInt(params[9]);
		Font font = new Font(fontName, fonttype, fontsize);
		Color c = new Color(tR, tG, tB);
		TextboxModel t = new TextboxModel(x, y, text, font, c, fontsize);
		this.textboxes.add(t);
	}
	private void drawWeblink(String[] params) {
		int x = Integer.parseInt(params[1]);
		int y = Integer.parseInt(params[2]);
		String url = params[3];
		int urlSize = Integer.parseInt(params[4]);
		WebpageModel w = new WebpageModel(url);
		w.setLocation(x, y);
		this.webpages.add(w);
	}
	private void parseData(String data) {
		String[] components = data.split("}");
		//Now check for additions to the whiteboard
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
							info[2]
							);
						break;
					case 'l':
						break;
				}
			}
		}
	}
	private void editObjectOnTheWhiteboard(String before, String after) {
		before = before.replaceAll("}", "");
		after = after.replaceAll("}", "");
		String[] beforeMessage = before.split("[|]");
		String[] afterMessage = after.split("[|]");
		char indicator = beforeMessage[0].charAt(0);
		switch (indicator) {
			case 'T':
				editTextbox(before, after, beforeMessage, afterMessage);
				break;
			default:
				break;
		}
	}
	private void editTextbox(String before,
				String after,
				String[] beforeMessage,
				String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.textboxes.size(); i++) {
			potentialMatch = this.textboxes.
					 get(i).
					 getInfo().replaceAll("}", "");
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
		this.textboxes.set(searchIndex, editedTextbox);
		}
	}
	private void editWeblink(String before,
				 String after, 
				 String[] beforeMessage,
				 String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.webpages.size(); i++) {
			potentialMatch = this.webpages.
					 get(i).
					 getInfo().replaceAll("}", "");
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
			this.webpages.set(searchIndex, editedWebModel);
		}
	}
	private void editShape(String before,
			       String after, 
			       String[] beforeMessage,
			       String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.sshapes.size(); i++) {
			potentialMatch = this.sshapes.
					 get(i).
					 getInfo().replaceAll("}", "");
			System.out.println("before is: " + before);						
			System.out.println("potential match is: " + potentialMatch);
			if(potentialMatch.equals(before)) {
				searchIndex = i;
				break;
			}
		}
		if(searchIndex != -1) {
			ShapeModel editThis = this.sshapes.get(searchIndex);
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
			this.sshapes.set(searchIndex, editedShapemodel);
		}
	}
	private void editFile(String before,
			      String after, 
			      String[] beforeMessage, 
			      String[] afterMessage) {
		String potentialMatch = "";
		int searchIndex = -1;
		for(int i = 0; i < this.files.size(); i++) {
			potentialMatch = this.files.
					 get(i).
					 getData().replaceAll("}", "");
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
			potentialMatch = this.images.
					 get(i).
					 getData().replaceAll("}", "");
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
					ImageModel editedImage = new ImageModel(theImage,															width, 																height, 
										x, 
										y);
					editedImage.setBounds(x, y, width, height);
					editedImage.setTitle(title);
					images.set(searchIndex, editedImage);
				} catch (IOException e) {
				}
			}
		}
	}
	/**
	* Used for informing users of changes to the whiteboard
	*/
	public synchronized void update(String whiteboardData, String theUsername) {
		if(!whiteboardData.equals("*")) {
			parseData(whiteboardData);
			for (User u : this.myUsers) {
				if(!u.getUserName().equals(theUsername)) {
					u.addUpdateMessage(this, whiteboardData);
				}
			}
		}
	}


}