/*
 *  Trace.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;


//import fr.lip6.meta.traceGenerator.table.*;
//import fr.lip6.meta.traceGenerator.util.TraceIO;
/**
 * Represents an execution trace.
 * A trace is defined as a list of statements
 *  
 * @author Tewfik Ziaidi
 * @author Lom Messan Hillah  
 * @author Sylvain Lamprier
 * 
 */
public class Trace implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Statements of the trace
	 */
	private ArrayList<Statement> statements;
	
	//private TraceGenResult trg;
	
	
	/**constructor*/
	public Trace(){
		this.statements = new ArrayList<Statement>();
	}
	
	/**
	 * Adds a statement to the trace.
	 * 
	 * @param st 	The statement to add
	 */
	public void add(Statement st){
		this.statements.add(st); //On ajoute le statement
		st.setTrace(this);
	}

	/**
	 * Removes the statement at position id in the trace.
	 * 
	 * @param id	position in the trace of the statement to remove (with the first statement at index 0)
	*/
	public void removeStatement(int id){
		this.statements.remove(id);
	}
	
	
	/*public void setTraceGenResult(TraceGenResult trg){
		this.trg=trg;
		
	}*/
	
	/*public TraceGenResult getTraceGenResult()
	{
		return(trg);
	}*/
	
	/**
	 * Returns the statement of index id-1. (prefer the getStatement method)
	 * 
	 * @param id	position in the trace
	 * @return		the statement that is located at position id-1 in the trace (first index is 0)
	 */
	public Statement getByIndex(int id){
		return statements.get(id-1);
	}
	
	/**
	 * Returns the statement of index id. 
	 * 
	 * @param id	position in the trace
	 * @return		the statement that is located at position id in the trace (first index is 0)
	 */
	public Statement getStatement(int id){
		return statements.get(id);
	}
	
	/**
	 * Accesseur retournant la trace
	 */
	/*public Hashtable<Integer,Statement> getTrace(){
		return this.trace;
	}*/
	
	/**
	 * Returns the list of statements of the trace
	 * 
	 * @return The list of statements of the trace
	 */
	public ArrayList<Statement> getStatements() {
		return statements;
	}
	
	
	
	
	/**
	 * Returns the number of statements in the trace
	 * 
	 * @return the number of statements in the trace
	 */
	public int getSize(){
		return statements.size();
	}
	
	
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(Statement st:statements){
			sb.append(st.toString()).append("\n");
		}
		return sb.toString();
	}
	
	
	/**
	 * Returns true if the object given as argument contains equivalent statements in the same order.
	 * Equivalence between statements depends on the {@link StatementsStringifier} equivalence strategy set as a static variable of the class Statement  
	 *
	 * @param o		the trace to be compared with
	 * @see StatementsStringifier
	 */
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			Trace trace = (Trace) o;
			
			boolean egal = true;
			for(int i=0; i<trace.getSize(); i++){
				if (! statements.get(i).equals(trace.statements.get(i))){
					egal=false;
					break;
				}
				//egal = egal && && identifiants.get(i).equals(trace.getIdentifiants().get(i));
			}
			return egal;
		}
	}

	public void setStatements(ArrayList<Statement> statements) {
		this.statements = statements;
	}

	/*public void setIdentifiants(ArrayList<Integer> identifiants) {
		this.identifiants = identifiants;
	}*/
	
	public void serialize(String fileName) throws IOException{
		
			// ouverture d'un flux de sortie vers le fichier "personne.serial"
			FileOutputStream fos = new FileOutputStream(fileName);
	
			// creation d'un "flux objet" avec le flux fichier
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			try {
				// serialisation : ecriture de l'objet dans le flux de sortie
				oos.writeObject(this); 
				
				// on vide le tampon
				oos.flush();
				//System.out.println("la trace a ete serialisee");
			} finally {
				//fermeture des flux
				try {
					oos.close();
				} finally {
					fos.close();
				}
			}
		
	}
	
	public static Trace deserialize(String fileName) throws IOException{
		Trace ret=null;
		try{
			
			// ouverture d'un flux d'entr�e depuis le fichier "personne.serial"
			FileInputStream fis = new FileInputStream(fileName);
			// creation d'un "flux objet" avec le flux fichier
			ObjectInputStream ois= new ObjectInputStream(fis);
			try {	
				// deserialisation : lecture de l'objet depuis le flux d'entree
				ret = (Trace) ois.readObject(); 
			} finally {
				// on ferme les flux
				try {
					ois.close();
				} finally {
					fis.close();
				}
			}
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
			throw new IOException("Probleme classe");
		}
		return(ret);
	}
	
	public void save(String filename) {
        try{
          PrintStream p = new PrintStream(filename) ;
          for(Statement st : statements) {
            p.println(st);
          }
          p.close();
        }
        catch(IOException e){
        	System.out.println("Probleme sauvegarde modele "+filename);
        }
    }
	
	public static ArrayList<Trace> getTracesFromDir(String repertoire){
		ArrayList<Trace> traces=new ArrayList<Trace>();
		try{
			File rep=new File(repertoire);
			File[] files=rep.listFiles();
			for(int i=0;i<files.length;i++){
			   if (files[i].getName().indexOf(".trace")>0){
				   //System.out.println(files[i].getName());
				   Trace trace=deserialize(files[i].getAbsolutePath());
				   traces.add(trace);
			   }
			}
		}
		catch(IOException e){
			   System.out.println(e.getMessage());
		}
		return(traces);
	}
		
}
