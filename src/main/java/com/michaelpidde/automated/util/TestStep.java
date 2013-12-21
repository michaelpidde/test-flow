package com.michaelpidde.automated.util;

public class TestStep {
	public String imagePath = "";
	public String description = "";
	
	public TestStep(String description, String imagePath) {
		this.description = description;
		this.imagePath = imagePath;
	}
	
	public String getImagePath() { return imagePath; }
	public String getDescription() { return description; }
}