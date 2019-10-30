package main.modelGen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import fsa.FSA;
import fsa.GenerateDOT;
import miners.KTail.KTail;
import traces.Trace;


public class MainGen {
	
	static int rank = 2;
	static String dir;
	static boolean timerMode;
	static boolean tmp;
	static String dest;
	
	//main entry of the program 
	public static void main(String[] args) throws Exception {
		final long timeProg1 = System.currentTimeMillis();
		KtailOptions.setOptions(args);
		String[] tracesF = getTraces(dir);
		
		//1st part : Trace analysis
		final long timeCor1 = System.currentTimeMillis();
		String s = Extract.analysis(tracesF);
		final long timeCor2 = System.currentTimeMillis();
		
		
		//2nd part : synchronization : grouping
		final long timeClust1 = System.currentTimeMillis();
		sortFile();
		final long timeClust2 = System.currentTimeMillis();
		Group c = new Group(s);
		ArrayList<ArrayList<Trace>> alTraces = c.Synchronization();
		
		
		//3rd part : synchronization : KTail
		final long timeKTail1 = System.currentTimeMillis();
		KTail instance = new KTail(rank);
		int i = 1;
		for (ArrayList<Trace> traces : alTraces) {
			FSA test = instance.transform(traces);
	    	GenerateDOT.printDot(test, MainGen.dest+"/C"+i+"tmp.dot");
	    	i++;
		}
		final long timeKTail2 = System.currentTimeMillis();
		
		//4th part : parser
		final long timePars1 = System.currentTimeMillis();
		
		int j;
		Pattern pat = Pattern.compile(".*C\\d+tmp.dot");
		File dossier = new File(MainGen.dest);
		if (!dossier.exists()) {
		}
		File[] racine = dossier.listFiles();
		for (File dot : racine) {
			String dotStr = dot.toString();
			Matcher m = pat.matcher(dotStr);
			if(m.matches()) {
				j = Integer.parseInt(dotStr.substring(MainGen.dest.length()+2, dotStr.length()-7));
				ParserDot parser = new ParserDot(j);
				String fileName = parser.parser(dot);
				GraphExporter.generatePngFileFromDotFile(fileName);
				if(!tmp) {
					dot.delete();
				}
			}
		}
		final long timePars2 = System.currentTimeMillis();
		
		final long timeProg2 = System.currentTimeMillis();
		if (timerMode) {
			System.out.println("Correlation Duration: " + (timeCor2 - timeCor1) + " ms");
			System.out.println("Clustering Duration: " + (timeClust2 - timeClust1) + " ms");
			System.out.println("KTail Duration: " + (timeKTail2 - timeKTail1) + " ms");
			System.out.println("Parser Duration: " + (timePars2 - timePars1) + " ms");
            System.out.println("Program Duration: " + (timeProg2 - timeProg1) + " ms");
        }	
		
		File x = new File(MainGen.dest+"/RESULTAT.txt");
		if(x.exists()) {
			BufferedWriter br = new BufferedWriter(new FileWriter(x, true));
			br.write("--------------------------------\nTOTAL :\n"+ParserDot.nbEtatTot+" States\n"
				+ParserDot.nbTransitionTot+" Transitions\n"+ "Correlation Duration: " 
				+ (timeCor2 - timeCor1) + " ms\nClustering Duration: " + (timeClust2 - timeClust1) 
				+ " ms\n"+ "KTail Duration: " + (timeKTail2 - timeKTail1) + " ms\nParser Duration: " 
				+ (timePars2 - timePars1) + " ms\n"+ "Program Duration: " + (timeProg2 - timeProg1) + " ms\n");
			br.close();
		}
	}
	
	private static void sortFile() throws IOException {
		File repertoire = new File(dest+"/trace/");
		File[] files = repertoire.listFiles();
		int j = 1;
		for (File f: files) {
			InputStream input = new FileInputStream(f);
			File out = new File(dest+"/trace/T"+j);
			OutputStream output = new FileOutputStream(out);
			IOUtils.copy(input, output);
			f.delete();
			input.close();
			output.close();
			j++;
		}
	}

	/*get list of traces */   
    private static String[] getTraces(String dir){
    	File d = new File(dir);
    	String[] traces = null;
    	if (d.exists()) {
    		if (d.isDirectory()) {
        		traces = d.list();
        		String[] tracesP = new String[traces.length];
        		for(int i = 0; i<traces.length; i++) {
        			tracesP[i] = MainGen.dir+"/"+traces[i];
        		}
        		return tracesP;
    		}
    	}
    	return traces;
    }
}
