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
	public static Trace logOrigin;
	public static double fact = 20.0;
	
	public static Dependency Dep;

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
		System.out.println("traces built");
		Dep = new Dependency();
		for (Trace trace: traces) { //we suppose that their is only one log
			logOrigin = trace;
			T.addAll(Split(trace));
		}
		System.out.println("split done");
		int i = 1;
		File dir = new File(output);
		dir.mkdir();
		//
		//TODO getDependencies(T);
		//
		try {
			for(Trace t: T) {
				File f = new File(output+"/"+i);
				t.writeTrace(f);
				i++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Dep : " + Dep.toString());
		
		System.out.println("DAG :\n " + Dep.getDag());
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
		//System.out.println("new trace:\n");

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
		Dep.extend(L, tprime);
		i++;
		eventAnalysis:
			while (i <= trace.size()) {
				aj = trace.getEvent(i - 1);
				if (aj.isReq()) {
					//case 2
					for (ArrayList<Event> LReq: SR) {
						if (SA.contains(aj.getFrom()) &&
								!LReq.isEmpty() && aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo()) && //verif LReq non vide
								!pendingRequest(aj, SR)) {
							tprime.addEvent(aj);
							LReq.add(aj);
							SA.add(aj.getFrom());
							SA.add(aj.getTo());
							Dep.extend(LReq, tprime);
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
							//System.out.println("case 3:" + aj.getDate(regex).toString());
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
						if(!(SA.contains(aj.getFrom()) ||
								aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo()) ||
								pendingRequest(aj, SR))) {
							break;
						}
						if(c==SR.size()) {
							int end = logOrigin.indexOf(aj);
							if (isContinuation(tprime, aj, logOrigin.subTrace(calc(end), end+1))) {
								tprime.addEvent(aj);
								ArrayList<Event> LR = new ArrayList<Event>();
								LR.add(aj);
								SR.add(LR);
								SA.add(aj.getFrom());
								SA.add(aj.getTo());
								Dep.extend(LR, tprime);
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
					for (ArrayList<Event> LReq: SR) {	
						if(!LReq.isEmpty() && aj.getFrom().equals(LReq.get(LReq.size() - 1).getTo()) &&
								aj.getTo().equals(LReq.get(LReq.size() - 1).getFrom())) {
							tprime.addEvent(aj);
							LReq.remove(LReq.size() - 1);
							i++;
							continue eventAnalysis;	
						}
					}
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

	public static int calc(int end) {
		//System.out.println("other :" + logOrigin.getEvent(end) + "\n");
		int res = end;
		String comp = logOrigin.getEvent(end).getFrom();
		//ArrayList<Event> Lresp = new ArrayList<Event>();
		Event Lreq;
		Event Lresp;
		Date dreq = null;
		Date dresp;
		for (int i = end; i > 0; --i){
			Event e = logOrigin.getEvent(i);
			//System.out.println(logOrigin.subTrace(0, i));
			if (e.isReq() && (e.getFrom().equals(comp) || e.getTo().equals(comp)) && logOrigin.subTrace(0, i).complete(comp)) {
				Lreq = e;
				dreq = Lreq.getDate(regex);
				res = i;
			}
			if (!e.isReq() && (e.getFrom().equals(comp) || e.getTo().equals(comp)) && logOrigin.subTrace(0, i + 1).complete(comp)) {
				Lresp = e;
				dresp = Lresp.getDate(regex);
				if (dreq.getTime() - dresp.getTime() > getMean(comp)) {
					return res;
				}
			}
		}
		return 0;
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

	public static boolean isContinuation(Trace t, Event aj, Trace trace) {
		//part data
		/*for(Event ai : t.getSeq()) {
			if ((aj.getFrom().equals(ai.getFrom()) || aj.getFrom().equals(ai.getTo())) && aj.dataSimilarity(ai)){
				return true;
			}
		}*/

		/* test */ 
		ArrayList<Event> candidate = new ArrayList<Event>();
		//System.out.println(trace.toString());
		for(Event ai : trace.getSeq()) {
			if (aj.getFrom().equals(ai.getTo()) && aj.dataSimilarity(ai)){
				candidate.add(ai);
			}
		}
		if (!candidate.isEmpty() && t.getSeq().containsAll(candidate)) {
			//System.out.println(candidate.toString());
			return true;
		}

		//part time
		/*double mean getMean(aj.getFrom());
		Event prec = t.lastReq(aj.getFrom());
		double diff = aj.getDate(regex).getTime() - prec.getDate(regex).getTime();
		if (diff < mean/fact) { 
			return true;
		}*/
		return false;
	}

	public static double getMean(String comp) {
		if (means.containsKey(comp)) {
			return means.get(comp);
		}
		else {
			double sum = 0.0;
			double c = 0.0;
			for (Trace trace: traces) {
				Date d1 = null;
				for (Event e: trace.getSeq()) {
					if ((e.getFrom().equals(comp) || e.getTo().equals(comp)) && e.isReq()) {
						Date d2 = e.getDate(regex);
						if(d1 == null){
							d1 = d2;
							continue;
						}
						else {
							double dif = d2.getTime()-d1.getTime();
							c++;
							sum = sum + dif;
							d1 = d2;
						}
					}
				}
			}
			System.out.println("mean(" + comp + ") : " + sum/c);
			means.put(comp, sum/c);
			return sum/c;
		}
	}

}
