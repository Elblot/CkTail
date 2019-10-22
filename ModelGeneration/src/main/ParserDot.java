package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;



public class ParserDot {

	int j, nbEtat = 0, nbTransition = 0;
	static int nbEtatTot, nbTransitionTot;

	
	public ParserDot(int j) {
		this.j = j;
	}
	
	
	public String parser(File f) {
		File file = new File(MainC.dest+"/RESULTAT.txt");
		try {
			file.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		String fileName = null;
		ArrayList<String> arStrAvecDoublon = new ArrayList<String>();
		try {
			arStrAvecDoublon = readFile(f);
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		String strEntier = nettoie(arStrAvecDoublon);
		ArrayList<Integer> listInt = spliter(strEntier);
		Set<Integer> sInt = new TreeSet<Integer>();
		sInt.addAll(listInt);
		ArrayList<Integer> finalInt = new ArrayList<Integer>(sInt);
		String stRemplaceInt = remplaceInt(finalInt, strEntier);
		try {
			fileName = ecriture(finalInt, stRemplaceInt);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			resultat(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		nbEtatTot = nbEtatTot+nbEtat;
		nbTransitionTot=nbTransitionTot+nbTransition;
		return fileName;
	}
	
	
	//read file and stock lines in an ArrayList
	public ArrayList<String> readFile(File f) throws IOException {
		String line;
		ArrayList<String> tab = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		line = br.readLine();
		while(line != null) {
			tab.add(line);
			line = br.readLine();
		}
		br.close();
		f = null;
		return tab;
	}
	
	
	//remove duplicated string and stock them into a String
	public String nettoie(ArrayList<String> arStrAvecDoublon) {
		Set<String> set = new LinkedHashSet<String>();

		set.addAll(arStrAvecDoublon);	
		ArrayList<String> arStrSansDoublon = new ArrayList<String>(set);
		String strEntier="";
		for(String sr:arStrSansDoublon) {
			strEntier = strEntier + sr + "\n";
		}
		return strEntier;
	}
	
	
	//obtain state numbers in an ArrayList
	public ArrayList<Integer> spliter(String str) {
		String[] splited = str.split("S");
		ArrayList<Integer> listInt = new ArrayList<Integer>();		
		for(String ssplit :splited) {
			String[] num = ssplit.split("\\[");
			for(String snum:num) {
				String[] num1 = snum.split(" ");
				for(String snum1:num1) {
					try {
						listInt.add(Integer.parseInt(snum1));
					}
					catch(NumberFormatException e) {}
				}
			}
		}
		return listInt;
	}
	
	
	//Replace unnatural state names by new ones
	public String remplaceInt(ArrayList<Integer> finalInt, String strEntier) {
		String st="";
		for(int i = 0; i < finalInt.size(); i++) {
			st = strEntier.replaceAll("S"+finalInt.get(i)+" ", "S"+i+" ");
			strEntier = st;
		}
		for(int i = 0; i < finalInt.size(); i++) {
			st = strEntier.replaceAll("S"+finalInt.get(i)+"\\[", "S"+i+"\\[");
			strEntier = st;
		}
		return st;
	}
	
	
	//rewrite dot file with changes
	public String ecriture(ArrayList<Integer> finalInt, String st) throws IOException {
		String[] atrier = st.split("\n");
		HashMap<Integer, String> intShape = new HashMap<Integer, String>();
		for(String str:atrier) {
			if(str.contains("shape=")) {
				int numero = Integer.parseInt(str.substring(1, str.indexOf("[")));
				String shape = str.substring(str.indexOf("shape="), str.indexOf("]"));
				intShape.put(numero, shape);
			}
		}
		
		String fileName = MainC.dest+"/C"+j+".dot";
		File file = new File(fileName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(atrier[0]+"\n");
		bw.write(atrier[1]+"\n");
		for(int i = 1; i < finalInt.size(); i++) {
			if(intShape.containsKey(i)) {
				bw.write("S" + i + "[label=S" + i + "," + intShape.get(i) + "];\n");
			}
			else {
				bw.write("S"+i+"[label=S"+i+",shape=circle];\n");
			}
			this.nbEtat++;
		}
		/*if(!MainC.hide) {
			switch(MainC.algo) {
			case "strong":
				for(int i = 1; i < finalInt.size(); i++) {
					bw.write("\"S"+i+"\'\"[label=\"S"+i+"\'\",shape=circle];\n");
					this.nbEtat++;
				}
			}
		}*/
		
		bw.write("S00 -> S1\n");
		for(int i = 1; i < finalInt.size(); i++) {//i it�re sur les noms d'�tats qui existent
			ArrayList<String> ar = new ArrayList<String>();
			for(int k = 3; k < atrier.length-1; k++) {//k it�re sur les lignes que doit contenir le fichier
				if(atrier[k].contains("S"+i+" ") || atrier[k].contains("S"+i+"[") && !atrier[k].contains(" S"+i)){
					ar.add(atrier[k]);
				}
			}
			for(int l = 0; l < finalInt.size(); l++) {//l it�re sur les noms d'�tats qui existent
				for(int m = 0; m < ar.size(); m++) {//m it�re sur les �l�ments de l'arraylist
					if(ar.get(m).contains("-> S"+l+"[")) {
						bw.write(ar.get(m)+"\n");
						this.nbTransition++;
					}
				}
			}
		}
		/*if(!MainC.hide) {
			switch(MainC.algo) {
			case "strong":
				for(int i = 1; i<finalInt.size(); i++) {
					bw.write("S"+i+" -> \"S"+i+"\'\"[label =\"call_C\"]\n");
					this.nbTransition++;
					bw.write("\"S"+i+"\'\" -> S"+i+"[label =\"return_C\"]\n");
					this.nbTransition++;
				}
			}
		}*/
		bw.write(atrier[atrier.length-1]+"\n");
		bw.close();
		return fileName;
	}
	
	
	public void resultat(File f) throws IOException {
		if(!f.exists()) {
			System.err.println("pb RESULTAT n existe pas");
			System.exit(1);
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		bw.write("C"+j+".dot contient :\n"+this.nbEtat+" �tats\n"+this.nbTransition+" transitions\n\n");
		bw.close();
	}
	
}
