/*
 * TestFlow configuration factory of sorts...
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

package com.michaelpidde.testflow.engine.config;

import com.michaelpidde.testflow.engine.config.ConfigJson;
import com.michaelpidde.testflow.engine.config.ConfigMysql;

public final class Config {
	public static IConfig get(String type) {
		IConfig config;
		switch(type) {
			case "mysql":
				config = new ConfigMysql();
				break;
			default:
				config = new ConfigJson(type);
				break;
		}
		return config;
	}
}