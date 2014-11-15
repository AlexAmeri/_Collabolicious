package edu.mccc.cos210.fp2014.col.client.whiteboardSelection;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.whiteboardSelection.WhiteboardSelectionSidebarView;
import edu.mccc.cos210.fp2014.col.client.tools.ToolButton;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.MouseInputAdapter;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
/**
 * This view is primarly for,
 * Adding, opening and selecting existing whiteboards
 * contains WhiteboardModelCollection which is an array of all whiteboard values
 */
public class WhiteboardSelectionView extends JPanel{
	private static final long serialVersionUID = 1L;
	private CollaboliciousFrame window;
	private WhiteboardModel[] whiteboardModels;

	public WhiteboardSelectionView(
		CollaboliciousFrame mainWindow,
		WhiteboardModel[] whiteboardModels) {
		this.window = mainWindow;
		this.window.getContentPane().setBackground(Color.WHITE);
		this.whiteboardModels = whiteboardModels;
		show();	
	}
		@SuppressWarnings("deprecation")
		public void show() {

			WhiteboardSelectionSidebarView sidebar =
			 new WhiteboardSelectionSidebarView(this.window);
			WhiteboardSelectionCollectionView whiteboardCollection =
			 new WhiteboardSelectionCollectionView(
			 	this.window, 
			 	this.whiteboardModels
			 );

			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.setBackground(Color.WHITE);
			this.window.getContentPane().add(mainPanel, BorderLayout.CENTER);
			mainPanel.add(
				whiteboardCollection.show(), 
				BorderLayout.CENTER
			);

			JPanel topMenu = new JPanel();
			topMenu.setBackground(Color.WHITE);
			topMenu.setBorder(
				BorderFactory.createMatteBorder(
					10, 
					5, 
					10, 
					5, 
					Color.WHITE
				)
			);
			topMenu.setLayout(new BorderLayout());
			ToolButton btnOpenLocal = new ToolButton("OPEN_LOCAL", "../assets/icon-open.png");
			ToolButton btnAddNew = new ToolButton("ADD_NEW", "../assets/icon-add.png");
			// btnAddNew.addActionListener(new ActionListener() { 
			//      public void actionPerformed(ActionEvent e) {
			//      	WhiteboardModel whiteboard = new WhiteboardModel("New Whiteboard");
			//      	window.showWhiteboardView(whiteboard);
			//      }
			//    });
			topMenu.add(btnOpenLocal, BorderLayout.LINE_START);
			topMenu.add(btnAddNew, BorderLayout.LINE_END);
			mainPanel.add(topMenu, BorderLayout.NORTH);
			this.window.setVisible(true);
			System.out.println("DONE LOADING");
		}
}