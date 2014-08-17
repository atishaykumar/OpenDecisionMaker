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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import r2b2.odm.AhpController;
import r2b2.odm.gui.util.ResultListItem;
import r2b2.odm.model.AhpModel;
import r2b2.odm.model.Alternative;
import r2b2.odm.model.Criterion;
import r2b2.odm.model.Goal;
import r2b2.odm.reporting.ReportPrinter;

import com.swtdesigner.SWTResourceManager;

/**
 * Class for the Result Tab of the GUI
 * @author Rotter 
 */
public class CompResult extends Composite {

	private Table tableResult;
	private Table tableCritAlt;
	private Table tableCR;
	private Hashtable<Double, String> crValues;
	private ArrayList<Criterion> critList;
	private double criticalCR;
	private String[][] matrix;
	private boolean sortFlag;
	private DecimalFormat df;
	protected AhpModel model;
	protected Shell shell;
	public boolean calcFlag = false;

	/**
	 * set the boolean if the Calculation can be done
	 * 
	 * @param calcFlag
	 */
	public void setCalcFlag(boolean calcFlag) {
		this.calcFlag = calcFlag;
	}

	AhpController controller;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompResult(Composite parent, int style) {
		super(parent, SWT.NONE);
		setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		setLayout(new GridLayout(5, false));

		shell = getShell();

		Label lblStepResult = new Label(this, SWT.NONE);
		lblStepResult.setText("Step 6: Result");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label seperator_top = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator_top.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 5, 1));

		Label labelResult = new Label(this, SWT.NONE);
		labelResult.setFont(SWTResourceManager.getFont("Segoe UI", 9,
				SWT.NORMAL));
		labelResult.setText("Result/Ranking");
		new Label(this, SWT.NONE);

		Label lblTotalInconsistancyFactor = new Label(this, SWT.CENTER);
		lblTotalInconsistancyFactor
				.setToolTipText("The Consistancy Ratio CR  represents the performance of the AHP.\r\n\r\nA High Consistancy Ratio is a sign for unlogical weightings\r\n\r\nAn AHP Analysis with a CR Value above 0,1 is considered as random \r\nIn such cases the results shouldn't be used to make a decision.");
		lblTotalInconsistancyFactor.setFont(SWTResourceManager.getFont(
				"Segoe UI", 9, SWT.NORMAL));
		GridData gd_lblTotalInconsistancyFactor = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_lblTotalInconsistancyFactor.heightHint = 23;
		lblTotalInconsistancyFactor
				.setLayoutData(gd_lblTotalInconsistancyFactor);
		lblTotalInconsistancyFactor.setText("Consistency Ratios CRs: ");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		tableResult = new Table(this, SWT.BORDER);
		tableResult.setLinesVisible(true);
		GridData gd_tableResult = new GridData(SWT.LEFT, SWT.FILL, false, true,
				2, 1);
		gd_tableResult.heightHint = 135;
		gd_tableResult.widthHint = 298;
		tableResult.setLayoutData(gd_tableResult);
		tableResult.setHeaderVisible(true);

		TableColumn tblclmnRanking = new TableColumn(tableResult, SWT.NONE);
		tblclmnRanking.setWidth(55);
		tblclmnRanking.setText("Ranking");

		TableColumn tblclmnAlternative = new TableColumn(tableResult, SWT.NONE);
		tblclmnAlternative.setWidth(169);
		tblclmnAlternative.setText("Alternative");

		TableColumn tblclmnValue = new TableColumn(tableResult, SWT.NONE);
		tblclmnValue.setWidth(91);
		tblclmnValue.setText("Value");

		tableCR = new Table(this, SWT.BORDER | SWT.None);
		GridData gd_tableCR = new GridData(SWT.LEFT, SWT.FILL, true, true, 3, 1);
		gd_tableCR.heightHint = 116;
		gd_tableCR.widthHint = 215;
		tableCR.setLayoutData(gd_tableCR);
		tableCR.setHeaderVisible(true);
		tableCR.setLinesVisible(true);

		TableColumn tblclmnCriterionName = new TableColumn(tableCR, SWT.NONE);
		tblclmnCriterionName.setWidth(153);
		tblclmnCriterionName.setText("Name");

		TableColumn tblclmnCrValue = new TableColumn(tableCR, SWT.NONE);
		tblclmnCrValue.setWidth(79);
		tblclmnCrValue.setText("CR Value");
		// Sorting Listener
		Listener sortListener = new Listener() {
			public void handleEvent(Event e) {
				sortCRMatrix();
			}
		};
		// add listeners to columns
		tblclmnCriterionName.addListener(SWT.Selection, sortListener);
		tblclmnCrValue.addListener(SWT.Selection, sortListener);

		Label lblAlternativecriterionMatrix = new Label(this, SWT.NONE);
		lblAlternativecriterionMatrix.setLayoutData(new GridData(SWT.LEFT,
				SWT.BOTTOM, false, false, 1, 1));
		lblAlternativecriterionMatrix.setFont(SWTResourceManager.getFont(
				"Segoe UI", 9, SWT.NORMAL));
		lblAlternativecriterionMatrix.setText("Alternative/Criterion Matrix");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		tableCritAlt = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tableCritAlt = new GridData(SWT.FILL, SWT.FILL, true, true,
				5, 1);
		gd_tableCritAlt.heightHint = 96;
		gd_tableCritAlt.widthHint = 607;
		tableCritAlt.setLayoutData(gd_tableCritAlt);
		tableCritAlt.setHeaderVisible(true);
		tableCritAlt.setLinesVisible(true);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label seperator_bottom = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator_bottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 5, 1));

		// Button for the sensitivity analysis
		final Button btn_Sensitivity = new Button(this, SWT.NONE);
		// Listener to open the Sensitivity Analysis window
		btn_Sensitivity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				SensitivityAnalysisDialog dlg = new SensitivityAnalysisDialog(
						shell, SWT.None);
				dlg.open(model.getGoal(), model.getAlternatives());
			}
		});

		GridData gd_btn_Sensitivity = new GridData(SWT.RIGHT, SWT.TOP, true,
				false, 1, 1);
		gd_btn_Sensitivity.heightHint = 42;
		gd_btn_Sensitivity.widthHint = 138;
		btn_Sensitivity.setLayoutData(gd_btn_Sensitivity);
		btn_Sensitivity.setImage(SWTResourceManager.getImage(CompResult.class,
				"/r2b2/odm/gui/resources/chart_bar-32.png"));

		btn_Sensitivity.setText("Show Sensitivity");
		/**
		 * chart_bar-32.png Part of the Primo Icon Set by Webdesigner Depot
		 * http://www.webdesignerdepot.com
		 */

		final Button btn_Print = new Button(this, SWT.NONE);
		btn_Print.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				//
				//
				// MessageDialog.openInformation(shell,
				// "Operation Successfull", "Load successful.");
				//

				FileDialog fd = new FileDialog(shell, SWT.SAVE);
				fd.setFilterExtensions(new String[] { "*.pdf", "*.*" });
				String filePath = fd.open();
				if (!(filePath == null)) {

					shell.setEnabled(false);
					shell.setCursor(new Cursor(getDisplay(), SWT.CURSOR_WAIT));
					ReportPrinter reportPrinter = new ReportPrinter();
					reportPrinter.printReport(model, controller.getConfig()
							.getCriticalCrValue(), filePath);
					shell.setEnabled(true);
					shell.setCursor(new Cursor(getDisplay(), SWT.CURSOR_ARROW));
				}

			}

		}

		);
		GridData gd_btn_Print = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btn_Print.widthHint = 122;
		btn_Print.setLayoutData(gd_btn_Print);
		btn_Print.setText("Create Pdf");
		btn_Print.setImage(SWTResourceManager.getImage(CompResult.class,
				"/r2b2/odm/gui/resources/Pdf-32.png"));
		/**
		 * pdf icon Title: DelliOS System Icons Author: Wendell Fernandes
		 * http://www.dellustrations.com/
		 * 
		 * License: This is a collection of icon that can be used commercially
		 * and for personal projects.
		 * 
		 * © All rights reserved Wendell Fernandes -
		 * http://www.dellustrations.com/276
		 */
		new Label(this, SWT.NONE);

		Label label_help = new Label(this, SWT.NONE);
		label_help
				.setToolTipText("Create Pdf:\r\nA pdf file with all data of this odm project will be created\r\n\r\nShow Sensitivity:\r\nA window with a simulation of the stabilitiy of the top level criteria will be displayed\r\n\r\nIf the buttons are not enabled, the criteria and/or alternatives are not fully rated");
		label_help.setAlignment(SWT.CENTER);
		label_help
				.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		GridData gd_label_help = new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1);
		gd_label_help.widthHint = 31;
		label_help.setLayoutData(gd_label_help);
		label_help.setText("?");

		final Button btn_Back = new Button(this, SWT.NONE);
		btn_Back.setToolTipText("go to the previous tab Weighting Alternatives");
		btn_Back.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				back();
			}
		});
		GridData gd_btn_Back = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_btn_Back.heightHint = 25;
		gd_btn_Back.widthHint = 89;
		btn_Back.setLayoutData(gd_btn_Back);
		btn_Back.setText("Back");
		// Listener to update the Tab when it is selected
		this.addListener(SWT.Show, new Listener() {

			@Override
			public void handleEvent(Event event) {
				/**
				 * Calculate the New Data of the new Input if the model is fully
				 * filled out if that is not the case, its not possible to
				 * create the pdf or to show the sensitivity analysis and the
				 * buttons are disabled
				 */

				if (calcFlag == false) {
					btn_Print.setEnabled(false);
					btn_Sensitivity.setEnabled(false);
					btn_Back.setEnabled(false);
					emptyResultTable();
					emptyCRMatrix();
					emptyAltCritTable();

				} else {
					calculateAllResults(model);
					btn_Print.setEnabled(true);
					btn_Sensitivity.setEnabled(true);
					btn_Back.setEnabled(true);
				}

			}
		});

	}

	/**
	 * sort the Consistancy Ratio, eighter descending or ascending
	 */
	private void sortCRMatrix() {
		emptyCRMatrix();
		Vector<Double> doubleValues = new Vector<Double>(crValues.keySet());
		if (sortFlag == false) {
			sortFlag = true;
			Collections.sort(doubleValues);
		} else {
			sortFlag = false;
			Collections.sort(doubleValues, Collections.reverseOrder());
		}

		for (int i = 0; i < doubleValues.size(); i++) {
			Double key = doubleValues.elementAt(i);
			addSingleCrValue(crValues.get(key), key);

		}

	}

	/**
	 * 
	 * @param modelly
	 *            Complete AHP Model containing Goal, Alternatives, Criteria,
	 *            Inconsistancy
	 * @return String Matrix consisting of Ranking, Alternative and Value
	 */
	private String[][] createTableDataResult(AhpModel modelly) {
		double comparer;
		ResultListItem mod;
		Goal goally = modelly.getGoal();
		df = new DecimalFormat("0.00%");

		double[] resultModel = goally.getAlternativeValues(false);

		// Copy results to a new array to leave original unchanged
		double[] results = Arrays.copyOf(resultModel, resultModel.length);

		ArrayList<Alternative> alternatives = modelly.getAlternatives();
		// Temporary Array for the unsorted Result
		ResultListItem[] rmodsTemp = new ResultListItem[results.length];
		String[][] matrix = new String[results.length][3];

		// fill list with data
		for (int i = 0; i < results.length; i++) {
			ResultListItem rmod = new ResultListItem();
			rmod.setResult(results[i]);
			rmod.setAlternative(alternatives.get(i).toString());
			rmodsTemp[i] = rmod;

		}
		// sort result
		Arrays.sort(results);
		// sort the table
		for (int i = 0; i < results.length; i++) {
			comparer = results[i];
			for (int j = 0; j < results.length; j++) {
				mod = rmodsTemp[j];
				if (comparer == mod.getResult()) {
					mod.setRanking(results.length - i);
					matrix[i][0] = String.valueOf(mod.getRanking());
					matrix[i][1] = mod.getAlternative();
					matrix[i][2] = String.valueOf(df.format(comparer));
					break;
				}
			}
		}
		// Return sorted String Matrix
		return matrix;
	}

	/**
	 * Adds a new row to the result table
	 * 
	 * @param dataRow
	 *            String Array containg Ranking, Alternative Name, Value for
	 *            each Row
	 */
	private void fillResultTable(AhpModel modelly) {
		matrix = createTableDataResult(modelly);

		for (int i = matrix.length - 1; i >= 0; i--) {
			String[] dataRow = new String[3];
			dataRow[0] = matrix[i][0];
			dataRow[1] = matrix[i][1];
			dataRow[2] = matrix[i][2];
			// generate a new Array Item
			TableItem tableItem = new TableItem(tableResult, SWT.NONE);
			tableItem.setText(dataRow);
		}

	}

	/**
	 * Fills the CR Matrix with the Consistency Ratio values of all levels
	 * 
	 * @param goally
	 * @param criticalCR
	 */

	private void generateCrMatrix(Goal goally) {
		// Emptys hashlist which is used to store the real double values
		crValues = new Hashtable<Double, String>();
		critList = goally.getCriteria();
		addSingleCrValue(goally.getName(), goally.getConsistencyRatio());
		crValues.put(new Double(goally.getConsistencyRatio()), goally.getName());
		// iterate over critList to get the sublevels
		for (Iterator<Criterion> iterator = critList.iterator(); iterator
				.hasNext();) {
			Criterion criterion = (Criterion) iterator.next();

			if (criterion.hasSubCriteria() == true) {
				getSubCriteria(criterion);
			} else {
				addSingleCrValue(criterion.getName(),
						criterion.getConsistencyRatio());
				crValues.put(criterion.getConsistencyRatio(),
						criterion.getName());

			}

		}

	}

	/**
	 * Gets all subcriteria below a certain criterion
	 * 
	 * @param criterionWithSubs
	 *            criterion with subcriteria
	 * @param criticalCR
	 *            the critical CR value
	 */
	private void getSubCriteria(Criterion criterionWithSubs) {
		addSingleCrValue(criterionWithSubs.getName(),
				criterionWithSubs.getConsistencyRatio());
		crValues.put(criterionWithSubs.getConsistencyRatio(),
				criterionWithSubs.getName());

		ArrayList<Criterion> subCrits = criterionWithSubs.getCriteria();

		for (Iterator<Criterion> iterator = subCrits.iterator(); iterator
				.hasNext();) {
			Criterion criterion = (Criterion) iterator.next();

			if (criterion.hasSubCriteria() == true) {
				getSubCriteria(criterion);
			} else {
				addSingleCrValue(criterion.getName(),
						criterion.getConsistencyRatio());
				crValues.put(criterion.getConsistencyRatio(),
						criterion.getName());

			}

		}

	}

	/**
	 * Adds a single Criterion CR Pair to the Consistancy Ratio Matrix
	 * 
	 * @param criterionName
	 *            the Criterion name
	 * @param crValue
	 *            the Consistancy Ratio (CR) value
	 * @param criticalCrValue
	 *            the CR value set by the user
	 * 
	 */
	private void addSingleCrValue(String criterionName, double crValue) {
		// formating the output for the table
		df = new DecimalFormat("0.0000");
		// generate Colors Red and Black
		Color red = new Color(null, 255, 0, 0);
		Color black = new Color(null, 0, 0, 0);

		String dataRow[] = { criterionName, df.format(crValue) };
		TableItem tableItem = new TableItem(tableCR, SWT.NONE);
		if (crValue > criticalCR) {
			tableItem.setForeground(1, red);
		} else {
			tableItem.setForeground(1, black);
		}
		tableItem.setText(dataRow);
	}

	/**
	 * Generates a Matrice consisting of all Criteria and
	 * Alternattive_Weightings
	 * 
	 * @param modelly
	 *            AHPModel input
	 * @return String Matrix
	 */
	private String[][] generateCritAltMatrix(AhpModel modelly) {

		// generate Goal and Criterion Objects
		Goal goally = new Goal("TempGoal");
		Criterion crit;
		Alternative altern;
		// integer for indexes of the string matrix
		int i = 1;

		// double array for the results of each alternative
		double[] alterValues;
		// set the format of to %
		df = new DecimalFormat("#.00%");
		// string matrix for the return
		String[][] critAltMatrix;

		// generate Arraylist with all Alternatives and Criterion
		ArrayList<Alternative> altList = new ArrayList<Alternative>();
		ArrayList<Criterion> Crity = new ArrayList<Criterion>();

		// get the needed lists (criterion and alternative list)
		goally = modelly.getGoal();
		Crity = goally.getCriteria();
		altList = modelly.getAlternatives();

		// initiate and resize the return matrix
		critAltMatrix = new String[Crity.size() + 1][altList.size() + 1];

		// get all criterion names and write them into the result matrice
		for (Iterator<Criterion> iter = Crity.iterator(); iter.hasNext();) {
			crit = iter.next();
			// get the values of a criterion for all alternatives
			alterValues = crit.getAlternativeValues();
			for (int j = 1; j < alterValues.length + 1; j++) {
				critAltMatrix[i][j] = df.format(alterValues[j - 1]);
			}
			// fill in all Criterion names
			critAltMatrix[i][0] = crit.getName();
			i++;
		}
		i = 1;
		for (Iterator<Alternative> iter = altList.iterator(); iter.hasNext();) {
			altern = iter.next();
			// fill in all Alternative names
			critAltMatrix[0][i] = altern.getName();
			i++;
		}
		return critAltMatrix;

	}

	/**
	 * Inserts all needed Columns to the Cirterion / Result Matrix
	 * 
	 * @param critAltMatrix
	 */
	private void generateCriterionAlternativeMatrice(AhpModel modelly) {
		// refactoring of matrix Variable
		matrix = generateCritAltMatrix(modelly);
		// generate Columns, first column always the same
		TableColumn tblclmnCriteria = new TableColumn(tableCritAlt, SWT.NONE, 0);
		tblclmnCriteria.setWidth(150);
		tblclmnCriteria.setText("Alternative/Criterion");
		for (int i = 1; i < matrix.length; i++) {
			TableColumn column = new TableColumn(tableCritAlt, SWT.NONE, i);
			column.setWidth(100);
			column.setText(matrix[i][0]);
		}
		// Fill the matrice
		fillCritAltMatrice(matrix);
	}

	/**
	 * Fills the Criteria Alternative Matrice with the data values
	 * 
	 * @param critAltMatrix
	 *            criteria/Alternative Matrice with all Alternative/Criterion
	 *            Labels and Alternative/Criterion Values
	 */
	private void fillCritAltMatrice(String[][] critAltMatrix) {

		String[] dataRow = new String[critAltMatrix.length];
		for (int i = 1; i < critAltMatrix[0].length; i++) {
			TableItem tableItem = new TableItem(tableCritAlt, SWT.NONE);
			for (int j = 0; j < dataRow.length; j++) {

				dataRow[j] = critAltMatrix[j][i];

			}
			tableItem.setText(dataRow);

		}

	}

	/**
	 * removes all tableItems of the resulttable
	 */
	private void emptyResultTable() {
		tableResult.removeAll();
	}

	/**
	 * removes all data from the Critierion Ratio
	 */
	private void emptyCRMatrix() {
		tableCR.removeAll();
	}

	/**
	 * removes all tableItems and all columns beside the first column of the
	 * Criterion/Alternative Table
	 */
	private void emptyAltCritTable() {
		// remove all rows
		tableCritAlt.removeAll();
		// remove all columns
		TableColumn[] tbcRemove = tableCritAlt.getColumns();
		for (int i = 0; i < tbcRemove.length; i++) {
			tbcRemove[i].dispose();
		}

	}

	/**
	 * Public Method to Display an already calculated AHPModel in the Result Tab
	 * 
	 * @param model
	 */
	public void calculateAllResults(AhpModel model) {

		model.calculateResults();

		Goal tempGoal;
		tempGoal = model.getGoal();
		criticalCR = controller.getConfig().getCriticalCrValue();

		// Fill Resulttable
		emptyResultTable();
		fillResultTable(model);
		// Fill Alternative Criterion Matrix
		emptyAltCritTable();
		generateCriterionAlternativeMatrice(model);
		emptyCRMatrix();
		generateCrMatrix(tempGoal);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Set the AHP Model for this Tab
	 * 
	 * @param ahpModel
	 */
	public void setAhpModel(AhpModel ahpModel) {
		model = ahpModel;

	}

	/**
	 * Set The controller for this Tab
	 * 
	 * @param controller
	 */
	public void setController(AhpController controller) {
		this.controller = controller;
	}

	/**
	 * Method to go to the previous tab
	 */
	protected void back() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(4);
	}

}
