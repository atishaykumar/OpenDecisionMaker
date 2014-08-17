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
package r2b2.odm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;

import r2b2.odm.model.AhpModel;
import r2b2.odm.model.Goal;

/**
 * The Controller for save, load, sql, config and model interaction between the single tabs
 * @author Alex
 * 
 */
public class AhpController {

	/**
	 * The name of the default configuration file name
	 */
	public static final String CONFIGFILENAME = "OdmConfig.dat";

	/**
	 * The default ODM file extension
	 */
	public static final String ODMEXT = ".odm";

	AhpConfig config;
	AhpModel model;

	/**
	 * Creates a new AhpController object.
	 * 
	 * @param defaultConfig
	 * @throws IOException
	 */
	public AhpController(boolean defaultConfig) throws IOException {
		model = new AhpModel();
		if (defaultConfig) {
			config = new AhpConfig();
		} else
			tryLoadConfig();
	}

	/**
	 * Saves the current AhpModel to the file. Overwrites existing files.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void saveModelToFile(String fileName) throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(fileName));
			oos.writeObject(model);
			oos.close();
		} finally {
			if (oos != null)
				oos.close();
		}
	}

	/**
	 * Loads a saved AhpModel from a file.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void loadModelFromFile(String fileName) throws IOException {
		Object oIn = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fileName));
			oIn = ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
		if (oIn instanceof AhpModel) {
			model = (AhpModel) oIn;
		} else {
			throw new InvalidParameterException(
					"File does not contain a valid ahp model object.");
		}
	}

	/**
	 * Safes the current AhpModel to the database
	 * 
	 * @throws SQLException
	 */
	public void saveModelToDataBase() throws SQLException {
		config.getDbCon().save(model);
	}

	/**
	 * Loads a saved model from the database
	 * 
	 * @param goalId
	 *            the Id of the goal
	 * @throws SQLException
	 */
	public void loadModelFromDatabase(int goalId) throws SQLException {
		model = config.getDbCon().load(goalId);
	}

	/**
	 * Returns a list of all goals saved in the data base.
	 * 
	 * @return Arraylist
	 * @throws SQLException
	 */
	public ArrayList<Goal> getGoalsFromDataBase() throws SQLException {

		return config.getDbCon().getGoalList();
	}

	/**
	 * Saves the configuration to the default destination.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveConfig() throws FileNotFoundException, IOException {
		String fileName = CONFIGFILENAME;

		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(new FileOutputStream(fileName));
			oos.writeObject(config);
			oos.flush();
			oos.close();
		} finally {
			if (oos != null) {
				oos.close();
			}
		}
	}

	/**
	 * Can be used to verify a working database connection.
	 * @return <code>true</code> if the database connection has been configured
	 */
	public boolean isSqlConfigured() {
		return config.getDbCon().isConfigured();
	}

	/**
	 * @return the config
	 */
	public AhpConfig getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(AhpConfig config) {
		this.config = config;
	}

	/**
	 * @return the model
	 */
	public AhpModel getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(AhpModel model) {
		this.model = model;
	}

	/**
	 * Tries to load the configuration from the default path. Creates a new
	 * configuration if the configuration could not be found.
	 * 
	 * @throws IOException
	 */
	private void tryLoadConfig() throws IOException {

		File configFile = new File(CONFIGFILENAME);

		if (configFile.exists()) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(configFile));
				Object oIn;

				oIn = ois.readObject();
				if (oIn instanceof AhpConfig) {
					config = (AhpConfig) oIn;
				} else {
					config = new AhpConfig();
					saveConfig();
				}
				ois.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (ois != null) {
					ois.close();
				}
			}
		} else {
			config = new AhpConfig();
		}
	}

}
