package main.modelGen;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class KtailOptions {
	
	public static void setOptions(String[] args) throws Exception {
		final Options options = configParameters();
	    final CommandLineParser parser = new DefaultParser();
	    try {
		    final CommandLine line = parser.parse(options, args);
		    
		    MainGen.dir = line.getOptionValue("input");
		    MainGen.dest = line.getOptionValue("dest");
		   		    
		    // Timer
		    boolean timerMode = line.hasOption("timer");
		    if(timerMode) {
		    	MainGen.timerMode = true;
		    }
		    
		    boolean tmp = line.hasOption("tmp");
		    if(tmp) {
		    	MainGen.tmp = true;
	    }
	    }catch(Exception e) {
	    	System.out.println("Usage : MainC -i <directory> -o <destination>\n"
	    			+ "Options :\n"
	    			+ "-t\tshow the duration of each step of the program\n"
	    			);  
	    	System.exit(1);}
	}
	
	private static Options configParameters() {
	
		final Option dirFileOption = Option.builder("i")
				.longOpt("input")
				.desc("directory to use")
				.hasArg(true)
				.argName("input")
				.required(true)
				.build();
	
	    final Option timerFileOption = Option.builder("t") 
	            .longOpt("timer") 
	            .desc("Timer") 
	            .hasArg(false) 
	            .required(false) 
	            .build();
	    
	    final Option tmpOption = Option.builder("w")
				.longOpt("tmp")
				.desc("show temporal file used to create .dot files")
				.hasArg(false)
				.argName("tmp")
				.required(false)
				.build();
	    
	    final Option destinationOption = Option.builder("o")
				.longOpt("dest")
				.desc("set the name of the directory where files will be placed")
				.hasArg(true)
				.argName("dest")
				.required(true)
				.build();
	
	    final Options options = new Options();
	
	    options.addOption(dirFileOption);
	    options.addOption(timerFileOption);
	    options.addOption(tmpOption);
	    options.addOption(destinationOption);
	
	    return options;
	}
}
