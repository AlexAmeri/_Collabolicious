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

public class Testing {

	ToolButton b2;

	public static void main(String[] ergs) {
		Testing testing = new Testing();
	}

	public Testing(){
		JFrame testing = new JFrame();

		b2 = 
			new ToolButton(
				"TESTING",
				"icon-eraser-off.png",
				"icon-eraser-on.png",
				45,
				45
			);

		JPanel panel = new JPanel();
		panel.add(b2);

		b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                b2.setSelected();
            }
        });

		testing.setContentPane(panel);
		testing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testing.setTitle("DemoCustomButtonUI");
		testing.pack();
		testing.show();
	}

}