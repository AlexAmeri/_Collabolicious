package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.objects.FileModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
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
* Contains the settings that can be applied to the FileModel
*/
public class ToolFileView {
	
	private JPanel content;
	private File directory;
	
	public ToolFileView(final FileModel fileModel) {

		content = new JPanel();
		content.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;  
	    gbc.anchor = GridBagConstraints.FIRST_LINE_START;  
	    gbc.fill = GridBagConstraints.HORIZONTAL;  
	    gbc.gridx = 0;  
	    gbc.gridy = GridBagConstraints.RELATIVE;  

		SpinnerNumberModel fileSizeModel = 
			new SpinnerNumberModel(0, 0, 1000, 1);
		final JSpinner spinner = new JSpinner(fileSizeModel);
		spinner.setValue(fileModel.getWidth());
		spinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            fileModel.setSize(
	            	(Integer)spinner.getValue(), 
	            	(Integer)spinner.getValue()
	            );
	        }
	    });
		content.add(spinner, gbc);
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	System.out.println("START!");
	        	JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
			    int returnVal = chooser.showOpenDialog(
			    	new JFrame()
			    );
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
					String s = chooser.getSelectedFile().getName();
					System.out.println(s);
					fileModel.setTitle(s);
					fileModel.setContents(
						chooser.getSelectedFile()
					);
					System.out.println(fileModel.getTitle());
			    };
			}
	    });
	    gbc.weighty = 1.0; 
	    content.add(btnUpload, gbc);
	    JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	// Downloads the file
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(
					JFileChooser.DIRECTORIES_ONLY
					);
				int result = chooser.showSaveDialog(
					new JFrame()
				);
				if (result == JFileChooser.APPROVE_OPTION) {
					directory = chooser.getSelectedFile();
					fileModel.downloadFile(directory);
				} else {
				}
			}
	    });
		content.add(btnDownload, gbc);
        content.setVisible(true);
        content.setPreferredSize(content.getPreferredSize());
	}

	public JPanel show() { 
		return this.content;
	}

}