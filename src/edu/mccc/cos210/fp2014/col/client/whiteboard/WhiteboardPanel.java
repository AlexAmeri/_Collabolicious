package edu.mccc.cos210.fp2014.col.client.whiteboard;

import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.tools.ToolView;
import edu.mccc.cos210.fp2014.col.client.tools.Tools;
import edu.mccc.cos210.fp2014.col.client.objects.TextboxModel;
import edu.mccc.cos210.fp2014.col.client.objects.PointModel;
import edu.mccc.cos210.fp2014.col.client.objects.ImageModel;
import edu.mccc.cos210.fp2014.col.client.objects.LinkModel;
import edu.mccc.cos210.fp2014.col.client.objects.WebpageModel;
import edu.mccc.cos210.fp2014.col.client.objects.FileModel;
import edu.mccc.cos210.fp2014.col.client.objects.ShapeModel;
import edu.mccc.cos210.fp2014.col.client.objects.Shape;
import edu.mccc.cos210.fp2014.col.client.objects.ShapeRecognizer;

import edu.mccc.cos210.fp2014.col.client.objects.Objects;
import edu.mccc.cos210.fp2014.col.client.objects.ObjectMouseListener;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;
import java.io.IOException;

public class WhiteboardPanel extends JPanel
               implements 
               MouseListener, 
               MouseMotionListener, 
               KeyListener {
                  
   private static final long serialVersionUID = 1L;
   private WhiteboardModel whiteboard;
   private int prevX, prevY;
   private int x, y;
   private boolean dragging; 
   private Graphics2D graphicsForDrawing;
   private ArrayList<PointModel> points; 
   private ArrayList<TextboxModel> textboxes;
   private ArrayList<ImageModel> images; 
   private ArrayList<WebpageModel> webpages;
   private ArrayList<ShapeModel> shapes;
   public ImageModel imageModel;
   public TextboxModel textModel;
   public WebpageModel webpageModel;
   public ShapeModel shapeModel;
   public FileModel fileModel;
   public LinkModel linkModel;
   private ToolView toolView;    
   public JPanel toolPopover;
   public WhiteboardView whiteboardView;
   
   private ArrayList<Integer> xPoints;
   private ArrayList<Integer> yPoints;
   private ShapeRecognizer digitRecognizer;

   public WhiteboardPanel(
      WhiteboardView whiteboardView,
      WhiteboardModel whiteboard, 
      ToolView toolView, 
      JPanel toolPopover) {
     
      this.whiteboard = whiteboard;
      this.whiteboardView = whiteboardView;
      this.toolPopover = toolPopover;
      this.points = this.whiteboard.getPoints();
      this.textboxes = this.whiteboard.getTextboxes();     
      this.images = this.whiteboard.getImages();
      this.webpages = this.whiteboard.getWebpages();
      this.shapes = this.whiteboard.getShapes();
      this.xPoints = new ArrayList<Integer>();
      this.yPoints = new ArrayList<Integer>();
      this.digitRecognizer = new ShapeRecognizer();
      this.toolView = toolView;    
      this.setLayout(null);
      this.setBackground(Color.WHITE);

      addMouseListener(this);
      addMouseMotionListener(this);
      addKeyListener(this);
	  if (!whiteboard.networked) {
		loadWhiteboard();
	  }
      createObjects();
   }

   public void createObjects() {
      this.imageModel = this.toolView.getImageModel();
      this.imageModel.setVisible(false);
      this.add(this.imageModel); 

      this.textModel = this.toolView.getTextModel();
      this.textModel.setVisible(false);
      this.add(textModel); 

      this.webpageModel = this.toolView.getWebpageModel();
      this.webpageModel.setVisible(false);
      this.add(webpageModel); 

      this.shapeModel = this.toolView.getShapeModel();
      this.shapeModel.setVisible(false);
      this.add(shapeModel); 

      this.fileModel = this.toolView.getFileModel();
      this.fileModel.setVisible(false);
      this.add(fileModel); 

      this.linkModel = this.toolView.getLinkModel();
      this.linkModel.setVisible(false);
      this.add(linkModel);
   }

   public void loadWhiteboard() {

      for(int i = 0; i < textboxes.size(); i++){
        
         TextboxModel oldTextModel = textboxes.get(i);

         TextboxModel textModel = new TextboxModel(
               oldTextModel.x, 
               oldTextModel.y, 
               oldTextModel.getContent(),
               oldTextModel.getFont(),
               oldTextModel.getColor(),
               oldTextModel.getFontSize()
            );
         
         ObjectMouseListener textModelListener =
            new ObjectMouseListener(this.toolView);
         textModel.addMouseListener(textModelListener);
         textModel.addMouseMotionListener(textModelListener);
         textModel.addKeyListener(textModelListener);
         textModel.setLocation(
            oldTextModel.x, 
            oldTextModel.y
         );
         textModel.setPreferredSize(textModel.getPreferredSize());
         this.whiteboard.addTextbox(textModel);
         this.add(textModel);
         this.revalidate();
         textboxes.remove(i);
      }   

      for(int i = 0; i < images.size(); i++){

         ImageModel oldImageModel = images.get(i);
         
         ImageModel imageModel = new ImageModel(
            oldImageModel.getContents(),
            oldImageModel.width,  
            oldImageModel.height,
            oldImageModel.x, 
            oldImageModel.y
         ); 
         ObjectMouseListener imageModelListener = 
            new ObjectMouseListener(this.toolView);
         imageModel.addMouseListener(imageModelListener);
         imageModel.addMouseMotionListener(imageModelListener);
         imageModel.addKeyListener(imageModelListener);
         this.whiteboard.addImage(imageModel);
         this.add(imageModel); 
         this.revalidate();
         this.whiteboardView.clearSelectedTool();

         images.remove(i);
      }  

      for(int i = 0; i < images.size(); i++){

         ShapeModel oldShapeModel = shapes.get(i);
         
         ShapeModel shapeModel = new ShapeModel(
            oldShapeModel.getX(), 
            oldShapeModel.getY(),
            oldShapeModel.getShape(),
            oldShapeModel.getColor(),
            oldShapeModel.getShapeSize()
         ); 
         ObjectMouseListener imageModelListener = 
            new ObjectMouseListener(this.toolView);
         shapeModel.addMouseListener(imageModelListener);
         shapeModel.addMouseMotionListener(imageModelListener);
         shapeModel.addKeyListener(imageModelListener);
         this.add(shapeModel); 
         this.revalidate();
         this.whiteboardView.clearSelectedTool();

         shapes.remove(i);
      } 
      repaint();   
   }

   public void addNotify() {
        super.addNotify();
        requestFocus();
    }

   @Override
   public void paintComponent(Graphics g) { 
      super.paintComponent(g);
      Graphics2D p = (Graphics2D) g;

      for(int i = 0; i < this.whiteboard.getPoints().size(); i++){
      	 this.whiteboard.getPoints().get(i).draw(p);
      }
      this.toolPopover.repaint();
   }
   
   private void setColor(Color newColor) {
   	graphicsForDrawing.setColor(newColor);
   }

   private Color getColor() {
   	return graphicsForDrawing.getColor();
   }
  
   private void setUpDrawingGraphics() {
      revalidate();
      graphicsForDrawing = (Graphics2D) getGraphics();
   }

   public void mouseMoved(MouseEvent evt) {

      this.imageModel.setVisible(false);
      this.webpageModel.setVisible(false);
      this.shapeModel.setVisible(false);
      this.fileModel.setVisible(false);
      this.linkModel.setVisible(false);
      this.textModel.setVisible(false);

   	x = evt.getX();
   	y = evt.getY();

   	setUpDrawingGraphics();

      switch(this.whiteboard.getSelectedTool()){

         case NONE:
         	break;

         case TEXTBOX:
            this.textModel.setVisible(true);
            this.textModel.setLocation(
               x - this.textModel.getWidth() / 2,
               y - this.textModel.getHeight() / 2
            );
            break;

         case PICTURE:
            this.imageModel.setVisible(true);
            this.imageModel.setLocation(
               x - this.imageModel.getWidth() / 2,
               y - this.imageModel.getHeight() / 2
            );
            break;	

         case WEB:
            this.webpageModel.setVisible(true);
            this.webpageModel.setLocation(
               x - this.webpageModel.getWidth() / 2,
               y - this.webpageModel.getHeight() / 2
            );
            break;

         case SHAPE:
            this.shapeModel.setVisible(true);
            this.shapeModel.setLocation(
               x - this.shapeModel.getWidth() / 2,
               y - this.shapeModel.getHeight() / 2
            );
            break;

         case FILE:
            this.fileModel.setVisible(true);
            this.fileModel.setLocation(
               x - this.fileModel.getWidth() / 2,
               y - this.fileModel.getHeight() / 2
            );
            break;

         case LINK:
            this.linkModel.setVisible(true);
            this.linkModel.setLocation(
               x - this.linkModel.getWidth() / 2,
               y - this.linkModel.getHeight() / 2
            );
            break;

      }
   }
   
   public void mousePressed(MouseEvent evt) {
   	if (dragging == true)
   	  return;      

      setUpDrawingGraphics();

   	x = evt.getX();
   	y = evt.getY();
   	prevX = x;
   	prevY = y;
   	dragging = true;

   	switch(this.whiteboard.getSelectedTool()){

   		case NONE:
   			break;

   		case TEXTBOX:
            this.textModel.setVisible(false);
   		   TextboxModel textModel = new TextboxModel(
               x, 
               y, 
               this.textModel.getContent(),
               this.textModel.getFont(),
               this.textModel.getColor(),
               this.textModel.getFontSize()
            );
            ObjectMouseListener textModelListener =
               new ObjectMouseListener(this.toolView);
            textModel.addMouseListener(textModelListener);
            textModel.addMouseMotionListener(textModelListener);
            textModel.addKeyListener(textModelListener);
            textModel.setLocation(x, y);
            textModel.setFont(this.textModel.getFont());
            this.whiteboard.addTextbox(textModel);
            this.add(textModel);
            this.revalidate();
            this.whiteboardView.clearSelectedTool();
   			break;

         case PICTURE:
            this.imageModel.setVisible(false);
            ImageModel imageModel = new ImageModel(
               this.imageModel.getContents(),
			   this.imageModel.width,
			   this.imageModel.height,
               x, 
               y
            );
            ObjectMouseListener imageModelListener = 
               new ObjectMouseListener(this.toolView);
            imageModel.addMouseListener(imageModelListener);
            imageModel.addMouseMotionListener(imageModelListener);
            imageModel.addKeyListener(imageModelListener);
            imageModel.setBounds(x, y, 100, 100);
            this.whiteboard.addImage(imageModel);
            this.add(imageModel); 
            this.revalidate();
            this.whiteboardView.clearSelectedTool();
            break;

         case WEB:
            this.webpageModel.setVisible(false);
            WebpageModel webpageModel = new WebpageModel(
               x, 
               y, 
               this.webpageModel.getContent(),
               this.webpageModel.getFont(),
               this.webpageModel.getColor(),
               this.webpageModel.getFontSize()
            ); 
            ObjectMouseListener webpageModelListener = 
               new ObjectMouseListener(this.toolView); 
            webpageModel.addMouseListener(webpageModelListener);
            webpageModel.addMouseMotionListener(webpageModelListener);
            webpageModel.addKeyListener(webpageModelListener);
            webpageModel.setLocation(x, y);
            this.whiteboard.addWebpage(webpageModel);
            this.add(webpageModel); 
            this.revalidate();
            this.whiteboardView.clearSelectedTool();
            break;

         case SHAPE:
            this.shapeModel.setVisible(false);
            ShapeModel shapeModel = new ShapeModel(
               x, 
               y, 
               this.shapeModel.getShape(), 
               this.shapeModel.getColor(), 
               this.shapeModel.getShapeSize()
            ); 
            ObjectMouseListener shapeModelListener = 
               new ObjectMouseListener(this.toolView); 
            shapeModel.addMouseListener(shapeModelListener);
            shapeModel.addMouseMotionListener(shapeModelListener);
            shapeModel.addKeyListener(shapeModelListener);
            shapeModel.setLocation(x, y);
            this.whiteboard.addShape(shapeModel);
            this.add(shapeModel); 
            this.revalidate();
            this.whiteboardView.clearSelectedTool();
            break;   

         case FILE:
            this.fileModel.setVisible(false);
            FileModel fileModel = new FileModel(
               this.fileModel.getTitle(),
               this.fileModel.getContents(),
               this.fileModel.getImage(),
               (int)this.fileModel.getSize().getWidth(),
               (int)this.fileModel.getSize().getHeight(),
               x, 
               y
            ); 
   			fileModel.setTitle(this.fileModel.getTitle());
            ObjectMouseListener fileModelListener = 
               new ObjectMouseListener(this.toolView); 
            fileModel.addMouseListener(fileModelListener);
            fileModel.addMouseMotionListener(fileModelListener);
            fileModel.addKeyListener(fileModelListener);
            fileModel.setLocation(x, y);
            this.whiteboard.addFile(fileModel);
            this.add(fileModel); 
            this.revalidate();
            this.whiteboardView.clearSelectedTool();
            break;  

         case LINK:
            this.linkModel.setVisible(false);
            LinkModel linkModel = new LinkModel(
               this.linkModel.getContent(),
               (int)this.linkModel.getSize().getHeight(),
               (int)this.linkModel.getSize().getWidth()
            );

            ObjectMouseListener linkModelListener = 
               new ObjectMouseListener(this.toolView);
            linkModel.addMouseListener(linkModelListener);
            linkModel.addMouseMotionListener(linkModelListener);
            linkModel.addKeyListener(linkModelListener);
            linkModel.setBounds(x, y, 100, 100);
            this.whiteboard.addLink(linkModel);
            this.add(linkModel); 
            this.revalidate();
            this.whiteboardView.clearSelectedTool();
            break;
   	}

   }
   
   public void mouseReleased(MouseEvent evt) {

      setUpDrawingGraphics();
      x = evt.getX();
      y = evt.getY();

      if ((boolean)this.toolView.getMarkerModel().getAttribute("OCRMode")) {
         int[] drawnDigit = digitRecognizer.recognizeShape(
         				this.xPoints,
         				this.yPoints
         );

         TextboxModel textModel = new TextboxModel(
               drawnDigit[1] / 2, 
               drawnDigit[2] / 2, 
               Integer.toString(drawnDigit[0]),
               new Font("Arial", Font.PLAIN, drawnDigit[3]),
               Color.BLACK,
               drawnDigit[3]
         );
         ObjectMouseListener textModelListener =
            new ObjectMouseListener(this.toolView);
         textModel.addMouseListener(textModelListener);
         textModel.addMouseMotionListener(textModelListener);
         textModel.addKeyListener(textModelListener);
         textModel.setLocation(drawnDigit[1], drawnDigit[2]);
         textModel.setFont(textModel.getFont());
         this.whiteboard.addTextbox(textModel);
         this.add(textModel);
         this.revalidate();

         for(int i = 0; i < this.xPoints.size() - 1; i++) {
            setColor(Color.WHITE);
            graphicsForDrawing.setStroke(
               new BasicStroke(
                  (Integer) toolView.
                  getMarkerModel().getAttribute("lineThickness")
               )
            );
            graphicsForDrawing.drawLine(
               this.xPoints.get(i), 
               this.yPoints.get(i), 
               this.xPoints.get(i + 1), 
               this.yPoints.get(i + 1)
            );
            this.whiteboard.addPoint(
               new PointModel(
                  new Line2D.Double(
                     this.xPoints.get(i), 
                     this.yPoints.get(i), 
                     this.xPoints.get(i + 1), 
                     this.yPoints.get(i + 1)
                  ), 
                  (Integer) toolView.
                     getMarkerModel().getAttribute("lineThickness"),
                  Color.WHITE
               )
            );
         }

         this.xPoints.clear();
         this.yPoints.clear();

      }

      if (dragging == false)
         return;
      dragging = false;
      graphicsForDrawing.dispose();
      graphicsForDrawing = null;
   }
   
   public void mouseDragged(MouseEvent evt) {

      if (dragging == false)
      return; 

      x = evt.getX();
      y = evt.getY();

      switch(this.whiteboard.getSelectedTool()){

         case NONE:
            break;

         case ERASER:
            setColor(Color.WHITE);
            graphicsForDrawing.setStroke(
               new BasicStroke(
                  (Integer) toolView.
                  getEraserModel().getAttribute("eraserThickness")
               )
            );
            graphicsForDrawing.drawLine(prevX, prevY, x, y);
            this.whiteboard.addPoint(
            new PointModel(
               new Line2D.Double(prevX, prevY, x, y), 
                  (Integer) toolView.
                  getEraserModel().getAttribute("eraserThickness"), 
                  this.getColor()
               )
            );
         break;

         case MARKER:
            setColor((Color) toolView.
               getMarkerModel().getAttribute("lineColor"));
            graphicsForDrawing.setStroke(
               new BasicStroke(
                  (Integer) toolView.
                  getMarkerModel().getAttribute("lineThickness")
               ) 
            );
            graphicsForDrawing.drawLine(prevX, prevY, x, y);
            this.whiteboard.addPoint(
               new PointModel(
                  new Line2D.Double(prevX, prevY, x, y), 
                  (Integer) toolView.
                     getMarkerModel().getAttribute("lineThickness"), 
                  this.getColor()
               )
            );
	    if ((boolean)this.toolView.getMarkerModel().getAttribute("OCRMode")){
		this.xPoints.add(x);
		this.yPoints.add(y);
	    }
         break;
      }

      prevX = x;
      prevY = y;

   }

   public void mouseEntered(MouseEvent evt) { }
   public void mouseExited(MouseEvent evt) { }
   public void mouseClicked(MouseEvent evt) { }

   public void keyPressed(KeyEvent e) {}
   public void keyReleased(KeyEvent e) {}
   public void keyTyped(KeyEvent e) {}

   public void drawPoint() {
      repaint();
   }
   public void addShape(ShapeModel s) {
      ObjectMouseListener shapeModelListener = 
         new ObjectMouseListener(this.toolView);
      s.addMouseListener(shapeModelListener);
      s.addMouseMotionListener(shapeModelListener);
      s.addKeyListener(shapeModelListener);
      this.add(s);
      this.revalidate();
      repaint();

   }
   public void editShape(ShapeModel before, ShapeModel after) {
     this.remove(before);
     addShape(after);
   }
   public void addTextbox(TextboxModel textModel) {
      ObjectMouseListener textModelListener = 
         new ObjectMouseListener(this.toolView);
      textModel.addMouseListener(textModelListener);
      textModel.addMouseMotionListener(textModelListener);
      textModel.addKeyListener(textModelListener);
      textModel.setFont(textModel.getFont());
      this.add(textModel);
      this.revalidate();
      repaint();
   }
   public void editTextbox(TextboxModel before, TextboxModel after) {
      this.remove(before);
      addTextbox(after);
   }
   public void addWeblink(WebpageModel webModel) {
      ObjectMouseListener webpageModelListener = 
         new ObjectMouseListener(this.toolView);
      webModel.addMouseListener(webpageModelListener);
      webModel.addMouseMotionListener(webpageModelListener);
      webModel.addKeyListener(webpageModelListener);
      this.add(webModel);
      this.revalidate();
      repaint();
   }
   public void editWeblink(WebpageModel before, WebpageModel after) {
      this.remove(before);
      addWeblink(after);
   }
   public void addImage(ImageModel imageModel) {
      ObjectMouseListener imageModelListener = 
         new ObjectMouseListener(this.toolView);
      imageModel.addMouseListener(imageModelListener);
      imageModel.addMouseMotionListener(imageModelListener);
      imageModel.addKeyListener(imageModelListener);
      this.add(imageModel); 
      this.revalidate();
      repaint();
   }
   public void editImage(ImageModel before, ImageModel after) {
      this.remove(before);
      addImage(after);
   }
   public void addFile(FileModel file) {
      ObjectMouseListener fileModelListener = 
         new ObjectMouseListener(this.toolView);
      file.addMouseListener(fileModelListener);
      file.addMouseMotionListener(fileModelListener);
      file.addKeyListener(fileModelListener);
      this.add(file); 
      this.revalidate();
      repaint();
   }
   public void editFile(FileModel before, FileModel after) {
      this.remove(before);
      addFile(after);
   }
   public void addWhiteboardLink(LinkModel linkModel) {
      ObjectMouseListener linkModelListener = 
        	new ObjectMouseListener(this.toolView);
      linkModel.addMouseListener(linkModelListener);
      linkModel.addMouseMotionListener(linkModelListener);
      linkModel.addKeyListener(linkModelListener);
      this.add(linkModel); 
      this.revalidate();
      repaint();
   }
    
}