package edu.mccc.cos210.fp2014.col.client.whiteboardSelection;

import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.whiteboardSelection.WhiteboardSelectionItemView;
import edu.mccc.cos210.fp2014.col.client.UI.CustomBasicButtonUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
/**
 * The styling for the WhiteboardSelectionView's children
 * Contains WhiteboardModel
 * Allows user to enter WhiteboardView once this is clicked 
 */
public class WhiteboardSelectionItemView{

	private CollaboliciousFrame window;
	public WhiteboardModel whiteboard;
	public JButton itemView;

	public WhiteboardSelectionItemView(
		WhiteboardModel whiteboard, 
		CollaboliciousFrame window) {
		this.window = window;
		this.whiteboard = whiteboard;
	}

	public JButton show() {
		this.itemView = new JButton(this.whiteboard.getName());
		this.itemView.setUI(new CustomBasicButtonUI());
		this.itemView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	window.showWhiteboardView(whiteboard);
            }
        });
		return this.itemView;
	}

}