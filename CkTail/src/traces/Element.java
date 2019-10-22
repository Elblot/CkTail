/*
 *  Element.java
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

import java.io.Serializable;

/**
 * Represents an element of a program.
 * 
 * @author Tewfik Ziaidi
 * @author Lom Messan Hillah
 * 
 * 
 */
public class Element implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Attribut protege representant le nom de l'element
	 */
	protected String name;
	
	//Constructeur protege
	protected Element(String name){
		this.name = name;
	}
	
	protected Element(){
		super();
		this.name = null;
	}
	
	/**
	 * Accesseur protege retournant le nom de l'Element
	 */
	protected String getName(){
		return name;
	}
	
	public String getAdress(){
		return super.toString();
	}
	
	/**
	 * Accesseur protege modifiant le nom de l'Element
	 */
	protected void setName(String new_name){
		this.name=new_name;
	}
	
	/**
	 * Returns true if both elements have the same name
	 */
	public boolean equals(Object o){
		if(o==this) return true;
		else if((o==null) || (o.getClass() != this.getClass()))return false;
		else{
			Element object =(Element)o;		
			return (this.name.equals(object.getName()));
		}
	}

	public String toString(){
		return(name);
	}
}
