package main;
import java.util.*;
import java.io.*;
 
class readIni {
	/**
	 * read the user.ini file.
	 */
	
  public static String read() {
    readIni ini = new readIni();
    return ini.command();
    }
 
  public String command() {
    try{
    	String s;
    	Properties p = new Properties();
    	p.load(new FileInputStream("user.ini"));
    	s = p.getProperty("dotCommand");

      return s;
    }
    catch (Exception e) {
      System.out.println(e);
      }
	return null;
    }
}