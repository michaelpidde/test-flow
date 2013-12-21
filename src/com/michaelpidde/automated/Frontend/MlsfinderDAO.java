package com.michaelpidde.automated.Frontend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class MlsfinderDAO {
	protected static final String dbServer = "jdbc:sqlserver://---ip---;";
	protected static Connection connection;
	
	
	
	public static final Connection connect(String db) throws SQLException {
		Properties properties = new Properties();
		properties.setProperty("database", db);
		properties.setProperty("user", "---");
		properties.setProperty("password", "---");
		Connection connection = null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			connection = DriverManager.getConnection(dbServer, properties);
		} catch(ClassNotFoundException e) {
			System.out.println(e.toString());
		} catch(InstantiationException e) {
			System.out.println(e.toString());
		} catch(IllegalAccessException e) {
			System.out.println(e.toString());
		}
		
		return connection;
	}
	
	
	
	private static final ResultSet query(String sql) {
		ResultSet result = null;
		try {
			connection = connect("---database---");
			PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result = statement.executeQuery();
		} catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	
	
	public static final ArrayList<String> getMarketList() {
		ArrayList<String> markets = new ArrayList<String>();
		try {
			ResultSet result = query("---");
			while(result.next()) {
				markets.add(result.getString(1));
			}
			
			// Close connection after working with result set.
			connection.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		return markets;
	}
	
	
	
	/*
	 * This method returns a structure ordered as such:
	 * { "Person Name"   -> [ "Url 1", "Url 2" ],
	 *   "Second Person" -> [ "Url 1", "Url 2" ]
	 * }
	 */
	public static final HashMap<String, ArrayList<String>> getStaffMarkets() {
		HashMap<String, ArrayList<String>> staff = new HashMap<String, ArrayList<String>>();
		ArrayList<String> urls = new ArrayList<String>();
		String previousName = "";
		
		try {
			String sql = "---";
			ResultSet result = query(sql);
			
			while(result.next()) {
				// We've iterated to a new DBA so add the results to the staff array and clear the urls list.
				if(!previousName.equals(result.getString(2))) {
					if(!urls.isEmpty()) {
						staff.put(previousName, urls);
						urls.clear();
					}
				}
				urls.add(result.getString(1));
				previousName = result.getString(2);
			}
			// TODO I don't like that I have to do this. Try to fix the loop above so I don't have to...
			staff.put(previousName, urls);
			
			// Close connection after working with result set.
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		return staff;
	}
}