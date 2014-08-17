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

import java.io.InputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import r2b2.odm.gui.Main_Gui;
import r2b2.odm.model.AhpModel;
import r2b2.odm.model.Alternative;
import r2b2.odm.model.Criterion;
import r2b2.odm.model.Goal;
import r2b2.odm.model.Weighting_Alternative;
import r2b2.odm.model.Weighting_Criteria;
import r2b2.odm.model.base.AhpNode;
import r2b2.odm.model.base.AhpObject;
import r2b2.odm.model.base.Weighting;

/**
 * Allows accessing the stored data of an AHP decision in a database.
 * 
 * @author Alex
 * 
 */

public abstract class AhpDbCon implements Serializable {

	/**
	 * Indicates whether the the connection was configured properly and if it
	 * worked.
	 */
	private boolean configured = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4454023695506836123L;

	// transient to allow serialization (serialization fails if con is not
	// transient and not null)
	protected transient Connection con;

	protected static final String cmdShowTables = "SHOW TABLES";
	protected static final String cmdSelectLastInsert = "SELECT LAST_INSERT_ID();";

	protected static final String tblNameAlternatives = "Alternatives";
	protected static final String tblNameCriteria = "Criteria";
	protected static final String tblNameSubcriteria = "Subcriteria";
	protected static final String tblNameGoals = "Goals";
	protected static final String tblNameTabDescriptions = "TabDescriptions";
	protected static final String tblNameWeightingsAlternatives = "WeightingsAlternatives";
	protected static final String tblNameWeightingsCriteria = "WeightingsCriteria";

	/**
	 * Initializes the JDBC driver for the connection
	 * 
	 * @throws ClassNotFoundException
	 */
	public void initDriver() throws ClassNotFoundException {
		Class.forName(getClassString());
	}

	/**
	 * Tests if a connection is possible, verifies database table structure
	 * 
	 * @return <p>
	 *         <code>true:</code> Connection success, existing database
	 *         structure
	 *         </p>
	 * 
	 *         <p>
	 *         <code>false:</code> Connection success, no database structure
	 *         </p>
	 * 
	 *         otherwise exception
	 * @throws SQLException
	 */
	public boolean testConnection() throws SQLException {

		boolean dbStructureExists = false;

		try {
			// Get connection
			con = DriverManager.getConnection(getConnectionString());

			// See if connection works
			if (!con.isClosed()) {
				boolean structureExists = verifyDbStructure(con
						.createStatement());
				if (structureExists)
					dbStructureExists = true;
				else
					dbStructureExists = false;
			} else {
				throw new SQLException("Could not establish connection");
			}

			configured = dbStructureExists;

		} finally {
			// Close connection
			if (con != null)
				con.close();
		}

		return dbStructureExists;
	}

	/**
	 * Loads the list of goals stored in the database
	 * 
	 * @return goal list
	 * @throws SQLException
	 */
	public ArrayList<Goal> getGoalList() throws SQLException {

		ArrayList<Goal> goals = new ArrayList<Goal>();
		try {
			con = DriverManager.getConnection(getConnectionString());

			Statement stmt = con.createStatement();

			String sqlQuery = "SELECT * FROM " + tblNameGoals;
			ResultSet resultSet = stmt.executeQuery(sqlQuery);

			while (resultSet.next()) {
				Goal goal = new Goal(resultSet.getString("Name"));
				goal.setId(resultSet.getInt("idGoals"));
				goal.setDescription(resultSet.getString("Description"));
				goals.add(goal);
			}

		} finally {
			if (con != null)
				con.close();
		}

		return goals;
	}

