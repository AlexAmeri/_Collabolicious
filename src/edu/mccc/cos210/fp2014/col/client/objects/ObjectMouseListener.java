package edu.mccc.cos210.fp2014.col.client.objects;

import edu.mccc.cos210.fp2014.col.client.tools.ToolView;
import edu.mccc.cos210.fp2014.col.client.objects.TextboxModel;
import edu.mccc.cos210.fp2014.col.client.objects.PointModel;
import edu.mccc.cos210.fp2014.col.client.objects.ImageModel;
import edu.mccc.cos210.fp2014.col.client.objects.LinkModel;
import edu.mccc.cos210.fp2014.col.client.objects.WebpageModel;
import edu.mccc.cos210.fp2014.col.client.objects.ShapeModel;
import edu.mccc.cos210.fp2014.col.client.objects.FileModel;

import java.awt.Component;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

public class ObjectMouseListener 
      implements MouseListener, MouseMotionListener, KeyListener {

   private Point loc;
   private Point locationOnScreen;
   private ToolView toolView;
   private Component selectedComponent;

   public ObjectMouseListener(ToolView toolView) {
      this.toolView = toolView;
   }

   @Override
   public void mousePressed(MouseEvent e) {
      String objectName = (((Component) e.getSource())
         .getClass().getSimpleName());
      
      Objects object = Objects.valueOf(objectName.toUpperCase());
      switch(object){

         case IMAGEMODEL: 
            toolView.showPictureView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (ImageModel)e.getSource()
            );
            break;
         case WEBPAGEMODEL:
            toolView.showWebpageView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (WebpageModel)e.getSource()
            );
            break;
         case TEXTBOXMODEL:
            toolView.showTextView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (TextboxModel)e.getSource()
            );
            break;
         case SHAPEMODEL:
            System.out.println("SHAPE SELECTED");
            toolView.showShapeView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (ShapeModel)e.getSource()
            );
            break;
         case FILEMODEL: 
            toolView.showFileView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (FileModel)e.getSource()
            );
            break;
         case LINKMODEL:
            toolView.showLinkView(
                  new Point(
                     (int)((Component)e.getSource()).getLocation().getX() 
                     + ((Component)e.getSource()).getWidth() + 10,
                     (int)((Component)e.getSource()).getLocation().getY()
                  ),
                  (LinkModel)e.getSource()
            );
            break;
      }

      this.selectedComponent = (Component)e.getSource();
      Component comp = (Component)e.getSource();
      this.loc = comp.getLocation();
      locationOnScreen = e.getLocationOnScreen();
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      Component comp = (Component)e.getSource();
      Point newLocationOnScreen = e.getLocationOnScreen();
      int x = newLocationOnScreen.x - locationOnScreen.x + this.loc.x;
      int y = newLocationOnScreen.y - locationOnScreen.y + this.loc.y;
      comp.setLocation(x, y);
   }

   @Override
   public void mouseDragged(MouseEvent e) {

      String objectName = (((Component) e.getSource())
         .getClass().getSimpleName());
      
      Objects object = Objects.valueOf(objectName.toUpperCase());
      switch(object){

         case IMAGEMODEL: 
            toolView.showPictureView(
               ((Component)e.getSource()).getLocation(), 
               (ImageModel)e.getSource()
            );
            break;
         case WEBPAGEMODEL:
            toolView.showWebpageView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (WebpageModel)e.getSource()
            );
            break;
         case TEXTBOXMODEL:
            toolView.showTextView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (TextboxModel)e.getSource()
            );
            break;
         case SHAPEMODEL:
            toolView.showShapeView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (ShapeModel)e.getSource()
            );
            break;
         case FILEMODEL: 
            toolView.showFileView(
               new Point(
                  (int)((Component)e.getSource()).getLocation().getX() 
                  + ((Component)e.getSource()).getWidth() + 10,
                  (int)((Component)e.getSource()).getLocation().getY()
               ),
               (FileModel)e.getSource()
            );
            break;
         case LINKMODEL:
            toolView.showLinkView(
                  new Point(
                     (int)((Component)e.getSource()).getLocation().getX() 
                     + ((Component)e.getSource()).getWidth() + 10,
                     (int)((Component)e.getSource()).getLocation().getY()
                  ),
                  (LinkModel)e.getSource()
            );
            break;

      }

      ((JLabel) e.getSource()).setBorder(
         BorderFactory.createMatteBorder(
            2, 2, 2, 2, new Color(118, 214, 255, 65)
         )
      );
      try {
         Component comp = (Component)e.getSource();
         Point newLocationOnScreen = e.getLocationOnScreen();
         int x = newLocationOnScreen.x - locationOnScreen.x + loc.x;
         int y = newLocationOnScreen.y - locationOnScreen.y + loc.y;
         comp.setLocation(x, y);
      }catch(Exception ex){
         System.out.println(ex);
      }
   }
   public void mouseEntered(MouseEvent e) {
      ((Component)e.getSource()).requestFocus();
      ((JLabel) e.getSource()).setBorder(
         BorderFactory.createMatteBorder(
            2, 2, 2, 2, new Color(118, 214, 255, 65)
         )
      );
   }
   public void mouseExited(MouseEvent e) {
      ((JLabel) e.getSource()).setBorder(
         BorderFactory.createMatteBorder(
            0, 0, 0, 0, new Color(0,0,0,0)
         )
      );
   }
   public void mouseMoved(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) { } 

   public void keyPressed(KeyEvent ke) { 
      System.out.println(ke.getKeyCode());
      switch(ke.getKeyCode()){
         case 8:
            this.selectedComponent.setVisible(false);
            this.selectedComponent = null;
            break;
      }
   }
   public void keyReleased(KeyEvent ke) {}
   public void keyTyped(KeyEvent ke) {}
   
}