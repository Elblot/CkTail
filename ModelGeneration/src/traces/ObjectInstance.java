/*
 *  ObjectJava.java
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
 * Represents an instance / object of a program.
 * 
 * @author Tewfik Ziaidi
 * @author Lom Messan Hillah
 * @author Sylvain Lamprier
 */
public class ObjectInstance extends Element{

	private static final long serialVersionUID = 1L;
	private ObjectClass type;
	
	/**arguments when create this object*/
	private ArrayList<ObjectInstance> variables;
	
	
	
	
	
	/**constructor*/
	public ObjectInstance(String name, ObjectClass classJava){
		super(name);
		this.type = classJava;
		
	}
	
	
	/**
	 * Accesseur retournant le type de l'objet
	 */
	public ObjectClass getObjectClass(){
		return this.type;
	}
	
	/**
	 * Accesseur retournant le nom de l'objet
	 */
	public String getName(){
		return super.getName();
	}
	
	/**
	 * Accesseur modifiant le type de l'objet
	 */
	public void setClassJava(ObjectClass new_class){
		this.type=new_class;
	}
	
	/**
	 * Accesseur modifiant le nom de l'objet
	 */
	public void setName(String new_name){
		super.setName(new_name);
	}
	
	/**
	 * Methode qui renvoit vrai si objectJavaCompare et l'instance courante sont identiques
	 */
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			ObjectInstance object =(ObjectInstance)o;		
			return (this.name.equals(object.getName()))&&(this.type.getName().equals(object.getObjectClass().getName()));
		}
	}
	
	/**
	 * Affiche l'objet sous la forme "type[name]"
	 */
	public String toString(){
		return type.toString()+"["+name+"]";
	}
	
	// j'en fais une autre car je ne sais pas si changer l'autre risque d'entrainer des pbs... 
	// J'ai besoin de la modifier car le toString des statements affiche sender.getName:receiver.getName... alors que ici on affiche l'adresse memoire de l'objet plutot que son nom (pas de toString dans element)
	/*public String toString2(){
		return name +":" + type.toString();
	}*/

	public ArrayList<ObjectInstance> getVars() {
		return variables;
	}

	public void setVars(ArrayList<ObjectInstance> vars) {
		this.variables = vars;
	}
	
	/*public boolean compareArgs(Object o) {
		ObjectInstance obj = (ObjectInstance) o;
		if( this.variables != null && obj.getVars() != null) {
			//System.out.println(this.getArgs()+"***************"+obj.getArgs());
			for(int i=0; i< variables.size() ;i++) {
				if(!this.variables.get(i).equals(obj.getVars().get(i))) {
					//System.out.println(this.getArgs()+"******** a false *******"+obj.getArgs());
					return false;
				}
			}
		} else if (this.variables == null && obj.getVars() != null) {
			//System.out.println("*************** a false of null***************this"+obj.getArgs());
			return false;
		} else if (this.variables != null && obj.getVars() == null) {
			//System.out.println("*************** a false of null ***************obj"+this.args);
			return false;
		}
		
		//System.out.println(this.getArgs()+"&&&&& true &&&&"+obj.getArgs());
		return true;
	}*/
	
	
}
