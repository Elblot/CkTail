package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

	public static String log;
	public static String output;
	//public static String reg;
	public static boolean timerMode;

	public static HashMap<String, Double> means;
	public static HashSet<Trace> traces;
	public static Regex regex;
	public static double fact = 2.0;

	public static void main(String[] args) {
		//System.out.println("begin");
		ArrayList<Trace> T = new ArrayList<Trace>();
		means = new HashMap<String, Double>();
		try {
			MapperOptions.setOptions(args);
		} catch (Exception e) {
			System.err.println("pb option");
			System.exit(3);
		}
		traces = getTraces();
		//System.out.println(traces.toString());
		System.out.println("traces built");
		for (Trace trace: traces) {
			T.addAll(Split(trace));
		}
		System.out.println("split done");
		int i = 1;
		File dir = new File(output);
		dir.mkdir();
		try {
			for(Trace t: T) {
				File f = new File(output+"/"+i);
				t.writeTrace(f);
				i++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Files generated");
	}

	public static HashSet<Trace> getTraces() {
		HashSet<Trace> traces = new HashSet<Trace>();
		File directory = new File(log);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file: files) {
				traces.add(new Trace(file, regex));
			}
		}
		else {
			System.err.println("input must be a diretory");
			System.exit(3);
		}
		return traces;
	}

	// process on already built Trace
	public static ArrayList<Trace> Split(Trace trace){
		ArrayList<Trace> T = new ArrayList<Trace>();
		int i = 1;
		Event aj = trace.getEvent(i - 1);
		while(!aj.isReq()) {
			i++;
			if (i >= trace.size()) {
				return T;
			}
			aj = trace.getEvent(i - 1);
		}
		Trace tprime = new Trace();
		HashSet<ArrayList<Event>> SR = new HashSet<ArrayList<Event>>();
		Trace tprimeprime = new Trace(); 
		HashSet<String> SA = new HashSet<String>();
		tprime.addEvent(aj);
		ArrayList<Event> L = new ArrayList<Event>();
		L.add(aj);
		SR.add(L);
		SA.add(aj.getFrom());
		SA.add(aj.getTo());
		i++;
		eventAnalysis:
			while (i <= trace.size()) {
				//System.out.println(SR);
				aj = trace.getEvent(i - 1);
				if (aj.isReq()) {
					//case 2
					for (ArrayList<Event> LReq: SR) {
						if (SA.contains(aj.getFrom()) &&
								!LReq.isEmpty() && aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo()) && //verif LReq non vide
								!pendingRequest(aj, SR)) {
							//System.out.println("case 2 :" + aj.debug());
							tprime.addEvent(aj);
							LReq.add(aj);
							SA.add(aj.getFrom());
							SA.add(aj.getTo());
							i++;
							continue eventAnalysis;								
						}
					}
					//case 3
					int c = 0;
					for (ArrayList<Event> LReq: SR) {		
						c++;
						if(!(!SA.contains(aj.getFrom()) ||
								(!LReq.isEmpty() && aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo())) ||
								pendingRequest(aj, SR))) {
							break;
						}
						if(c==SR.size()) {
							//System.out.println("case 3 :" + aj.debug());
							tprimeprime.addEvent(aj);
							i++;
							continue eventAnalysis;	
						}
					}
					//case 4
					for (ArrayList<Event> LReq: SR) {
						if(SA.contains(aj.getFrom()) &&
								!LReq.isEmpty() &&
								pendingRequest(aj, SR)) {
							System.err.println("error, not follow H3");
							break eventAnalysis;
						}
					}
					//case 5
					c = 0;
					for (ArrayList<Event> LReq: SR) {	
						c++;
						//System.out.println("case 5 :" + aj.debug());
						if(!(SA.contains(aj.getFrom()) ||
								aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo()) ||
								pendingRequest(aj, SR))) {
							break;
						}
						if(c==SR.size()) {
							//System.out.println("pass case 5");
							if (isContinuation(tprime, aj)) {
								tprime.addEvent(aj);
								//LReq.add(aj);
								ArrayList<Event> LR = new ArrayList<Event>();
								LR.add(aj);
								SR.add(LR);
								SA.add(aj.getFrom());
								SA.add(aj.getTo());
							}
							else {
								tprimeprime.addEvent(aj);
							}
							i++;
							continue eventAnalysis;	
						}
					}
					System.out.println("passed in no cases");
				}
				else {
					//case 1
					//System.out.println("isResp");
					for (ArrayList<Event> LReq: SR) {	
						/*System.out.println(aj.getFrom() + " = " + LReq.get(LReq.size() - 1).getTo());
						System.out.println(aj.getTo() + " = " + LReq.get(LReq.size() - 1).getFrom());
						System.out.println("pending ? :" + pendingRequest(aj, SR));*/
						if(!LReq.isEmpty() && aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo()) &&
								aj.getTo().equals(LReq.get(LReq.size() - 1).getFrom())) {
							//System.out.println("case 1 :" + aj.debug());
							//System.out.println("yes");
							tprime.addEvent(aj);
							LReq.remove(LReq.size() - 1);
							i++;
							continue eventAnalysis;	
						}
					}
					//System.out.println("no");
					//System.out.println("case 0 :" + aj.debug());
					tprimeprime.addEvent(aj);
					i++;
				}
			}
		boolean empty = true;
		for (ArrayList<Event> LReq: SR) {
			if (!LReq.isEmpty()) {
				empty = false;
			}
		}
		if (empty){
			T.add(tprime);
		}
		else {
			System.out.println("not finished : " + tprime.debug());
		}
		//System.out.println(tprimeprime);
		if (!tprimeprime.isEmpty()) {
			T.addAll(Split(tprimeprime));
		}
		return T;
	}

	public static boolean pendingRequest(Event aj, HashSet<ArrayList<Event>> SR) {
		for (ArrayList<Event> LReq: SR) {
			for (Event e: LReq) {
				if (e.getFrom().equals(aj.getFrom())) {
					return true;
				}
			}
		}
		return false;
	}



	public static boolean isContinuation(Trace t, Event aj) {
		//part data
		/*for(Event ai : t.getSeq()) {
			if ((aj.getFrom().equals(ai.getFrom()) || aj.getFrom().equals(ai.getTo())) && aj.dataSimilarity(ai)){
				return true;
			}
		}*/
		//part time
		double mean = 0.0;
		if (means.containsKey(aj.getFrom())) {
			mean = means.get(aj.getFrom());
		}
		else {
			mean = getMean(aj.getFrom());
			means.put(aj.getFrom(), mean);
		}
		Event prec = t.lastReq(aj.getFrom());
		double diff = aj.getDate(regex).getTime() - prec.getDate(regex).getTime();
		if (diff < mean/fact) { 
			/*System.out.println(diff);
			System.out.println(mean);
			System.out.println(aj.getDate(regex));*/
			return true;
		}
		return false;
	}

	
	
	public static double getMean(String comp) {
		double sum = 0.0;
		double c = 0.0;
		for (Trace trace: traces) {
			Date d1 = null;
			for (Event e: trace.getSeq()) {
				if ((e.getFrom().equals(comp) || e.getTo().equals(comp)) && e.isReq()) {
					//System.out.println(line);
					Date d2 = e.getDate(regex);
					if(d1 == null){
						d1 = d2;
						continue;
					}
					else {
						double dif = d2.getTime()-d1.getTime();
						//if (line.contains("Host=" + comp)) {
						//	System.out.println("dif = " + dif);
						//}
					c++;
					sum = sum + dif;
					d1 = d2;
					}
				}
			}
		}
		return sum/c;
	}

}
