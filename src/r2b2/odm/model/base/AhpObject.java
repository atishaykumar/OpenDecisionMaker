/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Bender, Blocherer, Rossmehl and Rotter
 * 
 *         This file is part of Open Decision Maker.
 * 
 *         Open Decision Maker is free software: you can redistribute it and/or
 *         modify it under the terms of the GNU General Public License as
 *         published by the Free Software Foundation, either version 3 of the
 *         License, or (at your option) any later version.
 * 
 *         Open Decision Maker is distributed in the hope that it will be
 *         useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 *         of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with Open Decision Maker. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
package r2b2.odm.model.base;

import java.io.Serializable;

/**
 * Base class for the main objects in AHP
 * 
 * @author Alex
 */

public abstract class AhpObject implements Serializable {

	private static final long serialVersionUID = 5412564785759451497L;

	/**
	 * The id of the object
	 */
	private int id;

	/**
	 * The name of the object
	 */
	private String name;

	/**
	 * The description of the object
	 */
	private String description = "";

	/**
	 * @return <code>true</code> if the id of the object has been set
	 */
	public boolean hasId() {
		return (id != 0);
	}

	/**
	 * Get the Id
	 * 
	 * @return Id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param value
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
