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
 * Simple object representing the goal of an ahp process.
 * 
 * @author Alex
 */

public class Goal extends AhpNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5203859434093192421L;

	/**
	 * Creates a new goal object
	 * 
	 * @param name
	 *            The name of the goal
	 */
	public Goal(String name) {
		super();
		super.setName(name);
	}

}
