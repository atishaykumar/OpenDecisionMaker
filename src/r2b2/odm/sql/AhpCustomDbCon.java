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
package r2b2.odm.sql;

import java.io.Serializable;

/**
 * A class that allows accessing a user-specified database via JDBC
 * 
 * @author Alex
 * 
 */

public class AhpCustomDbCon extends AhpDbCon implements Serializable {

	private static final long serialVersionUID = -4651031956844999177L;

	private String classString;
	private String connectionString;

	/**
	 * Sets the class string for the JDBC driver
	 * 
	 * @param classString
	 */
	public void setClassString(String classString) {
		this.classString = classString;
	}

	/**
	 * Sets the connection string for the JDBC connection
	 * 
	 * @param connectionString
	 */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/* (non-Javadoc)
	 * @see r2b2.odm.sql.AhpDbCon#getClassString()
	 */
	@Override
	public String getClassString() {
		return classString;
	}

	/* (non-Javadoc)
	 * @see r2b2.odm.sql.AhpDbCon#getConnectionString()
	 */
	@Override
	public String getConnectionString() {
		return connectionString;
	}
}
