package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.UI.ColorButtonUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorPallet extends JPanel {
	private static final long serialVersionUID = 1L;
	public ColorPallet(final ToolModel markerTool){
		
		final JButton colorPicker = new ColorButton(
			(Color) markerTool.getAttribute("lineColor")
		);
	    colorPicker.addActionListener(new ActionListener() { 
	      public void actionPerformed(ActionEvent e) {
	        Color c;
	        c = JColorChooser.showDialog(
	                  null,
	                  "Change color of line", Color.blue);
	        colorPicker.setBackground(c); 
	        // Changes the color of the marker model
	        markerTool.updateAttribute("lineColor", c);
	      }
	    });
	    add(colorPicker);

		// Creates a bunch of colors
		final ColorButton[] buttons = {
			new ColorButton(Color.BLUE),
			new ColorButton(Color.GREEN),
			new ColorButton(Color.ORANGE),
			new ColorButton(Color.RED),
			new ColorButton(Color.PINK)
		};

		for(int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					markerTool.updateAttribute(
						"lineColor", 
						((ColorButton)e.getSource()).getBackground()
					);
				}
			});
			add(buttons[i]);
		}
	}

	private class ColorButton extends JButton {
		private static final long serialVersionUID = 1L;
		public ColorButton(Color color) {
			setBackground(color);
			setPreferredSize(new Dimension(50, 100));
			this.setUI(new ColorButtonUI(color));
		}
	}


}

