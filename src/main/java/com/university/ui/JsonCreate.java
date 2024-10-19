package com.university.ui;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class JsonCreate {
	public static void main(String[] args) {
		String filename="Studentssss";
		try(FileWriter file=new FileWriter(filename)){
			
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		}
}
