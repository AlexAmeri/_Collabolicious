package edu.mccc.cos210.fp2014.col.client.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;

/**
* Holds the neccessary attributes of a marker line
*/
public class PointModel {
	protected Line2D line;
	protected int stroke;
	protected Color color; 
	public PointModel(
		Line2D line, 
		int stroke, 
		Color color) {
		this.line = line;
		this.stroke = stroke;
		this.color = color;
	}

	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(this.stroke));
		g.setColor(this.color);
		g.drawLine(
			(int)this.line.getX1(),
			(int)this.line.getY1(),
			(int)this.line.getX2(),
			(int)this.line.getY2()
		);
	}
	public String getInfo() {
		String info = "P|";
		info += (int)this.line.getX1() + "|";
		info += (int)this.line.getY1() + "|";
		info += (int)this.line.getX2() + "|";
		info += (int)this.line.getY2() + "|";
		info += this.stroke + "|";
		info += this.color.getRed() + "|";
		info += this.color.getGreen() + "|";
		info += this.color.getBlue() + "|}";
		return info;
	}
}