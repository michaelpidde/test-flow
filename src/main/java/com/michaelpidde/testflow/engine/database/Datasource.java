/*
 * TestFlow generic Datasource class
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

public class Datasource {
	private String type;
	private String url;
	private String username;
	private String password;
	private String database;

	public Datasource(String type, String url, String username, 
		String password, String database) {

		this.type = type;
		this.url = url;
		this.username = username;
		this.password = password;
		this.database = database;
	}

	public String getType() {
		return this.type;
	}

	public String getUrl() {
		return this.url;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getDatabase() {
		return this.database;
	}
}