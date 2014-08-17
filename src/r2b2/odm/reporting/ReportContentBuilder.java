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
package r2b2.odm.reporting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import r2b2.odm.model.AhpModel;
import r2b2.odm.model.Alternative;
import r2b2.odm.model.Criterion;
import r2b2.odm.model.Goal;
import r2b2.odm.model.base.AhpNode;
import r2b2.odm.model.base.AhpObject;
import r2b2.odm.model.base.Weighting;

/**
 * The ReportContentBuilder class transforms the necessary model data into a
 * format which can be integrated into the PDF report.
 * 
 * @author Rossmehl
 * 
 */

public class ReportContentBuilder {

	/**
	 * decimal format for weightings and consistency ratios
	 */
	DecimalFormat weightingDf = new DecimalFormat("0.00");

	/**
	 * decimal format for rankings (in percentage)
	 */
	DecimalFormat percentageDf = new DecimalFormat("0.00%");

	/**
	 * This method creates a ReportContent object which contains all necessary
	 * data for an exported report.
	 * 
	 * @param model
	 * @return ReportContent object filled with necessary data for the report
	 *         creation
	 */
	public ReportContent getReportContentFromAhpModel(AhpModel model) {

		// instantiate necessary objects and initialize variables
		ArrayList<Detail> detailList = new ArrayList<Detail>();
		String goalName = null;
		String resultConsistencyRatio = null;
		ArrayList<Comparison> resultAlternativeCriterionMatrix = new ArrayList<Comparison>();
		ArrayList<Ranking> resultCriteria = new ArrayList<Ranking>();
		ArrayList<Ranking> resultAlternatives = new ArrayList<Ranking>();

		ReportContent reportContent = new ReportContent();

		// get all main criteria from model
		ArrayList<Criterion> modelMainCriteria = model.getGoal().getCriteria();

		// iterate over main criteria and add create Detail object and add it to
		// the detailList
		// the detailNumber is responsible for the serial numeration of the
		// criteria
		int detailNumber = 1;

		for (Criterion criterion : modelMainCriteria) {

			createAndAddDetail(criterion, detailList, model,
					Integer.toString(detailNumber));
			detailNumber++;
		}

		// Get name of the goal
		Goal goal = model.getGoal();
		goalName = goal.getName();

		// get and format the consistency ratio of the AHP goal
		resultConsistencyRatio = weightingDf.format(goal.getConsistencyRatio());

		// create ranking of the alternatives
		resultAlternatives = createAlternativeRanking(goal, model);

		// create ranking of the criteria
		resultCriteria = createCriterionRanking(goal, model);

		// create alternative-criterion matrix
		resultAlternativeCriterionMatrix = createCriterionAlternativeMatrix(model);

		// Fill reportContent object
		reportContent.setGoalName(goalName);
		reportContent.setDetails(detailList);
		reportContent
				.setResultAlternativeCriterionMatrix(resultAlternativeCriterionMatrix);
		reportContent.setResultAlternatives(resultAlternatives);
		reportContent.setResultConsistencyRatio(resultConsistencyRatio);
		reportContent.setResultCriteria(resultCriteria);

		return reportContent;
	}

	/**
	 * Creates a detail object and fills it with data. The detail object is
	 * added to the detailList. Furthermore the method iterates over all sub
	 * criteria of the criterion and calls this method recursively.
	 * 
	 * @param criterion
	 * @param detailList
	 * @param model
	 * @param number
	 */
	private void createAndAddDetail(Criterion criterion,
			ArrayList<Detail> detailList, AhpModel model, String number) {

		// instantiate necessary objects and initialize variables
		Detail detail = null;

		boolean isSubcriteria = true;
		String detailType = null;
		String detailName = null;
		String detailDescription = null;
		String detailConsistencyRatio = null;
		String detailParents = null;
		ArrayList<Ranking> detailRanking = new ArrayList<Ranking>();
		ArrayList<Comparison> detailComparison = new ArrayList<Comparison>();

		// add a full stop after number --> the detailNumber displays the
		// criterion numeration within the report
		String detailNumber = number + ".";

		// check if criterion has sub criteria and set boolean "isSubcriteria"
		if (criterion.hasSubCriteria()) {
			isSubcriteria = false;
		}

		// The detailType ist later displayed in the criteria summary of the
		// report.
		// Set detailType to "Main Criterion" if criterion is a first level
		// criterion.
		// Criteria of lower levels are of the type "Sub Criterion".
		if (criterion.getParent() instanceof Goal)
			detailType = "Main Criterion";
		else
			detailType = "Sub Criterion";

		// get the name and the description of the criterion
		detailName = criterion.getName();
		detailDescription = setDescriptionString(criterion);

		// get and format the consistency ratio of the weighting matrix of the
		// criterion
		detailConsistencyRatio = weightingDf.format(criterion
				.getConsistencyRatio());

		// For reasons of the user's orientation the parent(s) of every
		// criterion is displayed in the report.
		// If the criterion is a first level criterion, it has no parent
		// criterion and the string is set to "-".
		AhpNode parent = criterion.getParent();

		if (parent instanceof Goal) {
			detailParents = "-";
		} else {
			detailParents = parent.toString();
		}

		// create detail ranking
		detailRanking = createAlternativeRanking(criterion, model);

		// create detail comparison
		detailComparison = createDetailComparison(criterion, model);

		// Create new Detail object
		detail = new Detail(isSubcriteria, detailName, detailDescription,
				detailNumber, detailParents, detailComparison, detailRanking,
				detailConsistencyRatio);
		detail.setType(detailType);

		// add detail object to detailList
		detailList.add(detail);

		// Iterate over all sub criteria of this criterion and
		if (criterion.hasSubCriteria()) {
			ArrayList<Criterion> subcriteriaList = criterion.getCriteria();
			int subDetailNumber = 1;
			for (Criterion subcriterion : subcriteriaList) {
				createAndAddDetail(subcriterion, detailList, model,
						detailNumber + Integer.toString(subDetailNumber));
				subDetailNumber++;
			}
		}

	}

