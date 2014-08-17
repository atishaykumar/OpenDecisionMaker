
  
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

import java.util.ArrayList;
/**
 *  Holds all data of an AhpModel which is needed for the report creation in the necessary data structure. 
 *  @author Rossmehl 
 */
public class ReportContent {

	private String goalName;
	private ArrayList<Ranking> resultAlternatives;
	private String resultConsistencyRatio;
	private ArrayList<Comparison> resultAlternativeCriterionMatrix;
	private ArrayList<Ranking> resultCriteria;
	private ArrayList<Detail> details;
	public ReportContent() {

	}

	/**
	 * Returns name of the goal.
	 * 
	 * @return goal name
	 */
	public String getGoalName() {
		return goalName;
	}

	/**
	 * Sets goal name.
	 * 
	 * @param goalName
	 */
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	/**
	 * Returns array list of the sorted alternative ranking.
	 * 
	 * @return alternative ranking
	 */
	public ArrayList<Ranking> getResultAlternatives() {
		return resultAlternatives;
	}

	/**
	 * Sets the alternative ranking.
	 * 
	 * @param resultAlternatives
	 */
	public void setResultAlternatives(ArrayList<Ranking> resultAlternatives) {
		this.resultAlternatives = resultAlternatives;
	}

	/**
	 * Returns consistency ratio of the Ahp Analysis.
	 * 
	 * @return resultConsistencyRatio
	 */
	public String getResultConsistencyRatio() {
		return resultConsistencyRatio;
	}

	
	/**
	 * Sets consistency ratio of the Ahp Analysis.
	 * 
	 * @param resultConsistencyRatio
	 */
	public void setResultConsistencyRatio(String resultConsistencyRatio) {
		this.resultConsistencyRatio = resultConsistencyRatio;
	}

	/**
	 * Get array list of the alternative-main criterion-matrix
	 * 
	 * @return resultAlternativeCriterionMatrix
	 */
	public ArrayList<Comparison> getResultAlternativeCriterionMatrix() {
		return resultAlternativeCriterionMatrix;
	}

	/**
	 * Set alternative-main criterion-matrix
	 * 
	 * @param resultAlternativeCriterionMatrix
	 */
	public void setResultAlternativeCriterionMatrix(
			ArrayList<Comparison> resultAlternativeCriterionMatrix) {
		this.resultAlternativeCriterionMatrix = resultAlternativeCriterionMatrix;
	}

	/**
	 * Returns sorted array list of the criteria ranking.
	 * 
	 * @return resultCriteria
	 */
	public ArrayList<Ranking> getResultCriteria() {
		return resultCriteria;
	}

	/**
	 * Set the criteria ranking.
	 * 
	 * @param resultCriteria
	 */
	public void setResultCriteria(ArrayList<Ranking> resultCriteria) {
		this.resultCriteria = resultCriteria;
	}

	/**
	 * Returns array list of all detail elements. 
	 * 
	 * @return details
	 */
	public ArrayList<Detail> getDetails() {
		return details;
	}

	/**
	 * Set details.
	 * 
	 * @param details
	 */
	public void setDetails(ArrayList<Detail> details) {
		this.details = details;
	}

}
