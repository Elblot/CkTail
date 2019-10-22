/*
 *  StateNode.java
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


package miners.KTail;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;

import fsa.FSA;
import fsa.State;
import fsa.Transition;

import traces.Statement;
import traces.Trace;
//import fr.lip6.meta.strategie.StatementComparaisonStrategy;

/**
 * 
 * A State that is contained in a tree structure for easier access (used by KTAIL).
 * 
 * @author Sylvain Lamprier
 *
 */
public class StateNode extends State{
	private HashMap<String, StateNode> fils; // Si pas feuille, noeud fils
	//private State state=null;   // Si feuille, contient l'etat
	private Statement statement=null;
	//private HashMap<StateNode, Transition> transitions; 
	private HashMap<String,Transition> transitions;
	private StateNode parent=null;
	
	public StateNode(){   
		fils=new HashMap<String,StateNode>();
		//transitions=new HashMap<StateNode,Transition>();
		transitions=new HashMap<String,Transition>();
		
		//state=new State();
		//state=null;
	}
	public StateNode(Statement s, StateNode parent){
		fils=new HashMap<String,StateNode>();
		statement=s;
		this.parent=parent;
		//state=new State();
		//transitions=new HashMap<StateNode,Transition>();
		transitions=new HashMap<String,Transition>();
	}
	public StateNode getSetStateNode(ArrayList<Statement> s){
		StateNode ret=null;
		if ((s.size()==0)){
			ret=this;
		}
		else{
			Statement statement=s.remove(0);
			//System.out.println("Statement : "+statement);
			String sttext=statement.getText();
			//System.out.println(sttext);
			if (!fils.containsKey(sttext)){
				StateNode sn=new StateNode(statement,this);
				fils.put(sttext,sn);
			}
			if (sttext.length()==0){
				s=new ArrayList<Statement>();
			}
			ret=fils.get(sttext).getSetStateNode(s);
		}
		return(ret);
	}
	/*public void setState(State s){
		state=s;
	}
	public State getState(){
		return(state);
	}*/
	public void addTransition(Statement st,StateNode target){
		Transition tr=new Transition(this,st,target);
		addTransition(tr);
		/*if (!transitions.contains(target)){
			Transition tr=new Transition(this,st,target);
			transitions.put(target, tr);
		}*/
	}
	public void addTransition(Transition tr){
		Transition te=transitions.get(tr.toString());
		if(te!=null){
			te.addWeight(1);
		}
		else{
			transitions.put(tr.toString(),tr);
		}
	}
	/*public HashMap<StateNode,Transition> getTransitions(){
		return(transitions);
	}*/
	public HashSet<Transition> getTransitions(){
		return(new HashSet<Transition>(transitions.values()));
	}
	
	
}

