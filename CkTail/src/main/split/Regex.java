package main.split;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	//contain every regular expression of the regex file.
	ArrayList<Pattern> alRegex;
	String dateFormat;
	SimpleDateFormat sdf;
	BufferedReader br;

	//constructor
	public Regex(String reg) {
		alRegex = new ArrayList<Pattern>();
		Pattern p = Pattern.compile("-(\\w)\\s(.*)");
		try {
			br = new BufferedReader(new FileReader(new File(reg)));
			String line = br.readLine();
			while(line != null) {
				Matcher m = p.matcher(line);
				if(m.find()) {
					if(m.group(1).equals("r")) {
						if (m.group(2).contains("<label>") && m.group(2).contains("<date>")) {
							alRegex.add(Pattern.compile(m.group(2)));
						}
						else {
							System.err.println("regex must contain a <label> and a <date>");
							System.exit(3);
						}
					}
					if(m.group(1).equals("d")) {
						dateFormat = m.group(2);
					}
				}
				line = br.readLine();
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS", Locale.UK);
	}

	public String getDateFormat() {
		return dateFormat;
	}
	
	public SimpleDateFormat getSDF() {
		return sdf;
	}
	
	public ArrayList<Pattern> getPatterns(){
		return alRegex;
	}
	

}
