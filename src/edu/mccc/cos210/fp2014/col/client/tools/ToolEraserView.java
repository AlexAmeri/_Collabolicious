package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
* Contains the settings that can be applied to the EraserTool
*/
public class ToolEraserView {
	
	private JPanel content;
	
	public ToolEraserView(final ToolModel eraserTool) {

		content = new JPanel();
		content.setLayout(
			new BoxLayout(
				content, 
				BoxLayout.Y_AXIS
			)
		);

		JPanel eraserThickness = new JPanel();
		final JSlider spinner = new JSlider(
			JSlider.HORIZONTAL,
			1, 
			100, 
			((Integer)eraserTool.getAttribute("eraserThickness"))
		);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
			    eraserTool.updateAttribute(
			    	"eraserThickness", 
			    	spinner.getValue()
			    );
			}
		});
		spinner.setMajorTickSpacing(10);
		spinner.setMinorTickSpacing(1);
		eraserThickness.add(spinner);
		content.add(eraserThickness);

        content.setVisible(true);
        content.setPreferredSize(content.getPreferredSize());

	}

	public JPanel show() { 
		return this.content;
	}


}