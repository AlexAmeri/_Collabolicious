package edu.mccc.cos210.fp2014.col.client.objects;

import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 *
 * Link Model 
 * Holds all information pretaining to the Link object
 * 
 */
public class LinkModel extends JLabel {

    private static final long serialVersionUID = 1L;
    public int x;
    public int y;
	protected WhiteboardModel whiteboardModel;
	protected Color color; 
	protected int height;
	protected int width;
	protected BufferedImage img;

	public LinkModel(WhiteboardModel whiteboardModel) {
		setContent(whiteboardModel);
		setSize(100, 100);
		try{
			this.img = ImageIO.read(
				getClass().getResource("../assets/icon-link.png")
			);
			setIcon(
	        	new ImageIcon(
	        		resizeImage(
	        			this.img, 
	        			(int)this.getSize().getWidth(), 
	        			(int)this.getSize().getHeight()
	        		)
	        	)
	        );
		}catch(IOException e){}
		if(whiteboardModel != null){
			setText(whiteboardModel.getName());
		    setHorizontalTextPosition(JLabel.CENTER);
		    setVerticalTextPosition(JLabel.BOTTOM);
		}
		repaint();
	}

	public LinkModel(
		WhiteboardModel whiteboardModel, 
		int height, 
		int width) {
		setSize(height, width);
		setContent(whiteboardModel);
		try{
			this.img = ImageIO.read(
				getClass().getResource("../assets/icon-link-off.png")
			);
			setIcon(
	        	new ImageIcon(
	        		resizeImage(
	        			this.img, 
	        			(int)this.getSize().getWidth() - 25, 
	        			(int)this.getSize().getHeight() - 25
	        		)
	        	)
	        );
		}catch(IOException e){}
		if(whiteboardModel != null){
			setText(whiteboardModel.getName());
		    setHorizontalTextPosition(JLabel.CENTER);
		    setVerticalTextPosition(JLabel.BOTTOM);
		}
		repaint();
	}

	public void setContent(WhiteboardModel whiteboardModel){
		this.whiteboardModel = whiteboardModel;
		if(this.whiteboardModel != null) {
			setText(whiteboardModel.getName());
		    setHorizontalTextPosition(JLabel.CENTER);
		    setVerticalTextPosition(JLabel.BOTTOM);
		    repaint();
		}
	}

	public WhiteboardModel getContent(){
		return this.whiteboardModel;
	}

	 @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(new Color(0,0,0,0));
        setIcon(
        	new ImageIcon(
        		resizeImage(
        			this.img, 
        			(int)this.getSize().getWidth() - 25, 
        			(int)this.getSize().getHeight() - 25
        		)
        	)
        );
    }

    public void setColor(Color color) {
    	this.color = color;
    }

    public Color getColor() {
    	return this.color;
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

    @Override 
    public void setSize(int height, int width) {
    	super.setSize(height, width);
    	this.width = width;
    	this.height = height;
    	setBounds(
    		(int)this.getLocation().getX(),
    		(int)this.getLocation().getY(),
    		this.width,
    		this.height
    	);
    }
	
	public String getInfo() {
		String s = "l|";
		s += getX() + "|";
		s += getY() + "|";
		s += whiteboardModel.getName() + "|}";
		return s;
	}

}