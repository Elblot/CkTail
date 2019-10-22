package main.modelGen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import traces.Method;
import traces.Statement;
import traces.Trace;



public class Group {

	String s;
	int sizeTracei;
	int[] component;


	public Group(String s) {
		this.s = s;
	}

	public ArrayList<ArrayList<Trace>> Synchronization() throws Exception {
		int index = 0;
		ArrayList<String> components = new ArrayList<String>();
		ArrayList<ArrayList<String>> newtraces = addFileClust();
		component = new int[newtraces.size()];
		for (ArrayList<String> trace : newtraces) {
			String compnb = GetNumber(trace);
			if (!NewContains(components, compnb)) {
				components.add(compnb);
			}
			component[index] = NewIndexOf(components, compnb);
			index++;
		}
		ArrayList<ArrayList<Trace>> alTraces = finalClustering(component, newtraces );
		return alTraces;
	}

	public static boolean NewContains(ArrayList<String> components, String compnb) {
		for (String component : components) {
			if (component.equals(compnb)) {
				return true;
			}
		}
		return false;
	}

	public static int NewIndexOf(ArrayList<String> components, String compnb) {
		int index = 0;
		for (String component : components) {
			if (component.equals(compnb)) {
				return index;
			}
			index++;
		}
		return -9000;
	}


	/* 
	 * 
	 * change below to fit the identifiers parameter
	 * 
	 */
	/* get all the n of C_n */
	public static String GetNumber(ArrayList<String> trace) {
		String ID = "????";
		String event = trace.get(0);
		if (event.startsWith("!")) {
			int h = event.indexOf("Host=");
			if (h != -1) {
				ID = event.substring(h + 5, event.indexOf(";", h + 5));
				/* 5 is the length of "Host=" */
			}
		}
		else if (event.startsWith("?")) {
			int d = event.indexOf("Dest=");
			if (d != -1) {
				if (event.indexOf(";", d+5) > d) {
					ID = event.substring(d + 5, event.indexOf(";", d + 5));
				}
				else {
					ID = event.substring(d + 5, event.indexOf(")", d + 5));
				}
			}
		}
		return ID;
	}

	//read file to stock lines in an ArrayList.
	public ArrayList<ArrayList<String>> addFileClust() throws Exception {
		int n = 1;
		ArrayList<ArrayList<String>> newtraces = new ArrayList<ArrayList<String>>();
		File f = new File(s + "/trace" + n);
		while (f.exists()){
			BufferedReader br = new BufferedReader(new FileReader(f));
			ArrayList<String> alString = new ArrayList<String>();
			String line = br.readLine();
			while(line != null) {
				alString.add(line);
				line = br.readLine();
			}
			br.close();
			newtraces.add(alString);
			++n;
			f = new File(s + "/trace" + n);
		}
		sizeTracei = newtraces.size();
		int k = 1;
		File Tn = new File(s + "/T" + k);
		while (Tn.exists()){
			BufferedReader br = new BufferedReader(new FileReader(Tn));
			ArrayList<String> alString = new ArrayList<String>();
			String line = br.readLine();
			while(line != null) {
				alString.add(line);
				line = br.readLine();
			}
			br.close();
			newtraces.add(alString);
			++k;
			Tn = new File(s + "/T" + k);
		}
		return newtraces;
	}



	//sort files in a ArrayList (depends of the algorithm)
	public ArrayList<ArrayList<Trace>> finalClustering(int[] clusters, ArrayList<ArrayList<String>> newtraces){
		int nbClust = Utility.maxArray(clusters);
		ArrayList<ArrayList<Trace>> alTrace = new ArrayList<ArrayList<Trace>>();
		for(int i = 0; i <= nbClust; i++) {
			ArrayList<Trace> a = new ArrayList<Trace>();
			for(int j = 0; j < clusters.length; j++) {
				if (clusters[j] == i) {
					if (j >= sizeTracei) {
						Trace t = fileToTrace(s + "/T" + (j - sizeTracei +1));
						a.add(t);
					}
					else {
						Trace t = fileToTrace(s + "/trace" + (j+1));
						a.add(t);
					}
				}
			}
			alTrace.add(a);
		}
		return alTrace;
	}


	//read files to transform them in Trace.
	private Trace fileToTrace(String file) {
		//System.out.println(file);
		//System.out.println("size = " + sizeTracei);
		File f = new File(file);
		if(!f.exists()) {
			return null;
		}
		Trace trace = new Trace();
		int i = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				//System.out.println(line);
				if (line.contains("call")) {
					String str = line.substring(line.indexOf("T")+1);
					i = Integer.parseInt(str);
					line = "call_C"+i;
				}
				if (line.contains("return")) {
					String str = line.substring(line.indexOf("T")+1);
					i = Integer.parseInt(str);
					line = "return_C"+i;	
				}
				Method m = new Method(line);
				Statement st = new Statement(m);
				trace.add(st);
				line = br.readLine();
			}
			br.close();
		}catch(FileNotFoundException e) {
			System.out.println("file not found");
			System.exit(3);
		}catch(IOException e) {
			System.out.println("I dont know what the fuck append");
			System.exit(3);
		}

		return trace;
	}

}