	/**
	 * Saves all user input of an AHP decision to the database.
	 * 
	 * @param model
	 *            model to be saved
	 * @return the result of the process
	 * @throws SQLException
	 */
	public boolean save(AhpModel model) throws SQLException {

		boolean saveSuccess = false;

		try {
			con = DriverManager.getConnection(getConnectionString());

			// Do save as one transaction, to prevent inconsistent data
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();

			Goal goal = model.getGoal();

			// See if goal is already in database
			// A goal without id cannot be stored in the database
			if (goal.hasId()) {
				String sqlQuery = "SELECT COUNT(*) FROM " + tblNameGoals
						+ " WHERE idGoals=?";
				PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
				prepStmt.setInt(1, goal.getId());
				// Execute query
				ResultSet resultSet = prepStmt.executeQuery();

				resultSet.first();
				int count = resultSet.getInt(1);

				if (count == 1) {
					removeData(stmt, goal.getId());
				} else if (count > 1) {
					throw new SQLException(
							"Possible data inconsistency! Multiple data sets with the same idGoals were returned!");
				}

				// Update goal
				sqlQuery = "UPDATE Goals SET Name=?, Description=? WHERE idGoals=?";

				prepStmt = con.prepareStatement(sqlQuery);
				prepStmt.setString(1, goal.getName());
				prepStmt.setString(2, goal.getDescription());
				prepStmt.setInt(3, goal.getId());
				// Execute query
				prepStmt.execute();

			} else {
				saveGoal(stmt, goal);
			}

			for (Criterion subCriterion : goal.getCriteria()) {
				saveCriterionAndSubcriteria(stmt, subCriterion, goal);
			}

			saveAlternatives(stmt, model.getAlternatives(), goal);

			saveCriteriaWeightings(stmt, model.getCriteriaWeightings(), goal);
			saveAlternativeWeightings(stmt, model.getAlternativeWeightings(),
					goal);

			saveTabDescriptions(stmt, goal.getId(), model.getTabDescCriteria(),
					model.getTabDescAlternatives(),
					model.getTabDescWeightingsCrit(),
					model.getTabDescWeightingsAlt());

			// Commit changes
			con.commit();

			saveSuccess = true;

		} finally {
			if (con != null)
				con.close();
		}
		return saveSuccess;
	}

	/**
	 * Loads a complete AhpModel from the data base, using the given goal id.
	 * 
	 * @param goalId
	 * @return model
	 * @throws SQLException
	 */
	public AhpModel load(int goalId) throws SQLException {

		AhpModel model = new AhpModel();

		try {
			con = DriverManager.getConnection(getConnectionString());

			Statement stmt = con.createStatement();

			Goal goal = getGoal(goalId);
			// Set goal in model
			model.setGoal(goal);

			// Read criteria and subcriteria-relationships
			buildNodeStructure(stmt, goal);

			ArrayList<Alternative> alternatives = getAlternatives(stmt, goal);
			// Add alternatives to model
			model.getAlternatives().addAll(alternatives);

			addWeightingsCriteria(stmt, goal);

			addWeightingsAlternatives(stmt, goal, alternatives);

			loadTabDescriptions(stmt, goal.getId(), model);

		} finally {
			if (con != null)
				con.close();
		}

		return model;
	}

	/**
	 * Creates the necessary data tables in the database
	 * 
	 * @return <code>true</code> if creation was completed successfully
	 * @throws SQLException
	 */
	public boolean createDbStructure() throws SQLException {

		boolean reOpened = false;
		try {
			// Reopen connection if necessary
			if (con.isClosed()) {
				con = DriverManager.getConnection(getConnectionString());
				reOpened = true;
			}
			String createScript = getCreateScript();

			// Script has to be split in single commands, otherwise it will not
			// work

			String sqlQuery = "";
			String[] lines = createScript.split("\r\n");

			Statement stmt = con.createStatement();

			for (String thisLine : lines) {
				// Skip comments and empty lines
				if (thisLine.length() > 0 && thisLine.charAt(0) == '-'
						|| thisLine.length() == 0)
					continue;
				sqlQuery = sqlQuery + " " + thisLine;
				// If ";" is at the end, the command is complete
				if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
					sqlQuery = sqlQuery.replace(';', ' '); // Remove the ; since
															// jdbc complains
					stmt.execute(sqlQuery);

					// Empty query
					sqlQuery = "";
				}
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			if (reOpened) {
				con.close();
			}
		}
		return true;

	}

	/**
	 * @return the SQL script to create the database structure
	 */
	public String getCreateScript() {

		InputStream s = Main_Gui.class
				.getResourceAsStream("../sql/mysqlcreatescript.txt");
		Scanner sc = new Scanner(s);
		sc.useDelimiter("\\Z"); // read to the end of file. all at one.
		String contents = sc.next();

		return contents;
	}

