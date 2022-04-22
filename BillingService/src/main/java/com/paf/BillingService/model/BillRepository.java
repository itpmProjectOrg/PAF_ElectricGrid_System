package com.paf.BillingService.model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.paf.BillingService.utilities.*;

public class BillRepository {
	
	private static Connection con = null;
	
	//reading all bills for required billAccount No
	public static List<Bill> getBills (String accountNo){
		String query = "select * from Bill where accountNo = ?";
		List<Bill> billList = new ArrayList<>();
		
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, accountNo);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int ID = rs.getInt(1);
				String aNo = rs.getString(2);
				String month = rs.getString(3);
				String year = rs.getString(4);
				int consumeUnits = rs.getInt(5);
				double totalAmount = rs.getDouble(6);
				
				Bill b = new Bill(ID, aNo,month,year,consumeUnits,totalAmount);
				billList.add(b);
			}
			
			ps.close();
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while reading bills");
		}
		
		if(billList.isEmpty()) {
			return null;
		}
		else {
			return billList;
		}
	}
	
	//reading a bill for required billAccount No based on required month and year
	public static Bill getBillBasedMonthYear (String accountNo,String month,String year){
		String query = "select * from Bill where accountNo = ? and month = ? and year = ?";
		Bill myBill = null;
		
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, accountNo);
			ps.setString(2, month);
			ps.setString(3, year);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				int ID = rs.getInt(1);
				String aNo = rs.getString(2);
				String mon = rs.getString(3);
				String yr = rs.getString(4);
				int consumeUnits = rs.getInt(5);
				double totalAmount = rs.getDouble(6);
					
				myBill = new Bill(ID, aNo,mon,yr,consumeUnits,totalAmount);
				
			}
			
			ps.close();
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while reading bills based on month & date");
		}
		
		return myBill;
		
	}
	
	//calculating total amount to be paid based on given consume units
	public static double getTotalPaymentAmount(int consumeUnits) {
		double totalAmount = 0;
		double unitPrice= 0;
		double unitPrice_firstPart = 7.85; //first units 28 (0-28)
		double unitPrice_secondPart = 7.85; //units (29-56)
		double unitPrice_thirdPart = 10.0; //units (57-84)
		
		//implement logic to calculate total amount
		if(consumeUnits >= 85 ) {
			unitPrice = 28;
			totalAmount = unitPrice * consumeUnits;
			return totalAmount;
		}
		else {
			int remainder = consumeUnits % 28;
			int ans = consumeUnits / 28;
			
			if(ans == 1 && remainder == 0 || ans == 0) {
		
				if(ans == 0) {
					totalAmount = remainder * unitPrice_firstPart;
				}
				else {
					totalAmount = unitPrice_firstPart * 28;
				}
			}
			
			else if ((ans == 1 && remainder != 0) ||( ans == 2 && remainder == 0)) {
				
				if(ans == 1 && remainder != 0) {
					totalAmount = (unitPrice_firstPart * 28) + (remainder * unitPrice_secondPart);
				}
				else {
				
					totalAmount = (unitPrice_firstPart * 28) + (unitPrice_secondPart * 28);
				}
			}
			else { // units 57-84
				
				totalAmount = (unitPrice_firstPart * 28) + (unitPrice_secondPart * 28) + (remainder * unitPrice_thirdPart);
			}
		}
		
		return totalAmount;
	}
	
	//check duplication bills for insert
	public static boolean isBillValid(String accountNo,String month, String year) {
		String query = "select * from Bill where accountNo = ? and month = ? and year = ?";
		boolean isValid = false;
		
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, accountNo);
			ps.setString(2, month);
			ps.setString(3, year);
			
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				isValid = true;
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while checking validation for the insert of the bill");
		}
		return isValid;
	}
	
	//adding new bill
	public static Bill addNewBill(String accountNo, String month, String year, int consumeUnits) {
		
		double totalAmount = 0.0;
		Bill mybill = null;
		String query = "insert into Bill values (?,?,?,?,?,?)";
		
		//checking the bill is already exist or not
		boolean isValid = isBillValid(accountNo, month, year);
		
		if(isValid == false) {
			System.out.println("Bill is already exists");
			return null;
		}
		
		totalAmount = getTotalPaymentAmount(consumeUnits);	
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, 0);
			ps.setString(2, accountNo);
			ps.setString(3, month);
			ps.setString(4, year);
			ps.setInt(5, consumeUnits);
			ps.setDouble(6,totalAmount);
			
			int noRows = ps.executeUpdate();
			
			if(noRows > 0) {
				mybill = getBillBasedMonthYear(accountNo,month,year);
			}
			ps.close();
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while adding a new bill");
		}
			
	    return mybill;
	}
	
	//check duplication bills for UPDATE
	public static boolean isBillValidForUpdate(int billID,String accountNo,String month, String year) {
		String query = "select * from Bill where accountNo = ? and month = ? and year = ? and billID != ?";
		boolean isValid = false;
		
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, accountNo);
			ps.setString(2, month);
			ps.setString(3, year);
			ps.setInt(4, billID);
			
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				isValid = true;
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while checking validation for update of the bill");
		}
		return isValid;
	}
	
	//update bill 
	public static Bill updateBill(int billID, String accountNo, String month, String year, int consumeUnits) {
		
		double totalAmount = 0.0;
		Bill mybill = null;
		String query = "update Bill set accountNo = ? , month = ? , year = ? , consumeUnits = ?, totalAmount = ? where billID = ?";
		
		//checking the bill to be updated is already exist or not
		boolean isValid = isBillValidForUpdate(billID,accountNo, month, year);
		
		if(isValid == false) {
			System.out.println("Bill is already exists.");
			return null;
		}
		
		totalAmount = getTotalPaymentAmount(consumeUnits);
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, accountNo);
			ps.setString(2, month);
			ps.setString(3, year);
			ps.setInt(4, consumeUnits);
			ps.setDouble(5,totalAmount);
			ps.setInt(6, billID);
			
			int noRows = ps.executeUpdate();
			
			if(noRows > 0) {
				mybill = getBillBasedMonthYear(accountNo,month,year);
			}
			ps.close();
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while updating the bill");
		}
		
		return mybill;
		
	}
	
	//delete bill
	public static String deleteBill(int billID) {
		
		String output = "";
		String query = "delete from Bill where billID = ? ";
		int noRows = 0;
		
		try {
			con = DBConnect.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, billID);
			noRows = ps.executeUpdate();
			
			if(noRows > 0) {
				output = "Bill is deleted successfully";
			}
			else{
				output = "Bill deletion is unsuccessful";
			}
				
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while deleting the bill");
			output = "Bill deletion is unsuccessful";
		}
		
		return output;
		
	}
	

}
