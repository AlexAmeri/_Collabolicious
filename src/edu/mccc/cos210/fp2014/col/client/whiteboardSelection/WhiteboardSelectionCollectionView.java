package edu.mccc.cos210.fp2014.col.client.whiteboardSelection;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.whiteboardSelection.WhiteboardSelectionItemView;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.BorderFactory;
/**
 * Holds all of the item views for whiteboard models
 */
public class WhiteboardSelectionCollectionView {

	private CollaboliciousFrame window;
	private WhiteboardModel[] whiteboardModels;

	public WhiteboardSelectionCollectionView(
		CollaboliciousFrame window, 
		WhiteboardModel[] whiteboardModels) {
		this.whiteboardModels = whiteboardModels;
		this.window = window;
	}

		public JPanel show() {
			JPanel collectionView = new JPanel();
			collectionView.setBackground(Color.WHITE);
			collectionView.setBorder(
				BorderFactory.createMatteBorder(
					20, 
					15, 
					20, 
					15, 
					Color.WHITE
				)
			);
			//GridLayout gridLayout = new GridLayout(0, 3, 25, 25);
			//collectionView.setLayout(gridLayout);
			JScrollPane pane = new JScrollPane();
			pane.setPreferredSize(new Dimension(285 , 390));
			pane.createHorizontalScrollBar();
			pane.createVerticalScrollBar();
			JPanel subDashBoard2 = new JPanel();
			subDashBoard2.setPreferredSize(new Dimension(256 , 
											whiteboardModels.length / 3 * 100 + 50
											)
										  );
			subDashBoard2.setLayout(null);
			subDashBoard2.removeAll();
			pane.setViewportView(subDashBoard2);
			collectionView.add(pane);
			JLabel title = new JLabel("My Whiteboards");
			Font f = new Font("Sans", Font.PLAIN, 25);
			title.setFont(f);
			title.setSize(title.getPreferredSize());
			title.setLocation(50 , 5);
			subDashBoard2.add(title);
			for (int i = 0; i < whiteboardModels.length; i++) {
				WhiteboardSelectionItemView itemView = 
					new WhiteboardSelectionItemView(whiteboardModels[i], this.window);
				subDashBoard2.add(itemView.show());	
				itemView.itemView.setSize(new Dimension(80 , 80));
				itemView.itemView.setLocation(
									(i * 85) % 255 + 10,
									(i / 3) * 100 + 50
									);
			}

			return collectionView;
		}

}