	/**
	 * @return the classString
	 */
	public abstract String getClassString();

	/**
	 * @return the connectionString
	 */
	public abstract String getConnectionString();

	/**
	 * Checks if the database has an existing database structure
	 * 
	 * @return <code>false</code> if at least one table is missing
	 * @throws SQLException
	 */
	private boolean verifyDbStructure(Statement stmt) throws SQLException {
		boolean hasGoals = false;
		boolean hasCriteria = false;
		boolean hasSubcriteria = false;
		boolean hasAlternatives = false;
		boolean hasTabDescriptions = false;
		boolean hasWeightingsAlt = false;
		boolean hasWeightingsCrit = false;

		try {

			// Get tables of database
			ResultSet result = stmt.executeQuery(cmdShowTables);
			result.beforeFirst();
			while (result.next()) {
				// Get current table name
				String colValue = result.getString(1);

				// Verify needed tables exist
				if (colValue.equalsIgnoreCase(tblNameAlternatives)) {
					hasAlternatives = true;
				} else if (colValue.equalsIgnoreCase(tblNameCriteria)) {
					hasCriteria = true;
				} else if (colValue.equalsIgnoreCase(tblNameSubcriteria)) {
					hasSubcriteria = true;
				} else if (colValue.equalsIgnoreCase(tblNameGoals)) {
					hasGoals = true;
				} else if (colValue.equalsIgnoreCase(tblNameTabDescriptions)) {
					hasTabDescriptions = true;
				} else if (colValue
						.equalsIgnoreCase(tblNameWeightingsAlternatives)) {
					hasWeightingsAlt = true;
				} else if (colValue.equalsIgnoreCase(tblNameWeightingsCriteria)) {
					hasWeightingsCrit = true;
				}

			}

			// Return false if at least one table is missing
			if (hasAlternatives && hasCriteria && hasSubcriteria && hasGoals
					&& hasTabDescriptions && hasWeightingsAlt
					&& hasWeightingsCrit) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw e;
		}
	}

	/**
	 * Reads the goal with the given id from the database
	 * 
	 * @param stmt
	 * @param goalId
	 * @return the goal matching the id
	 * @throws SQLException
	 */
	private Goal getGoal(int goalId) throws SQLException {
		// Prepare goal
		Goal goal = new Goal("");

		// Get goal data
		String sqlQuery = "SELECT * FROM " + tblNameGoals + " WHERE idGoals=?";
		PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
		prepStmt.setInt(1, goalId);
		ResultSet resultSet = prepStmt.executeQuery();

		// Go to beginning of result
		if (resultSet.first()) {
			// Read and set data
			goal.setId(resultSet.getInt("idGoals"));
			goal.setName(resultSet.getString("Name"));
			goal.setDescription(resultSet.getString("Description"));

		} else {
			throw new InvalidParameterException("No goal with the id " + goalId
					+ "could be found.");
		}
		// Set should only contain one row
		if (resultSet.next()) {
			throw new SQLException(
					"Found more than one data set for specified goal id");
		}

		return goal;
	}

	/**
	 * Gets all the alternatives for a given goal stored in a database
	 * 
	 * @param stmt
	 * @param goal
	 * @return arraylist list of alternatives for the goal
	 * @throws SQLException
	 */
	private ArrayList<Alternative> getAlternatives(Statement stmt, Goal goal)
			throws SQLException {

		// Prepare results
		ArrayList<Alternative> results = new ArrayList<Alternative>();

		// Prepare query
		String sqlQuery = "SELECT * FROM " + tblNameAlternatives
				+ " WHERE Goals_idGoals=?";
		PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
		prepStmt.setInt(1, goal.getId());
		// Execute query
		ResultSet resultSet = prepStmt.executeQuery();

		// Build alternatives
		while (resultSet.next()) {
			// Get data
			int id = resultSet.getInt("idAlternatives");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");

			// Set data
			Alternative alt = new Alternative(goal, name);
			alt.setDescription(description);
			alt.setId(id);

			// Add alternative to list
			results.add(alt);
		}

		return results;
	}

