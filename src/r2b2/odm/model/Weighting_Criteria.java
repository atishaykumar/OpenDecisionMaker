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

import r2b2.odm.model.base.Weighting;

/**
 * Weights two criteria
 * 
 * @author Alex
 */

public class Weighting_Criteria extends Weighting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5880888581733705129L;

	/**
	 * Creates a new criteria weighting
	 * 
	 * @param crit1
	 *            Factor1
	 * @param crit2
	 *            Factor2
	 */
	public Weighting_Criteria(Criterion crit1, Criterion crit2) {
		super(crit1, crit2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.model.base.Weighting#getFactor1()
	 */
	public Criterion getFactor1() {
		return (Criterion) super.getFactor1();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.model.base.Weighting#getFactor2()
	 */
	public Criterion getFactor2() {
		return (Criterion) super.getFactor2();
	}

}
