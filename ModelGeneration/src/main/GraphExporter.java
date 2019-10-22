package main;
/*
 * This code is in part based on Clemens Hammacher's code.
 * 
 * Source: https://ccs.hammacher.name
 * 
 * License: Eclipse Public License v1.0.
 */

import java.io.IOException;
import java.util.logging.Logger;


public class GraphExporter {
	
	/**
	 *This class create a .png file from a .dot file.
	 *User must specify the dot command of his system
	 *in a "user.ini" file under this format :
	 *dotCommand=<myDotCommand>
	 */
	
    static Logger logger = Logger.getLogger("GraphExporter");
    static String dotCommand = null;

	private static void getConfig() {
		dotCommand = readIni.read();
	}

    public static void generatePngFileFromDotFile(String fileName) {
    	getConfig();
        String pngFile = fileName.substring(0, fileName.length()-3);
        if (dotCommand == null) {
        	System.out.println("No dot command");
            return;
        }
        String imageExt = "png";
        String execCommand = dotCommand + " -T" + imageExt + " " + fileName+ " -o " +pngFile+imageExt;
        logger.info("Exporting graph to: " + pngFile.toString() + imageExt);
        Process dotProcess;
        
        try {
            dotProcess = Runtime.getRuntime().exec(execCommand);
        } catch (IOException e) {
            logger.info("Could not run dotCommand '" + execCommand + "': " + e.getMessage());
            return;
        }
        
        try {
            dotProcess.waitFor();
        } catch (InterruptedException e) {
            logger.severe("Waiting for dot process interrupted '" + execCommand + "': " + e.getMessage());
        }
    }
   
}
