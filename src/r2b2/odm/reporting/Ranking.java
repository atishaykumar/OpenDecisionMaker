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

/**
 * JavaBean that holds data for a report ranking element.
 * 
 * @author Rossmehl
 */
public class Ranking {

	private String name;
	private String ranking;
	private String description;
	private String value;

	/**
	 * Creates ranking object.
	 * 
	 * @param name
	 * @param ranking
	 * @param description
	 * @param value
	 */
	public Ranking(String name, String ranking, String description, String value) {
		this.name = name;
		this.ranking = ranking;
		this.description = description;
		this.value = value;

	}

	/**
	 * Returns ranking name.
	 * 
	 * @return ranking name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets ranking name.
	 * 
	 * @param name
	 *            set the name of the ranking
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns ranking.
	 * 
	 * @return ranking.
	 */
	public String getRanking() {
		return ranking;
	}

	/**
	 * Sets ranking.
	 * 
	 * @param ranking
	 */
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	/**
	 * Returns description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns value.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
