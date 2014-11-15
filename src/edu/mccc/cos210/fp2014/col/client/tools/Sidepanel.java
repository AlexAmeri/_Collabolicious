package edu.mccc.cos210.fp2014.col.client.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Sidepanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public Sidepanel() {
		setBackground(new Color(38, 139, 210, 45));
		setOpaque(false);
		setBorder(
			new CompoundBorder(
	          new LineBorder(
	            new Color(44, 158, 219),
	            2,
	            true
	            ),
	          BorderFactory.createMatteBorder(
	            2, 2, 2, 2, Color.WHITE)
	        )
		);
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getForeground());
		for (Component c : getComponents()) {
			if (c instanceof JPanel){
				c.setBackground(new Color(0,0,0,0));
	        	for (Component e : ((JPanel)c).getComponents()) {
					if (e instanceof JPanel)
			        	e.setBackground(new Color(38, 139, 210, 0));
			    }
			}else{
				c.setBackground(new Color(0,0,0,0));
			}
	    }
	}

}