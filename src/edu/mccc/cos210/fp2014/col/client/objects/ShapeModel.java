package edu.mccc.cos210.fp2014.col.client.objects;

import edu.mccc.cos210.fp2014.col.client.objects.Shape;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;

/**
 *
 * Shape Model holds all information pretaining to the Shape object
 * 
 */
public class ShapeModel extends JLabel {

    private static final long serialVersionUID = 1L;
    public int x;
    public int y;
	protected Shape shape;
	protected Color color; 
	protected Dimension shapeSize;
	String before = "";
	private WhiteboardModel observer = null;

	public ShapeModel() {
		setShape(Shape.NONE);
		setShapeSize(new Dimension(10, 10));
		setColor(Color.BLACK);
	}
	
	public void setObserver(WhiteboardModel w) {
		this.observer = w;
	}
	
	private WhiteboardModel getObserver() {
		return this.observer;
	}

	public ShapeModel(
		Shape shape, 
		Dimension shapeSize, 
		Color color) {
		setShape(shape);
		setShapeSize(shapeSize);
		setColor(color);
	}

	public ShapeModel(
		int x, 
		int y, 
		Shape shape, 
		Color color, 
		Dimension shapeSize) {
		setShape(shape);
		setColor(color);
		setShapeSize(shapeSize);
		setLocation(x, y);
	}

	public void setColor(Color color) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.color = color;
		repaint();
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public Color getColor() {
		return this.color;
	}

	public void setShape(Shape shape) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.shape = shape;
		repaint();
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public Shape getShape() {
		return this.shape;
	}

	public void setShapeHeight(int height) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.shapeSize.setSize(
			this.shapeSize.getHeight(),
			(double)height
		);
		super.setSize(
			getShapeSize()
		);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public int getShapeHeight() {
		return (int)this.shapeSize.getHeight();
	}

	public void setShapeWidth(int width) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.shapeSize.setSize(
			(double)width,
			this.shapeSize.getHeight()
		);
		super.setSize(
			getShapeSize()
		);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public int getShapeWidth() {
		return (int)this.shapeSize.getWidth();
	}

	public void setShapeSize(Dimension size) {
		if (getObserver() != null) {
			before = getInfo();
		}
		this.shapeSize = size;
		setSize(size);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	public Dimension getShapeSize() {
		return this.shapeSize;
	}

	@Override
	public void setSize(Dimension size) {
		if (getObserver() != null) {
			before = getInfo();
		}
		setBounds(
			this.x,
			this.y,
			(int)getShapeSize().getWidth(),
			(int)getShapeSize().getHeight()
		);
		if (getObserver() != null) {
			notifyListeners(before, getInfo());
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		setBounds(
			this.x,
			this.y,
			(int)getShapeSize().getWidth(),
			(int)getShapeSize().getHeight()
		);

		g.setColor(getColor());
		switch(getShape()) {
			case CIRCLE:
				g.drawOval(
					0, 
					0, 
					(int)getShapeSize().getHeight(), 
					(int)getShapeSize().getWidth()
				);
				g.fillOval(
					0, 
					0, 
					(int)getShapeSize().getHeight(), 
					(int)getShapeSize().getWidth()
				);
				break;
			case SQUARE:
				g.drawRect(
					0, 
					0, 
					(int)getShapeSize().getHeight(), 
					(int)getShapeSize().getWidth()
				);
				g.fillRect(
					0, 
					0, 
					(int)getShapeSize().getHeight(), 
					(int)getShapeSize().getWidth()
				);
				break;
			case RECTANGLE:
				break;
		}
		g.dispose();
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
		String reply = "L|";
		reply += getX() + "|";
		reply += getY() + "|";
		reply += (int)getShapeSize().getHeight() + "|";
		reply += (int)getShapeSize().getWidth() + "|";
		reply += getColor().getRed() + "|";
		reply += getColor().getGreen() + "|";
		reply += getColor().getBlue() + "|";
		reply += getShape().toString() + "|}";
		return reply;
	}
	private void notifyListeners(String before, String after) {
		if (getObserver() != null) {
			getObserver().editObjectOnTheWhiteboard(before, after, true);
		}
	}
}