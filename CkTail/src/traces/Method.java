/*
 *  MethodJava.java
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

/**
 * Represents a method of a program.
 * 
 * @author Tewfik Ziaidi
 * @author Lom Messan Hillah
 * @author Sylvain Lamprier
 *
 */
public class Method extends Element{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Attribut representant les parametres de la methode.
	 * Un parametre est definit de la maniere : type nom_parametre
	 * On cree pour cela un tableau associatif ou la cle est le nom du parametre et sa valeur correspond a son type
	 */
	private ArrayList<ObjectInstance> parameters = new ArrayList<ObjectInstance>();
	/**
	 * Attribut representant le type de retour de la methode
	 */
	private ObjectClass typeRetour;
	
	/**constructor*/
	public Method(String name, ArrayList<ObjectInstance> parametres,ObjectClass typeRetour){
		super(name);
		this.parameters=parametres;
		this.typeRetour=typeRetour;
	}
	public Method(String name){
		this(name,new ArrayList<ObjectInstance>(),null);
	}
	
	public Method(){
		super();
		this.typeRetour = null;
	}
	
	/**
	 * Accesseur retournant le type de retour de la methode
	 */
	public ObjectClass getTypeRetour(){
		return this.typeRetour;
	}
	
	/**
	 * Returns the list of parameters of the method
	 */
	public ArrayList<ObjectInstance> getParameters(){
		return this.parameters;
	}
	

	/**
	 * Returns the name of the method
	 */
	public String getName(){
		return super.getName();
	}
	
	/**
	 * Returns the return class of the method
	 */
	public void setTypeRetour(ObjectClass new_type){
		this.typeRetour=new_type;
	}
	
	/**
	 * Sets the list of parameters of the method
	 */
	public void setParameters(ArrayList<ObjectInstance> new_param){
		this.parameters=new_param;
	}
	
	/**
	 * Set the name of the method
	 */
	public void setName(String new_name){
		super.setName(new_name);
	}
	
	/**
	 * Returns true if o is a method of same name, returning same class of object and owning same parameters
	 */
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			Method method  =(Method)o;		
			boolean listeIdentique = true;
			for(int i=0; i<parameters.size(); i++){
				listeIdentique = listeIdentique && parameters.get(i).equals(method.getParameters().get(i));
			}
			return (this.name.equals(method.getName()))&&(this.typeRetour.equals(method.getTypeRetour()))&&listeIdentique;
		}
		
		
	}
		
	/*
	 * Methode qui affiche la methode
	 */
	public String toString(){
		return this.name +"()";
	}
	
	public int hashCode( ) {
		System.out.println("hash de methode "+name);
		return name.hashCode( );
	}
}
