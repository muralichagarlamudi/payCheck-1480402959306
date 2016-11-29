package com.ibm.prap.paycheck;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class ConnectionManager {
	
	
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	
	public static Connection getConnection() {
		try {
			if (con == null){
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection("jdbc:oracle:thin:@9.220.129.23:1521:IBMVIS","apps","apps");
				return con;
			}
			else
				return con;    
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	} 
	
	public int claimStatus() {
		int chkNo = 0;
		con = getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select check_number from ap_checks_all where check_id = 91735");
			while(rs.next())  {
				chkNo = rs.getInt(1);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return chkNo;
	}
	
	public String checkStatus(String invoiceNum) {
		
		String invoiceStatus = null;
		int chkNo = 0;
		int amt = 0;
		String holdReason = null;
		con = getConnection();
		try {
			String getChkStatus = "{call Pay_check(?,?,?,?)}";
			CallableStatement cStmt = con.prepareCall(getChkStatus);
			cStmt.setString(1, invoiceNum);
			cStmt.registerOutParameter(2, Types.INTEGER);
			cStmt.registerOutParameter(3, Types.VARCHAR);
			cStmt.registerOutParameter(4, Types.VARCHAR);
			
			int a = cStmt.executeUpdate();
			
			if(a > 0) {
				chkNo = cStmt.getInt(2);
				if (chkNo != 0) {
					amt = cStmt.getInt(3);
					//System.out.println("CHK# "+chkNo);
					//System.out.println("Amount: "+amt);
					invoiceStatus = "Approved";
				}
				holdReason = cStmt.getString(4);
				if (holdReason != null)
					invoiceStatus = "On Hold. Reason, "+holdReason;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return invoiceStatus;
	}
	//This is to run the unit test case.
	/*public static void main(String args[]) {
		ConnectionManager dbCon = new ConnectionManager();
		//System.out.println("Invoice Status: "+dbCon.checkStatus("ERS-4400-109091"));
		System.out.println("Invoice Status: "+dbCon.checkStatus("SSCUS022009766"));
	}*/

}
