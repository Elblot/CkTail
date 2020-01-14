package main;

import java.io.File;

public class Main {

	public static String log;
	public static String regex;
	public static String output;
	static boolean timerMode;
	
	public static void main(String[] args) {
		try {
			FullOptions.setOptions(args);
		} catch (Exception e) {
			System.err.println("pb option");
			System.exit(3);
		}
		File dir = new File("tmp");
		dir.mkdir();
		int i = 1;
		String tmp = "tmp/tmp" + i;
		dir = new File(tmp);
		while(dir.exists()) {
			i++;
			tmp = "tmp/tmp" + i;
			dir = new File(tmp);
		}	
		final long timeSplit1 = System.currentTimeMillis();
		String[] argsSplit = {"-i", log, "-r", regex, "-o", tmp};
		main.split.MainSplit.main(argsSplit);
		final long timeSplit2 = System.currentTimeMillis();
		
		final long timeGen1 = System.currentTimeMillis();
		String[] argsGen = {"-i", tmp+"/traces", "-o", output};
		try {
			main.modelGen.MainGen.main(argsGen);
		} catch (Exception e) {
			System.err.println("failed to infer model from traces");
			e.printStackTrace();
		}
		final long timeGen2 = System.currentTimeMillis();

		if (timerMode) {
			System.out.println("Split Duration: " + (timeSplit2 - timeSplit1) + " ms");
			System.out.println("modle Generation Duration: " + (timeGen2 - timeGen1) + " ms");
        }	
	}

}
