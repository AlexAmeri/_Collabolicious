package edu.mccc.cos210.fp2014.col.client.whiteboardSelection;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.UI.CustomBasicButtonUI;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * The navigation and filter for the WhiteboardSelectionView
 * Main purpose to display filter, 
 * Options, user info and to logout of the application
 */
public class WhiteboardSelectionSidebarView extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private CollaboliciousFrame window;
	private JButton btnExit;
	private JButton btnNew;
	private JButton all;
	private JButton personal;
	private JButton shared;

	public WhiteboardSelectionSidebarView(
		CollaboliciousFrame mainWindow) {
		window = mainWindow;
		show();	
	}
		@SuppressWarnings("deprecation")
		public void show() {

			JPanel sideMenu = new JPanel();
			sideMenu.setBackground(Color.WHITE);
			sideMenu.setSize(150, window.getHeight());
			sideMenu.setLayout(new BorderLayout());
			window.getContentPane().add(
				sideMenu, 
				BorderLayout.LINE_START
			);

			JPanel pExit = new JPanel();
	        pExit.setBackground(Color.WHITE);
	        pExit.setBorder(
	        	BorderFactory.createMatteBorder(
	        		0, 
	        		5, 
	        		0,
	        		5, 
	        		Color.WHITE
	        	)
	        );
			btnExit = new JButton("Exit");
			btnExit.setUI(new CustomBasicButtonUI());
			btnExit.addActionListener(new ActionListener() { 
		      public void actionPerformed(ActionEvent e) {
		      	window.showLoginView();
		      }
		    });
		    pExit.add(btnExit);
			sideMenu.add(pExit, BorderLayout.PAGE_END);
			pExit.setSize(
				150, 
				(int)pExit.getPreferredSize().getHeight()
			);
			JPanel pSettings = new JPanel();
	        pSettings.setBackground(Color.WHITE);
	        pSettings.setBorder(
	        	BorderFactory.createMatteBorder(
	        		0, 
	        		5, 
	        		0,
	        		5, 
	        		Color.WHITE
	        	)
	        );
			JButton btnUser = new JButton("Settings");
			btnUser.setUI(new CustomBasicButtonUI());
			pSettings.add(btnUser);
			sideMenu.add(pSettings, BorderLayout.PAGE_START);
			pSettings.setSize(
				150, 
				(int)pSettings.getPreferredSize().getHeight()
			);
	        Box filterPanel = Box.createVerticalBox();  
	        sideMenu.add(filterPanel, BorderLayout.CENTER);
	        filterPanel.setBackground(Color.WHITE);
	        filterPanel.setBorder(
	        	BorderFactory.createMatteBorder(
	        		window.getHeight() / 4, 
	        		5, 
	        		window.getHeight() / 4,
	        		5, 
	        		Color.WHITE
	        	)
	        );
			btnNew = new JButton("New Whiteboard");
			btnNew.setUI(new CustomBasicButtonUI());
			btnNew.addMouseListener(new MouseInputAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					window.showNewWhiteboardView();
				}
			});
			filterPanel.add(btnNew);  
			filterPanel.add(
	        	Box.createRigidArea(
	        		new Dimension(5,5)
	        	)
	        );			
	        all = new JButton("All");
	        all.setUI(new CustomBasicButtonUI());
	        filterPanel.add(all);  
	        filterPanel.add(
	        	Box.createRigidArea(
	        		new Dimension(5,5)
	        	)
	        );
			personal = new JButton("Personal");
			personal.setUI(new CustomBasicButtonUI());
	        filterPanel.add(personal);   
	        filterPanel.add(
	        	Box.createRigidArea(
	        		new Dimension(5,5)
	        	)
	        );
			shared = new JButton("Shared");
			shared.setUI(new CustomBasicButtonUI());
	        filterPanel.add(shared); 
			window.setVisible(true);
		}
}