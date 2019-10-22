/*
 *  ClassJava.java
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
 * Represents a program Class.
 * 
 * @author Tewfik Ziaidi
 * @author Lom Messan Hillah
 * @author Sylvain Lamprier
 * 
 */
public class ObjectClass extends Element{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**constructor*/
	public ObjectClass(String name){
		super(name);
	}
	
	/**
	 * Accesseur retournant le nom de la classe
	 */
	public String getName(){
		return super.getName();
	}
	
	/**
	 * Accesseur modifiant le nom de la classe
	 */
	public void setName(String new_name){
		super.setName(new_name);
	}
	

	/**
	 * Methode qui renvoit vrai si classJavaCompare et l'instance courante sont identiques
	 */
	public boolean equals(Object o){
		
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			ObjectClass classJava = (ObjectClass) o;
			return this.name.equals(classJava.getName());
		}
		
	}
	
	/**
	 * Methode qui affiche la classe
	 */
	public String toString(){
		return this.name;
	}
	
}
