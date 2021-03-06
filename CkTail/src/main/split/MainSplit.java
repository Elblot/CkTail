package main.split;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import dependencies.Dependency;
import model.Event;
import model.Trace;

public class MainSplit {

	public static String log;
	public static String output;
	//public static String reg;
	public static boolean timerMode;

	public static HashMap<String, Double> means;
	public static Trace trace;
	public static Regex regex;
	public static Trace logOrigin;
	//public static double interval = 2000.0;//in milliseconds 
	public static double fact = 10.0;

	public static Dependency Dep;

	public static void main(String[] args) {
		final long timebuildingTraces1 = System.currentTimeMillis();
		ArrayList<Trace> T = new ArrayList<Trace>();
		means = new HashMap<String, Double>();
		try {
			MapperOptions.setOptions(args);
		} catch (Exception e) {
			System.err.println("pb option");
			System.exit(3);
		}
		trace = new Trace(new File(log), regex);
		System.out.println("traces built");
		final long timebuildingTraces2 = System.currentTimeMillis();
		final long timesplit1 = System.currentTimeMillis();
		Dep = new Dependency();
		logOrigin = trace;
		T.addAll(Split(trace));
		System.out.println("split done");
		final long timesplit2 = System.currentTimeMillis();
		final long timegenfile1 = System.currentTimeMillis();
		int i = 1;
		File dir = new File(output);
		dir.mkdir();
		dir = new File(output + "/traces");
		dir.mkdir();

		/* not for the time, will destroy my hdd */
		try {
			for(Trace t: T) {
				File f = new File(output+"/traces/"+i);
				t.writeTrace(f);
				i++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		final long timegenfile2 = System.currentTimeMillis();
		final long timedag1 = System.currentTimeMillis();
		Dep.buildDAG(output);

		System.out.println("Dep : " + Dep.toString());
		System.out.println("DAG :\n " + Dep.getDag());
		System.out.println("Files generated");
		final long timedag2 = System.currentTimeMillis();
		System.out.println("building Traces: " + (timebuildingTraces2 - timebuildingTraces1) + " ms");
		System.out.println("split: " + (timesplit2 - timesplit1) + " ms");
		System.out.println("file generation: " + (timegenfile2 - timegenfile1) + " ms");
		System.out.println("dag generation: " + (timedag2 - timedag1) + " ms");
		System.out.println("Total: " + (timedag2 + timesplit2 + timebuildingTraces2 + timegenfile2 - timegenfile1 -  timebuildingTraces1 - timesplit1 - timedag1) + " ms");
	}

	public static ArrayList<Trace> Split(Trace trace){
		ArrayList<Trace> T = new ArrayList<Trace>();
		int i = 1;
		Event aj = trace.getEvent(i - 1);
		while(!aj.isReq()) {
			System.out.println("not a req: " + aj.debug());
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
							if (checkDependencies(tprime, aj) || checkTime(tprime, aj)) {   //checkTime(tprime.getEvent(0), aj)) {
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
				else if (aj.isResp()){
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
				else {
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

	public static boolean checkTime(Trace t, Event aj) {
		//System.out.println(aj.debug());
		double mean =  getMean(aj.getFrom());
		Event prec = t.lastReq(aj.getFrom());
		double diff = aj.getDate(regex).getTime() - prec.getDate(regex).getTime();
		if (diff < mean/fact) { 
			return true;
		}
		return false;
	}
	
	/*public static boolean checkTime(Event first, Event aj) {
		double diff = aj.getDate(regex).getTime() - first.getDate(regex).getTime();
		//System.out.println
		//System.out.println("last:" + last.getDate(regex) + ", ai:" + aj.getDate(regex) + ", diff:" + diff);
		if (diff > 0 && diff < interval) { 
			return true;
		}
		return false;
	}*/


	public static boolean checkDependencies(Trace t, Event aj) {
		ArrayList<Event> chain = new ArrayList<Event>();
		Trace sub = logOrigin.subTrace(0,logOrigin.indexOf(aj));//
		ArrayList<Event> dependency = new ArrayList<Event>();
		int c = 0;
		ArrayList<Event> begin = sub.getEvery(aj.getFrom(), aj.getTo());
		begin.add(aj);
		for (@SuppressWarnings("unused") Event e: begin) {
			ArrayList<Event> dep = checkDependencies(sub, aj, chain);//
			if (!dep.isEmpty()) {
				c++;
				dependency.addAll(dep);
				if (c > 1) {
					return false;
				}
			}
		}
		if (c == 1 && t.containsAll(dependency)) {
			ArrayList<String> ld = new ArrayList<String>();
			ld.add(aj.getTo());
			ld.add(dependency.get(0).getFrom());
			Dep.add(ld);
			System.out.println("dep:" + ld);
			return true;
		}
		else {
			return false;
		}
	}

	public static ArrayList<Event> checkDependencies(Trace t, Event aj, ArrayList<Event> chain) {
		ArrayList<Event> res = new ArrayList<Event>();
		Event aprime = null;
		boolean one = false;
		for(int i = t.size() - 1 ; i >= 0; i--) {
			Event e = t.getEvent(i);
			if (e.dataSimilarity(aj) && e.getTo().equals(aj.getFrom()) && !chain.contains(e)){
				if (one) {
					return new ArrayList<Event>();
				}
				aprime = e;
				one = true;
				res.add(e);
				res.addAll(chain);
			}
		}
		if (one) {
			res = checkDependencies(t, aprime, res);
		}
		else {
			res.addAll(chain);
		}
		return res;
	}

	public static double getMean(String comp) {
		if (means.containsKey(comp)) {
			return means.get(comp);
		}
		else {
			double sum = 0.0;
			double c = 0.0;
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
			System.out.println("mean(" + comp + ") : " + sum/c);
			means.put(comp, sum/c);
			return sum/c;
		}
	}

}
