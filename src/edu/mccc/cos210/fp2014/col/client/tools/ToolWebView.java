package edu.mccc.cos210.fp2014.col.client.tools;
import edu.mccc.cos210.fp2014.col.client.objects.WebpageModel;
import edu.mccc.cos210.fp2014.col.client.UI.ColorButtonUI;
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
* Contains the settings that can be applied to the webpageModel
*/
public class ToolWebView {
	
	private JPanel content;
	
	public ToolWebView(final WebpageModel webpageModel) {

		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		ColorPallet colorPallet = new ColorPallet(webpageModel);
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
			new ItemChangeListener(webpageModel)
		);
		content.add(fontList);

		JPanel textContent = new JPanel();
		textContent.setLayout(
			new BoxLayout(
				textContent, 
				BoxLayout.LINE_AXIS
			)
		);
		final JTextField txtContent = new JTextField();
		txtContent.setText(webpageModel.getContent());
	    txtContent.getDocument().addDocumentListener(
	    	new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					change();
				}
				public void removeUpdate(DocumentEvent e) {
					change();
				}
				public void insertUpdate(DocumentEvent e) {
					change();
				}
				public void change(){
					webpageModel.setContent(
						txtContent.getText()
					);
				}
			});
	    textContent.add(txtContent);
		content.add(textContent);

		JPanel lineThickness = new JPanel();
		final JSlider spinner = new JSlider(
			JSlider.HORIZONTAL,
			1, 
			100, 
			((Integer)webpageModel.getFontSize())
		);
		spinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            webpageModel.setFontSize(
	            	(Integer)spinner.getValue()
	            );
	        }
	    });
	    spinner.setMajorTickSpacing(10);
        spinner.setMinorTickSpacing(1);
		lineThickness.add(spinner);
		content.add(lineThickness);

		JPanel visitUrl = new JPanel();
		visitUrl.setLayout(
			new BoxLayout(
				visitUrl, 
				BoxLayout.LINE_AXIS
			)
		);
		JButton btnGo = new JButton("Visit");
		btnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(
						java.net.URI.create(webpageModel.getContent())
					);
				}
				catch (java.io.IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
        });
		visitUrl.add(btnGo);
		content.add(visitUrl);
        content.setVisible(true);
        content.setPreferredSize(content.getPreferredSize());

	}

	public JPanel show() { 
		return this.content;
	}

	public class ColorPallet extends JPanel {
		private static final long serialVersionUID = 1L;
		public ColorPallet(final WebpageModel webpageModel){
			
			final JButton colorPicker = new ColorButton(
				webpageModel.getColor()
			);
		    colorPicker.addActionListener(new ActionListener() { 
		      public void actionPerformed(ActionEvent e) {
		        Color c;
		        c = JColorChooser.showDialog(
		                  null,
		                  "Change color of line", Color.blue);
		        colorPicker.setBackground(c); 
		        // Changes the color of the marker model
		        webpageModel.setColor(c);
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
						webpageModel.setColor(	
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
	    WebpageModel webpageModel;
	    public ItemChangeListener(WebpageModel webpageModel) {
	    	this.webpageModel = webpageModel;
	    }
	    @Override
	    public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				String fontName = (String)event.getItem();
				webpageModel.setFont(
					new Font(
						fontName, 
						0, 
						webpageModel.getFontSize()
					)
				);
			}
	    }       
	}

}