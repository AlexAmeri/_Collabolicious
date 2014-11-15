package edu.mccc.cos210.fp2014.col.client.whiteboardSelection;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import java.util.ArrayList;
import java.awt.Container;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
public class WhiteboardCreateView {
	private JButton register, logIn, offLine;
	private JTextField userName, password;
	private FlowLayout flowLayout;
	private BoxLayout boxLayout;
	private JPanel panel1, panel2, panel3, panel4, subPane, subPanel, subPanel1, subPanel2;
	private JLabel welcome, usernameLabel, passwordLabel;
	private int width = 500, heigth = 500;
	private CollaboliciousFrame frame;
	private boolean isInOfflineMode = false;
	private JTextField f;
	private JTextField field; 
	private String uploadedWhiteboardData = null;
	private ArrayList<String> teammates = new ArrayList<String>();
	public WhiteboardCreateView(CollaboliciousFrame frame) {
		this.frame = frame;
		subPane = new JPanel();
		subPane.setLayout(new BoxLayout(subPane, BoxLayout.Y_AXIS));
		drawCreate();
	}
	private void drawCreate() {
		final JPanel pa = new JPanel();
		pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
		pa.setBackground(Color.WHITE);
		//Set Name
		JPanel pa1 = new JPanel();
		pa1.setBackground(Color.WHITE);	
		JLabel jl = new JLabel("New Whiteboard");
		jl.setFont(new Font(jl.getText(), Font.PLAIN, 35));
		jl.setAlignmentX(Component.CENTER_ALIGNMENT);
		pa1.add(jl);
		JPanel pa2 = new JPanel();
		pa2.setBackground(Color.WHITE);
		f = new JTextField(26);
		JLabel jl1 = new JLabel("  Whiteboard Name     ");
		jl1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pa2.add(jl1);
		pa2.add(f);
	    // Panel title
		JLabel title = new JLabel("Currently Shared With:");
		Font font = new Font("Sans", Font.PLAIN, 20);
		title.setFont(font);
		title.setSize(title.getPreferredSize());
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel spacer = new JLabel(" ");
		subPane.add(title);
		subPane.add(spacer);
	    //Teammate
		JPanel pan = new JPanel();
		pan.setBackground(Color.WHITE);
		JButton jbt = new JButton("Add Teammate");
		jbt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String newTeammateName = field.getText();
				try {
					boolean exists = frame.
							 getNetworkManager().
							 sendCheckIfUserExistsMessage(newTeammateName);
					if (exists) {
						teammates.add(newTeammateName);
						JLabel name = new JLabel(newTeammateName);
						name.setAlignmentX(Component.CENTER_ALIGNMENT);
						subPane.add(name);
						subPane.revalidate();
						subPane.repaint();
					} else {
						JFrame failFrame = new JFrame();
						failFrame.setLocationRelativeTo(null);
						failFrame.add(new JLabel("That User doesn't exist in our Database"));
						failFrame.pack();
						failFrame.setVisible(true);
					}
				} catch (Exception e) {
				}
			}
		});
		jbt.setSize(jbt.getPreferredSize());
		field = new JTextField(26);
		pan.add(jbt);
		pan.add(field);	
		JScrollPane pane1 = new JScrollPane();
		pane1.setPreferredSize(new Dimension(395, 150));
		//pane1.createHorizontalScrollBar();
		pane1.createVerticalScrollBar();
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(65, 150));
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);	
		subPane.setPreferredSize(new Dimension(395, 200));
		pane1.setViewportView(subPane);
		container.add(panel);
		container.add(pane1);
		JPanel pa3 = new JPanel();
		pa3.setBackground(Color.WHITE);
		JButton b = new JButton("CANCEL");
		b.addMouseListener(new MouseInputAdapter() {	
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isInOfflineMode) {	
					frame.showOnlineWhiteboardSelectionView();
				} else {
					frame.showWhiteboardSelectionView();
				}		
			}
		});
		pa3.add(b);
		JPanel subpa3 = new JPanel();
		subpa3.setBackground(Color.WHITE);
		subpa3.setPreferredSize(new Dimension(275, 40));
		pa3.add(subpa3);
		JButton createWB = new JButton("CREATE");
		createWB.addMouseListener(new MouseInputAdapter() {	
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isInOfflineMode) {
					try {
						WhiteboardModel w = new WhiteboardModel(f.getText() + "@" + 									
						frame.getNetworkManager().getUsername());
						w.networked = true;
						frame.getOnlineWhiteboardModels().add(w);
						frame.getNetworkManager().sendCreateWhiteboardMessage(f.getText());
						for(String s : teammates) {
							frame.getNetworkManager().sendShareWhiteboardMessage(f.getText(), s);
							w.addUser(s);
						}
					} catch (Exception g) {
					}
					frame.showOnlineWhiteboardSelectionView();
				} else {
					WhiteboardModel w = new WhiteboardModel(f.getText());
					frame.getOfflineWhiteboardModels().add(w);
					frame.showWhiteboardSelectionView();
				}	
			}
		});
		createWB.setSize(createWB.getPreferredSize());
		pa3.add(createWB);		
		pa.add(pa1);
		pa.add(pa2);
		pa.add(pan);
		pa.add(container);
		pa.add(pa3);
		frame.add(pa);
		frame.pack();
	}
	private void startWhiteboard() {
	}	
}
