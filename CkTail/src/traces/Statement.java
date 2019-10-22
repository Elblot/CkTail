/*
 *  Statement.java
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

import java.util.ArrayList;

import fsa.Trigger;
/**
 * A Statement represents the label of an invocation of a method.
 * Caller:Calle.Method()
 * 
 * @author Tewfik Ziaidi
 * @author Lom Messan Hillah  
 * @author Sylvain Lamprier
 * 
 */

public class Statement extends Trigger{
	
	
	/**
	 * Number of already constructed statements
	 */
	public static int nbreStmt = 0;
	
	public static StatementsStringifier strEquivalence=new Objects_MethodNameStatementStringifier();
	public static StatementsStringifier strStringifier=new Objects_MethodNameStatementStringifier();
	
	
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Caller of the corresponding method
	 */
	private ObjectInstance sender;
	
	
	/**
	 * Callee of the corresponding method
	 */
	private ObjectInstance receiver;
	
	/** 
	 * Method of the statement
	 */
	private Method method;

	/**
	 * Name of the statement
	 */
	private String name;
	
	/**
	 * Corresponding closing statement. (the closing statement is the one that represents the closing of the method called here)
	 */
	private Statement closingStatement=null;
	 
	/**
	 * Is this statement a closing of a method ?
	 */
	private boolean closing=false; // true => C'est un Statement de fin de methode
	
	/**
	 * Trace containing this statement
	 */
	private Trace trace;
	
	/**
	 * constructor without name (an empty name is given to the statement)
	 * */
	public Statement(ObjectInstance sender, Method method, ObjectInstance receiver){
		this("",sender,method,receiver,false);
	}
	
	/**
	 * constructor without name of a closing statement (an empty name is given to the statement)
	 * */
	public Statement(ObjectInstance sender, Method method, ObjectInstance receiver,boolean closing){
		this("",sender,method,receiver,closing);
	}
	
	/**
	 * constructor of a statement
	 * */
	public Statement(String name, ObjectInstance sender, Method method, ObjectInstance receiver){
		this(name,sender,method,receiver,false);
	}
	
	/**
	 * my constructor
	 * 
	 * 
	 */
	public Statement(Method method) {
		this("",null,method,null,false);
	}
	
	/**
	 * constructor of a closing statement
	 * */
	public Statement(String name, ObjectInstance sender, Method method, ObjectInstance receiver,boolean closing){
		this.name = name;
		this.sender=sender;
		this.method=method;
		this.receiver=receiver;
		this.closing=closing;
		nbreStmt++;
		//System.out.println("ici 2");
	}
	
	
	
	/**
	 * empty constructor
	 * calls empty constructors for all components
	 * */
	public Statement(){
		super();
		this.name = ""; //S"+nbreStmt;
		nbreStmt++;
		this.sender= new ObjectInstance("",new ObjectClass(""));
		this.method= new Method("", new ArrayList<ObjectInstance>(), new ObjectClass(""));
		this.receiver= new ObjectInstance("",new ObjectClass(""));
		//System.out.println("ici 3");
	}

	/** 
	 * sets the trace containing this statement
	 * @param trace
	 */
	public void setTrace(Trace trace){
		this.trace=trace;
	}
	
	/**
	 * 
	 * @return the trace containing this statement
	 */
	public Trace getTrace(){
		return(trace);
	}
	
	/**
	 * 
	 * @return true if the statement corresponds to a closing of method
	 */
	public boolean isClosing(){
		return(closing);
	}
	
	/**
	 * 
	 * @return the name of the statement
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return the caller object of the corresponding method invocation
	 */
	public ObjectInstance getSender(){
		return this.sender;
	}
	
	/**
	 * 
	 * @return the callee object of the corresponding method invocation
	 */
	public ObjectInstance getReceiver(){
		return this.receiver;
	}
	
	
	/**
	 * 
	 * @return the method of the statement
	 */
	public Method getMethod(){
		return this.method;
	}

	/**
	 * Allows to change the name of the statement
	 * @param new_name
	 */
	public void setName(String new_name){
		this.name = new_name;
	}
	
	/**
	 * returns true if the stringifying strategy strEquivalence set as a static variable of the class returns the same string for both statements.
	 * 
	 * @return true if o is equivalent to this statement
	 */
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		Statement statement = (Statement)o;
		return(strEquivalence.getText(this).equals(strEquivalence.getText(statement)));
	}

	/**
	 * Stringifies the statement , at the taste of the stringifying strategy strStringifier.
	 *
	 *@return the String representing the statement
	 */
	public String toString(){
		return(strStringifier.getText(this));
	}
	/*public String toString(){
		ObjectInstance caller=getSender();
		ObjectInstance callee=getReceiver();
		Method meth=getMethod();
		String c="";
		if (isClosing()){c="closing_";} 
		String m=meth.toString();
		if (m.compareTo("()")==0){return("");}
		ObjectClass callerclass=caller.getObjectClass();
		ObjectClass calleeclass=callee.getObjectClass();
		return callerclass.getName()+"_"+caller.getName()+":"+calleeclass.getName()+"_"+callee.getName()+"."+c+m;
	}*/

	
	/**
	 * Returns the string representing the statement according to the stringifying strategy strEquivalence set for the class
	 * Used by some miners to represent statements as string in their structures (see for example in miners.StateNode)
	 * @return the string representing the statement
	 */
	public String getText(){
		return(strEquivalence.getText(this));
	}
	
	/*public int compareTo(Statement o) {
		int x=0;
		if (o.isClosing()!=closing){x=1;}
		return ((this.method.getName().compareTo(o.getMethod().getName())) + x );
	}*/
	
	/*public int hashCode() {
		//System.out.print("hash de statement "+name);
		//System.out.println(toString());
		String n="";
		if ((method!=null) && (method.getName()!=null)){
			n=method.getName();
		}
		return n.hashCode( );
	}*/

	/**
	 * 
	 * 
	 * @param closing determines whether the copy should be a closing of method or not
	 * @return a copy of the statement 
	 */
	public Statement getCopy(boolean closing){
		Statement ret=null;
		if (closing){
		   ret=new Statement(this.name+"_closing",this.sender,this.method,this.receiver,closing);
		}
		else{
			ret=new Statement(this.name,this.sender,this.method,this.receiver,closing);
		}
		return(ret);
	}
	
	
	/**
	 * Returns the statement corresponding to the closing of the represented method invocation. 
	 * If closingStatement is not defined, sets it by using getCopy(true)
	 * 
	 * @return the closing statement corresponding to this statement
	 */
	public Statement getClosingStatement(){
		if (closingStatement==null){
			closingStatement=getCopy(true);
		}
		return(closingStatement);
	}
	
	/**
	 * sets this statement as a closing of method invocation 
	 */
	public void setIsClosing(){
		this.closing = true;
	}
}
	