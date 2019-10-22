/*
 *  Transition.java
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
import java.math.BigInteger;
import java.util.ArrayList;


/**
 * 
 * Represents a transition of the automaton
 * 
 * @author Sylvain Lamprier
 * @author Lom Messan Hillah
 * @author Tewfik Ziaidi
 * 
 */
public class Transition implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Source state of the transition
	 */
	private State sourceState; 
	
	/**
	 * Target state of the transition
	 */
	private State targetState;
	/*public static int nbTr=0;
	private int id;*/
	
	/**
	 * Object of the transition (typically a statement)
	 */
	private Trigger trigger;
	
	private double weight=1; // only for pfsa
	
	
	private ArrayList<BigInteger> transit=null; // Pour FSA.computeUnifDistribution  
	
	//Constructor
	public Transition(State sourceState, Trigger trigger, State targetState){
		this.sourceState = sourceState;
		this.trigger = trigger;
		this.targetState = targetState;
	}
	public Transition(Transition t){
		this(t.sourceState, t.trigger, t.targetState);
		weight=t.weight;
	}
	
	public void setTransit(ArrayList<BigInteger> transit){
		this.transit=transit;
	}
	public ArrayList<BigInteger> getTransit(){
		return(transit);
	}
	public BigInteger getTransit(int i){
		return(transit.get(i));
	}
	/**
	 * Returns the source state
	 */
	public State getSource(){
		return this.sourceState;
	}
	
	/**
	 * Returns the target state
	 */
	public State getTarget(){
		return this.targetState;
	}
	
	/**
	 * Accesseur retournant le nom de la transition 
	 */
	public Trigger getTrigger(){
		return trigger;
	}
	
	/**
	 * Sets the source state
	 */
	public void setSourceState(State new_source_state){
		this.sourceState = new_source_state;
	}
	
	/**
	 * Sets the target state
	 */
	public void setTargetState(State new_target_state){
		this.targetState = new_target_state;
	}
	
	/**
	 * Sets the label of the transition 
	 */
	public void setTrigger(Trigger new_trigger){
		this.trigger = new_trigger;
	}
	
	/**
	 * Gets a string representing the label of the transition
	 * @return a string representation of the label of the transition
	 */
	public String getLabel(){
		
		  return trigger.toString();	
	}

	/*
	 * Methode qui retourne une transition sous la forme d'une chaine de caractere
	 * A REVOIR
	 */
	public String toString(){
		return  "(" + sourceState + ")"
				+ "--" + trigger + "-->" 
				+ "(" +  targetState +")" ;
	}
	
	@Override
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			Transition t = (Transition)o;
			return ((this.sourceState.equals(t.getSource())) && (this.trigger.equals(t.getTrigger())) && (this.targetState.equals(t.getTarget())));
		}
	}

	public int hashCode() {
		return(this.toString().hashCode());
	}
		
	public void setWeight(double w){
		weight=w;
	}
	
	public void addWeight(double w){
		weight+=w;
	}
	
	public double getWeight(){
		return weight;
	}
}
