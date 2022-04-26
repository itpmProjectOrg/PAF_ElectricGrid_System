package com.paf.ia.model;
import java.sql.*;

public class IARepo {
	private Connection connect() {
		Connection con = null;
		try
		{ Class.forName("com.mysql.jdbc.Driver");
		//Provide the correct details: DBServer/DBName, username, password
		con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/eg_db", "PAF_user1", "paf_user1"); }
		catch (Exception e) {e.printStackTrace();}
		return con;
	}
	
	public String insertAlerts(String dis, String time, String date) {
		
	
	String output = "";
	 
	try
	{
	Connection con = connect(); 
	if (con == null)
	{return "Error while connecting to the database for inserting."; }
	// create a prepared statement
	String query = " insert into interruptionalert (`alertID`,`alertDescription`,`scheduleTime`,`scheduleDate`)"
	+ " values (?, ?, ?, ?)"; 
	
	PreparedStatement preparedStmt = con.prepareStatement(query);
	// binding values
	preparedStmt.setInt(1, 0);
	preparedStmt.setString(2, dis); 
	preparedStmt.setString(3, time); 
	preparedStmt.setString(4, date);
	
	preparedStmt.execute(); 
	
	con.close();
	output = "Inserted successfully";
	}
	catch(Exception e) {
		output = "Error while inserting the item."; 
		System.err.println(e.getMessage());
	}
	
		return output;
	}
	
	public String readAlerts() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null)
			{return "Error while connecting to the database for reading."; }
			
			output = "<table border='1'><tr><th>Alert Message</th><th>Time</th>" + "<th>Date</th>" + "<th>Update</th><th>Remove</th></tr>";
			
			String query = "select * from interruptionalert"; 
			Statement stmt = con.createStatement(); 
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				String alertID = Integer.toString(rs.getInt("alertID"));
				String alertDes = rs.getString("alertDescription");
				String scTime = rs.getString("scheduleTime");
				String scDate = rs.getString("scheduleDate");
				
				output += "<tr><td>" + alertDes + "</td>"; 
				output += "<td>" + scTime + "</td>"; 
				output += "<td>" + scDate + "</td>"; 
				
				
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btn btn-secondary'></td>"
				+ "<td><form method='post' action='items.jsp'>"
						+ "<input name='btnRemove' type='submit' value='Remove' class='btn btn-danger'>"+ "<input name='itemID' type='hidden' value='" + alertID + "'>" + "</form></td></tr>";
			}
			con.close();
			output += "</table>";
		}
		catch (Exception e) {
			output = "Error while reading the items."; 
			System.err.println(e.getMessage());
		}
		return output;
	}

	
	public String updateAlert(String ID, String des, String time, String date) {
		String output = "";
		try
		{
		Connection con = connect(); 
		if (con == null)
		{return "Error while connecting to the database for updating."; }
		// create a prepared statement
		String query = "UPDATE items SET alertDescription=?,scheduleTime=?,scheduleDate=? WHERE alertID=?";
		PreparedStatement preparedStmt = con.prepareStatement(query);
		// binding values
		preparedStmt.setString(2, des);
		preparedStmt.setString(3, time); 
		preparedStmt.setString(4, date); 
		preparedStmt.setInt(1, Integer.parseInt(ID));
		// execute the statement
		preparedStmt.execute(); 
		con.close();
		output = "Updated successfully";
		}
		catch (Exception e) {
			output = "Error while updating the item."; 
			System.err.println(e.getMessage());
		}
		
		return output;
		
	}
	
	public String deleteAlert(String itemID) {
		String output = "";
		
		try {
			Connection con = connect(); 
			if (con == null)
			{return "Error while connecting to the database for deleting."; } 
			
			// create a prepared statement
			String query = "delete from interruptionalert where alertID=?"; 
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(itemID));
			// execute the statement
			preparedStmt.execute(); 
			con.close();
			
			output = "Deleted successfully";
		}
		catch(Exception e) {
			output = "Error while deleting the item."; 
			System.err.println(e.getMessage());
		}
		return output;
	}
}
