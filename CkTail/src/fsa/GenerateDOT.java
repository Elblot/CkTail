/*
 *  GenerateDOT.java
 * 
 *  Copyright (C) 2012-2013 Sylvain Lamprier, Tewfik Ziaidi, Lom Messan Hillah and Nicolas Baskiotis
 * 
 *  This file is part of CARE.
 * 
 *   CARE is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CARE is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CARE.  If not, see <http://www.gnu.org/licenses/>.
 */


package fsa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import traces.Method;
import traces.ObjectClass;
import traces.ObjectInstance;
//import traces.Parameter;
import traces.Statement;


/**
 * This class is used to export / import FSA in format supported by Graphviz visualization tool.
 * @author tewfik ziaidi
 * @author sylvain lamprier
 * @author LGY
 **/

public class GenerateDOT {
	
	static PrintStream out;
	
	public GenerateDOT(){}

/**
* Print DOT file
*/
	public static void printDot(FSA lts, String fileName) {

		System.out.println("Ecriture dot file dans "+fileName);
		Hashtable<String, String> names = new Hashtable<String, String>();
		State initialState = lts.getInitialState();
		int num = 1;

		try {

			File dotFile = new File(fileName);
			FileOutputStream fout = new FileOutputStream(dotFile);
			out = new PrintStream(fout);
			char data[] = { '"' };
			String vide = new String(data);

			out.println("digraph LTS {");
			out.println("S00" + "[shape=point]");
			out.println("S00 -> "+initialState.getName());

			for (int i = 0; i < lts.getTransitions().size(); i++) {

				Transition trs = (Transition) lts
						.getTransitions().get(i);
				State source = trs.getSource();
				State target = trs.getTarget();
				String ssName = trs.getSource().getName();
				String SS;

				/*if (source.equals(initialState)) {
					SS = "S0";
					
					
				} else {*/
					if (!names.containsKey(ssName)) {
						names.put(ssName, ssName);
						SS = ssName;
						num = num + 1;
					} else {
						SS = (String) names.get(ssName);
					}
				//}

				String stName = trs.getTarget().getName();
				String TS;
				/*if (target.equals(initialState)) {
					TS = "S0";
				} else {*/
					if (!names.containsKey(stName)) {
						names.put(stName, stName);
						TS = stName;
						num = num + 1;
					} else {
						TS = (String) names.get(stName);
					}
				//}

				Trigger ev = trs.getTrigger();
				String eventLabel;

				if (ev != null)
					eventLabel = trs.getTrigger().toString();
				else
					eventLabel = "";
				//eventLabel += trs;

				/*if(target.getName().equals("S4")){
					System.out.println("target ="+target+" "+target.getAdress());
					State s=lts.getFinalState();
					System.out.println("s ="+s+" "+s.getAdress());
					
				}*/
				
				//System.out.println("source ="+source+";");
				
				if (lts.getFinalStates().contains(source)) {
					//System.out.println("ok source");
					out.println(SS + "[shape=doublecircle];");
				}
				if (lts.getFinalStates().contains(target)) {
					//System.out.println("ok target");
					
					out.println(TS + "[shape=doublecircle];");
				}

				out.println(SS + " -> " + TS + "[label =" + vide + eventLabel
						+ vide + "];");
			}
			out.println("}");
			fout.close();
		} catch (IOException o) {
			o.getStackTrace();
		}
	}

