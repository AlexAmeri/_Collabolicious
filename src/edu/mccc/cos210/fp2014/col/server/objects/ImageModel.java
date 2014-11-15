package edu.mccc.cos210.fp2014.col.server.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 *
 * Image Model 
 * Holds all information pretaining to the Image object
 * 
 */
public class ImageModel extends JLabel {

	private static final long serialVersionUID = 1L;
	public int x;
    public int y;
	public int width;
	public int height;
	private String title;
	private BufferedImage content;

	public ImageModel(
		BufferedImage bufferedImage, 
		int x, int y) {
		setOpaque(false);
		setContents(bufferedImage);
		setLocation(x,y);
		getPictureAsHexString();
	}

	public ImageModel(
		BufferedImage bufferedImage, 
		int width, 
		int height, 
		int x, 
		int y) {
		setOpaque(false);
		setContents(bufferedImage);
		setSize(width, height);
	}

	public ImageModel(
		String filename, 
		int width, 
		int height) {
		setOpaque(false);
		setContents(filename, width, height);
		setSize(width, height);
	}

	public ImageModel(
		String filename, 
		int width, 
		int height, 
		int x, 
		int y) {
		setOpaque(false);
		setContents(filename, width, height);
		setSize(width, height);
		setLocation(x, y);
	}	
	
	public void setContents(BufferedImage content) {
		this.content = content;
		this.revalidate();
	}

	public void setContents(
		File file, 
		int width, 
		int height) {
		try {
			    this.content = ImageIO.read(
			    	file
			    );
			    setIcon(
			    	new ImageIcon(
			    		resizeImage(
			    			this.content, 
			    			width, 
			    			height
			    		)
			    	)
			    );
				getPictureAsHexString();
		} catch (IOException ex) {
		}
	}

	public void setContents(
		String filename, 
		int width, 
		int height) {
		try {
			    this.content = ImageIO.read(
			    	getClass().getResource(filename)
			    );
			    setIcon(
			    	new ImageIcon(
			    		resizeImage(
			    			this.content, 
			    			width, 
			    			height
			    		)
			    	)
			    );
		} catch (IOException ex) {
		}
	}

	 @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(0,0,0,0));
        setIcon(
        	new ImageIcon(
        		resizeImage(
        			this.content, 
        			this.getWidth(), 
        			this.getHeight()
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

	public BufferedImage getContents() {
		return this.content;
	}

	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public void setSize(int height, int width){
		this.height = height;
		this.width = width;
		setBounds(
			this.x, 
			this.y, 
			this.width, 
			this.height
		);
		super.setSize(width, height);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		super.setLocation(x,y);
	}

	public byte[] getImageData() {
		byte[] imageInBytes = null;
		try {
			ByteArrayOutputStream outputStream = 
				new ByteArrayOutputStream();
			ImageIO.write(
				this.content, 
				"jpg", 
				outputStream
			);
			outputStream.flush();
			imageInBytes = outputStream.toByteArray();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageInBytes;
	}

	/**
	* Returns a Hexidecimal String,
	* Which is equal to the byte array 
	* Representing the file contents.
	*/
	private String getPictureAsHexString() {
		final char[] hexArray = 
			"0123456789ABCDEF".toCharArray();
		byte[] buffer = getImageData();
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
	public String getData() {
		String reply = "F|";
		reply += this.x + "|";
		reply += this.y + "|";
		reply += this.width + "|";
		reply += this.height + "|";
		reply += this.title + "}";
		return reply;	
	}
	/**
     * This method is called ONLY when 
     * Sending this image's data across the network,
	 * Otherwise just use getData()
	 */
	public String getNetworkSharingData() {
		String reply = "F|";
		reply += this.x + "|";
		reply += this.y + "|";
		reply += this.width + "|";
		reply += this.height + "|";
		reply += this.title + "|";
		reply += getPictureAsHexString() + "|}";
		return reply;	
	}
}