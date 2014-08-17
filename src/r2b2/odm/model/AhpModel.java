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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

import r2b2.odm.model.base.AhpNode;
import r2b2.odm.model.base.Weighting;
import r2b2.odm.model.util.MathHelper;
import r2b2.odm.model.util.Matrix;

/**
 * Holds and manages the data of an AHP decision. Interface for controllers to
 * access the AHP data.
 * 
 * @author Alex
 */

public class AhpModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1818392596680105999L;

	/**
	 * The goal of the AHP model
	 */
	Goal goal;

	/**
	 * The alternatives to achieve a given goal
	 */
	ArrayList<Alternative> alternatives = new ArrayList<Alternative>();

	// The tab descriptions
	String tabDescCriteria = "";
	String tabDescAlternatives = "";
	String tabDescWeightingsCrit = "";
	String tabDescWeightingsAlt = "";

	/**
	 * @param goal
	 *            the goal to set
	 */
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * The list of alternatives
	 * 
	 * @return the alternatives
	 */
	public ArrayList<Alternative> getAlternatives() {
		return alternatives;
	}

	/**
	 * Add an alternative to the list
	 * 
	 * @param alt
	 */
	public void addAlternative(Alternative alt) {
		alternatives.add(alt);
	}

	/**
	 * Remove an alternative from the list
	 * 
	 * @param alt
	 */
	public void removeAlternative(Alternative alt) {
		alternatives.remove(alt);
	}

	/**
	 * Add a criterion to the node structure
	 * 
	 * @param crit
	 */
	public void addCriterion(Criterion crit) {
		AhpNode parent = crit.getParent();

		// Register criterion with parent node
		parent.getCriteria().add(crit);
	}

	@Deprecated
	public void exampleDataRandom() {

		goal = new Goal("Goalneu!");

		Alternative alt1 = new Alternative(goal, "Golf");
		Alternative alt2 = new Alternative(goal, "206");
		Alternative alt3 = new Alternative(goal, "Saxo");
		Alternative alt4 = new Alternative(goal, "Clio");

		addAlternative(alt1);
		addAlternative(alt2);
		addAlternative(alt3);
		addAlternative(alt4);

		Criterion crit1 = new Criterion(goal, "Styling");
		Criterion crit2 = new Criterion(goal, "Verl‰sslichkeit");
		Criterion crit3 = new Criterion(goal, "Sparsamkeit");
		Criterion crit31 = new Criterion(crit3, "SparsamkeitAutobahn");
		Criterion crit32 = new Criterion(crit3, "SparsamkeitLandstraﬂe");
		Criterion crit33 = new Criterion(crit3, "SparsamkeitStadt");

		addCriterion(crit1);
		addCriterion(crit2);
		addCriterion(crit3);
		addCriterion(crit31);
		addCriterion(crit32);
		addCriterion(crit33);

		createCriteriaWeightings();

		// Weightings should be done
		doRandomCriteriaWeightings(goal);

		// Data should be put in matrices
		createMatricesFromCriteriaWeightings();

		createAlternativeWeightings();

		doRandomAlternativeWeightings(goal);

		createMatricesFromAlternativeWeightings();

		// results should be calculated
		calculateResults(false);

		// show results, get values without recalculation
		double[] results = goal.getAlternativeValues(false);

		for (int i = 0; i < results.length; i++) {
			System.out.println(alternatives.get(i) + " " + results[i]);
		}

		double sum = 0;
		for (double d : results) {
			sum += d;
		}
		System.out.println();
		System.out.println("Sum: " + sum);

	}
	
	@Deprecated
	public void exampleData() {

		goal = new Goal("Buy a New Maschine");

		 Alternative alt1 = new Alternative(goal, "Machine 1");
		 Alternative alt2 = new Alternative(goal, "Machine 2");
		 Alternative alt3 = new Alternative(goal, "Machine 3");
		
		 addAlternative(alt1);
		 addAlternative(alt2);
		 addAlternative(alt3);
		
		 Criterion crit1 = new Criterion(goal, "expense");
		 Criterion crit2 = new Criterion(goal, "operability");
		 Criterion crit3 = new Criterion(goal, "reliability");
		 Criterion crit4 = new Criterion(goal, "flexibility");
		
		 addCriterion(crit1);
		 addCriterion(crit2);
		 addCriterion(crit3);
		 addCriterion(crit4);
		
		 createCriteriaWeightings();
		
		 ArrayList<Weighting> allWeights = goal.getWeightings();
		
		 Weighting weightCriteria;
		 // Weightings should be done
		
		 // expense <>oper
		 weightCriteria = allWeights.get(0);
		 weightCriteria.setValue(0.3333);
		
		 // expense <>reli
		 weightCriteria = allWeights.get(1);
		 weightCriteria.setValue(5);
		
		 // expense <>flex
		 weightCriteria = allWeights.get(2);
		 weightCriteria.setValue(1);
		
		 // oper<>reli
		 weightCriteria = allWeights.get(3);
		 weightCriteria.setValue(5);
		
		 // oper<>flex
		 weightCriteria = allWeights.get(4);
		 weightCriteria.setValue(1);
		
		 // reli<>flex
		 weightCriteria = allWeights.get(5);
		 weightCriteria.setValue(0.2);
		
		 // Data should be put in matrices
		 createMatricesFromCriteriaWeightings();
		
		 createAlternativeWeightings();
		
		 ArrayList<Weighting> allWeights2 = getAlternativeWeightings();
		
		 Weighting weightAlternative;
		 // Expense M1 <->M2
		 weightAlternative = allWeights2.get(0);
		 weightAlternative.setValue(5);
		
		 // Expense M1 <->M3
		 weightAlternative = allWeights2.get(1);
		 weightAlternative.setValue(9);
		
		 // Expense M2 <->M3
		 weightAlternative = allWeights2.get(2);
		 weightAlternative.setValue(3);
		
		 // Operability M1 <->M2
		 weightAlternative = allWeights2.get(3);
		 weightAlternative.setValue(1);
		
		 // Operability M1 <->M3
		 weightAlternative = allWeights2.get(4);
		 weightAlternative.setValue(5);
		
		 // Operability M2 <->M3
		 weightAlternative = allWeights2.get(5);
		 weightAlternative.setValue(3);
		
		 // Rel M1 <->M2
		 weightAlternative = allWeights2.get(6);
		 weightAlternative.setValue(0.3333);
		
		 // Rel M1 <->M3
		 weightAlternative = allWeights2.get(7);
		 weightAlternative.setValue(0.1111);
		
		 // Rel M2 <->M3
		 weightAlternative = allWeights2.get(8);
		 weightAlternative.setValue(0.3333);
		
		 // Flex M1 <->M2
		 weightAlternative = allWeights2.get(9);
		 weightAlternative.setValue(0.1111);
		
		 // Flex M1 <->M3
		 weightAlternative = allWeights2.get(10);
		 weightAlternative.setValue(0.2);
		
		 // Flex M2 <->M3
		 weightAlternative = allWeights2.get(11);
		 weightAlternative.setValue(2);
		
		 createMatricesFromAlternativeWeightings();
		
		 // results should be calculated
		 calculateResults(false);



	}

	@Deprecated
	private void doRandomCriteriaWeightings(AhpNode parentNode) {
		Random random = new Random();

		for (Weighting weighting : parentNode.getWeightings()) {
			double val = random.nextInt(9) + 1;
			if (random.nextBoolean()) {
				weighting.setValue(val);
			} else {
				weighting.setValue(1 / val);
			}
		}

		ArrayList<Criterion> subCriteria = parentNode.getCriteria();
		for (Criterion criterion : subCriteria) {
			if (criterion.hasSubCriteria()) {
				doRandomCriteriaWeightings(criterion);
			}
		}
	}

	@Deprecated
	private void doRandomAlternativeWeightings(AhpNode parentNode) {
		if (parentNode.hasSubCriteria()) {
			for (AhpNode subCrit : parentNode.getCriteria()) {
				doRandomAlternativeWeightings(subCrit);
			}
		} else {
			Random random = new Random();
			for (Weighting weighting : parentNode.getWeightings()) {
				double val = (random.nextInt(9) + 1);
				if (random.nextBoolean()) {
					weighting.setValue(val);
				} else {
					weighting.setValue(1 / val);
				}
			}
		}

	}

	/**
	 * Returns a list of all criteria weightings
	 * 
	 * @return Arraylist of All Criteria
	 */
	public ArrayList<Weighting> getCriteriaWeightings() {
		return getCriteriaWeightings(goal);
	}

	/**
	 * Returns a list of all alternative weightings
	 * 
	 * @return ArrayList of all Criteria Weightings
	 */
	public ArrayList<Weighting> getAlternativeWeightings() {
		return getAlternativeWeightings(goal);
	}

	/**
	 * Returns a list of all criteria weightings beneath a certain node.
	 * 
	 * @param parentNode
	 * @return the criteria weightings
	 */
	private ArrayList<Weighting> getCriteriaWeightings(AhpNode parentNode) {

		// Return empty ArrayList if node has no subcriteria
		if (!parentNode.hasSubCriteria()) {
			return new ArrayList<Weighting>();
		}

		// Get weightings of the subcriteria of this node
		ArrayList<Weighting> weightings = new ArrayList<Weighting>(
				parentNode.getWeightings());

		// Get the subcriteria
		ArrayList<Criterion> subCriteria = parentNode.getCriteria();

		// If a subcriterion has subcriteria, add its weightings to the list
		for (Criterion criterion : subCriteria) {
			weightings.addAll(getCriteriaWeightings(criterion));
		}

		return weightings;
	}

	/**
	 * Returns a list of all alternative weightings beneath a certain node.
	 * 
	 * @param parentNode
	 * @return the alternative weightings
	 */
	private ArrayList<Weighting> getAlternativeWeightings(AhpNode parentNode) {
		// Prepare list of weightings
		ArrayList<Weighting> weightings = new ArrayList<Weighting>();

		// If the node has subcriteria, it can not have alternative weightings.
		// Therefore add the alternative weightings of the subcriteria.
		if (parentNode.hasSubCriteria()) {
			ArrayList<Criterion> subCriteria = parentNode.getCriteria();

			for (Criterion criterion : subCriteria) {
				weightings.addAll(getAlternativeWeightings(criterion));
			}

		} else {
			// If there are no subcriteria, the weightings of this node are
			// alternative weightings.
			return parentNode.getWeightings();
		}

		return weightings;
	}

	public void calculateResults(boolean createMatrices) {
		if (createMatrices) {
			createMatricesFromCriteriaWeightings();
			createMatricesFromAlternativeWeightings();
		}

		// Recursively go through the node tree and calculate the new values
		goal.getAlternativeValues(true);
	}

	/**
	 * Recalculates all values to update the results
	 */
	public void calculateResults() {
		calculateResults(true);
	}

	/**
	 * Uses the values of the criteria weightings to create the matrices
	 */
	public void createMatricesFromCriteriaWeightings() {

		// Start recursively creating the matrices
		createMatricesFromCriteriaWeightings(goal);
	}

	/**
	 * Uses the values of the alternative weightings to create the matrices
	 */
	public void createMatricesFromAlternativeWeightings() {

		// Start recursively creating the matrices
		createMatricesFromAlternativeWeightings(goal);
	}

	/**
	 * Creates all the necessary criteria weightings.
	 */
	public void createCriteriaWeightings() {

		// Start recursively creating the weightings
		createCriteriaWeightings(goal);
	}

	/**
	 * Creates all the necessary alternative weightings
	 */
	public void createAlternativeWeightings() {
		// Start recursively creating the weightings;
		createAlternativeWeightings(goal);

	}

	/**
	 * Creates all the criteria weightings of a node and its subcriteria
	 * 
	 * @param parentNode
	 */
	private void createCriteriaWeightings(AhpNode parentNode) {

		ArrayList<Criterion> subCriteria;
		ArrayList<Weighting> criteriaWeightings;
		ArrayList<Criterion> tempList = new ArrayList<Criterion>();

		subCriteria = parentNode.getCriteria();
		criteriaWeightings = new ArrayList<Weighting>();

		// Copy criteria to temporary list
		tempList = new ArrayList<Criterion>(subCriteria);

		while (tempList.size() > 1) {
			// Create weightings for first criterion with each of the
			// remaining criteria
			Criterion current = tempList.get(0);
			for (int i = 1; i < tempList.size(); i++) {
				criteriaWeightings.add(new Weighting_Criteria(current, tempList
						.get(i)));
			}
			// Remove current criterion from list
			tempList.remove(current);
		}

		// Copy values of old weightings to new objects
		ArrayList<Weighting> oldWeightings = parentNode.getWeightings();
		for (Weighting weightingOld : oldWeightings) {
			for (Weighting weightingNew : criteriaWeightings) {
				if (weightingNew.getFactor1() == weightingOld.getFactor1()
						&& weightingNew.getFactor2() == weightingOld
								.getFactor2()) {
					weightingNew.setValue(weightingOld.getValue());
				}
			}
		}

		// Set new weightings for node
		parentNode.setWeightings(criteriaWeightings);

		// Create weightings for subcriteria, where needed
		for (AhpNode subCriterion : subCriteria) {
			if (subCriterion.hasSubCriteria())
				createCriteriaWeightings(subCriterion);
		}
	}

	/**
	 * Creates all the alternative weightings of a node or its subcriteria
	 * 
	 * @param parentNode
	 */
	private void createAlternativeWeightings(AhpNode parentNode) {

		// If the node has subcriteria, no alternative weightings have to be
		// done
		if (parentNode.hasSubCriteria()) {
			for (Criterion subCriterion : parentNode.getCriteria()) {
				createAlternativeWeightings(subCriterion);
			}
		} else {
			// Create alternative weightings for this node
			ArrayList<Weighting> alternativeWeightings = new ArrayList<Weighting>();

			// Copy alternatives to temporary list
			ArrayList<Alternative> tempList = new ArrayList<Alternative>(
					alternatives);

			while (tempList.size() > 1) {
				// Create weightings for first alternative with each of the
				// remaining alternatives
				Alternative current = tempList.get(0);
				for (int i = 1; i < tempList.size(); i++) {
					// Create weighting
					Weighting_Alternative alternativeWeighting = new Weighting_Alternative(
							parentNode, current, tempList.get(i));

					// Add weighting
					alternativeWeightings.add(alternativeWeighting);

				}
				// Remove current alternative from list
				tempList.remove(current);
			}

			// Copy values of old weightings to new objects
			ArrayList<Weighting> oldWeightings = parentNode.getWeightings();
			for (Weighting weightingOld : oldWeightings) {
				for (Weighting weightingNew : alternativeWeightings) {
					if (weightingNew.getFactor1() == weightingOld.getFactor1()
							&& weightingNew.getFactor2() == weightingOld
									.getFactor2()) {
						weightingNew.setValue(weightingOld.getValue());
					}
				}
			}

			// Set new weightings for node
			parentNode.setWeightings(alternativeWeightings);
		}

	}

	/**
	 * Uses the values of the criteria weightings to create the matrices of one
	 * node and its subcriteria
	 * 
	 * @param parentNode
	 */
	private void createMatricesFromCriteriaWeightings(AhpNode parentNode) {
		int criteriaCount;
		ArrayList<Weighting> weightings;
		weightings = parentNode.getWeightings(); 
		criteriaCount = parentNode.getCriteria().size();

		// If there are less than two criteria, no matrices can be created
		if (criteriaCount < 2) {
			parentNode.setWeightingMatrix(null);
			return;
		}

		double[][] data = new double[criteriaCount][criteriaCount];

		// Set "1"-Values
		for (int i = 0; i < criteriaCount; i++) {
			data[i][i] = 1;
		}

		// Sets the loop to begin after the 1/1-values (criteriumX vs.
		// itself)
		int skipCols = 1;

		// Start with first weighting
		int weightingIndex = 0;

		// Fill rows with values
		for (int iRow = 0; iRow < criteriaCount; iRow++) {
			for (int iCol = skipCols; iCol < criteriaCount; iCol++) {
				data[iRow][iCol] = weightings.get(weightingIndex).getValue();

				// go to next weighting
				weightingIndex++;
			}
			// Increase number of skipped columns
			skipCols++;
		}
		// Create matrix from data
		Matrix m = new Matrix(data);

		// Fill out missing inverse values
		completeMatrix(m);

		// Set matrix
		parentNode.setWeightingMatrix(m);

		// Calculate and set CR
		parentNode.setConsistencyRatio(MathHelper.calculateConsistencyRatio(m));

		// Create matrices for subcriteria, where needed
		for (AhpNode subCriterion : parentNode.getCriteria()) {
			if (subCriterion.hasSubCriteria())
				createMatricesFromCriteriaWeightings(subCriterion);
		}
	}

	/**
	 * Uses the values of the alternative weightings to create the matrices of a
	 * node or its subcriteria
	 * 
	 * @param parentNode
	 */
	private void createMatricesFromAlternativeWeightings(AhpNode parentNode) {

		// A node has no alternative weightings if it has subcriteria
		if (parentNode.hasSubCriteria()) {
			for (AhpNode subNode : parentNode.getCriteria()) {
				// Create the matrices of the subcriteria
				createMatricesFromAlternativeWeightings(subNode);
			}
		} else {
			// Create the matrices of the weightings

			ArrayList<Weighting> weightings = parentNode.getWeightings();

			int alternativeCount = alternatives.size();

			// If there are less than two alternatives, no matrices can be
			// created
			if (alternativeCount < 2) {
				parentNode.setWeightingMatrix(null);
				return;
			}
			double[][] data = new double[alternativeCount][alternativeCount];

			// Set "1"-Values
			for (int i = 0; i < alternativeCount; i++) {
				data[i][i] = 1;
			}

			// Sets the loop to begin after the 1/1-values (alternative X vs.
			// itself)
			int skipCols = 1;

			// Start with first weighting
			int weightingIndex = 0;

			// Fill rows with values
			for (int iRow = 0; iRow < alternativeCount; iRow++) {
				for (int iCol = skipCols; iCol < alternativeCount; iCol++) {
					data[iRow][iCol] = weightings.get(weightingIndex)
							.getValue();

					// go to next weighting
					weightingIndex++;
				}
				// Increase number of skipped columns
				skipCols++;
			}
			// Create matrix from data
			Matrix m = new Matrix(data);

			// Fill out missing inverse values
			completeMatrix(m);

			// Set matrix for node
			parentNode.setWeightingMatrix(m);

			// Calculate and set CR
			parentNode.setConsistencyRatio(MathHelper
					.calculateConsistencyRatio(m));
		}
	}

	/**
	 * <p>
	 * Completes the matrix data after user input with the inverse values
	 * </p>
	 * 
	 * Before:
	 * 
	 * <pre>
	 * { 1,		3,		2 }
	 * { 0,		1,		2 }
	 * { 0,		0,		1 }
	 * </pre>
	 * 
	 * After:
	 * 
	 * <pre>
	 * { 1,		3,		2 }
	 * { 1/3,	1,		2 }
	 * { 1/2,	1/2,	1 }
	 * </pre>
	 * 
	 * @param m
	 */
	private void completeMatrix(Matrix m) {

		double[][] data = m.getData();

		// Sets the loop to begin after the 1/1-values (criteriumX vs. itself)
		int skipCols = 1;

		for (int iRow = 0; iRow < m.getRowcount(); iRow++) {
			for (int iCol = skipCols; iCol < m.getColumncount(); iCol++) {
				// Replace target field with inverse value of source field
				data[iCol][iRow] = (double) 1 / data[iRow][iCol];
			}
			// Increase number of skipped columns
			skipCols++;
		}
	}

	/**
	 * Recursively goes through the node structure to find a certain id
	 * 
	 * @param parent
	 * @param idCriterion
	 * @return <code>null</code> if no criterion matching the id could be found
	 */
	public static Criterion findCriterionById(AhpNode parent, int idCriterion) {
		for (Criterion subCriterion : parent.getCriteria()) {
			// Return this subcriterion if it matches the id
			if (subCriterion.getId() == idCriterion) {
				return subCriterion;
			} else {
				// Look for id in subtree
				Criterion crit = findCriterionById(subCriterion, idCriterion);
				if (crit != null) {
					return crit;
				}
			}
		}
		// Did not find the id in this tree, return null
		return null;
	}

	/**
	 * Returns the alternative with a given id
	 * 
	 * @param alternatives
	 * @param idAlternative
	 * @return alternative
	 */
	public static Alternative findAlternativeById(
			ArrayList<Alternative> alternatives, int idAlternative) {
		for (Alternative alternative : alternatives) {
			if (alternative.getId() == idAlternative) {
				return alternative;
			}
		}

		throw new InvalidParameterException(
				"No argument with this id could be found:" + idAlternative);
	}

	/**
	 * @return the tabDescAlternatives
	 */
	public String getTabDescAlternatives() {
		return tabDescAlternatives;
	}

	/**
	 * @param tabDescAlternatives
	 *            the tabDescAlternatives to set
	 */
	public void setTabDescAlternatives(String tabDescAlternatives) {
		this.tabDescAlternatives = tabDescAlternatives;
	}

	/**
	 * @return the tabDescWeightingsCrit
	 */
	public String getTabDescWeightingsCrit() {
		return tabDescWeightingsCrit;
	}

	/**
	 * @param tabDescWeightingsCrit
	 *            the tabDescWeightingsCrit to set
	 */
	public void setTabDescWeightingsCrit(String tabDescWeightingsCrit) {
		this.tabDescWeightingsCrit = tabDescWeightingsCrit;
	}

	/**
	 * @return the tabDescWeightingsAlt
	 */
	public String getTabDescWeightingsAlt() {
		return tabDescWeightingsAlt;
	}

	/**
	 * @param tabDescWeightingsAlt
	 *            the tabDescWeightingsAlt to set
	 */
	public void setTabDescWeightingsAlt(String tabDescWeightingsAlt) {
		this.tabDescWeightingsAlt = tabDescWeightingsAlt;
	}

	/**
	 * @return the tabDescCriteria
	 */
	public String getTabDescCriteria() {
		return tabDescCriteria;
	}

	/**
	 * @param tabDescCriteria
	 *            the tabDescCriteria to set
	 */
	public void setTabDescCriteria(String tabDescCriteria) {
		this.tabDescCriteria = tabDescCriteria;
	}
}
