package main;

import java.util.ArrayList;
import java.util.Arrays;

public class Dependency {

	static ArrayList<ArrayList<String>> Dep;
	static ArrayList<String> components;


	/*public Dependency(ArrayList<Trace> T) {
		this();
	}*/

	public Dependency() {
		Dep = new ArrayList<ArrayList<String>>();
		components= new ArrayList<String>();
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
	}

	public String toString() {
		return Arrays.deepToString(Dep.toArray());
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


}
