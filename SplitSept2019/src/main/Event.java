package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;


public class Event {

	public static String from = "Host";
	public static String to = "Dest";
	
	private String label;
	private ArrayList<String> params;
	public String date;  //public for debug
	
	
	public Event(String line, Matcher m) {
		date = m.group("date");
		label = m.group("label");
		params = new ArrayList<String>();
		int n = 1;
		try {
		while(m.group("param" + n) != null) {
			//System.out.println(n);
			//System.out.println(m.group("param" + n));
			params.add(m.group("param" + n));
			n++;
		}
		}catch(IllegalArgumentException e) {
			//end of while
		}
	}

	
	
	public String getLabel() {
		return label;
	}
	
	public ArrayList<String> getparams() {
		return params;
	}

	public String getFrom() {
		String res = "";
		for (String param:params) {
			if (param.startsWith(from + "=")){
				return param.substring(from.length() + 1);
			}
		}
		System.err.println("no From in :" + this.toString());
		System.exit(3);
		return res;
	}
	
	public String  getTo() {
		String res = "";
		for (String param:params) {
			if (param.startsWith(to + "=")){
				return param.substring(to.length() + 1);
			}
		}
		System.err.println("no From in :" + this.toString());
		System.exit(3);
		return res;
	}
	
	public Date getDate(Regex regex) {
		SimpleDateFormat sdf = regex.getSDF();
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			System.err.println("problem with date format");
			System.exit(3);
		}
		return d;
	}
	
	public String toString() {
		String res = label + "(";
		res = res + "date=" + date + ";";//tmp
		for (String param: params) {
			res = res + param + ";";
		}
		res = res.substring(0, res.length()-1);
		res = res +")";
		return res;
	}
	
	public String debug() {
		String res = label + "(";
		res = res + date;
		res = res + params.get(0);
		res = res + params.get(1);
		res = res +")\n";
		return res;
	}
	
	public boolean dataSimilarity(Event ai) {
		ArrayList<String> paramsi = ai.getparams();
		ArrayList<String> paramsj = this.getparams();
		for (String parami: paramsi) {
			if (!(parami.contains(from) || parami.contains(to))) {
				for (String paramj: paramsj) {
					if (paramj.equals(parami)) {
						System.out.println("match :" + parami);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//only for our cases
	public boolean isReq() {
		if (this.toString().contains("esponse")) {
			return false;
		}
		else {
			return true;
		}
	}
	
}

