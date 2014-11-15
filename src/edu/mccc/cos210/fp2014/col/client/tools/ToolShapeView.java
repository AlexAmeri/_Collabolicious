package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.objects.ShapeModel;
import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.tools.ToolButton;
import edu.mccc.cos210.fp2014.col.client.objects.Shape;
import edu.mccc.cos210.fp2014.col.client.UI.ColorButtonUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JColorChooser;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
* Contains the settings that can be applied to the imageModel
*/
public class ToolShapeView {
	
	private JPanel content;
	private ShapeModel shapeModel;
	
	public ToolShapeView(final ShapeModel shapeModel) {
		this.shapeModel = shapeModel;
		content = new JPanel();
		content.setLayout(
			new BoxLayout(
				content, 
				BoxLayout.Y_AXIS
			)
		);
		ColorPallet colorPallet = new ColorPallet(shapeModel);
		JScrollPane colorScrollPane = new JScrollPane(colorPallet);
		colorScrollPane.setPreferredSize(
			new Dimension(content.getWidth() + 15, 125)
		);
		colorScrollPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_NEVER
		);
		content.add(colorScrollPane);

		ShapePallet shapePallet = new ShapePallet(
			this.shapeModel
		);
		JScrollPane shapeScrollPane = new JScrollPane(
			shapePallet
		);
		shapeScrollPane.setPreferredSize(
			new Dimension(150, 65)
		);
		shapeScrollPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_NEVER
		);
		content.add(shapeScrollPane);

		JPanel shapeHeight = new JPanel();
		final JSlider spinner = new JSlider(
			JSlider.HORIZONTAL,
			1, 
			1000, 
			(Integer)this.shapeModel.getShapeHeight()
		);
		spinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
				System.out.println(shapeModel);
	        	shapeModel.setShapeHeight(
	        		spinner.getValue()
	        	);
	        }
	    });
	    spinner.setMajorTickSpacing(10);
        spinner.setMinorTickSpacing(1);
		shapeHeight.add(spinner);
		content.add(shapeHeight);

	    content.setVisible(true);
	    content.setPreferredSize(
	    	content.getPreferredSize()
	    );
	}

	public JPanel show() { 
		return this.content;
	}

	public class ColorPallet extends JPanel {
		private static final long serialVersionUID = 1L;
		public ColorPallet(final ShapeModel shapeModel){

			final JButton colorPicker = new ColorButton(
				shapeModel.getColor()
			);
		    colorPicker.addActionListener(new ActionListener() { 
		      public void actionPerformed(ActionEvent e) {
		        Color c;
		        c = JColorChooser.showDialog(
		                  null,
		                  "Change color of line", Color.blue);
		        colorPicker.setBackground(c); 
		        // Changes the color of the marker model
		        shapeModel.setColor(c);
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
				buttons[i].addActionListener(
				new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						shapeModel.setColor(	
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
				setUI(new ColorButtonUI(color));
			}
		}
	}

	private class ShapePallet extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public ShapePallet(final ShapeModel shapeModel){
			
			// Creates a bunch of shapes
			final ShapeButton[] buttons = {
				new ShapeButton(Shape.CIRCLE),
				new ShapeButton(Shape.SQUARE)
			};

			for(int i = 0; i < buttons.length; i++) {
				final int f = i;
				buttons[i].addActionListener(
				new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						shapeModel.setShape(buttons[f].getShape());
					}
				});
				add(buttons[i]);
			}

		}

		private class ShapeButton extends JButton {
			private static final long serialVersionUID = 1L;
			private Shape shape;
			private BufferedImage img;

			public ShapeButton(Shape shape) {
				this.shape = shape;
				switch (this.shape) {
					case CIRCLE :
						setPicture(
							"../assets/icon-circle.png"
						);
						break;
					case RECTANGLE :
						setPicture(
							"../assets/icon-square.png"
						);
						break;
					case SQUARE:
						setPicture(
							"../assets/icon-square.png"
						);
						break;
				}
				setPreferredSize(new Dimension(50, 50));
			}

			public Shape getShape() {
				return this.shape;
			}

			public void setPicture(String filename) {
				try {
			    	this.img = ImageIO.read(
			    		getClass().getResource(filename)
			    );
			    	setIcon(new ImageIcon(resizeImage(img, 50, 50)));
				} catch (IOException ex) {

				}
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
		}

	}
}