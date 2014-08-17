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
package r2b2.odm.model;

import java.io.Serializable;

import r2b2.odm.model.base.AhpNode;

/**
 * Allows the weighting of alternatives against goals and parent criteria
 * 
 * @author Alex
 */

public class Criterion extends AhpNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 781126894296556226L;
	/**
	 * The goal or parent criteria
	 */
	private AhpNode parent;

	/**
	 * @param parent
	 * @param name
	 */
	public Criterion(AhpNode parent, String name) {
		super();
		super.setName(name);
		this.parent = parent;
	}

	/**
	 * @return the parent node
	 */
	public AhpNode getParent() {
		return parent;
	}

	/**
	 * Sets the parent node
	 * 
	 * @param parent
	 */
	public void setParent(AhpNode parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.model.base.AhpObject#toString()
	 */
	@Override
	public String toString() {
		// Build path to criterion, eg.
		// Criterion1.Criterion1.1.Criterion1.1.2....

		// Exclude Goal
		if (parent instanceof Goal) {
			return super.getName();
		} else {
			return parent.toString() + "." + super.getName();
		}
	}
}
