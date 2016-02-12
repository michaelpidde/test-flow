/*
 * TestFlow directory utilities.
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

package com.michaelpidde.testflow.engine.util;

import java.io.File;
import java.util.ArrayList;
import java.io.FilenameFilter;

public class Directory {
	public static ArrayList<String> listDirectoryFiles(File directory, final String accept) {
		ArrayList<String> files = new ArrayList<String>();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String name) {
				return name.endsWith(accept);
			}
		};
		for(File file : directory.listFiles(filter)) {
			if(!file.isDirectory()) {
				files.add(file.getName());
			}
		}
		return files;
	}


	public static ArrayList<String> listDirectories(File directory, final String exclude) {
		ArrayList<String> directories = new ArrayList<String>();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String name) {
				return !name.endsWith(exclude);
			}
		};
		for (File file : directory.listFiles(filter)) {
			if(file.isDirectory()) {
				directories.add(file.getName());
			}
		}
		return directories;
	}
}