	/**
	 * Builds the AHP node structure, with the goal as the top node. Reads all
	 * criteria and their subcriteria from the database.
	 * 
	 * @param stmt
	 * @param goal
	 * @throws SQLException
	 */
	private void buildNodeStructure(Statement stmt, Goal goal)
			throws SQLException {

		// Get subcriteria ids
		String sqlSubQuery = "SELECT Criteria_idCriteriaChild FROM "
				+ tblNameSubcriteria + " WHERE Goals_idGoals=?";

		// Get top-level criteria
		String sqlQuery = "SELECT * FROM " + tblNameCriteria
				+ " WHERE Goals_idGoals=? AND idCriteria NOT IN ("
				+ sqlSubQuery + ")";

		PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
		prepStmt.setInt(1, goal.getId());
		prepStmt.setInt(2, goal.getId());

		// Execute query
		ResultSet resultSet = prepStmt.executeQuery();

		// Add Criteria from resulting data set
		addCriteriaFromResultSet(resultSet, goal);

		// Using the top-level of criteria, look for the subcriteria
		for (Criterion criterion : goal.getCriteria()) {
			// Look for subcriteria for given criterion
			addSubcriteria(stmt, criterion);
		}

	}

	/**
	 * Looks for subcriteria for given criterion, and adds them. Recursively
	 * calls itself.
	 * 
	 * @param stmt
	 * @param parent
	 * @throws SQLException
	 */
	private void addSubcriteria(Statement stmt, Criterion parent)
			throws SQLException {

		// Subquery looking for subcriteria relationships for this criterion
		String subQuery = "(SELECT * FROM " + tblNameSubcriteria
				+ " WHERE Criteria_idCriteriaParent=?) AS sub";
		// Join resulting table with criteria table to get data of subcriteria
		String select = "SELECT * FROM " + tblNameCriteria + ", " + subQuery;
		String where = " WHERE idCriteria = sub.Criteria_idCriteriaChild";

		String sqlQuery = select + where;
		PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
		prepStmt.setInt(1, parent.getId());
		// Execute query
		ResultSet resultSet = prepStmt.executeQuery();

		// Build and add subcriteria
		addCriteriaFromResultSet(resultSet, parent);

		// Recursive call that stops when no subcriteria are defined for this
		// node
		for (Criterion subCriterion : parent.getCriteria()) {
			addSubcriteria(stmt, subCriterion);
		}
	}

	/**
	 * Uses the data of a result set to add subcriteria to a given parent node
	 * 
	 * @param resultSet
	 * @param parent
	 * @throws SQLException
	 */
	private void addCriteriaFromResultSet(ResultSet resultSet, AhpNode parent)
			throws SQLException {
		while (resultSet.next()) {
			// Get data
			int id = resultSet.getInt("idCriteria");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");

			// Set data
			Criterion crit = new Criterion(parent, name);
			crit.setDescription(description);
			crit.setId(id);

			// Add subcriterion to parent
			parent.getCriteria().add(crit);
		}
	}

	/**
	 * Adds all criteria weightings to the appropriate node in the node
	 * structure
	 * 
	 * @param stmt
	 * @param goal
	 * @throws SQLException
	 */
	private void addWeightingsCriteria(Statement stmt, Goal goal)
			throws SQLException {
		// Select all weightings for the goal id
		String sqlQuery = "SELECT * FROM " + tblNameWeightingsCriteria
				+ " WHERE Goals_idGoals=?";

		PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
		prepStmt.setInt(1, goal.getId());

		// Execute query
		ResultSet resultSet = prepStmt.executeQuery();

		while (resultSet.next()) {
			// Get data
			int idCriterion1 = resultSet.getInt("Criteria_idCriteria");
			int idCriterion2 = resultSet.getInt("Criteria_idCriteria2");
			double value = resultSet.getDouble("Value");

			// Match ids to objects
			Criterion crit1 = AhpModel.findCriterionById(goal, idCriterion1);
			Criterion crit2 = AhpModel.findCriterionById(goal, idCriterion2);

			// Verify criteria have been found
			if ((crit1 == null) || (crit2 == null)) {
				throw new SQLException(
						"Possible data inconsistency. Weighting with nonexistent criteria returned");
			}

			// Create weighting
			Weighting_Criteria weighting = new Weighting_Criteria(crit1, crit2);
			weighting.setValue(value);

			// Add weighting to parent node
			crit1.getParent().getWeightings().add(weighting);
		}

	}

