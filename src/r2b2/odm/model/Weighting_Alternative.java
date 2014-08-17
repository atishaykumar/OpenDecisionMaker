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
import r2b2.odm.model.base.Weighting;

/**
 * Weights two alternatives
 * 
 * @author Alex
 */

public class Weighting_Alternative extends Weighting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3395935009199980121L;
	/**
	 * The parent node against which the two alternatives are weighed
	 */
	AhpNode parentNode;

	/**
	 * Creates a new alternative weighting
	 * 
	 * @param alt1
	 *            Factor1
	 * @param alt2
	 *            Factor2
	 */
	public Weighting_Alternative(AhpNode node, Alternative alt1,
			Alternative alt2) {
		super(alt1, alt2);
		parentNode = node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.model.base.Weighting#getFactor1()
	 */
	@Override
	public Alternative getFactor1() {
		return (Alternative) super.getFactor1();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.model.base.Weighting#getFactor2()
	 */
	@Override
	public Alternative getFactor2() {
		return (Alternative) super.getFactor2();
	}

	/**
	 * @return the parentNode
	 */
	public AhpNode getParentNode() {
		return parentNode;
	}

}