	/**
	 * Returns an arrayList with Comparison objects for the Alternative-Main
	 * criterion-Matrix of a specific AhpModel.
	 * 
	 * @param model
	 * @return
	 */
	private ArrayList<Comparison> createCriterionAlternativeMatrix(
			AhpModel model) {
		// create empty comparison list.
		ArrayList<Comparison> comparisonList = new ArrayList<Comparison>();

		// Get alternatives and main criteria of the model
		ArrayList<Criterion> criterionList = model.getGoal().getCriteria();
		ArrayList<Alternative> alternativeList = model.getAlternatives();

		// iterate over each criterion
		for (Criterion criterion : criterionList) {

			// get the name and the alternative values of each criterion
			double[] alternativeValues = criterion.getAlternativeValues();
			String criterionName = criterion.getName();

			// iterate over the alternative List and match alternative name and
			// its value
			for (int i = 0; i < alternativeList.size(); i++) {
				String alternativeName = alternativeList.get(i).getName();

				// format the value as percentage
				String value = percentageDf.format(alternativeValues[i]);

				// create Comparison item and add it to the comparisonList
				Comparison comp = new Comparison(alternativeName,
						criterionName, value);

				comparisonList.add(comp);
			}
		}

		return comparisonList;
	}

	/**
	 * Returns an array list of comparison JavaBeans. Depending on whether the
	 * AhpNode has sub criteria or not, the comparison matrix contains sub
	 * criteria or alternatives.
	 * 
	 * @param node
	 * @param model
	 * @return ArrayList with
	 */
	private ArrayList<Comparison> createDetailComparison(AhpNode node,
			AhpModel model) {

		// instantiate array list of the type "Comparison"
		ArrayList<Comparison> comparisonList = new ArrayList<Comparison>();

		// If this criterion (AhpNode) has sub criteria create a comparison
		// which contains those sub criteria.
		if (node.hasSubCriteria()) {
			ArrayList<Weighting> weightingList = node.getWeightings();
			ArrayList<Criterion> subCriterionList = node.getCriteria();

			ArrayList<AhpObject> ahpObjectList = castArrayListToTypeAhpObject(subCriterionList);
			comparisonList = createComparison(ahpObjectList, weightingList);

			// If the criterion does not contain sub criteria, the comparison
			// contains alternatives.
		} else {
			ArrayList<Weighting> weightingList = node.getWeightings();
			ArrayList<Alternative> alternativeList = model.getAlternatives();

			ArrayList<AhpObject> ahpObjectList = castArrayListToTypeAhpObject(alternativeList);
			comparisonList = createComparison(ahpObjectList, weightingList);

		}
		return comparisonList;

	}

	/**
	 * Returns an array list that contains data for a ranking of criteria of a
	 * specific AhpNode.
	 * 
	 * @param node
	 * @param model
	 * @return
	 */
	private ArrayList<Ranking> createCriterionRanking(AhpNode node,
			AhpModel model) {

		// get criteria and criteria weighting of the node
		ArrayList<Criterion> criterionList = node.getCriteria();
		double[] nodeValues = node.getCriteriaWeighting(false);

		// cast elements of criterionList to the type "AhpObject"
		ArrayList<AhpObject> ahpObjectList = castArrayListToTypeAhpObject(criterionList);

		// create and return ranking
		return createRanking(ahpObjectList, nodeValues);
	}

