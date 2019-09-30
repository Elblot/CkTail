package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DAG {
	public ArrayList<String> nodes;
	public HashSet<String[]> transitions;

	public DAG() {
		nodes = new ArrayList<String>();
		transitions = new HashSet<String[]>();
	}

	public void addNode(String node) {
		nodes.add(node);
	}

	public void addNodes(HashSet<String> node) {
		nodes.addAll(node);
	}

	public void addTransition(String node1, String node2) {
		String[] transition = {node1, node2};
		for (String[] trans: transitions) {
			if (transition[0].equals(trans[0]) && transition[1].equals(trans[1])) {
				return;
			}
		}
		transitions.add(transition);
	}

	public void addTransition(ArrayList<String> dep) {
		for (int i = 0; i < dep.size() - 1; i++) {
			addTransition(dep.get(i), dep.get(i+1));
		}
	}

	public String toString() {
		String res= "";
		return res;
	}

	public void dotGen(String filename) {
		String comp = "";
		try {
			File dot = new File(filename);
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(dot, true));
			bw.write("digraph LTS {\n" + 
					"S00[shape=point]\n");
			for (String compo: nodes) {
				bw.write("S" + nodes.indexOf(compo) +"[label=\"" + compo + "\",shape=circle];\n");
				if (filename.contains("/DAG/" + compo + ".dot")){
					comp = compo;
				}
			}
			bw.write("S00 -> S" + nodes.indexOf(comp) + "\n");
			for (String[] trans: transitions) {
				bw.write("S"+ nodes.indexOf(trans[0]) + " -> S" + nodes.indexOf(trans[1]) + "\n");
			}
			bw.write("}");
			bw.close();
		}catch (FileNotFoundException e) {
			System.out.println("file not found " + e);
		}catch (IOException e){
			System.out.println("error " + e);
		}

		String execCommand = "dot -Tpdf " + comp + ".dot -o " + comp + ".pdf";
		Process dotProcess;
		try {
			dotProcess = Runtime.getRuntime().exec(execCommand);
		} catch (IOException e) {
			System.err.println("Could not run dotCommand '" + execCommand + "': " + e.getMessage());
			return;
		}

		try {
			dotProcess.waitFor();
		} catch (InterruptedException e) {
			System.err.println("Waiting for dot process interrupted '" + execCommand + "': " + e.getMessage());
			return;
		}
	}


}
