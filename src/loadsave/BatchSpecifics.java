package loadsave;

import java.io.Serializable;

import gui.BatchState;
import gui.IndexerFrameLayout.Image.DrawingComponent;

public class BatchSpecifics implements Serializable
{
	private int verticalDivider;
	private int horizontalDivider;
	
	//batch specific data
	private double imagescale;
	private boolean inverted;
	private boolean highlighted;
	
	private int imagelocation_x;
	private int imagelocation_y;
	
	public BatchSpecifics(BatchState batchState)
	{
		if(batchState.getCurrentBatch() != null)
		{
			
			verticalDivider = batchState.getIndexerframe().getVdividerlocation();
			horizontalDivider = batchState.getIndexerframe().getHdividerlocation();
			
			
			DrawingComponent dc = batchState.getIndexerframe().getDrawFrame().getDrawingComponent();
			imagescale = dc.getScale();
			inverted = dc.getInverted();
			highlighted = dc.getHighlights();
			
			imagelocation_x = dc.getW_translateX();
			imagelocation_y = dc.getW_translateY();
		}
	}

	public int getVerticalDivider() {
		return verticalDivider;
	}

	public void setVerticalDivider(int verticalDivider) {
		this.verticalDivider = verticalDivider;
	}

	public int getHorizontalDivider() {
		return horizontalDivider;
	}

	public void setHorizontalDivider(int horizontalDivider) {
		this.horizontalDivider = horizontalDivider;
	}

	public double getImagescale() {
		return imagescale;
	}

	public void setImagescale(double imagescale) {
		this.imagescale = imagescale;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public int getImagelocation_x() {
		return imagelocation_x;
	}

	public void setImagelocation_x(int imagelocation_x) {
		this.imagelocation_x = imagelocation_x;
	}

	public int getImagelocation_y() {
		return imagelocation_y;
	}

	public void setImagelocation_y(int imagelocation_y) {
		this.imagelocation_y = imagelocation_y;
	}
	
	

}
