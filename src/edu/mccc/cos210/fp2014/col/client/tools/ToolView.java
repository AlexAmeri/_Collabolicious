package edu.mccc.cos210.fp2014.col.client.tools;

import edu.mccc.cos210.fp2014.col.client.tools.ToolModel;
import edu.mccc.cos210.fp2014.col.client.tools.ToolMarkerView;
import edu.mccc.cos210.fp2014.col.client.objects.ImageModel;
import edu.mccc.cos210.fp2014.col.client.objects.LinkModel;
import edu.mccc.cos210.fp2014.col.client.objects.WebpageModel;
import edu.mccc.cos210.fp2014.col.client.objects.TextboxModel;
import edu.mccc.cos210.fp2014.col.client.objects.ShapeModel;
import edu.mccc.cos210.fp2014.col.client.objects.FileModel;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.JPanel;

/*
* Contains all methods to call each individual tool view
*/
public class ToolView {

	public JPanel sidePanel;
	public CollaboliciousFrame window;
	public final ToolModel markerTool = new ToolModel();
	public final ToolModel eraserTool = new ToolModel();
	public final TextboxModel textModel;
	public final ImageModel imageModel;
	public final WebpageModel webpageModel;
	public final ShapeModel shapeModel;
	public final FileModel fileModel;
	public final LinkModel linkModel;

	/**
	 * Default public constructor
	 */
	public ToolView(
		JPanel sidePanel,
		CollaboliciousFrame window) {
		
		this.sidePanel = sidePanel;
		this.window = window;

		this.markerTool.updateAttribute(
			"lineThickness", 
			new Integer(1)
		);
		this.markerTool.updateAttribute(
			"lineColor", 
			Color.BLACK
		);
		this.markerTool.updateAttribute(
        	"OCRMode", 
        	false
        );
		
		this.eraserTool.updateAttribute(
			"eraserThickness", 
			new Integer(5)
		);

		this.imageModel = new ImageModel(
			"../assets/icon-add.png", 
			100, 
			100
		);
		this.webpageModel = new WebpageModel("Link...");
		this.textModel = new TextboxModel("Text...");
		this.shapeModel = new ShapeModel();
		this.fileModel = new FileModel();
		this.linkModel = new LinkModel(null);
	}

	public void showWebpageView(Point position) {
		ToolWebView webView = new ToolWebView(this.webpageModel);
		showSidebar(
			position, 
			webView.show()
		);
	}

	public void showWebpageView(
		Point position, 
		WebpageModel webpageModel) {
		ToolWebView webView = new ToolWebView(webpageModel);
		showSidebar(
			position, 
			webView.show()
		);
	}

	public WebpageModel getWebpageModel() {
		return this.webpageModel;
	}

	public void showPictureView(Point position) {
		ToolPictureView pictureView = 
			new ToolPictureView(this.imageModel);
		showSidebar(
			position, 
			pictureView.show()
		);
	}

	public void showPictureView(
		Point position, 
		ImageModel imageModel) {
		position.setLocation(
			(int)(position.getX() + imageModel.getWidth() + 15),
			(int)position.getY()
		);
		ToolPictureView pictureView = new ToolPictureView(imageModel);
		showSidebar(
			position, 
			pictureView.show()
		);
	}

	public void showTextView(Point position) {
		showTextView(
			position, 
			this.textModel
		);
	}

	/**
	 * Shows the Text Tool Panel
	 * @param textTool private tool model instantiated at runtime
	 */
	public void showTextView(
		Point position, 
		TextboxModel textModel) {
		ToolTextView textboxView = new ToolTextView(textModel);
		showSidebar(
			position, 
			textboxView.show()
		);
	}

	public void showLinkView(Point position) {
		showLinkView(
			position, 
			this.linkModel
		);
	}

	public void showLinkView(Point position, LinkModel linkModel) {
		ToolLinkView linkView = new ToolLinkView(linkModel, this.window);
		showSidebar(
			position, 
			linkView.show()
		);
	}

	public void showMarkerView(Point position) {
		showMarkerView(
			position, 
			this.markerTool
		);
	}

	/**
	 * Shows the Marker Tool Panel
	 * @param markerTool private tool model instantiated at runtime
	 */
	private void showMarkerView(
		Point position, 
		ToolModel markerTool) {
		ToolMarkerView markerView = new ToolMarkerView(markerTool);
		showSidebar(
			position, 
			markerView.show()
		);
	}

	public void showEraserView(Point position) {
		showEraserView(
			position, 
			eraserTool
		);
	}

	/**
	 * Shows the Eraser Tool Panel
	 * @param eraserTool private tool model instantiated at runtime
	 */
	private void showEraserView(
		Point position, 
		ToolModel eraserTool) {
		ToolEraserView eraserView = new ToolEraserView(
			eraserTool
		);
		showSidebar(
			position, 
			eraserView.show()
		);
	}
	
	public void showShapeView(Point position) {
		ToolShapeView shapeView = new ToolShapeView(
			this.shapeModel
		);
		showSidebar(
			position, 
			shapeView.show()
		);
	}

	public void showShapeView(
		Point position, 
		ShapeModel shapeModel) {
		ToolShapeView shapeView = new ToolShapeView(
			shapeModel
		);
		showSidebar(
			position, 
			shapeView.show()
		);
	}

	public void showFileView(Point position) {
		ToolFileView fileView = new ToolFileView(
			this.fileModel
		);
		showSidebar(
			position, 
			fileView.show()
		);
	}

	public void showFileView(
		Point position, 
		FileModel fileModel) {
		ToolFileView fileView = new ToolFileView(
			fileModel
		);
		showSidebar(
			position, 
			fileView.show()
		);
	}

	public ShapeModel getShapeModel() {
		return this.shapeModel;
	}

	public ImageModel getImageModel() {
		return this.imageModel;
	}

	/**
	 * Returns the most up to date model with all of the attributes
	 * @return ToolModel keys(lineThickness, lineColor)
	 */
	public ToolModel getMarkerModel() {
		return this.markerTool;
	}

	/**
	 * Returns the most up to date model 
	 * With all of the attributes
	 * @return ToolModel keys(eraserThickness)
	 */
	public ToolModel getEraserModel() {
		return this.eraserTool;
	}

	/**
	 * Returns the fileModel used for intial movement
	 * @return [description]
	 */
	public FileModel getFileModel() {
		return this.fileModel;
	}

	/**
	 * Returns the most up to date model with all of the attributes
	 * @return ToolModel keys(textColor)
	 */
	public TextboxModel getTextModel() {
		return this.textModel;
	}

	public LinkModel getLinkModel() {
		return this.linkModel;
	}

	/**
	 * Shows a sidebar with a specified panel
	 * @param panel the panel that is going to be rendered
	 */
	public void showSidebar(Point position, JPanel p) {
		this.sidePanel.removeAll();
		JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				hideSidebar();
			}
		});
		this.sidePanel.add(btnClose, BorderLayout.SOUTH);
		this.sidePanel.add(p, BorderLayout.NORTH);
		this.sidePanel.setBounds(
			(int)position.getX() + 95,
			(int)position.getY(),
			(int)this.sidePanel.getPreferredSize().getWidth(),
			(int)this.sidePanel.getPreferredSize().getHeight() + 
				btnClose.getHeight() + 5
		);
		this.sidePanel.setVisible(true);
	}

	public void hideSidebar() {
		this.sidePanel.setVisible(false);
	}

}