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
package r2b2.odm.gui.components;

import org.eclipse.swt.widgets.Composite;

import r2b2.odm.sql.AhpDbCon;

/**
 * The base composite for selecting the database.
 * 
 * @author Alex
 * 
 */
public abstract class DbSelectComp extends Composite {

	private AhpDbCon dbCon;

	/**
	 * @return the dbCon
	 */

	public AhpDbCon getDbCon() {
		return dbCon;
	}

	/**
	 * @param con
	 *            the dbCon to set
	 */
	public void setDbCon(AhpDbCon con) {
		this.dbCon = con;
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DbSelectComp(Composite parent, int style) {
		super(parent, style);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
