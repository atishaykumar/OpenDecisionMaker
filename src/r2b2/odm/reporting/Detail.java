
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
 * JavaBean class which holds the data of a report detail. At the moment a detail is a criterion and includes a comparison matrix
 * of alternatives or subcriteria. Furthermore it contains its consistency ratio and a result ranking of alternatives or
 * criteria.
 * It also contains helping attributes (parents, type and number) which shall display the position of the criterion in the criterion tree.  
 * @author Rossmehl
 */
public class Detail {

	private String type;
	private String parents;
	private String name;
	private String number;
	private String description;
	private ArrayList<Comparison> comparison;
	private ArrayList<Ranking> ranking;
	private String consistencyRatio;

	/**
	 * Creates detail object.
	 * 
	 * @param isSubCriteria
	 * @param name
	 * @param description
	 * @param number
	 * @param parents
	 * @param comparison
	 * @param ranking
	 * @param consistencyRatio
	 */
	public Detail(boolean isSubCriteria, String name, String description,
			String number, String parents, ArrayList<Comparison> comparison,
			ArrayList<Ranking> ranking, String consistencyRatio) {

		// if detail is a subcriterion, set its type to "Sub Criterion" else to "Main Criterion"
		if (isSubCriteria) {
			this.type = "Sub Criterion";
		} else {
			this.type = "Main Criterion";
		}

		this.number = number;
		this.parents = parents;
		this.name = name;
		this.description = description;
		this.comparison = comparison;
		this.ranking = ranking;
		this.consistencyRatio = consistencyRatio;

	}

	/**
	 * Returns type. Either "Main Criterion" or "Sub Criterion" 
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	
	/**
	 * Sets type.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns array list with the detail comparison.
	 *  
	 * @return comparison
	 */
	public ArrayList<Comparison> getComparison() {
		return comparison;
	}

	/**
	 * Sets comparison.
	 * 
	 * @param comparison
	 */
	public void setComparison(ArrayList<Comparison> comparison) {
		this.comparison = comparison;
	}

	/**
	 * Returns array list of detail ranking.
	 * 
	 * @return ranking
	 */
	public ArrayList<Ranking> getRanking() {
		return ranking;
	}

	/**
	 * Sets ranking.
	 * 
	 * @param ranking
	 */
	public void setRanking(ArrayList<Ranking> ranking) {
		this.ranking = ranking;
	}

	
	/**
	 * Returns string of the consistency ratio of the detail.
	 * 
	 * @return consistencyRatio
	 */
	public String getConsistencyRatio() {
		return consistencyRatio;
	}

	/**
	 * Set consistency ratio.
	 * 
	 * @param consistencyRatio
	 */
	public void setConsistencyRatio(String consistencyRatio) {
		this.consistencyRatio = consistencyRatio;
	}

	/**
	 * Returns a string of the parents of the detail separated by "."
	 * 
	 * For example: "MainCriteria1.SubCriteria2
	 * 
	 * @return parent name of the selected element
	 */
	public String getParents() {
		return parents;
	}

	/**
	 * Sets detail parents. 
	 * It is separated by "."
	 * 
	 * For example: "MainCriteria1.SubCriteria2
	 * 
	 * @param parents
	 */
	public void setParents(String parents) {
		this.parents = parents;
	}

	
	/**
	 * Returns detail number.
	 * The number appears only in the report and displays the level and the position of the detail.
	 * 
	 * For example: "2.1."
	 * 
	 * @return number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Sets detail number.
	 * The number appears only in the report and displays the level and the position of the detail.
	 * 
	 * For example: "2.1."
	 * 
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * Returns the detail description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets detail description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
