package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.tools.ColorPallet;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
* Contains the settings that can be applied to the MarkerTool
*/
public class ToolMarkerView {
	
	private JPanel content;
	
	public ToolMarkerView(final ToolModel markerTool) {

		content = new JPanel();
		content.setLayout(
			new BoxLayout(
				content, 
				BoxLayout.Y_AXIS
			)
		);

		ColorPallet colorPallet = new ColorPallet(markerTool);
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

		JPanel lineThickness = new JPanel();
		final JSlider spinner = new JSlider(
			JSlider.HORIZONTAL,
			1, 
			100, 
			((Integer)markerTool.getAttribute("lineThickness"))
		);
		spinner.addChangeListener(new ChangeListener() {
		    @Override
		    public void stateChanged(ChangeEvent e) {
		        markerTool.updateAttribute(
		        	"lineThickness", 
		        	spinner.getValue()
		        );
		    }
		});
		spinner.setMajorTickSpacing(10);
		spinner.setMinorTickSpacing(1);
		lineThickness.add(spinner);
		content.add(lineThickness);

		JCheckBox chkOCR = new JCheckBox("OCR Mode");
		if((boolean)markerTool.getAttribute("OCRMode")){
			chkOCR.setSelected(true);
		}
		chkOCR.setPreferredSize(chkOCR.getPreferredSize());
		chkOCR.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
		        if (e.getStateChange() == ItemEvent.SELECTED){
		        	markerTool.updateAttribute(
			        	"OCRMode", 
			        	true
			        );
		        }else{
		        	markerTool.updateAttribute(
			        	"OCRMode", 
			        	false
			        );
		        }
		    }
		});
		content.add(chkOCR);

        content.setVisible(true);
        content.setPreferredSize(content.getPreferredSize());
	}

	public JPanel show() { 
		return this.content;
	}

}