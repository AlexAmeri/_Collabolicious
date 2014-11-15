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

	public ToolButton(
		String name, 
		String offImage,
		String onImage,
		int width, 
		int height) {
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
		if(isSelected){
			isSelected = false;
		}else{
			isSelected = true;
		}
	}

	public String getName() {
		return this.name;
	}

}