package edu.mccc.cos210.fp2014.col.client.objects;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;

/**
 *
 * Webpage Model holds all information pretaining to the Webpage object
 * 
 */
public class WebpageModel extends JLabel {

    private static final long serialVersionUID = 1L;
    public int x;
    public int y;
	protected String content;
	protected Color color; 
	protected int fontSize;
	private WhiteboardModel observer = null;
	String before = "";

	public WebpageModel(String string) {
		setContent(string);
		setFont(new Font("Arial", Font.PLAIN, 18));
		setColor(Color.BLACK);
		setFontSize(18);
	}

	public WebpageModel(
		int x, 
		int y, 
		String text, 
		Font font, 
		Color color, 
		int size) {
		setLocation(x, y);
		setContent(text);
		setFont(font);
		setColor(color);
		setFontSize(size);
	}
	
	public void setObserver(WhiteboardModel w) {
		this.observer = w;
	}
	
	public WhiteboardModel getObserver() {
		return this.observer;
	}

	public void setColor(Color color) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.color = color;
		this.setForeground(color);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public Color getColor() {
		return this.color;
	}

	public void setContent(String cotent) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.content = cotent;
		setText(this.content);
		setBounds(
			this.x,
			this.y,
			(int)getPreferredSize().getWidth(),
			(int)getPreferredSize().getHeight()
		);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public String getContent() {
		return this.content;
	}

	public void setFontSize(int size) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.fontSize = size;
		this.setFont(
			new Font(
				getFont().getFontName(), 
				Font.PLAIN, 
				size
			)
		);
		setBounds(
			this.x,
			this.y,
			(int)getPreferredSize().getWidth(),
			(int)getPreferredSize().getHeight()
		);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public int getFontSize() {
		return fontSize;
	}

	@Override
	public void setLocation(int x, int y) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.x = x;
		this.y = y;
		super.setLocation(x,y);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public String getInfo() {
	 	String reply = "I|";
		reply += super.getX() + "|";
	 	reply += super.getY() + "|";
	 	reply += this.content + "|";
	 	reply += this.fontSize + "}";
	 	return reply;
	}
	private void notifyListeners(String before, String after) {
		if (getObserver() != null) {
			getObserver().editObjectOnTheWhiteboard(before, after, true);
		}
	}
}