/*
 *  State.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import traces.Element;

/**
 * Classe qui represente un etat dans un automate
 * @author Neko
 * @author LGY
 */
public class State extends Element implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private static int nb_states=0;
	/*
	 * On definit un eventuel identifiant pour chaque etat sui representera sa position dans une trace
	 */
	//private int id;
	
	// lamprier : accepte quoi ?
	private boolean accept;
	
	// lamprier : NFA => Automate non deterministe ? A quoi correspond nfaStates ? A quoi cet attribut sert-il ?
	private Set<State> nfaStates = new LinkedHashSet<State>() ; 
	
	
	private HashMap<Integer,Integer> scores_sous_traces=new HashMap<Integer,Integer>(); // Utile pour FSA.accepteSousTrace();
	
	private Set<Transition> successeurs;
	private Set<Transition> predecesseurs;
	
	private String label;
	
	private HashSet<State> sureFuture=null; // states atteints forcement par ce state
	private HashSet<State> future=null; // states possibement atteints par ce state
	
	public static int nbManipSeq=0;
	public static int nbVisitsStates=0;
	
	//Constructeur
	public State(String name){
		super(name);
		nb_states++;
		successeurs=new LinkedHashSet<Transition>();
		predecesseurs=new LinkedHashSet<Transition>();
	}
	
	
	/*
	 * Constructeur dont le nom par defaut est "S" suivi du nombre de states deja crees
	 */
	public State() {
		super("S"+(nb_states+1));
		nb_states++;
		successeurs=new LinkedHashSet<Transition>();
		predecesseurs=new LinkedHashSet<Transition>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addSuccesseur(Transition t){
		if(t.getSource()!=this){
			throw new RuntimeException("add transition "+t+" as successor of "+this+" is problematic");
		}
		//if(successeurs.contains(t)){System.out.println("already contains "+t);}
		successeurs.add(t);
	}
	public void addPredecesseur(Transition t){
		if(t.getTarget()!=this){
			throw new RuntimeException("add transition "+t+" as predecessor of "+this+" is problematic");
		}
		predecesseurs.add(t);
	}
	public void removeSuccesseur(Transition t){
		successeurs.remove(t);
	}
	public void removePredecesseur(Transition t){
		predecesseurs.remove(t);
	}
	
	public void clearSuccesseurs(){
		successeurs=new LinkedHashSet<Transition>();
	}
	public void clearPredecesseurs(){
		predecesseurs=new LinkedHashSet<Transition>();
	}
	
	public ArrayList<Transition> getSuccesseurs(){
		return(new ArrayList<Transition>(successeurs));
	}
	public ArrayList<Transition> getPredecesseurs(){
		return(new ArrayList<Transition>(predecesseurs));
	}
	public ArrayList<Transition> getSuccesseurs(Trigger t){
		ArrayList<Transition> ret=new ArrayList<Transition>();
		for(Transition tr:successeurs){
			if (tr.getTrigger().equals(t)){
				ret.add(tr);
			}
		}
		return(ret);
	}
	public ArrayList<Transition> getPredecesseurs(Trigger t){
		ArrayList<Transition> ret=new ArrayList<Transition>();
		for(Transition tr:predecesseurs){
			if (tr.getTrigger().equals(t)){
				ret.add(tr);
			}
		}
		return(ret);
	}
	/*
	 * Accesseur retournant l'identifiant
	 */
	/*public int getId(){
		return this.id;
	}*/
	
	/*
	 * Accesseur retournant le nom de l'etat
	 */
	public String getName(){
		return super.getName();
	}
	
	/*
	 * Accesseur modifiant le nom de l'etat
	 */
	public void setName(String new_name){
		super.setName(new_name);
	}
	
	/*
	 * Methode qui renvoit vrai si l'instance courante et celle passee en parametre sont egales
	 */
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			State state = (State)o;
			
		//System.out.println(name+" "+state.getName());
		return this.name.equals(state.getName());
		}
	}
	
	/*
	 * Methode qui retourne le nom sous la forme d'une chaine de caractere
	 */
	public String toString(){
		
		return  name;
	}
	
	
	
	
	
	public void considerFuture(State from,HashSet<State> f,FSA lts){
		//nbVisitsStates++;
		boolean change=false;
		if (future==null){
			change=true;
			future=new HashSet<State>();
			future.addAll(f);
			if (from!=null){
				future.add(from);
			}
		}
		else{
			if (from!=null){
				if (!future.contains(from)){
					future.add(from);
					change=true;
				}
			}	
			for(State st:f){
				if (!future.contains(st)){
					future.add(st);
					change=true;
				}
			}
		}
		if (change){
			ArrayList<Transition> trs=this.getPredecesseurs();
			for(Transition tr:trs){
				State source=tr.getSource();
				source.considerFuture(this, future, lts);
			}
		}
	}
	public HashSet<State> getFutures(){
		return(future);
	}
	public void setSureFuture(HashSet<State> f){
		sureFuture=f;
	}
	/*public void considerSureFuture(State from,HashSet<State> f,LTS lts){
		boolean change=false;
		if (sureFuture==null){
			change=true;
			sureFuture=new HashSet<State>();
			if (!lts.isFinalState(this)){
				sureFuture.addAll(f);
				if (from!=null){
					sureFuture.add(from);
				}
			}
		}
		else{
			HashSet<State> old_sureFuture=sureFuture;
			sureFuture=new HashSet<State>();
			for(State st:old_sureFuture){
				if ((st.equals(from)) || (f.contains(st))){
					sureFuture.add(st);
				}
				else{
					change=true;
				}
			}
		}
		if (change){
			ArrayList<Transition> trs=this.getPredecesseurs();
			for(Transition tr:trs){
				State source=tr.getSource();
				source.considerSureFuture(this, sureFuture, lts);
			}
		}
	}*/
	public boolean isInSureFuture(State state){
		return(sureFuture.contains(state));
	}
	public HashSet<State> getSureFutures(){
		return(sureFuture);
	}
	public void removeSureFuture(State state){
		sureFuture.remove(state);
	}
	

	public void setNfaStates(State[] states) {
		for (int i=0; i < states.length; i ++){ 
			State state = (State)states[i];
			if (state != null ) nfaStates.add(state);
		}
	}
	
	
	public State[] getNfaStates(){
		
		return (State[]) nfaStates.toArray(new State[0]);
	}

	/*public void setId(int i) {
		id = i ;
	}*/

	/*public int compareTo(State o) {
		return //Integer.valueOf(id).compareTo(Integer.valueOf(o.getId()));
		
	}*/
	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	/*@Override
	public int hashCode() {
		
		return this.toString().hashCode();
	}*/
	
	public void reinitScoresSousTrace(){
		scores_sous_traces=new HashMap<Integer,Integer>();
	}
	
	public int getScoreSousTrace(int size){
		//Integer s=new Integer(size);
		if(scores_sous_traces.containsKey(size)){
			return(scores_sous_traces.get(size));
		}
		else{
			return(-1);
		}
	}
	public void setScoreSousTrace(int size,int score){
		//Integer s=new Integer(size);
		//Integer sco=new Integer(score);
		scores_sous_traces.put(size,score);
	}
		
}