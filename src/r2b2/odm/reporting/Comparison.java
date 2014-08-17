
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
 * JavaBean class which holds data for one comparison which is necessary to create matrices in the report.
 * @author Rossmehl
 * 
 */
public class Comparison {

	private String attribute1;
	private String attribute2;
	private String value;

	/**
	 * Creates an Comparison object. It includes two attributes and one value
	 * 
	 * @param attribute1
	 * @param attribute2
	 * @param value
	 */
	public Comparison(String attribute1, String attribute2, String value) {
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		this.value = value;

	}

	/**
	 * Returns attribute1.
	 * 
	 * @return attribute1
	 */
	public String getAttribute1() {
		return attribute1;
	}

	/**
	 * Sets attribute1.
	 * 
	 * @param attribute1
	 */
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	/**
	 * Returns attribute2.
	 * 
	 * @return attribute2
	 */
	public String getAttribute2() {
		return attribute2;
	}

	/**
	 * Sets attribute2.
	 * 
	 * @param attribute2
	 */
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	/**
	 * Returns comparsion value
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	
	/**
	 * Sets comparison value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
