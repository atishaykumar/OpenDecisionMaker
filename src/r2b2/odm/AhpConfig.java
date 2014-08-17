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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import r2b2.odm.sql.AhpDbCon;
import r2b2.odm.sql.AhpMySqlCon;

/**
 * The configuration of the program
 * 
 * @author Alex
 * 
 */
public class AhpConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1526144371625961437L;

	double criticalCrValue = 0.1;

	AhpDbCon dbCon;

	/**
	 * @return the criticalCrValue
	 */
	public double getCriticalCrValue() {
		return criticalCrValue;
	}

	/**
	 * @param criticalCrValue
	 *            the criticalCrValue to set
	 */
	public void setCriticalCrValue(double criticalCrValue) {
		this.criticalCrValue = criticalCrValue;
	}

	/**
	 * @return the dbCon
	 */
	public AhpDbCon getDbCon() {
		if (dbCon == null) {
			dbCon = new AhpMySqlCon();
		}
		return dbCon;
	}

	/**
	 * @param dbCon
	 *            the dbCon to set
	 */
	public void setDbCon(AhpDbCon dbCon) {
		this.dbCon = dbCon;
	}

	/**
	 * Creates a full copy of the configuration.
	 * 
	 * @return a deep copy of the object.
	 */
	public AhpConfig clone() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out;
		AhpConfig config = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			config = (AhpConfig) in.readObject();
		} catch (IOException e) {
			// print out standard exception info
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// // print out standard exception info
			e.printStackTrace();
		}

		return config;
	}
}
