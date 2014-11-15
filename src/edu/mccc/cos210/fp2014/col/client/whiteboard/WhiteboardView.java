package edu.mccc.cos210.fp2014.col.client.whiteboard;

import edu.mccc.cos210.fp2014.col.client.tools.Sidepanel;
import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.tools.ToolView;
import edu.mccc.cos210.fp2014.col.client.tools.Tools;
import edu.mccc.cos210.fp2014.col.client.tools.ToolButton;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.UI.ToolButtonUI;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class WhiteboardView {

	private CollaboliciousFrame window;
	private WhiteboardModel whiteboard;
	private WhiteboardPanel content;
	private JPanel toolBar;
	private ToolView toolView;
	private Sidepanel toolPopover = new Sidepanel();
	public ToolButton[] toolBarButtons = new ToolButton[9];

	public WhiteboardView(
		CollaboliciousFrame mainWindow, 
		WhiteboardModel whiteboardModel) {
		window = mainWindow;
		this.whiteboard = whiteboardModel;
		show();
	}

	public void show() {
		this.window.setResizable(true);
		setToolBar();
		setToolView();
		setZoomBar();
		setWhiteboardPanel();
		this.window.setVisible(true);
	}

	public void setWhiteboardPanel() {
		content = new WhiteboardPanel(
	    	this,
	    	this.whiteboard, 
	    	this.toolView,
	    	this.toolPopover
	    );
	    window.add(content, BorderLayout.CENTER);
	}
	public WhiteboardPanel getPanel() {
		return this.content;
	}
	public void setZoomBar() {
		ToolButton[] buttons = {
			new ToolButton(
				"EXIT", 
				"../assets/icon-exit-off.png",
				"../assets/icon-exit-on.png"
			)
		};

		JPanel toolButtons = new JPanel();
		toolButtons.setBorder(new CompoundBorder(
			BorderFactory.createMatteBorder(
                           0, 0, 0, 1, Color.BLACK),
			BorderFactory.createMatteBorder(
                           5, 10, 10, 15, Color.WHITE)
			));
		toolButtons.setLayout(new GridLayout(9, 1, 10, 10));
		toolButtons.setBackground(Color.WHITE);
	    for(int i = 0; i < buttons.length; i++) {
	    	toolButtons.add(buttons[i], i);
	    	buttons[i].addMouseListener(
	    		new ToolButtonListener(buttons[i].name)
	    	);
	    }
	    window.add(toolButtons, BorderLayout.LINE_END);
	}

	public void setToolView() {
		window.getContentPane().add(toolPopover);
	    this.toolView = new ToolView(toolPopover, this.window);
	}

	public void setToolBar() {
		ToolButton[] buttons = {
			new ToolButton(
				"TEXTBOX", 
				"../assets/icon-text-off.png",
				"../assets/icon-text-on.png"
			),
			new ToolButton(
				"MARKER", 
				"../assets/icon-marker-off.png",
				"../assets/icon-marker-on.png"
			),
			new ToolButton(
				"ERASER", 
				"../assets/icon-eraser-off.png",
				"../assets/icon-eraser-on.png"
			),
			new ToolButton(
				"PICTURE", 
				"../assets/icon-picture-off.png",
				"../assets/icon-picture-on.png"
			),
			new ToolButton(
				"SHAPE", 
				"../assets/icon-shape-off.png",
				"../assets/icon-shape-on.png"
			),
			new ToolButton(
				"WEB", 
				"../assets/icon-web-off.png",
				"../assets/icon-web-on.png"
			),
			new ToolButton(
				"LINK", 
				"../assets/icon-link-off.png",
				"../assets/icon-link-on.png"
			),
			new ToolButton(
				"FILE", 
				"../assets/icon-file-off.png",
				"../assets/icon-file-on.png"
			)
		};
		toolBarButtons = buttons;
		JPanel toolButtons = new JPanel();
		toolButtons.setBorder(new CompoundBorder(
			BorderFactory.createMatteBorder(
                           0, 0, 0, 1, Color.BLACK),
			BorderFactory.createMatteBorder(
                           5, 10, 10, 15, Color.WHITE)
			));
		toolButtons.setLayout(new GridLayout(0,1, 10,10));
		toolButtons.setBackground(Color.WHITE);
	    for(int i = 0; i < buttons.length; i++) {
	    	toolButtons.add(buttons[i]);
	    	buttons[i].addMouseListener(
	    		new ToolButtonListener(buttons[i].name)
	    	);
	    }
	    window.add(toolButtons, BorderLayout.LINE_START);
	}

	/*
	Handles all listening to if a tool button was selected
	 */
	private class ToolButtonListener extends MouseInputAdapter {
		private String name;
		public ToolButtonListener(String toolName) {
			name = toolName;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			for(int i = 0; i < toolBarButtons.length; i++) {
				toolBarButtons[i].setSelected(false);
		    }
			((ToolButton)e.getSource()).setSelected();
			Point position = new Point(
				(int)((Component)e.getSource()).getLocation().getX() + (((ToolButton)e.getSource()).getWidth() / 2),
				(int)((Component)e.getSource()).getLocation().getY()
			);
			ToolSelected(
				this.name,
				position
			);
		}
	}

	public void clearSelectedTool() {
		this.toolView.hideSidebar();
		this.whiteboard.clearSelectedTool();
	}

	public void ToolSelected(String name, Point position) {
		Tools tool = Tools.valueOf(name.toUpperCase());
		switch(tool){

			case EXIT:
					if (this.whiteboard.getNetworkManager() != null) {
						whiteboard.getNetworkManager().setWhiteboardModel(null);
						whiteboard.setNetworkManager(null);
						window.showOnlineWhiteboardSelectionView();
					} else {
						window.showLoginView();
					}
				break;

			case TEXTBOX:
					if(this.whiteboard.getSelectedTool() == Tools.TEXTBOX){
						clearSelectedTool();
						this.toolView.hideSidebar();
					}else{
						this.toolView.showTextView(position);
						this.whiteboard.setSelectedTool(Tools.TEXTBOX);
					}
				break;

			case ERASER:
					if(this.whiteboard.getSelectedTool() == Tools.ERASER){
						clearSelectedTool();
						this.toolView.hideSidebar();
					}else{
						this.toolView.showEraserView(position);
						this.whiteboard.setSelectedTool(Tools.ERASER);
					}
				break;

			case MARKER:
					if(this.whiteboard.getSelectedTool() == Tools.MARKER){
						clearSelectedTool();
						this.toolView.hideSidebar();
					}else{
						this.toolView.showMarkerView(position);
						this.whiteboard.setSelectedTool(Tools.MARKER);
					}
				break;


			case PICTURE:
					if(this.whiteboard.getSelectedTool() == Tools.PICTURE){
						clearSelectedTool();
						this.toolView.hideSidebar();
					}else{
						this.toolView.showPictureView(position);
						this.whiteboard.setSelectedTool(Tools.PICTURE);
					}
				break;

			case WEB:
					if(this.whiteboard.getSelectedTool() == Tools.WEB){
						clearSelectedTool();
						this.toolView.hideSidebar();
					}else{
						this.toolView.showWebpageView(position);
						this.whiteboard.setSelectedTool(Tools.WEB);
					}
				break;

			case LINK:
				if(this.whiteboard.getSelectedTool() == Tools.LINK){
						clearSelectedTool();
						this.toolView.hideSidebar();
					}else{
						this.toolView.showLinkView(position);
						this.whiteboard.setSelectedTool(Tools.LINK);
					}
				break;

			case FILE:
				if(this.whiteboard.getSelectedTool() == Tools.FILE){
					clearSelectedTool();
					this.toolView.hideSidebar();
				}else{
					this.toolView.showFileView(position);
					this.whiteboard.setSelectedTool(Tools.FILE);
				}
				break;

			case SETTINGS:

				break;

			case SHAPE:
				if(this.whiteboard.getSelectedTool() == Tools.SHAPE){
					clearSelectedTool();
					this.toolView.hideSidebar();
				}else{
					this.toolView.showShapeView(position);
					this.whiteboard.setSelectedTool(Tools.SHAPE);
				}
				break;

			case SAVE:
				System.out.println("SAVE ME!");
				break;

			case SHARE:
				break;
		}

	}	

}