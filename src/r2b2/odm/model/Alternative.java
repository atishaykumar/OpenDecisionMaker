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

import r2b2.odm.model.base.AhpObject;

/**
 * A possible alternative to achvieving a goal
 * 
 * @author Alex
 */

public class Alternative extends AhpObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3204063149460775885L;
	/**
	 * The goal that is to be achieved
	 */
	private Goal goal;

	/**
	 * @param goal
	 * @param name
	 */
	public Alternative(Goal goal, String name) {
		super();
		super.setName(name);
		this.goal = goal;
	}

	/**
	 * Returns the goal
	 * 
	 * @return goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * Sets the goal
	 * 
	 * @param parent
	 */
	public void setGoal(Goal parent) {
		this.goal = parent;
	}
}
