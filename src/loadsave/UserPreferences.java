package loadsave;
import gui.BatchState;
import gui.IndexerFrameLayout.Image.DrawingComponent;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.List;

import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

/**
 * SerializableBatchState is going to be your data structure that holds your variables
 * this should pass things around so that you can manipulate the data in the other classes
 * 	load and save
 * @author brian
 *
 */

@SuppressWarnings("serial")
public class UserPreferences implements Serializable
{
	//user specific data
	private User user;
	private Batch batch;
	private Project project;
	private String[][] tabledata;
	private List<Field> fields;
	private int SelectedCellRow;
	private int SelectedCellCol;
	
	private Dimension size;
	private Point framelocation;
	private BatchSpecifics batchinfo;
	
	
	
	public UserPreferences(BatchState batchState)
	{
		user = batchState.getUser();
		batch = batchState.getCurrentBatch();
		project = batchState.getCurrentProject();
		tabledata = batchState.gettabledata();
		fields = batchState.getAllFields();
		SelectedCellRow = batchState.getSelectedCellRow();
		SelectedCellCol = batchState.getSelectedCellCol();
		
		size = batchState.getIndexerframe().getSize();
		framelocation = batchState.getIndexerframe().getLocation();
		System.out.println("FRAMELOCATION: " + framelocation.toString());
		batchinfo = batchState.getIndexerframe().getBatchinfo();
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public Point getFramelocation() {
		return framelocation;
	}

	public void setFramelocation(Point framelocation) {
		this.framelocation = framelocation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String[][] getTabledata() {
		return tabledata;
	}

	public void setTabledata(String[][] tabledata) {
		this.tabledata = tabledata;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public int getSelectedCellRow() {
		return SelectedCellRow;
	}

	public void setSelectedCellRow(int selectedCellRow) {
		SelectedCellRow = selectedCellRow;
	}

	public int getSelectedCellCol() {
		return SelectedCellCol;
	}

	public void setSelectedCellCol(int selectedCellCol) {
		SelectedCellCol = selectedCellCol;
	}
	
	public BatchSpecifics getBatchinfo() {
		return batchinfo;
	}

	public void setBatchinfo(BatchSpecifics batchinfo) {
		this.batchinfo = batchinfo;
	}
	
	
	
}
