package ru.zenit.json.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;



public class FileJSON {
	
	public static String pathString;
	
	public static boolean verificationFile(String FileName){
		boolean res = true;
		File file = new File(FileName);
		try {
			printPaths(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return res;
	}
	
	
    public static String readFile(String file) throws IOException {
		Path p1 = Paths.get(file);
		System.out.println("readFile - begin ");
		pathString =  p1.getParent().toString();
		System.out.println("pathString = " + pathString);
		System.out.println("readFile - end ");
		
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }
	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	} 	
    
    
    public static void writeFile(String text, String Namefile) throws IOException {

		//file = pathString + "\\" + file;
		System.out.println("Save file = " + Namefile);
    	BufferedWriter writer = null; 
    	try 
    	{	
    		writer = new BufferedWriter(new FileWriter(new File(Namefile)));
    		writer.write(text);
    		
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
	    } finally {
	    	if (writer!=null) writer.close();
	    }	
    }
    
    private static String getFileNameWithoutExtension(File file) {
        String fileExt = "";
 
        try {
            if (file != null && file.exists()) {
            	//System.out.println(file.getParent());
                String name = file.getName();
                fileExt = name.replaceFirst("[.][^.]+$", "");
                fileExt = file.getParent() + "\\" + fileExt;
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileExt = "";
        }
 
        return fileExt;
    }	
    
	private static void printPaths(File file) throws IOException {
		System.out.println("Absolute Path: " + file.getAbsolutePath());
		System.out.println("Canonical Path: " + file.getCanonicalPath());
		System.out.println("Path: " + file.getPath());
	}    

}
