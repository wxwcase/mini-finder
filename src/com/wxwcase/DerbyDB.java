package com.wxwcase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbyDB {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String protocal = "jdbc:derby:";
	
	public static Connection createDBConnection() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		String url = protocal + "sampleDB";
		Connection conn = DriverManager.getConnection(url);
		return conn;
	}
	
	public boolean importData(String sql) throws SQLException, ClassNotFoundException {
		String dataStatement = sql;
		String tableName = "myData";
		boolean returnResult = false;
		try {
			connect = createDBConnection();
			if(!connect.isClosed()) {
				
			} else {
				returnResult = false;
			}
			if(!connect.isClosed()) {
				// we have a Derby Connection, test:
				System.out.println("Connection done!");
				Statement statement = connect.createStatement();
				// execute sql statement
				statement.executeUpdate(dataStatement);
				// test query execution:
				System.out.println("execution done!");
				statement.close();
				returnResult = true;
			} else {
				returnResult = false;
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {
				if(connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				System.out.println("SQLException " + e);
			}
			return returnResult;
		}
	}
	
	public boolean deleteRow() {
		return true;
	}
	
	public boolean deleteTable(String tableName) {
		String dataStatement = tableName;
		boolean returnResult = false;
		
		Connection connection = null;
		try {
			Class.forName(driver).newInstance();
			// Creating connection:
			connection = createDBConnection();
			if(!connection.isClosed()) {
				System.out.println("Connection done!");
				Statement statement = connection.createStatement();
				statement.executeUpdate("DROP TABLE " + tableName);
				System.out.println("Drop table: " + tableName + "done!");
				returnResult = true;
			}
			returnResult = false;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return returnResult;
	}
}
