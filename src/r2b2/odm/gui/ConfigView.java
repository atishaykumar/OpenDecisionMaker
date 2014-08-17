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
package r2b2.odm.gui;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import r2b2.odm.AhpConfig;
import r2b2.odm.gui.components.MySqlConfig;
import r2b2.odm.sql.AhpDbCon;
import r2b2.odm.sql.AhpMySqlCon;

/**
 * The view to configure the program.
 * 
 * @author Alex
 * 
 */

public class ConfigView extends Dialog {

	protected AhpConfig result;
	protected Shell shlOdmConfiguration;
	private Button customDbRdbtn;
	private Button mySqlRdbtn;
	private MySqlConfig dbSelectedComp;
	private Composite databaseComp;

	private AhpConfig config;
	private Button btnCreateDbStructure;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ConfigView(Shell parent, int style, AhpConfig config) {
		super(parent, style);
		setText("SWT Dialog");
		// Clone config to prevent unwanted changes to the old configuration
		this.config = config.clone();
	}

	/**
	 * Open the dialog.
	 * 
	 * @return Will return the new AhpConfig object, or <code>null</code> if
	 *         canceled
	 * 
	 */
	public AhpConfig open() {
		createContents();
		shlOdmConfiguration.open();
		shlOdmConfiguration.layout();
		Display display = getParent().getDisplay();

		while (!shlOdmConfiguration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlOdmConfiguration = new Shell(getParent(), SWT.DIALOG_TRIM);
		shlOdmConfiguration.setSize(549, 549);
		shlOdmConfiguration.setText("ODM Configuration");

		Composite crComp = new Composite(shlOdmConfiguration, SWT.BORDER);
		crComp.setBounds(10, 12, 523, 64);

		Spinner spinner = new Spinner(crComp, SWT.BORDER);
		spinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String valText = ((Spinner) arg0.widget).getText();
				NumberFormat fmt = NumberFormat.getInstance();
				Number number;
				try {
					number = fmt.parse(valText);
					config.setCriticalCrValue(number.doubleValue());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
		spinner.setToolTipText("All weightings exceeding this value will be flagged as potential inconsistencies. Literature recommends a value of 0.1.");
		spinner.setSelection((int) (config.getCriticalCrValue() * 100));
		spinner.setDigits(2);
		spinner.setBounds(224, 26, 66, 22);

		databaseComp = new Composite(shlOdmConfiguration, SWT.BORDER);
		databaseComp.setBounds(10, 82, 523, 394);

		mySqlRdbtn = new Button(databaseComp, SWT.RADIO);
		mySqlRdbtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doDbSelectionChanged();
			}
		});
		mySqlRdbtn.setBounds(10, 25, 90, 16);
		mySqlRdbtn.setVisible(false);
		mySqlRdbtn.setText("Use MySQL");

		customDbRdbtn = new Button(databaseComp, SWT.RADIO);
		customDbRdbtn.setEnabled(false);
		customDbRdbtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doDbSelectionChanged();
			}
		});
		customDbRdbtn.setBounds(143, 25, 186, 16);
		customDbRdbtn.setVisible(false);
		customDbRdbtn.setText("Use custom JDBC connection");

		dbSelectedComp = new MySqlConfig(databaseComp, SWT.NONE,
				config.getDbCon());
		dbSelectedComp.setBounds(10, 64, 500, 273);

		Button btnTestConnection = new Button(databaseComp, SWT.NONE);
		btnTestConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doTestConnection();
			}
		});
		btnTestConnection.setBounds(93, 355, 115, 25);
		btnTestConnection.setText("Test Connection");

		btnCreateDbStructure = new Button(databaseComp, SWT.NONE);
		btnCreateDbStructure.setEnabled(false);
		btnCreateDbStructure.setBounds(265, 355, 159, 25);
		btnCreateDbStructure.setText("Create Database Structure");

		Label crLbl = new Label(crComp, SWT.NONE);
		crLbl.setToolTipText("All weightings exceeding this value will be flagged as potential inconsistencies. Literature recommends a value of 0.1.");
		crLbl.setBounds(10, 29, 148, 15);
		crLbl.setText("Critical Consistency Ratio:");

		Button btnOk = new Button(shlOdmConfiguration, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AhpDbCon dbCon = dbSelectedComp.getAhpMySqlCon();

				if (!dbCon.isConfigured()) {
					if (!doTestConnection()) {
						boolean confirm = MessageDialog
								.openConfirm(shlOdmConfiguration, "",
										"Do you want to continue without working database connection?");

						// Cancel selection if user does not confirm
						if (!confirm)
							return;
					}
				}

				config.setDbCon(dbCon);

				result = config;

				shlOdmConfiguration.close();
			}
		});
		btnOk.setBounds(377, 488, 75, 25);
		btnOk.setText("OK");

		Button cancelBtn = new Button(shlOdmConfiguration, SWT.NONE);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				shlOdmConfiguration.close();
			}
		});
		cancelBtn.setText("Cancel");
		cancelBtn.setBounds(458, 488, 75, 25);

		// Copy initial values
		spinner.setSelection((int) (config.getCriticalCrValue() * (Math.pow(10,
				spinner.getDigits()))));

	}

	/**
	 * (Currently unused) Called when the user selects a different way of connecting to a database.
	 */
	public void doDbSelectionChanged() {
		Rectangle rec = dbSelectedComp.getBounds();
		dbSelectedComp.dispose();
		dbSelectedComp = new MySqlConfig(databaseComp, SWT.NONE,
				(AhpMySqlCon) config.getDbCon());
		dbSelectedComp.setBounds(rec);

	}

	/**
	 * Tries to connect to the data base and verify that the required data
	 * tables exist.
	 * 
	 * @return <code>true</code> if the connection is fully functional
	 */
	public boolean doTestConnection() {
		AhpDbCon ahpDbCon = dbSelectedComp.getAhpMySqlCon();

		try {
			boolean hasDbStructure = ahpDbCon.testConnection();
			if (!hasDbStructure) {
				boolean confirm = MessageDialog
						.openConfirm(
								shlOdmConfiguration,
								"No Database Structure",
								"Connection could be established. However, the required data tables have not been found. Click \"OK \" to create the data tables.");

				if (confirm) {
					hasDbStructure = ahpDbCon.createDbStructure();

					if (hasDbStructure) {
						MessageDialog
								.openInformation(shlOdmConfiguration,
										"Success",
										"The data tables have been created.");
					}
				} else
					btnCreateDbStructure.setEnabled(true);
			}

			if (hasDbStructure) {
				MessageDialog
						.openInformation(
								shlOdmConfiguration,
								"Success",
								"The connection to the database is working and the required data structure exists.");
			}
		} catch (SQLException e) {
			MessageDialog.openError(shlOdmConfiguration, "Error",
					e.getMessage());
			return false;
		}

		return true;
	}
}
