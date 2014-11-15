package edu.mccc.cos210.fp2014.col.client.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;

public class CustomBasicButtonUI extends ButtonUI {
     
      public CustomBasicButtonUI(){

      }

      public static ComponentUI createUI(JComponent c) {
         return new CustomBasicButtonUI();
      }

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
         d.width = (int)c.getParent().getSize().getWidth() - (c.getParent().getInsets().left + c.getParent().getInsets().right);
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
         int x = d.width;
         int y = d.height;

         int[] outerX = { 0, 0, x, x };
         int[] outerY = { 0, y, y, 0 };

         Polygon outer = new Polygon(
            outerX, 
            outerY, 
            outerX.length
         );

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
                  new Color(38, 139, 210),
                  2,
                  true
               )
            );

            g2.setColor(backgroundColor);
            g2.fillPolygon(outer);

            g2.setColor(new Color(33, 118, 199));
            String s = ((JButton)c).getText();
            x = I.left;
            y = I.top + fm.getAscent();
            g2.drawString(s, x, y);
         }
         g2.dispose();

      }

   }