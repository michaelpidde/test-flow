/*
 * TestFlow Test MySQL DAO
 *
 * Copyright (C) 2013 Michael Pidde <michael.pidde@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.mysql;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class TestDAO {
	private static Connection connection = null;


	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}


	public static Connection connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/testflow?user=root&password=password");
		} catch(SQLException e) {
			System.out.println(e.toString());
		}

		return connection;
	}



	public static void close() {
		if(connection != null) {
			try {
				connection.close();
			} catch(SQLException e) { }
			connection = null;
		}
	}


	public static ResultSet query(String sql) {
		Statement statement = null;
		ResultSet result = null;

		try {
			if(connection == null) {
				connection = connect();
			}
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
		} catch(SQLException e) {
			System.out.println(e.toString());
		}

		return result;
	}


	public static ResultSet query(String sql, ArrayList args) {
		ResultSet result = null;
		CallableStatement statement;
		boolean success;

		try {
			if(connection == null) {
				connection = connect();
			}
			statement = connection.prepareCall(sql);
			if(args.size() > 0) {
				for(int i = 0; i <= args.size()-1; i++) {
					statement.setString(i+1, args.get(i).toString());
				}
			}
			success = statement.execute();
			if(success) {
				result = statement.getResultSet();
			}
		} catch(SQLException e) {
			System.out.println(e.toString());
		}

		return result;
	}


	public static ArrayList<String> getApps() {
		ArrayList<String> apps = new ArrayList<String>();
		ResultSet result = null;

		try {
			result = query("select id, name from packages");
			while(result.next()) {
				apps.add(result.getString("name"));
			}
		} catch(SQLException e) {
			System.out.println(e.toString());
		} finally {
			if(result != null) {
				try {
					result.close();
				} catch(SQLException e) { }
				result = null;
			}

			close();
		}

		return apps;
	}


	public static ArrayList<String> getSuites(String app) {
		ArrayList<String> suites = new ArrayList<String>();
		ResultSet result = null;
		ArrayList<String> params = new ArrayList<String>();
		params.add(app);

		try {
			result = query("{call getSuites(?)}", params);

			while(result.next()) {
				suites.add(result.getString("name"));
			}
		} catch(SQLException e) {
			System.out.println(e.toString());
		} finally {
			if(result != null) {
				try {
					result.close();
				} catch(SQLException e) { }
				result = null;
			}

			close();
		}

		return suites;
	}


	public static ArrayList<String> getTests(String app) {
		ArrayList<String> tests = new ArrayList<String>();
		ResultSet result = null;
		ArrayList<String> params = new ArrayList<String>();
		params.add(app);

		try {
			result = query("{call getTests(?)}", params);

			while(result.next()) {
				tests.add(result.getString("name"));
			}
		} catch(SQLException e) {
			System.out.println(e.toString());
		} finally {
			if(result != null) {
				try {
					result.close();
				} catch(SQLException e) { }
				result = null;
			}

			close();
		}

		return tests;
	}


	public static String getBaseUrl(String app) {
		String baseUrl = null;
		ResultSet result = null;
		ArrayList<String> params = new ArrayList<String>();
		params.add(app);

		try {
			result = query("{call getBaseUrl(?)}", params);

			while(result.next()) {
				baseUrl = result.getString("baseUrl");
			}
		} catch(SQLException e) {
			System.out.println(e.toString());
		} finally {
			if(result != null) {
				try {
					result.close();
				} catch(SQLException e) { }
				result = null;
			}

			close();
		}

		return baseUrl;
	}
}