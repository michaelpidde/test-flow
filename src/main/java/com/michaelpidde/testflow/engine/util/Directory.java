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