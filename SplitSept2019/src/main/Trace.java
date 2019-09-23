package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class Trace {

	public ArrayList<Event> seq;	
	
	public Trace() {
		seq = new ArrayList<Event>();
	}
	
	public Trace(ArrayList<Event> t) {
		seq = t;
	}
	
	public Trace(File file, Regex regex) {
		seq = new ArrayList<Event>();
		try {
			BufferedReader br = new BufferedReader (new FileReader(file));
			String line = br.readLine();
			outside:
			while (line!=null) {
				for(int j = 0; j < regex.getPatterns().size(); j++) {
					Matcher m = regex.getPatterns().get(j).matcher(line);
					if(m.find()) {
						//System.out.println("pass");
						seq.add(new Event(line, m));
						line = br.readLine();
						continue outside;
					}
				}
				System.err.println("no regex match line: " + line);
				System.exit(3);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("File "+ file + " not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException");
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return Arrays.deepToString(seq.toArray());
	}
	
	public String debug() {
		String res = "[";
		for (Event e : seq) {
			res = res + e.debug() + ";" ;
		}
		return res;
	}
	
	public void writeTrace(File file) {
		try {
			BufferedWriter bw = new BufferedWriter (new FileWriter(file));
			for (Event e: seq) {
				bw.write(e + "\n");
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Event> getSeq() {
		//ArrayList<Event> res = new ArrayList<Event>(seq);
		return seq;
	}
	
	public void addEvent(Event e) {
		seq.add(e);
	}
	
	public boolean isEmpty() {
		return seq.isEmpty();
	}
	
	public Event getEvent(int index) {
		return seq.get(index);
	}
	
	public Event lastReq(String comp) {
		for (int i = seq.size()-1; i >= 0; i--) {
			Event e = seq.get(i);
			if (e.isReq() && (e.getparams().contains(Event.from + "=" + comp) ||
					e.getparams().contains(Event.to + "=" + comp))){
				return e;
			}
		}
		System.err.println("no lastReq in the trace case 5");
		System.exit(3);
		return null;
	}
	
	public int size() {
		return seq.size();
	}
}
