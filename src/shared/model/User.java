package shared.model;

import java.io.Serializable;

/**
 * User is going to be the model class representing the User table
create table user
(
	user_id integer primary key autoincrement
	username text not null,
	first_name text not null,
	last_name text not null,
	password text not null,
	num_records integer not null,
	email text not null, 
	batchassig integer not null, 
);
	
 **/

public class User implements Serializable
{
	private int user_id;
	private String username;
	private String first_name;
	private String last_name;
	private String password;
	private int num_records;
	private String email;
	private int batchassig;

	public User(int user_id, String username, String first_name, String last_name, String password, int num_records, String email,
			int batchassig)
	{
		super();
		this.user_id = user_id;
		this.username = username;
		this.first_name = first_name;
		this.last_name = last_name;
		this.password = password;
		this.num_records = num_records;
		this.email = email;
		this.batchassig = batchassig;
	}

	public int getUser_id() 
	{
		return user_id;
	}

	public void setUser_id(int user_id) 
	{
		this.user_id = user_id;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getFirst_name() 
	{
		return first_name;
	}

	public void setFirst_name(String first_name) 
	{
		this.first_name = first_name;
	}

	public String getLast_name() 
	{
		return last_name;
	}

	public void setLast_name(String last_name) 
	{
		this.last_name = last_name;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public int getNum_records() 
	{
		return num_records;
	}

	public void setNum_records(int num_records) 
	{
		this.num_records = num_records;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public int getBatchassig() 
	{
		return batchassig;
	}

	public void setBatchassig(int batchassig) 
	{
		this.batchassig = batchassig;
	}
	
	@Override
	public String toString() 
	{
		return "User [user_id=" + user_id + ", username=" + username
				+ ", first_name=" + first_name + ", last_name=" + last_name
				+ ", password=" + password + ", num_records=" + num_records
				+ ", email=" + email + ", batchassig=" + batchassig + "]";
	}
	
}
