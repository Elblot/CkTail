/*
 *  StatementsStringifier.java
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

package traces;

/**
 * Abstract class of stringifying strategy for statements.
 * May be used for comparison or displaying purposes.
 * 
 * @author Tewfik Ziaidi
 * @author Sylvain Lamprier
 *
 */
public abstract class StatementsStringifier {
	
	///**
	// * Compares two statements 
	 //* 
	 //* @param st1 
	 //* @param st2
	 //* @return true if statements are equivalent
	 //*/
	/*public boolean compareStatement(Statement st1, Statement st2){
		return(st1.toString().compareTo(st2.toString())==0);
	}*/
    
	/**
	 * Stringifies a statement.
	 * Is used to specify the equivalence level that is expected.
	 * For example if we wish a class level equivalence (no consideration of objects references, only their types), this method should return a string containing the class of the caller, the class of the callee and the name of the method. 
	 * If we rather expect object level comparaisons, this string must additionaly contain the name of the objects 
	 *   
	 * @param st
	 * @return a string representing the statement according to the equivalence level that is expected
	 */
	public abstract String getText(Statement st);
    
	public String toString(){
		return(""+this.getClass());
	}
	
	public abstract Statement getStatement(String s);
}
