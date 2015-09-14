package gui.IndexerFrameLayout.Image;


import gui.BatchState;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import shared.model.Field;

import java.io.*;
import java.net.URL;


@SuppressWarnings("serial")
public class DrawingComponent extends JComponent implements Serializable
{

	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	private int w_translateX;
	private int w_translateY;
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;

	private Image image;
	private Image invertedimage;
	private Image notinvertedimage;
	
	
	private int SelectedCellRow = -1;
	private int SelectedCellColumn = -1;
	private int SelectedCellWidth;
	private int SelectedCellHeight;
	private int SelectedCellX;
	private int SelectedCellY;
	
	private boolean isInverted = false;
	private boolean enableHighlights = true;

	private BatchState batchstate;
	public static Color highlightColor = new Color(193,253,252,192);
	public static Color transparentColor = new Color(210,240,246,0);
	public static Color currentColor;
	private DrawingShape batchHighlight;
	
	public DrawingComponent(String s, BatchState batchState)
	{
		batchstate = batchState;
		w_translateX = -500;
		w_translateY = -500;
		
		scale = .4;
		
		initDrag();

		this.setBackground(Color.GRAY);
		this.setPreferredSize(new Dimension(1500,450));
	
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseWheelAdapter);
		this.addMouseListener(mouseClicked);
		image = loadImage(s);
		currentColor = highlightColor;
		batchHighlight = (new DrawingRect(new Rectangle2D.Double(SelectedCellRow, SelectedCellColumn, SelectedCellWidth, SelectedCellHeight), currentColor));
		
	}
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}
	
	public void changeHighlight()
	{
		if(SelectedCellRow == -1 || SelectedCellRow == -1)
		{
			batchHighlight = ((new DrawingRect(new Rectangle2D.Double(0, 0, 0, 0), currentColor)));
		}
		else
		{
			batchHighlight = (new DrawingRect(new Rectangle2D.Double(SelectedCellX, SelectedCellY, SelectedCellWidth, SelectedCellHeight), currentColor));
		}
	}
	
	public void enableHighlighting()
	{
		if(enableHighlights)
		{
			enableHighlights = false;
			currentColor = transparentColor;
			changeHighlight();
		}
		else
		{
			enableHighlights = true;
			currentColor = highlightColor;
		}
		repaint();
	}
	
	private Image loadImage(String imageFile) 
	{
		try 
		{
			URL path = new URL(imageFile);
			image = ImageIO.read(path);
			
			notinvertedimage = image;
			BufferedImage temp = toBufferedImage(image);
			BufferedImage negative = new RescaleOp(-1.0f,255.0f,null).filter(temp,null);
			
			invertedimage = negative;
			isInverted = false;
			return image;
		}
		catch (IOException e) 
		{
			return NULL_IMAGE;
		}
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public boolean getInverted()
	{
		return isInverted;
	}
	
	public boolean getHighlights()
	{
		return enableHighlights;
	}
	
	public int getW_translateX() {
		return w_translateX;
	}

	public void setW_translateX(int w_translateX) {
		this.w_translateX = w_translateX;
	}

	public int getW_translateY() {
		return w_translateY;
	}

	public void setW_translateY(int w_translateY) {
		this.w_translateY = w_translateY;
	}

	public void zoomIn() 
	{
		scale = scale + .05;
		this.repaint();
	}
	
	public void zoomOut()
	{
		scale = scale - .05;
		if(scale <= 0.0)
		{
			scale = 0;
		}
		this.repaint();
	}
	
	public void InvertImage() throws IOException
	{
		if(isInverted)
		{
			image = notinvertedimage;
			isInverted = false;
		}
		else
		{
			image = invertedimage;
			isInverted = true;
		}
			this.repaint();
	}
	
	public void setTranslation(int w_newTranslateX, int w_newTranslateY) 
	{
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		g2.translate(getWidth()/2.0,getHeight()/2.0);
		g2.scale(scale, scale);
		g2.translate(w_translateX, w_translateY);
		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) 
	{
		g2.setColor(Color.GRAY);
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) 
	{
		DrawingShape d = (new DrawingImage(image, new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null))));
		d.draw(g2);
		batchHighlight.draw(g2);
	}
	
	public void highlightRectangle(int desiredrow, int desiredcol)
	{
		java.util.List<Field> fields = batchstate.getAllFields();
		
		int FirstYCoord = batchstate.getCurrentProject().getYcoord();
		int recordheight = batchstate.getCurrentProject().getRecordheight();
		
		int x = 1;
		for(Field f : fields)
		{	
				if(x == desiredcol)
				{
					SelectedCellWidth = f.getWidth();
					SelectedCellX = f.getXcoord();
				}
			x++;
		}
		SelectedCellRow = desiredrow;
		SelectedCellColumn = desiredcol;
		SelectedCellY = FirstYCoord + (desiredrow-1)*recordheight;
		batchstate.setSelectedCellCol(desiredcol);
		batchstate.setSelectedCellRow(desiredrow);
		SelectedCellHeight = recordheight;
		changeHighlight();
		repaint();	
	}
	
	private MouseAdapter mouseClicked = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2.0, getHeight()/2.0);
			transform.scale(scale, scale);
			transform.translate(w_translateX, w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) 
			{
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			
			DrawingImage draw_image = (new DrawingImage(image, new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null))));
			if (draw_image.contains(g2, w_X, w_Y)) 
			{
				hitShape = true;
				int FirstYCoord = batchstate.getCurrentProject().getYcoord();
				int numrecords = batchstate.getCurrentProject().getNumrecords();
				int recordheight = batchstate.getCurrentProject().getRecordheight();
				int row = -1;
				int col = -1;
				int lowerYBound = (numrecords * recordheight) + FirstYCoord;
				
				java.util.List<Field> fields = batchstate.getAllFields();
				
				//Determine the row of the Cell
				if(w_Y > FirstYCoord && w_Y < lowerYBound)
				{
					row = (int) (w_Y-FirstYCoord) / recordheight;
					row = (row+1);
					SelectedCellHeight = recordheight;
				}
				
				SelectedCellY = FirstYCoord + (row-1)*recordheight;
				//Determine the Column of the Cell
				int x = 1;
				for(Field f : fields)
				{
					int x_start = f.getXcoord();
					int x_end = x_start + f.getWidth();
					
					if(w_X > x_start && w_X < x_end)
					{
						col = x;
						SelectedCellWidth = f.getWidth();
						SelectedCellX = f.getXcoord();
					}
					x++;
				}
				SelectedCellRow = row;
				SelectedCellColumn = col;
				
				batchstate.setSelectedCellCol(SelectedCellColumn);
				batchstate.setSelectedCellRow(SelectedCellRow);
				changeHighlight();
				repaint();	
				try 
				{
					batchstate.getIndexerframe().getFieldhelpnavigator().setFieldHelp(batchstate.getFieldHelpURL());
					batchstate.getIndexerframe().getFieldhelpnavigator().repaint();
				}
				catch (IOException e1) 
				{	
					System.out.println("ERROR REDOING FIELD HELP");
					e1.printStackTrace();
				}
			}
			
			if (hitShape) 
			{
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
		
		}
	};
	
	private MouseAdapter mouseAdapter = new MouseAdapter() 
	{
		public void mousePressed(MouseEvent e) 
		{
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2.0, getHeight()/2.0);
			transform.scale(scale, scale);
			transform.translate(w_translateX, w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) 
			{
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			
			DrawingImage draw_image = (new DrawingImage(image, new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null))));
			if (draw_image.contains(g2, w_X, w_Y)) 
			{
				hitShape = true;
				repaint();
			}
			
			
			if (hitShape) 
			{
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
				
			}
		}

		public void mouseDragged(MouseEvent e) 
		{		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				AffineTransform transform = new AffineTransform();
				
				transform.translate(getWidth()/2, getHeight()/2);
				transform.scale(scale,scale);
				transform.translate(w_translateX, w_translateY);
				
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) 
				{
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;
				
				repaint();
			}
		}

		public void mouseReleased(MouseEvent e) 
		{
			initDrag();
		}

	};
	
	private MouseAdapter mouseWheelAdapter = new MouseAdapter()
	{
		public void mouseWheelMoved(MouseWheelEvent e) 
		{
			double notches = e.getPreciseWheelRotation();
			if(notches < 0)
			{
				zoomIn();
			}
			if(notches > 0)
			{
				zoomOut();
			}
			return;
		}
		
	};

	
	interface DrawingShape 
	{
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}


	class DrawingRect implements DrawingShape 
	{
		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) 
		{
			this.rect = rect;
			this.color = color;
		}

		public boolean contains(Graphics2D g2, double x, double y) 
		{
			return rect.contains(x, y);
		}

		public void draw(Graphics2D g2) 
		{
			g2.setColor(color);
			g2.fill(rect);
		}
		
		public Rectangle2D getBounds(Graphics2D g2) 
		{
			return rect.getBounds2D();
		}
		
		public void setColor(Color color)
		{
			this.color = color;
		}
	}

	class DrawingImage implements DrawingShape 
	{
		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) 
		{
			this.image = image;
			this.rect = rect;
		}

		//@Override
		public boolean contains(Graphics2D g2, double x, double y) 
		{
			return rect.contains(x, y);
		}

		//@Override
		public void draw(Graphics2D g2) 
		{
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		//@Override
		public Rectangle2D getBounds(Graphics2D g2) 
		{
			return rect.getBounds2D();
		}
	}


	
}