	/**
	 * Adds all alternative weightings to the appropriate node in the node
	 * structure
	 * 
	 * @param stmt
	 * @param goal
	 * @param alternatives
	 * @throws SQLException
	 */
	private void addWeightingsAlternatives(Statement stmt, Goal goal,
			ArrayList<Alternative> alternatives) throws SQLException {
		// Select all weightings for the goal id
		String sqlQuery = "SELECT * FROM " + tblNameWeightingsAlternatives
				+ " WHERE Goals_idGoals=?";

		PreparedStatement prepStmt = con.prepareStatement(sqlQuery);
		prepStmt.setInt(1, goal.getId());
		// Execute query
		ResultSet resultSet = prepStmt.executeQuery();

		while (resultSet.next()) {
			// Get data
			int idCriterion = resultSet.getInt("Criteria_idCriteria");
			int idAlternative1 = resultSet
					.getInt("Alternatives_idAlternatives");
			int idAlternative2 = resultSet
					.getInt("Alternatives_idAlternatives2");
			double value = resultSet.getDouble("Value");

			// Match ids to objects
			Criterion crit = AhpModel.findCriterionById(goal, idCriterion);
			// Verify criterion was found
			if (crit == null) {
				// This also happens when a model without criteria is saved to
				// the database
				throw new SQLException(
						"Possible data inconsistency. Alternative weighting with nonexistent criterion returned");
			}

			Alternative alt1 = AhpModel.findAlternativeById(alternatives,
					idAlternative1);
			Alternative alt2 = AhpModel.findAlternativeById(alternatives,
					idAlternative2);

			// Create weighting
			Weighting_Alternative weighting = new Weighting_Alternative(crit,
					alt1, alt2);
			weighting.setValue(value);

			// Add weighting to parent node
			crit.getWeightings().add(weighting);
		}
	}

	/**
	 * Loads the tab descriptions from the database
	 * 
	 * @param stmt
	 * @param goalId
	 * @param model
	 * @throws SQLException
	 */
	private void loadTabDescriptions(Statement stmt, int goalId, AhpModel model)
			throws SQLException {

		String sqlCmd = "SELECT * FROM " + tblNameTabDescriptions
				+ " WHERE Goals_idGoals=?";

		PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
		prepStmt.setInt(1, goalId);
		// Execute command
		ResultSet resultSet = prepStmt.executeQuery();

		if (resultSet.first()) {
			model.setTabDescAlternatives(resultSet
					.getString("AlternativesDesc"));
			model.setTabDescWeightingsCrit(resultSet
					.getString("WeightingsCriteriaDesc"));
			model.setTabDescWeightingsAlt(resultSet
					.getString("WeightingsAlternativesDesc"));
		}
	}

	/**
	 * Processes all tables and removes all entries matching the id of the goal
	 * 
	 * @param stmt
	 * @param idGoals
	 * @throws SQLException
	 */
	private void removeData(Statement stmt, int idGoals) throws SQLException {

		String sqlCmd;

		// Order is important due to foreign key checks
		String[] tableNames = { tblNameSubcriteria, tblNameTabDescriptions,
				tblNameWeightingsAlternatives, tblNameWeightingsCriteria,
				tblNameAlternatives, tblNameCriteria, };
		for (String tableName : tableNames) {
			sqlCmd = "DELETE FROM " + tableName + " WHERE Goals_idGoals=?";

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setInt(1, idGoals);

			// Execute command
			prepStmt.execute();
		}
	}

