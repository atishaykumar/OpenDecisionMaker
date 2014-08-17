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
import java.security.InvalidParameterException;
import java.util.ArrayList;

import r2b2.odm.model.Criterion;
import r2b2.odm.model.util.Matrix;

/**
 * A node in the structure of AHP. Holds information about underlying nodes and
 * their weightings.
 * 
 * @author Alex
 * 
 */

public class AhpNode extends AhpObject implements Serializable {

	private static final long serialVersionUID = -532779057677031828L;

	/**
	 * The subcriteria of this node
	 */
	ArrayList<Criterion> criteria;

	/**
	 * The weightings, which can be either of the subcriteria or the
	 * alternatives against this criteria
	 */
	ArrayList<Weighting> weightings;

	/**
	 * The matrix created from the weightings
	 */
	Matrix weightingMatrix;

	/**
	 * The Consistency Ratio of the weightings
	 */
	double consistencyRatio;

	/**
	 * A cache for the values of the alternatives added up to this node
	 */
	double[] alternativeValues;

	private double[] criteriaWeighting;

	private double[] eigenvector;

	/**
	 * <code>true</code> if this node has subcriteria
	 * 
	 * @return boolean if subcriteria exist
	 */
	public Boolean hasSubCriteria() {
		if (getCriteria().size() == 0)
			return false;
		else
			return true;
	}

	/**
	 * @return the consistencyRatio
	 */
	public double getConsistencyRatio() {
		return consistencyRatio;
	}

	/**
	 * @param consistencyRatio
	 *            the consistencyRatio to set
	 */
	public void setConsistencyRatio(double consistencyRatio) {
		this.consistencyRatio = consistencyRatio;
	}

	/**
	 * @return the criteria
	 */
	public ArrayList<Criterion> getCriteria() {
		if (criteria == null) {
			criteria = new ArrayList<Criterion>();
		}
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(ArrayList<Criterion> criteria) {
		this.criteria = criteria;
	}

	/**
	 * Can be either the criteria weightings of the subcriteria, or the
	 * alternative weightings if the node has no subcriteria.
	 * 
	 * @return the weightings
	 */
	public ArrayList<Weighting> getWeightings() {
		if (weightings == null) {
			weightings = new ArrayList<Weighting>();
		}
		return weightings;
	}

	/**
	 * @param weightings
	 *            the weightings to set
	 */
	public void setWeightings(ArrayList<Weighting> weightings) {
		this.weightings = weightings;
	}

	/**
	 * @return the weightingMatrix
	 */
	public Matrix getWeightingMatrix() {
		return weightingMatrix;
	}

	/**
	 * @param weightingMatrix
	 *            the weightingMatrix to set
	 */
	public void setWeightingMatrix(Matrix weightingMatrix) {
		this.weightingMatrix = weightingMatrix;
	}

	/**
	 * The weighting of the sub-criteria on their node-level
	 * 
	 * @param recalculate
	 * 
	 * @return weightings sub criteria
	 */
	public double[] getCriteriaWeighting(Boolean recalculate) {
		if (!this.hasSubCriteria()) {
			throw new InvalidParameterException(
					"A node without sub-criteria cannot have criteria weightings");
		}
		if ((criteriaWeighting == null) || recalculate) {
			criteriaWeighting = weightingMatrix.getEigenvectorFinal();
		}

		return criteriaWeighting;
	}

	/**
	 * Returns the values of the alternatives added up to this node.
	 * 
	 * @param recalculate
	 *            <p>
	 *            If <code>true</code>, the values for each alternative for each
	 *            sub-node will be recalculated and returned. This also updates
	 *            the cached values.
	 *            </p>
	 *            <p>
	 *            If <code>false</code>, existing cached values for the
	 *            alternatives will be returned.
	 *            </p>
	 * @return values of alternatives
	 */
	public double[] getAlternativeValues(Boolean recalculate) {

		// If no recalculation is needed, return stored data, if available
		if (!recalculate && alternativeValues != null) {
			return alternativeValues;
		} else
			alternativeValues = null;

		// If this is a node without subcriteria, the weightings stored in this
		// node are alternative weightings
		if (!this.hasSubCriteria()) {
			return getEigenvector(recalculate);

			// If there are subcriteria, the values of the alternatives in the
			// subcriteria have to be added up
		} else {
			ArrayList<Criterion> subCriteria = getCriteria();

			// Create target array
			double[] values = new double[subCriteria.get(0)
					.getAlternativeValues(recalculate).length];

			// Add rest of the values, start with second
			for (int i = 0; i < subCriteria.size(); i++) {
				double[] currentCriterionValues = subCriteria.get(i)
						.getAlternativeValues(recalculate);
				double criterionWeighting = getCriteriaWeighting(recalculate)[i];
				for (int j = 0; j < values.length; j++) {
					values[j] += currentCriterionValues[j] * criterionWeighting;
				}
			}

			alternativeValues = values;

			return values;
		}
	}

	/**
	 * @param recalculate
	 * @return the eigenvector of the node
	 */
	private double[] getEigenvector(Boolean recalculate) {
		if ((eigenvector == null) || recalculate)
			eigenvector = this.weightingMatrix.getEigenvectorFinal();
		return eigenvector;
	}

	/**
	 * Returns the the values of the alternatives added up to this node. Values
	 * can be cached and out of date.
	 * 
	 * @return Alternative Value
	 */
	public double[] getAlternativeValues() {
		return getAlternativeValues(false);
	}

	/**
	 * Resets all cached values of this node to null. Not recursive.
	 */
	public void resetCachedValues() {
		alternativeValues = null;
		criteriaWeighting = null;
		eigenvector = null;
	}

	/**
	 * Returns the alternative values for given criteria weightings. Does not
	 * change values. Used for sensitivity analysis.
	 * 
	 * @param critWeightings
	 * @return double alternative value 
	 */
	public double[] getAlternativeValues(double[] critWeightings) {

		if (!hasSubCriteria()) {
			throw new InvalidParameterException(
					"This operation is not possible without subcriteria");
		}

		ArrayList<Criterion> subCriteria = getCriteria();

		boolean recalculate = false;

		// Create target array
		double[] values = new double[subCriteria.get(0).getAlternativeValues(
				recalculate).length];

		for (int i = 0; i < subCriteria.size(); i++) {
			double[] currentCriterionValues = subCriteria.get(i)
					.getAlternativeValues(recalculate);
			double criterionWeighting = critWeightings[i];
			for (int j = 0; j < values.length; j++) {
				values[j] += currentCriterionValues[j] * criterionWeighting;
			}
		}
		return values;
	}
}
