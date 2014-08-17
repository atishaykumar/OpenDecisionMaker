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
 * A specialized class for accessing AHP data on a MySql database.
 * 
 * @author Alex
 * 
 */

public class AhpMySqlCon extends AhpDbCon implements Serializable {

	
	private static final long serialVersionUID = -3092581067136167165L;
	
	// The MySql connection information
	private static final String classString = "com.mysql.jdbc.Driver";
	String server = "Enter MySql Server Path";
	String username = "MySql User";
	String password = "";
	String database = "Enter Database Name";

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.sql.AhpDbCon#getConnectionString()
	 */
	@Override
	public String getConnectionString() {

		return "jdbc:mysql://" + server + "/" + database + "?user=" + username
				+ "&password=" + password;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see r2b2.javaluate.sql.AhpDbCon#getClassString()
	 */
	@Override
	public String getClassString() {
		return classString;
	}
}
