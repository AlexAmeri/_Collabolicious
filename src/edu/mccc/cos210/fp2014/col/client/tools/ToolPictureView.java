package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.objects.ImageModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/*
* Contains the settings that can be applied to the imageModel
*/
public class ToolPictureView {
	
	private JPanel content;
	
	public ToolPictureView(final ImageModel imageModel) {

		content = new JPanel();
		content.setLayout(
			new BoxLayout(
				content,
				BoxLayout.Y_AXIS
			)
		);
		JPanel pictureSize = new JPanel();
		pictureSize.setLayout(
			new BoxLayout(
				pictureSize, 
				BoxLayout.LINE_AXIS
			)
		);
		SpinnerNumberModel pictureSizeModel = 
			new SpinnerNumberModel(0, 0, 1000, 1);
		final JSpinner spinner = new JSpinner(pictureSizeModel);
		spinner.setValue(imageModel.getWidth());
		spinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            imageModel.setSize(
	            	(Integer)spinner.getValue(), 
	            	(Integer)spinner.getValue()
	            );
	        }
	    });
		pictureSize.add(spinner);
		content.add(pictureSize);
		JPanel pictureURL = new JPanel();
		pictureURL.setLayout(
			new BoxLayout(
				pictureURL, 
				BoxLayout.LINE_AXIS
			)
		);
		JButton btnUpload = new JButton("Upload");
		btnUpload.setPreferredSize(btnUpload.getPreferredSize());
		btnUpload.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
			    FileNameExtensionFilter filter = 
			    new FileNameExtensionFilter(
			        "JPG & GIF Images", 
			        "jpg", 
			        "gif", 
			        "png", 
			        "jpeg"
			    );
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(
			    	new JFrame()
			    );
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
					imageModel.setContents(
						chooser.getSelectedFile(),
						(Integer)spinner.getValue(),
						(Integer)spinner.getValue()
					);
			    };
			}
	    });
		pictureURL.add(btnUpload);
		content.add(pictureURL);
        content.setVisible(true);
        content.setSize(
        	(int)content.getPreferredSize().getWidth() + 20,
        	(int)content.getPreferredSize().getHeight()
        );

	}

	public JPanel show() { 
		return this.content;
	}

}