package com.paf.BillingService.model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.paf.BillingService.utilities.*;

public class BillRepository {
	
	private static Connection con = null;
	
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

}
