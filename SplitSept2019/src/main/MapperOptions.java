package main;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;


public class MapperOptions {
	
	public static void setOptions(String[] args) throws Exception {
		final Options options = configParameters();
	    final CommandLineParser parser = new DefaultParser();
	    try {
		    final CommandLine line = parser.parse(options, args);
		    	    	
		    Main.log = line.getOptionValue("input");
		    Main.regex = new Regex(line.getOptionValue("reg"));	 
		    Main.output = line.getOptionValue("output");
		    
		    
		    // Timer
		    boolean timerMode = line.hasOption("timer");
		    if(timerMode) {
		    	Main.timerMode = true;
		    }
		    
		   /* boolean selec = line.hasOption("selec");
		    if(selec) {
		    	Main.selec = true;
		    }*/
		    
		    /*boolean trace = line.hasOption("arg");
		    if(trace) {
		    	Main.trace = true;
		    }*/
	    
	    }catch(Exception e) {
	    	System.out.println("Usage : Main -i <input> -r <regex> -o <output>\n"
	    			+ "-i/-input : input file to analyse\n"
	    			+ "-r/-regex : regex file to use on the input file\n"
	    			+ "-o/-output : name of the output directory\n"
	    			+ "Options :\n"
	    			+ "-t\tshow the duration of each step of the program\n"
	    			//+ "-s\twrite non-matched lines on an other file\n"
	    			//+ "-a\t remove the value of every arg on each line\n"
	    			);
	    	System.exit(1);}
	}
	
	
	private static Options configParameters() {
	
		final Option dirFileOption = Option.builder("i")
				.longOpt("input")
				.desc("file to use")
				.hasArg(true)
				.argName("input")
				.required(true)
				.build();
		
		final Option regexOption = Option.builder("r")
				.longOpt("reg")
				.desc("regex to use")
				.hasArg(true)
				.argName("reg")
				.required(true)
				.build();
	
	    final Option timerFileOption = Option.builder("t") 
	            .longOpt("timer") 
	            .desc("Timer") 
	            .hasArg(false) 
	            .required(false) 
	            .build();
	    
	    final Option outputOption = Option.builder("o")
				.longOpt("output")
				.desc("output file")
				.hasArg(true)
				.argName("output")
				.required(true)
				.build();
	    
	    /*final Option selecOption = Option.builder("s") 
	            .longOpt("selec") 
	            .desc("write non-matched files in an other file") 
	            .hasArg(false) 
	            .required(false) 
	            .build();
	    
	    final Option traceOption = Option.builder("a") 
	            .longOpt("arg") 
	            .desc("remove the value of every arg on each line") 
	            .hasArg(false) 
	            .required(false)
	            .build();*/
	
	    final Options options = new Options();
	
	    options.addOption(dirFileOption);
	    options.addOption(regexOption);
	    options.addOption(timerFileOption);
	    options.addOption(outputOption);
	    //options.addOption(selecOption);
	    //options.addOption(traceOption);

	
	    return options;
	}
}
