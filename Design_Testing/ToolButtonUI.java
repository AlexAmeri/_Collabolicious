import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicToggleButtonUI;

public class ToolButtonUI extends BasicToggleButtonUI {
      
      private BufferedImage offImage;
      private BufferedImage onImage;

      public ToolButtonUI(
         String offImage,
         String onImage){
         try {
            this.offImage = ImageIO.read(
               getClass().getResource(offImage)
            );
            this.onImage = ImageIO.read(
               getClass().getResource(onImage)
            );
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }

      public static ComponentUI createUI(
         JComponent c,
         String offImage,
         String onImage) {
         return new ToolButtonUI(
            offImage,
            onImage
         );
      }

      public Dimension getPreferredSize(JComponent c) {
        return new Dimension(
          (int)c.getParent().getSize().getWidth() / 2,
          (int)c.getParent().getSize().getWidth() / 2
        );
      }

      public Dimension getMinimumSize(JComponent c) {
        return new Dimension(
          (int)c.getParent().getSize().getWidth() / 2,
          (int)c.getParent().getSize().getWidth() / 2
        );
      }

      public Dimension getMaximumSize(JComponent c) {
        return new Dimension(
          (int)c.getParent().getSize().getWidth() / 2,
          (int)c.getParent().getSize().getWidth() / 2
        );
      }

      public void paint(Graphics g, JComponent c) {
          c.setBorder(BorderFactory.createEmptyBorder());
          Graphics2D g2d = (Graphics2D) g.create();
          ToolButton model = (ToolButton) c;
          AffineTransform at = new AffineTransform();
          at.scale(
          (double) c.getWidth() / offImage.getWidth(),
          (double) c.getHeight() / offImage.getHeight()
          );
          if (model.isSelected()) {
             g2d.drawRenderedImage(onImage, at);
          } else {
             g2d.drawRenderedImage(offImage, at);         
          }
          g2d.dispose();
      }
}