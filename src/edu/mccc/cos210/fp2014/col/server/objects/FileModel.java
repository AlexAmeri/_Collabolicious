package edu.mccc.cos210.fp2014.col.server.objects;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.net.URISyntaxException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileOutputStream;

/**
 *
 * File Model holds all information pretaining to the File object
 * 
 */
public class FileModel extends JLabel {

	private static final long serialVersionUID = 1L;

	public int x;
    public int y;
	public Dimension fileSize;
	private String title;
	private byte[] contents;
	private BufferedImage img;

	/**
	 * Default Constructor
	 */
	public FileModel(){
		setOpaque(false);
		setBounds(0, 0, 100, 100);
		setPicture();
	}

	/**
	 * Constructor takes byte array and x-y position
	 * @param  content [description]
	 * @param  x       [description]
	 * @param  y       [description]
	 * @return         [description]
	 */
	public FileModel(
		byte[] content, 
		int x, 
		int y) {
		setOpaque(false);
		setContents(content);
		setLocation(x,y);
	}

	/**
	 * Constructor takes byte array, dimensions, and x-y position
	 * @param  content [description]
	 * @param  width   [description]
	 * @param  height  [description]
	 * @param  x       [description]
	 * @param  y       [description]
	 * @return         [description]
	 */
	public FileModel(
		byte[] content,
		BufferedImage img,
		int width, 
		int height, 
		int x, 
		int y) {
		setOpaque(false);
		setContents(content);
		setSize(width, height);
		setBounds(x, y, width, height);
		setPicture(img);
	}

	public FileModel(
		byte[] content,
		int width, 
		int height, 
		int x, 
		int y) {
		setOpaque(false);
		setContents(content);
		setSize(width, height);
		setBounds(height, width, x, y);
	}

	/**
	 * Sets the file content 
	 * @param content byte array as file
	 */
	public void setContents(byte[] content) {
		this.contents = content;
		// Needs to also place a picture of the right mime type
	}

	/**
	 * Sets the file content
	 * @param file File to be set to content
	 */
	public void setContents(File file) {
		try {
			byte[] buffer = new byte[(int) file.length()];
			FileInputStream fileStream = new FileInputStream(file);
			fileStream.read(buffer);
			fileStream.close();
			setContents(buffer);
			// Needs to also place a picture of the right mime type
		} catch (IOException ex) {
			// Add a label that say the picture was corrupted
			// Not supported...
		}
	}
	
	/**
	 * Sets the file content
	 * @param filename String path to be loaded as a file
	 */
	public void setContents(String filename) {
		try {
		    File file = new File(
		    	getClass().getResource(filename).toURI()
		    );
			setContents(file);
			// Needs to also place a picture of the right mime type
		} catch (URISyntaxException ux) {
			// Add a label that say the picture was corrupted
			// Not supported...
		}
	}
	
	/**
	 * returns the byte array of the file contents
	 */
	public byte[] getContents() {
		return this.contents;
	}

	public BufferedImage getImage() {
		return this.img;
	}

	public void setPicture(){
		try {
			    this.img = ImageIO.read(
			    	getClass().getResource("../assets/icon-file-none.png")
			    );
			    setIcon(
			    	new ImageIcon(
			    		resizeImage(
			    			this.img, 
			    			(int)getSize().getWidth(), 
			    			(int)getSize().getHeight()
			    		)
			    	)
			    );
		} catch (IOException ex) {
			// Add a label that say the picture was corrupted
			// Not supported...
		}
	}

	public void setPicture(BufferedImage img) {
		this.img = img;
		setIcon(
	    	new ImageIcon(
	    		resizeImage(
	    			img, 
	    			(int)getSize().getWidth(), 
	    			(int)getSize().getHeight()
	    		)
	    	)
	    );
	}

	private BufferedImage resizeImage(
		BufferedImage image, 
		int width, 
		int height) {

		BufferedImage img = new BufferedImage(
			width, 
			height, 
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D graphics = img.createGraphics();
		graphics.setRenderingHint (
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(
			image, (int)(width * 0.1), 
			(int)(width * 0.1), 
			(int)(width * 0.8), 
			(int)(height * 0.8), 
			null
		);
		graphics.dispose();
		return img;
	}

	/**
	 * [setTitle description]
	 * @param title [description]
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * [getTitle description]
	 * @return [description]
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * [setFileHeight description]
	 * @param height [description]
	 */
	public void setFileHeight(int height) {
		this.fileSize.setSize(
			this.fileSize.getHeight(),
			(double)height
		);
		super.setSize(
			getFileSize()
		);
	}

	/**
	 * [getFileHeight description]
	 * @return [description]
	 */
	public int getFileHeight() {
		return (int)this.fileSize.getHeight();
	}

	/**
	 * [setFileWidth description]
	 * @param width [description]
	 */
	public void setFileWidth(int width) {
		this.fileSize.setSize(
			(double)width,
			this.fileSize.getHeight()
		);
		super.setSize(
			getFileSize()
		);
	}

	/**
	 * [getFileWidth description]
	 * @return [description]
	 */
	public int getFileWidth() {
		return (int)this.fileSize.getWidth();
	}

	/**
	 * [setFileSize description]
	 * @param size [description]
	 */
	public void setFileSize(Dimension size) {
		this.fileSize = size;
		setSize(size);
	}

	/**
	 * [getFileSize description]
	 * @return [description]
	 */
	public Dimension getFileSize() {
		return this.fileSize;
	}

	/**
	 * [setSize description]
	 * @param size [description]
	 */
	@Override
	public void setSize(Dimension size) {
		setBounds(
			this.x,
			this.y,
			(int)getFileSize().getWidth(),
			(int)getFileSize().getHeight()
		);
	}

	/**
	 * [setLocation description]
	 * @param x [description]
	 * @param y [description]
	 */
	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		super.setLocation(x,y);
	}

	/**
	 * Turns the content byte array into a string
	 * @return a Hexidecimal String representation of the file
	 */
	private String getFileAsHexString() {
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
		byte[] buffer = this.contents;
		char[] hexChars = new char[buffer.length* 2];
	    for ( int j = 0; j < buffer.length; j++ ) {
	        int v = buffer[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
		String hexString = new String(hexChars);
	    System.out.println(hexString);
		return hexString;
		
	}
	
	public void downloadFile(File directoryToSaveIn) {
		try{
			File finalFile = new File(directoryToSaveIn, getTitle());
			FileOutputStream output = new FileOutputStream(finalFile);
			output.write(getContents());
			output.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Gets all attributes and stringifies them
	 * @return [description]
	 */
	public String getData() {
		// This is improperly formated
		String reply = "F|";
		reply += getX() + "|";
		reply += getY() + "|";
		reply += getTitle() + "}";
		return reply;	
	}

	/**
	 * Sends file and attributes accross the network
	 * @return [description]
	 */
	public String getNetworkSharingData() {
		String reply = "Q|";
		reply += getX() + "|";
		reply += getY() + "|";
		reply += getFileAsHexString() + "|";
		reply += getTitle() + "|}";
		return reply;
	}
}