	/**
	 * Read a lts from a dot file
	 **/
	public static FSA readFromDot(String filename) {
		// TODO lack of file format control
		FSA result = new FSA();
		State initialState;
		ArrayList<State> finalStates = new ArrayList<State>();
		Set<Transition> transitions = new HashSet<Transition>();
		
		String line = null;
		String[] sourceTarget = null;
		String[] targetTrigger = null;
		String[] senderReceiverMethod = null;
		String[] receiverMethod = null;// used for long name of a class with
										// many dots

		try {
			File dotFile = new File(filename);
			@SuppressWarnings("resource")
			BufferedReader bf = new BufferedReader(new FileReader(dotFile));

			for (int i = 0; i < 3; i++) {// jump off the first two line
				line = bf.readLine();
			}

			sourceTarget = line.split(" -> ");// handle the initial state
			if (sourceTarget[0].equals("S00")) {
				initialState = new State(sourceTarget[1]);
				result.setInitialState(initialState);
				result.addState(initialState);
			} else {
				System.out.println("Error!!! No initial state!!!");
				return null;
			}

			line = bf.readLine();
			int counter = 4;

			while (!line.equals("}")) {// handle all other lines

				sourceTarget = line.split("\\[shape\\=doublecircle\\]");

				if (sourceTarget.length < 2) {// if not final state
					sourceTarget = line.split(" \\-\\> ");
					if (sourceTarget.length < 2) {// format error
						System.out.println("Error!!! Wrong format!!! in line: "
								+ counter);
						return null;
					} else {
						// source state
						State sourcestate = result.getState(sourceTarget[0]);
						if(sourcestate==null){
							sourcestate=new State(sourceTarget[0]);
							result.addState(sourcestate);
						}
						
						// target state and trigger
						targetTrigger = sourceTarget[1]
								.split("\\[label \\=\"|\\(\\)\"\\];");
						// format error
						if ((targetTrigger == null) || (targetTrigger.length<2)) {
							System.out.println("Error!!! Wrong format!!! in line: "	+ counter);
							return null;
						} else {
							State targetstate = result.getState(targetTrigger[0]);
							if(targetstate==null){
								targetstate=new State(targetTrigger[0]);
								result.addState(targetstate);
							}
							

							// split trigger and sender receiver
							senderReceiverMethod = targetTrigger[1]
									.split("\\:");
							String stmtName = targetTrigger[1];
							String receiverName = null;
							String methodName = null;
							StringBuilder combined = new StringBuilder();

							// sender
							ObjectClass cjs = new ObjectClass(
									senderReceiverMethod[0]);
							ObjectInstance sender = new ObjectInstance(
									senderReceiverMethod[0], cjs);

							// receiver
							receiverMethod = senderReceiverMethod[1]
									.split("\\.");
							for (int i = 0; i < receiverMethod.length; i++) {
								if (i != receiverMethod.length - 1) {
									combined.append(receiverMethod[i]);
									if (i != receiverMethod.length - 2)
										combined.append(".");
								} else {
									methodName = receiverMethod[i];
								}
							}
							receiverName = combined.toString();

							// statement(trigger)
							ObjectClass cjr = new ObjectClass(receiverName);
							ObjectInstance receiver = new ObjectInstance(receiverName, cjr);
							ArrayList<ObjectInstance> paras = new ArrayList<ObjectInstance>();
							Method meth = new Method(methodName, paras,	null);
							Statement stmt = new Statement(stmtName, sender, meth, receiver);

							// construct transition
							Transition trans = new Transition(sourcestate,
									stmt, targetstate);
							if (!transitions.contains(trans)) {
								transitions.add(trans);
							}

						}

					}
				} else {// set final state
					State sourcestate = result.getState(sourceTarget[0]);
					if(sourcestate==null){
						sourcestate=new State(sourceTarget[0]);
						result.addState(sourcestate);
					}
					
					finalStates.add(sourcestate);
					
					//result.setFinalState(finalState);

					
				}

				line = bf.readLine();
				counter++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		result.addTransitions(new ArrayList<Transition>(transitions));
		result.setFinalStates(finalStates);
		
		
		return result;

	}
	
	public static void main(String[] args){
		
		FSA fsa=GenerateDOT.readFromDot("ATM/Model/blocs_sd_model.dot");
		GenerateDOT.printDot(fsa, "ATM/Model/blocs_sd_model2.dot");
	}
}