	/**
	 * Saves a goal to the database
	 * 
	 * @param stmt
	 * @param goal
	 * @throws SQLException
	 */
	private void saveGoal(Statement stmt, Goal goal) throws SQLException {
		String sqlCmd;
		// Check if goal already has an id
		if (!goal.hasId()) {
			sqlCmd = "INSERT INTO " + tblNameGoals + "(Name, Description)"
					+ " VALUES(?, ?)";

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setString(1, goal.getName());
			prepStmt.setString(2, goal.getDescription());

			// Execute command
			prepStmt.execute();

			// Get id given by server
			goal.setId(getLastServerGivenId(stmt));

		} else {
			sqlCmd = "INSERT INTO " + tblNameGoals
					+ "(idGoals, Name, Description)" + "VALUES(?, ?, ?)";

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setInt(1, goal.getId());
			prepStmt.setString(2, goal.getName());
			prepStmt.setString(3, goal.getDescription());

			// Execute command
			prepStmt.execute();
		}
	}

	/**
	 * Saves the alternatives to the database
	 * 
	 * @param stmt
	 * @param alternatives
	 * @param goal
	 * @throws SQLException
	 */
	private void saveAlternatives(Statement stmt,
			ArrayList<Alternative> alternatives, Goal goal) throws SQLException {
		for (Alternative alternative : alternatives) {
			saveAltCrit(stmt, alternative, goal);
		}
	}

	/**
	 * Saves an alternative or criterion to the database
	 * 
	 * @param stmt
	 * @param ahpObject
	 * @param goal
	 * @throws SQLException
	 */
	private void saveAltCrit(Statement stmt, AhpObject ahpObject, Goal goal)
			throws SQLException {

		// Get appropriate table name
		String tblName = getTblName(ahpObject);
		String sqlCmd;

		// Check if object already has an id
		if (!ahpObject.hasId()) {
			sqlCmd = "INSERT INTO " + tblName
					+ "(Name, Description, Goals_idGoals)" + " VALUES(?, ?, ?)";

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setString(1, ahpObject.getName());
			prepStmt.setString(2, ahpObject.getDescription());
			prepStmt.setInt(3, goal.getId());
			// Execute command
			prepStmt.execute();

			// Set id given by server
			ahpObject.setId(getLastServerGivenId(stmt));

		} else {
			String insert = "INSERT INTO " + tblName;
			String rows = "(id" + tblName
					+ ", Name, Description, Goals_idGoals)";
			String values = " VALUES(?, ?, ?, ?)";

			sqlCmd = insert + rows + values;

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setInt(1, ahpObject.getId());
			prepStmt.setString(2, ahpObject.getName());
			prepStmt.setString(3, ahpObject.getDescription());
			prepStmt.setInt(4, goal.getId());

			// Execute command
			prepStmt.execute();
		}

	}

	/**
	 * Save criterion and subcriteria
	 * 
	 * @param stmt
	 * @param criterion
	 * @param goal
	 * @throws SQLException
	 */
	private void saveCriterionAndSubcriteria(Statement stmt,
			Criterion criterion, Goal goal) throws SQLException {

		String sqlCmd;

		// Save criterion
		saveAltCrit(stmt, criterion, goal);

		// Look for subcriteria
		if (criterion.hasSubCriteria()) {
			for (Criterion subCriterion : criterion.getCriteria()) {

				// Save subcriteria
				saveCriterionAndSubcriteria(stmt, subCriterion, goal);

				// Create links in subcriteria-table
				sqlCmd = "INSERT INTO "
						+ tblNameSubcriteria
						+ "(Criteria_idCriteriaParent,Goals_idGoals,Criteria_idCriteriaChild)"
						+ " VALUES(?, ?, ?)";

				PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
				prepStmt.setInt(1, criterion.getId());
				prepStmt.setInt(2, goal.getId());
				prepStmt.setInt(3, subCriterion.getId());

				// Execute command
				prepStmt.execute();
			}
		}
	}

	/**
	 * Save the criteria weightings
	 * 
	 * @param stmt
	 * @param criteriaWeightings
	 * @param goal
	 * @throws SQLException
	 */
	private void saveCriteriaWeightings(Statement stmt,
			ArrayList<Weighting> criteriaWeightings, Goal goal)
			throws SQLException {

		String sqlCmd;
		for (Weighting weighting : criteriaWeightings) {

			sqlCmd = "INSERT INTO "
					+ tblNameWeightingsCriteria
					+ "(Criteria_idCriteria, Goals_idGoals, Criteria_idCriteria2, Value) "
					+ "VALUES(?, ?, ?, ?)";

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setInt(1, weighting.getFactor1().getId());
			prepStmt.setInt(2, goal.getId());
			prepStmt.setInt(3, weighting.getFactor2().getId());
			prepStmt.setDouble(4, weighting.getValue());

			// Execute command
			prepStmt.execute();
		}

	}

