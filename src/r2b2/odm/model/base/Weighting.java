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
 * Base class for the two types of weightings
 * 
 * @author Alex
 */

public abstract class Weighting implements Serializable {

	private static final long serialVersionUID = -1396463643901905320L;

	protected AhpObject factor1;
	protected AhpObject factor2;
	/**
	 * The value of the weighting. The default (invalid) value of 0.0 can be
	 * used to identify if a weighting has already been performed.
	 */
	protected double value = 0.0;

	/**
	 * @param factor1
	 * @param factor2
	 */
	public Weighting(AhpObject factor1, AhpObject factor2) {
		this.factor1 = factor1;
		this.factor2 = factor2;
	}

	/**
	 * Get the first factor of the weighting
	 * 
	 * @return first weighting factor
	 */
	public AhpObject getFactor1() {
		return factor1;
	}

	/**
	 * Get the second factor of the weighting
	 * 
	 * @return second weighting factor
	 */
	public AhpObject getFactor2() {
		return factor2;
	}

	/**
	 * Get the weighting value
	 * 
	 * @return weighting value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Set the weighting value
	 * 
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}
}
