/**
 * 
 * @author Bender, Blocherer, Rossmehl and Rotter
 * 
 *         This file is part of Open Decision Maker.
 * 
 *         Open Decision Maker is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU General Public
 *         License as published by the Free Software Foundation, either
 *         version 3 of the License, or (at your option) any later version.
 * 
 *         Open Decision Maker is distributed in the hope that it will be
 *         useful, but WITHOUT ANY WARRANTY; without even the implied
 *         warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *         See the GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with Open Decision Maker. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
package r2b2.odm.gui.components;

import java.util.EventObject;

import r2b2.odm.model.Criterion;

/**
 * An event that signals that the weighting of a criterion has been changed in
 * the gui.
 * 
 * @author Alex
 * 
 */
public class SaCriterionWeightingChangedEvent extends EventObject {

	Criterion criterion;
	int criterionIndex;
	double newValue;

	/**
	 * 
	 */

	private static final long serialVersionUID = -5966905079772099538L;

	/**
	 * @param source
	 *            the source object
	 * @param c
	 *            the criterion
	 * @param cX
	 *            the Criterion Index
	 * @param newValue
	 *            the new weighting Value
	 */
	public SaCriterionWeightingChangedEvent(Object source, Criterion c, int cX,
			double newValue) {
		super(source);
		this.criterion = c;
		this.criterionIndex = cX;
		this.newValue = newValue;
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	/**
	 * @param c
	 *            the criterion to set
	 */
	public void setCriterion(Criterion c) {
		this.criterion = c;
	}

	/**
	 * @return the criterionIndex
	 */
	public int getCriterionIndex() {
		return criterionIndex;
	}

	/**
	 * @param cX
	 *            the criterionIndex to set
	 */
	public void setCriterionIndex(int cX) {
		this.criterionIndex = cX;
	}

	/**
	 * @return the newValue
	 */
	public double getNewValue() {
		return newValue;
	}

	/**
	 * @param diff
	 *            the newValue to set
	 */
	public void setNewValue(double diff) {
		this.newValue = diff;
	}

}
