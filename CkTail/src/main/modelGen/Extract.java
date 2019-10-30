package main.modelGen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Extract {

	private static ArrayList<String> identifiers = new ArrayList<String>();


	//Build the new traces corresponding of the components
	public static String analysis(String[] traces){
		int j = 1;
		String fName = createDir();
		ArrayList<ArrayList<String>> alFiles = loadFiles(traces);
		//System.out.println(identifiers);
		buildID(alFiles);
		//System.out.println(identifiers);
		for(ArrayList<String> alFile:alFiles) {
			for(int i = 0; i < alFile.size(); i++) {
				String event = alFile.get(i);
				int source = identifiers.indexOf(getSource(event));
				int destination = identifiers.indexOf(getDestination(event));
				BufferedWriter bw;
				try {			
					bw = new BufferedWriter(new FileWriter(new File(fName + "/T" + (j+source-1)+"tmp"), true));
					bw.write("!" + event + "\n");
					bw.close();
					bw = new BufferedWriter(new FileWriter(new File(fName + "/T" + (j+destination-1)+"tmp"), true));
					bw.write("?" + event + "\n");
					bw.close();
				}
				catch (FileNotFoundException e) {
					System.out.println("file /T"+  (j+source-1) + "tmp or file /T\"+  (j+source-1) + \"tmp not found " + e);
					return null;
				} catch (IOException e){
					System.out.println("unknown error " + e);
					return null;
				}
			}
			j+=identifiers.size();
		}
		System.out.println(identifiers);
		return fName;
	}

	//build the ArrayList that contains all the identifier of the component
	static void buildID(ArrayList<ArrayList<String>> files) {
		for(ArrayList<String> alFile:files) {
			for(int i = 0; i < alFile.size(); i++) {
				String Host = getSource(alFile.get(i));
				String Dest = getDestination(alFile.get(i));
				if (!identifiers.contains(Host)) {
					identifiers.add(Host);
				}
				if (!identifiers.contains(Dest)) {
					identifiers.add(Dest);
				}
			}
		}
	}

	// get the source of the message
	private static String getSource(String sequence) {
		String Host = "????";
		int h = sequence.indexOf("Host=");
		if (h != -1) { //TODO else throw exception
			Host = sequence.substring(h + 5, sequence.indexOf(";", h+5));
		}
		return Host;
	}

	// get the destination of the message
	private static String getDestination(String sequence) {
		String Dest = "????";
		int h = sequence.indexOf("Dest=");
		if (h != -1) { //TODO else throw exception
			if (sequence.indexOf(";", h+5) > h) {
				Dest = sequence.substring(h + 5, sequence.indexOf(";", h+5));
			}
			else {
				Dest = sequence.substring(h + 5, sequence.indexOf(")", h+5));
			}
		}
		return Dest;
	}

	// read the traces and save them in an ArrayList 
	public static ArrayList<ArrayList<String>> loadFiles(String[] traces){
		ArrayList<ArrayList<String>> alFiles = new ArrayList<ArrayList<String>>();
		String line;
		try {
			for(int i = 0; i < traces.length; i++) {
				ArrayList<String> alFile = new ArrayList<String>();
				File f = new File(traces[i]);
				BufferedReader br = new BufferedReader(new FileReader(f));
				line = br.readLine();
				while(line != null) {
					alFile.add(line);
					line = br.readLine();
				}
				alFiles.add(alFile);
				br.close();
			}
			return alFiles;
		}catch(Exception e) {}
		return null;
	}

	// create the directory that will contain the nex traces
	private static String createDir() {
		String tmpName = null, fName = "RESULTS/"+MainGen.dest;
		int i = 1;
		File x = new File(fName);
		while(x.exists()) {
			tmpName = fName+i;
			x = new File(tmpName);
			i++;
		}
		if (tmpName != null) {
			fName = tmpName;
		}
		MainGen.dest = fName;

		fName = fName+"/trace";
		x = new File(fName);
		x.mkdirs();
		return fName;
	}

}