	/**
	 * Returns an array list that contains data for a ranking of alternatives of
	 * a specific AhpNode.
	 * 
	 * @param node
	 * @param model
	 * @return
	 */
	private ArrayList<Ranking> createAlternativeRanking(AhpNode node,
			AhpModel model) {

		// get Alternatives alternative values
		ArrayList<Alternative> alternativelist = model.getAlternatives();
		double[] nodeValues = node.getAlternativeValues(false);

		// cast elements of criterionList to the type "AhpObject"
		ArrayList<AhpObject> ahpObjectList = castArrayListToTypeAhpObject(alternativelist);

		// create and return ranking
		return createRanking(ahpObjectList, nodeValues);

	}

	/**
	 * Creates an array list which contains JavaBean objects. These objects
	 * contain data for one ranking table with the attributes "ranking", "name"
	 * and "value".
	 * 
	 * @param ahpObjectList
	 * @param nodeValues
	 * @return array list of ranking objects
	 */
	private ArrayList<Ranking> createRanking(
			ArrayList<AhpObject> ahpObjectList, double[] nodeValues) {
		// create ArrayList for the ranking
		ArrayList<Ranking> rankingList = new ArrayList<Ranking>();

		// iterate over ahpObjectList

		for (int i = 0; i < ahpObjectList.size(); i++) {

			AhpObject ahpObject = ahpObjectList.get(i);

			// Get name and description of every element and the corresponding
			// value from the double array.
			String name = ahpObject.getName();
			String value = String.valueOf(nodeValues[i]);
			String description = setDescriptionString(ahpObject);

			// create new alternative ranking object
			Ranking ranking = new Ranking(name, null, description, value);

			// add ranking object to list
			rankingList.add(ranking);
		}
		// sort and format the ranking list
		sortAndFormatRankingList(rankingList);

		return rankingList;
	}

	/**
	 * Creates an array list which contains JavaBean objects. This list contains
	 * all fields of a comparison matrix.
	 * 
	 * @param ahpObjectList
	 * @param weightingList
	 * @return
	 */
	private ArrayList<Comparison> createComparison(
			ArrayList<AhpObject> ahpObjectList,
			ArrayList<Weighting> weightingList) {

		// instantiate empty array list of type comparison
		ArrayList<Comparison> comparisonList = new ArrayList<Comparison>();

		// Iterate over the weightingList.
		// Get the names and the value and add a comparison to the
		// comparisonList.
		// Create the corresponding comparison object by switching the
		// attributes names and building the reciprocal of the weighting value
		for (Weighting weighting : weightingList) {
			String attribute1 = weighting.getFactor1().getName();
			String attribute2 = weighting.getFactor2().getName();
			String value = weightingDf.format((weighting.getValue()));
			String valueReciprocal = weightingDf.format(1 / weighting
					.getValue());

			Comparison comparison1 = new Comparison(attribute1, attribute2,
					value);
			Comparison comparison2 = new Comparison(attribute2, attribute1,
					valueReciprocal);

			comparisonList.add(comparison1);
			comparisonList.add(comparison2);
		}

		// Add comparison objects for each element of the ahpObjectList with the
		// value 1.
		for (AhpObject ahpObject : ahpObjectList) {
			String name = ahpObject.getName();
			Comparison comparison = new Comparison(name, name, "1");
			comparisonList.add(comparison);
		}

		return comparisonList;

	}

	/**
	 * Sorts a ranking list according to the value attribute of its elements in
	 * descending order.
	 * 
	 * @param rankingList
	 */
	private void sortAndFormatRankingList(ArrayList<Ranking> rankingList) {
		// sort alternativesRanking according to value
		Collections.sort(rankingList, new Comparator<Ranking>() {
			@Override
			public int compare(Ranking r1, Ranking r2) {

				if (r1.getValue() == r2.getValue()) {
					return 1;
				}
				// Compare by attribute --> descending order
				return r1.getValue().compareTo(r2.getValue()) * -1;
			}
		});

		// add ranking list objects and set decimal format of the value
		int rankingCounter = 1;
		for (Ranking ranking : rankingList) {

			ranking.setValue(percentageDf.format(Double.valueOf(ranking
					.getValue())));
			ranking.setRanking(Integer.toString(rankingCounter) + ".");
			rankingCounter++;

		}
	}

	/**
	 * Casts the Elements of an array list to the type AhpObject
	 * 
	 * @param arrayList
	 * @return
	 */
	private ArrayList<AhpObject> castArrayListToTypeAhpObject(
			ArrayList arrayList) {

		ArrayList<AhpObject> ahpObjectList = new ArrayList<AhpObject>();

		// iterate over array list and cast elements
		for (Object element : arrayList) {
			ahpObjectList.add((AhpObject) element);
		}
		return ahpObjectList;
	}

	/**
	 * Checks if description string is empty. If yes, it is set to
	 * "*no description available*".
	 * 
	 * @param ahpObject
	 * @return description string
	 */
	private String setDescriptionString(AhpObject ahpObject) {
		String description = ahpObject.getDescription();
		if (description == "") {
			description = "*no description available*";
		}
		return description;
	}

}
