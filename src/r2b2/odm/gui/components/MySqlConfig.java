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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import r2b2.odm.sql.AhpDbCon;
import r2b2.odm.sql.AhpMySqlCon;

/**
 * The composite for creating a MySql connection.
 * 
 * @author Alex
 * 
 */
public class MySqlConfig extends DbSelectComp {
	private Text serverTxt;
	private Text usernameTxt;
	private Text passwordTxt;
	private Text databaseTxt;
	AhpMySqlCon ahpMySqlCon;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public MySqlConfig(Composite parent, int style, AhpDbCon con) {
		super(parent, style);

		this.ahpMySqlCon = (AhpMySqlCon) con;

		FocusAdapter focAd = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((Text) e.widget).selectAll();
			}
		};

		serverTxt = new Text(this, SWT.BORDER);
		serverTxt.addFocusListener(focAd);
		serverTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				ahpMySqlCon.setServer(((Text) arg0.widget).getText());
			}
		});
		serverTxt.setBounds(295, 36, 195, 21);

		Label lblServer = new Label(this, SWT.NONE);
		lblServer.setBounds(10, 39, 225, 15);
		lblServer.setText("Server");

		usernameTxt = new Text(this, SWT.BORDER);
		usernameTxt.addFocusListener(focAd);
		usernameTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				ahpMySqlCon.setUsername(((Text) arg0.widget).getText());
			}
		});
		usernameTxt.setBounds(295, 73, 195, 21);

		Label lblUsername = new Label(this, SWT.NONE);
		lblUsername.setText("Username");
		lblUsername.setBounds(10, 76, 225, 15);

		passwordTxt = new Text(this, SWT.PASSWORD | SWT.BORDER);
		passwordTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				ahpMySqlCon.setPassword(((Text) arg0.widget).getText());
			}
		});
		passwordTxt.addFocusListener(focAd);
		passwordTxt.setBounds(295, 113, 195, 21);

		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setText("Password");
		lblPassword.setBounds(10, 116, 225, 15);

		databaseTxt = new Text(this, SWT.BORDER);
		databaseTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				ahpMySqlCon.setDatabase(((Text) arg0.widget).getText());
			}
		});
		databaseTxt.addFocusListener(focAd);
		databaseTxt.setBounds(295, 154, 195, 21);

		Label lblDatabase = new Label(this, SWT.NONE);
		lblDatabase.setText("Database");
		lblDatabase.setBounds(10, 157, 225, 15);

		// Copy initial values
		serverTxt.setText(ahpMySqlCon.getServer());
		usernameTxt.setText(ahpMySqlCon.getUsername());
		passwordTxt.setText(ahpMySqlCon.getPassword());
		databaseTxt.setText(ahpMySqlCon.getDatabase());

	}

	/* (non-Javadoc)
	 * @see r2b2.odm.gui.components.DbSelectComp#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * @return the ahpMySqlCon
	 */
	public AhpMySqlCon getAhpMySqlCon() {
		return ahpMySqlCon;
	}

	/**
	 * @param ahpMySqlCon
	 *            the ahpMySqlCon to set
	 */
	public void setAhpMySqlCon(AhpMySqlCon ahpMySqlCon) {
		this.ahpMySqlCon = ahpMySqlCon;
	}

}
