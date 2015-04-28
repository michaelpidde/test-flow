package com.michaelpidde.testflow.engine.util;

import com.michaelpidde.testflow.engine.util.Directory;

import java.io.File;
import java.io.IOException;
import java.lang.Object;
import java.util.ArrayList;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

public class PageObjectCompiler extends GroovyShell {

	public PageObjectCompiler(GroovyClassLoader loader, File directory) {
		try {
			ArrayList<String> files = Directory.listDirectoryFiles(directory, ".groovy");
			GroovyCodeSource code;

			for(String file : files) {
				code = new GroovyCodeSource(new File(directory.getAbsolutePath() + "/" + file));
				loader.parseClass(code, true);
			}
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}

}