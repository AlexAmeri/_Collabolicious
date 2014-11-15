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

public class Button {

	public static void main(String[] ergs) {
		Testing1 testing = new Testing1();
	}

}

class Testing1 {

      public Testing1() {
         JFrame testing = new JFrame();

         JButton b2 = new JButton("Custom Button");
         JTextField txt = new JTextField("HELLO");

         // set a custom UI delegate

         b2.setUI(new CustomButtonUI());
         txt.setUI(new CustomTextfieldUI());

         JPanel panel = new JPanel();
         panel.add(b2);
         panel.add(txt);
         panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

         // make panel this JFrame's content pane

         testing.setContentPane(panel);
         testing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         testing.setTitle("DemoCustomButtonUI");
         testing.pack();
         testing.show();
      }
   }

   class CustomButtonUI extends ButtonUI
   {
      final Insets I = new Insets(8, 15, 8, 15);

      public void installUI(JComponent c)
      {
         AbstractButton b = (AbstractButton)c;
         BasicButtonListener listener = new BasicButtonListener(b);
         b.addMouseListener(listener);
      }

      public void uninstallUI(JComponent c)
      {
         AbstractButton b = (AbstractButton)c;
         BasicButtonListener listener = new BasicButtonListener(b);
         b.removeMouseListener(listener);
      }

      public Insets getDefaultMargin(AbstractButton ab)
      {
         return I;
      }

      public Insets getInsets(JComponent c)
      {
         return I;
      }

      public Dimension getMaximumSize(JComponent c)
      {
         return this.getPreferredSize(c);
      }

      public Dimension getMinimumSize(JComponent c)
      {
         return this.getPreferredSize(c);
      }

      public Dimension getPreferredSize(JComponent c)
      {
         Graphics g = c.getGraphics();
         FontMetrics fm = g.getFontMetrics();

         Dimension d = new Dimension();
         d.width = fm.stringWidth(((JButton)c).getText()) +  I.left + I.right;
         d.height = fm.getHeight() + I.top + I.bottom;
         return d;
      }

      public void paint(Graphics g, JComponent c)
      {
         Graphics2D g2 = (Graphics2D)g;

         AbstractButton b = (AbstractButton)c;
         ButtonModel bm = b.getModel();

         FontMetrics fm = g2.getFontMetrics();

         Color backgroundColor;

         Dimension d = c.getPreferredSize();
         int x = d.width - 1;
         int y = d.height - 1;

         int[] outerX = { 0, 0, x, x };
         int[] outerY = { 0, y, y, 0 };

         Polygon outer = new Polygon(outerX, outerY, outerX.length);

         if (bm.isPressed())
         {
            backgroundColor = new Color(33, 118, 199, 75);
            c.setBorder(
               new LineBorder(
                  new Color(33, 118, 199, 90),
                  2,
                  true
               )
            );

            g2.setColor(backgroundColor);
            g2.fillPolygon(outer);

            g2.setColor(new Color(33, 118, 199, 50));
            String s = ((JButton)c).getText();
            x = I.left;
            y = I.top + fm.getAscent();
            g2.drawString(s, x, y);
         }
         else
         {
            backgroundColor = new Color(0, 0, 0, 15);
            c.setBorder(
               new LineBorder(
                  new Color(38, 139, 210, 50),
                  2,
                  true
               )
            );

            g2.setColor(new Color(33, 118, 199));
            String s = ((JButton)c).getText();
            x = I.left;
            y = I.top + fm.getAscent();
            g2.drawString(s, x, y);
         }

      }
   }

   class CustomTextfieldUI extends BasicTextFieldUI implements FocusListener
   {

      private boolean hideOnFocus;
      private Color color;

      public Color getColor() {
       return color;
      }

      public void setColor(Color color) {
       this.color = color;
       repaint();
      }

      private void repaint() {
       if(getComponent() != null) {
           getComponent().repaint();           
       }
      }

      public boolean isHideOnFocus() {
       return hideOnFocus;
      }

      public void setHideOnFocus(boolean hideOnFocus) {
       this.hideOnFocus = hideOnFocus;
       repaint();
      }

      @Override
      protected void paintSafely(Graphics g) {
       super.paintSafely(g);
       JTextComponent comp = getComponent();
       if(comp.getText().length() == 0 && (!(hideOnFocus && comp.hasFocus()))){
           if(color != null) {
               g.setColor(color);
           } else {
               g.setColor(comp.getForeground().brighter().brighter().brighter());              
           }
           int padding = (comp.getHeight() - comp.getFont().getSize())/2;         
       }
       if(comp.isFocusOwner()){
         comp.setBorder(
            new CompoundBorder(
               new LineBorder(
                  new Color(33, 118, 199, 90),
                  2,
                  true
               ),
               BorderFactory.createMatteBorder(
                           5, 10, 10, 15, Color.WHITE)
            )
         );
       }else{
         comp.setBorder(
            new CompoundBorder(
               new LineBorder(
                  new Color(33, 118, 199, 50),
                  2,
                  true
               ),
               BorderFactory.createMatteBorder(
                           5, 10, 10, 15, Color.WHITE)
               )
         );
       }
      }

      @Override
      protected void installListeners() {
       super.installListeners();
       getComponent().addFocusListener(this);
      }
      @Override
      protected void uninstallListeners() {
       super.uninstallListeners();
       getComponent().removeFocusListener(this);
      }

      public void focusGained(FocusEvent e) {
      }

      public void focusLost(FocusEvent e) {
      }
   }