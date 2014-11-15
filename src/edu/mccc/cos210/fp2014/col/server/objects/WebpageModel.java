package edu.mccc.cos210.fp2014.col.server.objects;

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

	public void setColor(Color color) {
		this.color = color;
		this.setForeground(color);
	}

	public Color getColor() {
		return this.color;
	}

	public void setContent(String cotent) {
		this.content = cotent;
		setText(this.content);
		setBounds(
			this.x,
			this.y,
			(int)getPreferredSize().getWidth(),
			(int)getPreferredSize().getHeight()
		);
	}

	public String getContent() {
		return this.content;
	}

	public void setFontSize(int size) {
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
	}

	public int getFontSize() {
		return fontSize;
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		super.setLocation(x,y);
	}

	public String getInfo() {
	 	String reply = "I|";
		reply += super.getX() + "|";
	 	reply += super.getY() + "|";
	 	reply += this.content + "|";
	 	reply += this.fontSize + "}";
	 	return reply;
	}
}