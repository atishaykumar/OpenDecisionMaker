/**
 * 
 * @author Bender, Blocherer, Rossmehl and Rotter
 * 
 *         This file is part of Open Decision Maker.
 * 
 *         Open Decision Maker is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU General Public
 *         License as published by the Free Software Foundation, either
 *         version 3 of the License, or (at your option) any later version.
 * 
 *         Open Decision Maker is distributed in the hope that it will be
 *         useful, but WITHOUT ANY WARRANTY; without even the implied
 *         warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *         See the GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with Open Decision Maker. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
package r2b2.odm.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import r2b2.odm.sql.AhpCustomDbCon;

/**
 * (currently not in use) A composite for a custom JDBC connection.
 * 
 * @author Alex
 * 
 */
public class DbCustomConfig extends DbSelectComp {
	private Text classNameTxt;
	private Text conStringTxt;
	private AhpCustomDbCon customDbCon;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public DbCustomConfig(Composite parent, int style) {
		super(parent, style);

		customDbCon = new AhpCustomDbCon();
		super.setDbCon(customDbCon);

		classNameTxt = new Text(this, SWT.BORDER);
		classNameTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				customDbCon.setClassString(((Text) arg0.widget).getText());
			}
		});
		classNameTxt.setBounds(10, 60, 480, 21);

		Label lblJdbcDriverClass = new Label(this, SWT.NONE);
		lblJdbcDriverClass.setText("JDBC Driver Class Name");
		lblJdbcDriverClass.setBounds(10, 23, 225, 15);

		conStringTxt = new Text(this, SWT.BORDER);
		conStringTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				customDbCon.setConnectionString(((Text) arg0.widget).getText());
			}
		});
		conStringTxt.setBounds(10, 167, 480, 21);

		Label lblJdbcConnectionStrinc = new Label(this, SWT.NONE);
		lblJdbcConnectionStrinc.setText("JDBC Connection String");
		lblJdbcConnectionStrinc.setBounds(10, 136, 225, 15);

	}

	/* (non-Javadoc)
	 * @see r2b2.odm.gui.components.DbSelectComp#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