	/**
	 * Save the alternative weightings
	 * 
	 * @param stmt
	 * @param alternativeWeightings
	 * @param goal
	 * @throws SQLException
	 */
	private void saveAlternativeWeightings(Statement stmt,
			ArrayList<Weighting> alternativeWeightings, Goal goal)
			throws SQLException {

		String sqlCmd;
		for (Weighting weighting : alternativeWeightings) {

			// Cast weighting to gain access to parent criterion
			Weighting_Alternative weightingAlt = (Weighting_Alternative) weighting;

			sqlCmd = "INSERT INTO "
					+ tblNameWeightingsAlternatives
					+ "(Criteria_idCriteria, Goals_idGoals, Alternatives_idAlternatives, Alternatives_idAlternatives2, Value) "
					+ " VALUES(?, ?, ?, ?, ?)";

			PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
			prepStmt.setInt(1, weightingAlt.getParentNode().getId());
			prepStmt.setInt(2, goal.getId());
			prepStmt.setInt(3, weighting.getFactor1().getId());
			prepStmt.setInt(4, weighting.getFactor2().getId());
			prepStmt.setDouble(5, weighting.getValue());

			// Execute command
			prepStmt.execute();
		}

	}

	/**
	 * Saves the tab descriptions to the database
	 * 
	 * @param stmt
	 * @param goalId
	 * @param tabDescGoal
	 * @param tabDescCriteria
	 * @param tabDescAlternatives
	 * @param tabDescWeightingsCrit
	 * @param tabDescWeightingsAlt
	 * @throws SQLException
	 */
	private void saveTabDescriptions(Statement stmt, int goalId,
			String tabDescCriteria, String tabDescAlternatives,
			String tabDescWeightingsCrit, String tabDescWeightingsAlt)
			throws SQLException {

		String sqlCmd = "INSERT INTO "
				+ tblNameTabDescriptions
				+ "(Goals_idGoals, CriteriaDesc, AlternativesDesc, WeightingsCriteriaDesc, WeightingsAlternativesDesc) "
				+ "VALUES ( ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "CriteriaDesc=?, AlternativesDesc=?, WeightingsCriteriaDesc=?, WeightingsAlternativesDesc=?";

		PreparedStatement prepStmt = con.prepareStatement(sqlCmd);
		prepStmt.setInt(1, goalId);
		prepStmt.setString(2, tabDescCriteria);
		prepStmt.setString(3, tabDescAlternatives);
		prepStmt.setString(4, tabDescWeightingsCrit);
		prepStmt.setString(5, tabDescWeightingsAlt);
		prepStmt.setString(6, tabDescCriteria);
		prepStmt.setString(7, tabDescAlternatives);
		prepStmt.setString(8, tabDescWeightingsCrit);
		prepStmt.setString(9, tabDescWeightingsAlt);

		// Execute command
		prepStmt.execute();
	}

	/**
	 * @param stmt
	 * @param ahpObject
	 * @return the last Id given by the server
	 * @throws SQLException
	 */
	private int getLastServerGivenId(Statement stmt) throws SQLException {

		// Get last id given by server.
		// The function is connection-specific, so its return value
		// is not affected by another connection which is also performing
		// inserts.
		ResultSet resultSet = stmt.executeQuery(cmdSelectLastInsert);
		resultSet.first();
		return resultSet.getInt(1);
	}

	/**
	 * @param ahpObject
	 * @return the table name for the object
	 */
	private String getTblName(AhpObject ahpObject) {
		String tblName;
		if (ahpObject instanceof Goal) {
			tblName = tblNameGoals;
		} else if (ahpObject instanceof Alternative) {
			tblName = tblNameAlternatives;
		} else {
			tblName = tblNameCriteria;
		}
		return tblName;
	}

	/**
	 * @return the configured
	 */
	public boolean isConfigured() {
		return configured;
	}

	/**
	 * @param configured
	 *            the configured to set
	 */
	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

}
