/*
 * TestFlow generic MySQL class
 *
 * This file is part of TestFlow.
 *
 * Copyright (C) 2014-2016 Michael Pidde <michael.pidde@gmail.com>
 *
 * TestFlow is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * TestFlow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TestFlow; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.michaelpidde.testflow.engine.database;

import com.michaelpidde.testflow.engine.database.Datasource;
import com.michaelpidde.testflow.engine.config.IConfig;
import com.michaelpidde.testflow.engine.util.TestResult;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class MySql implements IDatabase {
	private Connection connection;
	private IConfig config;
	private Datasource datasource;

	public MySql(IConfig config) {
		this.config = config;
		this.datasource = config.getLoggingDatasource();

		try {
			this.connection = DriverManager.getConnection(
				"jdbc:mysql://" + this.datasource.getUrl() + 
				"/" + this.datasource.getDatabase() + 
				"?user=" + this.datasource.getUsername() + 
				"&password=" + this.datasource.getPassword()
			);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void insertResult(TestResult result) {
		Statement statement = null;
		ResultSet queryResult = null;

		try {
			statement = this.connection.createStatement();
			String query = "insert into testRuns (testId, passed, timeElapsed, steps)"
				+ " values ("
				+ "";
			queryResult = statement.executeQuery(query);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void insertSuiteRun(ArrayList<ArrayList<TestResult>> suite) {}
}