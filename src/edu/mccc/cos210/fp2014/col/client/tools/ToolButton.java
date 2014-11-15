package edu.mccc.cos210.fp2014.col.client.tools;
import edu.mccc.cos210.fp2014.col.client.UI.ToolButtonUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ToolButton extends JButton {
	
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	public String name;
	public boolean isSelected;
	
	public ToolButton() {
		super();
	}

	public ToolButton(String name, String imageLocation) {
		super();
		try {
		    BufferedImage img = ImageIO.read(getClass().getResource(imageLocation));
		    setIcon(new ImageIcon(resizeImage(img, 25, 25)));
		} catch (IOException ex) {}
	}

	public ToolButton(
		String name, 
		String offImage,
		String onImage) {
		setName(name);
		this.setUI(
			new ToolButtonUI(
				offImage,
				onImage
			)
		);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected() {
		if(this.isSelected){
			this.isSelected = false;
		}else{
			this.isSelected = true;
		}
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ToolButton(
		String name, 
		String imageLocation, 
		int width, 
		int height) {
		super();
		this.name = name;
		try {
			    this.img = ImageIO.read(
			    	getClass().getResource(imageLocation)
			    );
			    setIcon(
			    	new ImageIcon(
				    	resizeImage(
				    		img, 
				    		width, 
				    		height
				    	)
				    )
				);
		} catch (IOException ex) {
		}
		setBorder(
			new LineBorder(
				new Color(
					38 , 
					139, 
					210, 
					75
				), 
				2, 
				true
			)
		);
	}

	@Override
	public Dimension getMinimumSize() {
	    return new Dimension(getHeight(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(getHeight(), getHeight());
	}

	@Override
	public Dimension getMaximumSize() {
	    return new Dimension(getHeight(), getHeight());
	}

	@Override
	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(d);
		resizeImage(
			this.img, 
			(int)d.getWidth(), 
			(int)d.getHeight()
		);
	} 

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
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
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		graphics.drawImage(
			image, 
			(int)(width * 0.1), 
			(int)(width * 0.1), 
			(int)(width * 0.8), 
			(int)(height * 0.8), 
			null
		);
		graphics.dispose();
		return img;
	}

	@Override
	public void setBounds(
		int x, 
		int y, 
		int width, 
		int height) {
		super.setBounds(
			x,
			y,
			width,
			height
		);
	}
}