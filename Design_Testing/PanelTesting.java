import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.LineBorder;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;
import javax.swing.border.CompoundBorder;

public class PanelTesting {

	public PanelTesting() {
		JFrame testing = new JFrame();
		testing.setBackground(Color.BLUE);
		Panel p = new Panel();
		p.add(new JLabel("HELLO"));
		p.setSize(100, 100);
		testing.add(p, BorderLayout.CENTER);
		testing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testing.setTitle("DemoCustomButtonUI");
		testing.pack();
		testing.show();
	}

	public static void main(String[] ergs) {
		PanelTesting p = new PanelTesting();
	}


	private class Panel extends JPanel{

		public Panel() {
			super();
			this.setBackground(new Color(0,0,0,64));
		}

	}
}