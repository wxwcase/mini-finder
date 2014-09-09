package com.wxwcase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLDatabase {
	// comment added
	
	public boolean importData(String sql) {
		String dataStatement = sql;
		boolean returnResult = false;
		
		Connection connection = null;
		try {
			// SETTING: userName, passWord and Database:
			String userName = "wei";
			String passWord = "xl";
			String dataBase = "test";
			
			// Creating class:
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Creating connection:
			connection = DriverManager.getConnection("jdbc:mysql:///"+dataBase, userName, passWord);
			// Do we have a connection:
			if(!connection.isClosed()) {
				// we have a MySQL Connection
				// test connection:
				System.out.println("Connection done!");
				Statement statement = connection.createStatement();
//				statement.executeQuery("SELECT * FROM testtable");
				// execute sql statement
				statement.executeUpdate(dataStatement);
				// test query execution:
				System.out.println("execution done!");
				
				/*
				ResultSet resultSet = statement.getResultSet();
				// loop through rows of result set
				System.out.println("get result set: " + resultSet);
				while(resultSet.next()) {
					System.out.println("in result set...");
					int key = resultSet.getInt(1);
					String isDir = resultSet.getString(2);
					String fileName = resultSet.getString(3);
					String filePath = resultSet.getString(4);
					System.out.println("uID: " + key + "\n>>> Name: " + fileName + "\n>>> is directory: " + isDir + "\n>>> path: " + filePath);
				}												
				resultSet.close();
				*/
				
				statement.close();
				returnResult = true;
			} else {
				returnResult = false;
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
//			returnResult = false;
		} finally {
			try {
				if(connection != null) {
					connection.close();
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
			// SETTING: userName, passWord and Database:
			String userName = "wei";
			String passWord = "ke";
			String dataBase = "test";
			
			// Creating class:
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Creating connection:
			connection = DriverManager.getConnection("jdbc:mysql:///"+dataBase, userName, passWord);
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
