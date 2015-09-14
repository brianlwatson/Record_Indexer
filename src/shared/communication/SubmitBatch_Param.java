package shared.communication;

import java.io.Serializable;

import shared.model.User;

/**
 * Inputs:
 * 	User's name and password
 * 	BatchID
 * 	Field Values for the Batch
 * @author brian
 *
 */
public class SubmitBatch_Param extends DataTransport_Param implements Serializable
{
	private int batchID;
	private String fieldvalues;
	

	public int getBatchID() 
	{
		return batchID;
	}
	
	public void setBatchID(int batchID) 
	{
		this.batchID = batchID;
	}
	
	public String getFieldValues() 
	{
		return fieldvalues;
	}
	
	public void setFieldValues(String batchValue) 
	{
		this.fieldvalues = batchValue;
	}
	
}
