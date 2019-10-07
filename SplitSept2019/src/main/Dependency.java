package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Dependency {

	static ArrayList<ArrayList<String>> Dep;
	static HashSet<String> components;


	/*public Dependency(ArrayList<Trace> T) {
		this();
	}*/

	public Dependency() {
		Dep = new ArrayList<ArrayList<String>>();
		components= new HashSet<String>();
	}

	public ArrayList<ArrayList<String>> getDependencies(){
		return Dep;

	}

	public void extend(ArrayList<Event> l, Trace t) {
		ArrayList<String> ld = new ArrayList<String>();
		for (Event e : l) {
			if (!components.contains(e.getFrom())) {
				components.add(e.getFrom());
			}
			if (!components.contains(e.getTo())) {
				components.add(e.getTo());
			}
			ld.add(e.getFrom());
			if (ld.size() == l.size()){
				ld.add(e.getTo());
			}
		}
		if (!Dep.contains(ld)) {
			Dep.add(ld);
		}
		//ld = new ArrayList<String>();
		Event ak = l.get(l.size() - 1);
		Event a = null;
		String comp = ak.getFrom();
		int c = 0;
		for (Event e : t.getSeq()) {
			if (!components.contains(e.getFrom())) {
				components.add(e.getFrom());
			}
			if (!components.contains(e.getTo())) {
				components.add(e.getTo());
			}
			if (comp.equals(e.getTo()) && e.dataSimilarity(ak)) {
				c++; 
				a = e;
			}
		}
		if (c == 1) {
			ld.removeAll(ld);
			ld.add(a.getFrom());
			ld.add(ak.getFrom());
			ld.add(ak.getTo());
			if (!Dep.contains(ld)) {
				Dep.add(ld);
			}
		}
	}

	public String toString() {
		return Arrays.deepToString(Dep.toArray());
	}

	public void add(ArrayList<String> ld) {
		if (!Dep.contains(ld)) {
			Dep.add(ld);
		}
	}

	/* return the dag of dependencies corresponding to the component compo */
	public ArrayList<ArrayList<String>> getDag(String compo){
		ArrayList<ArrayList<String>> dag = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> dep : Dep) {
			for (int i = 0; i < dep.size(); i++) {
				if (compo.equals(dep.get(i))) {
					ArrayList<String> subdep = new ArrayList<String>(dep.subList(i, dep.size()));
					if (!dag.contains(subdep) && subdep.size() > 1 )
						dag.add(subdep);
				}
			}
		}
		return dag;
	}


	/* return all the dags in a String */
	public String getDag() {
		String res = "";
		for (String compo : components) {
			res = res + compo + " : " + Arrays.deepToString(getDag(compo).toArray()) + "\n";
		}
		return res;
	}

	public void buildDAG(String output) {
		File folder = new File(output + "/DAG");
		folder.mkdir();
		for (String comp: components) {
			DAG dag = new DAG();
			dag.addNodes(components);
			for (ArrayList<String> dep : Dep) {
				for (int i = 0; i < dep.size(); i++) {
					if (comp.equals(dep.get(i))) {
						ArrayList<String> subdep = new ArrayList<String>(dep.subList(i, dep.size()));
						if (subdep.size() > 1 ) {
							dag.addTransition(subdep);
						}
					}
				}
			}
			//File dot = new File(output + "/DAG/" + comp + ".dot");
			dag.dotGen(output + "/DAG/" + comp + ".dot");
		}
	}

}
