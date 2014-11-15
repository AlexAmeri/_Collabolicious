package edu.mccc.cos210.fp2014.col.client.tools;
import edu.mccc.cos210.fp2014.col.client.objects.LinkModel;
import edu.mccc.cos210.fp2014.col.client.tools.ColorPallet;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.UI.ColorButtonUI;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JColorChooser;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
/*
* Contains the settings that can be applied to the linkModel
*/
public class ToolLinkView {
	
	private JPanel content;
	public CollaboliciousFrame window;
	private boolean onlineLink;

	public ToolLinkView(
		final LinkModel linkModel, 
		final CollaboliciousFrame window) {

		this.window = window;
		content = new JPanel();
		content.setLayout(
			new BoxLayout(
				content, 
				BoxLayout.Y_AXIS
			)
		);
		this.onlineLink = window.getOnlineMode();
		ColorPallet colorPallet = new ColorPallet(linkModel);
		JScrollPane scrollPane = new JScrollPane(colorPallet);
		scrollPane.setPreferredSize(
			new Dimension(
				content.getWidth() + 15, 
				125
			)
		);
		scrollPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_NEVER
		);
		content.add(scrollPane);

		GraphicsEnvironment e = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); 
	    String[] fontNames = new String[fonts.length]; 
	    for (int i = 0; i < fonts.length; i++) {
	    	fontNames[i] = fonts[i].getFontName();
	    }

		final JComboBox<String> fontList = 
		new JComboBox<String>(fontNames);
		fontList.setSelectedIndex(4);
		fontList.addItemListener(
			new ItemChangeListener(linkModel)
		);
		content.add(fontList);

		ArrayList<WhiteboardModel> whiteboardModels = 
	    	this.window.getOnlineWhiteboardModels();

		String[] whiteboards = new String[whiteboardModels.size()]; 
	    for (int i = 0; i < whiteboardModels.size(); i++) {
	    	whiteboards[i] = whiteboardModels.get(i).getName();
	    }

		final JComboBox<String> whiteboardList = 
			new JComboBox<String>(whiteboards);
		whiteboardList.setSelectedIndex(0);
		whiteboardList.addItemListener(
			new WhiteboardSelectionListener(linkModel, whiteboardModels)
		);
		content.add(whiteboardList);

		JPanel textContent = new JPanel();
		textContent.setLayout(
			new BoxLayout(
				textContent, 
				BoxLayout.LINE_AXIS
			)
		);
		JPanel lineThickness = new JPanel();
		final JSlider spinner = new JSlider(
			JSlider.HORIZONTAL,
			1, 
			500, 
			(int)linkModel.getSize().getHeight()
		);
		spinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            linkModel.setSize(
	            	(Integer)spinner.getValue(),
	            	(Integer)spinner.getValue()
	            );
	        }
	    });
	    spinner.setMajorTickSpacing(10);
        spinner.setMinorTickSpacing(1);
		lineThickness.add(spinner);
		content.add(lineThickness);
		
		try {
			String nullTest = linkModel.getContent().getName();
		} catch (Exception exception) {
			linkModel.setContent(whiteboardModels.get(0));
		}
		
		JButton btnVist = new JButton("Vist");
        btnVist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("VISIT " + linkModel.getContent().getName());
				if(onlineLink) {
					linkModel.getContent().networked = true;
				} else {
					linkModel.getContent().networked = false;
				}
				window.showWhiteboardView(linkModel.getContent());
			}
		});
		content.add(btnVist);

        content.setVisible(true);
        content.setPreferredSize(content.getPreferredSize());

	}

	public JPanel show() { 
		return this.content;
	}

	public class ColorPallet extends JPanel {
		private static final long serialVersionUID = 1L;
		public ColorPallet(final LinkModel linkModel){
			
			final JButton colorPicker = new ColorButton(
				linkModel.getColor()
			);
		    colorPicker.addActionListener(new ActionListener() { 
		      public void actionPerformed(ActionEvent e) {
		        Color c;
		        c = JColorChooser.showDialog(
		                  null,
		                  "Change color of line", Color.blue);
		        colorPicker.setBackground(c); 
		        // Changes the color of the marker model
		        linkModel.setColor(c);
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
						linkModel.setColor(	
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

	public class ItemChangeListener implements ItemListener{
	    LinkModel linkModel;
	    public ItemChangeListener(LinkModel linkModel) {
	    	this.linkModel = linkModel;
	    }
	    @Override
	    public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				String fontName = (String)event.getItem();
				linkModel.setFont(
					new Font(
						fontName, 
						0, 
						18
					)
				);
			}
	    }       
	}

	public class WhiteboardSelectionListener implements ItemListener{
	    LinkModel linkModel;
		ArrayList<WhiteboardModel> whiteboardModels;
	    public WhiteboardSelectionListener(LinkModel linkModel, ArrayList<WhiteboardModel> whiteboardModels) {
	    	this.linkModel = linkModel;
	    	this.whiteboardModels = whiteboardModels;
	    }
	    @Override
	    public void itemStateChanged(ItemEvent event) {
	    	if (event.getStateChange() == ItemEvent.SELECTED) {
				String whiteboardName = (String)event.getItem();
				for(int i = 0; i < this.whiteboardModels.size(); i++) {
					if(whiteboardName.equals(this.whiteboardModels.get(i).getName())){
						this.linkModel.setContent(this.whiteboardModels.get(i));
					}
				}
			}
	    }       
	}

}