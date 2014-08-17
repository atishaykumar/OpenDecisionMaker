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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import r2b2.odm.AhpController;
import r2b2.odm.model.AhpModel;

/**
 * Represents the central TabFolder in the middle of the GUI
 * 
 * @author Wolfgang
 * 
 */
public class GuiTabFolder extends Composite {

	// folder
	private TabFolder tabFolder;

	// tabs
	private CompGoal compGoal;
	private CompWeightingAlternatives weightingAlternatives;
	private CompInsertCriteria compInsertCriteria;
	private CompWeightingCriteria compWeightingCriteria;
	private CompInsertAlternatives compInsertAlternatives;
	private CompResult result;

	
	private AhpController controller;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param controller
	 */
	public GuiTabFolder(Composite parent, int style, AhpController controller) {
		super(parent, style);
		this.controller = controller;
		// layout
		setLayout(new GridLayout(1, false));

		// folder
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		// tabs
		TabItem tbtmProblem = new TabItem(tabFolder, SWT.NONE);
		tbtmProblem.setText("Goal");
		compGoal = new CompGoal(tabFolder, SWT.NONE);
		tbtmProblem.setControl(compGoal);

		TabItem tbtmSolutions = new TabItem(tabFolder, SWT.NONE);
		tbtmSolutions.setText("Alternatives");
		compInsertAlternatives = new CompInsertAlternatives(tabFolder, SWT.NONE);
		tbtmSolutions.setControl(compInsertAlternatives);

		TabItem tbtmCriteria = new TabItem(tabFolder, SWT.NONE);
		tbtmCriteria.setText("Criteria");
		compInsertCriteria = new CompInsertCriteria(tabFolder, SWT.NONE);
		tbtmCriteria.setControl(compInsertCriteria);

		TabItem tbtmWeightingcriteria = new TabItem(tabFolder, SWT.NONE);
		tbtmWeightingcriteria.setText("Weighting Criteria");
		compWeightingCriteria = new CompWeightingCriteria(tabFolder, SWT.NONE);
		tbtmWeightingcriteria.setControl(compWeightingCriteria);

		TabItem tbtmWeightingAlternatives_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmWeightingAlternatives_1.setText("Weighting Alternatives");
		weightingAlternatives = new CompWeightingAlternatives(tabFolder,
				SWT.NONE);
		tbtmWeightingAlternatives_1.setControl(weightingAlternatives);

		TabItem tbtmResult = new TabItem(tabFolder, SWT.NONE);
		tbtmResult.setText("Result");
		result = new CompResult(tabFolder, SWT.NONE);
		tbtmResult.setControl(result);
		result.setController(controller);

	}

	/**
	 * This is the method to set an AhpModel to the GUI (GuiTabFolder)
	 * 
	 * @param ahpModel
	 */
	public void setAhpModel(AhpModel ahpModel) {
		// deliver the ahpModel to all tabs
		weightingAlternatives.setAhpModel(ahpModel);
		compGoal.setAhpModel(ahpModel);
		compInsertAlternatives.setAhpModel(ahpModel);
		compWeightingCriteria.setAhpModel(ahpModel);
		compInsertCriteria.setAhpModel(ahpModel);
		result.setAhpModel(ahpModel);

		// Select the first tab an refresh it
		tabFolder.setSelection(0);
		compGoal.refresh();

		// refresh the preconditions
		refreshPreConditons();

		// refresh the layout
		tabFolder.layout();
	}

	public void setController(AhpController controller) {
		this.controller = controller;
	}

	/**
	 * Refreshes the preconditions for the next buttons of all tabs.
	 */
	public void refreshPreConditons() {
		// set all pre conditions to false
		compInsertAlternatives.setPreCondIsFulfilled(false);
		compInsertCriteria.setPreCondIsFulfilled(false);
		compWeightingCriteria.setPreCondIsFulfilled(false);
		weightingAlternatives.setPreCondIsFulfilled(false);
		result.setCalcFlag(false);

		// refresh the integreties of the steps and set the preconditions
		compGoal.refreshIntegrity();
		if (compGoal.isIntegrity()) {

			compInsertAlternatives.setPreCondIsFulfilled(true);
			compInsertAlternatives.refreshIntegrity();
			if (compInsertAlternatives.isIntegrity()) {

				compInsertCriteria.setPreCondIsFulfilled(true);
				compInsertCriteria.refreshIntegrity();
				if (compInsertCriteria.isIntegrity()) {

					compWeightingCriteria.setPreCondIsFulfilled(true);
					weightingAlternatives.setPreCondIsFulfilled(true);

					compWeightingCriteria.refreshIntegrity();
					if (compWeightingCriteria.isIntegrity()) {

						weightingAlternatives.setIntegrity2(true);
						weightingAlternatives.refreshIntegrity();
						if (weightingAlternatives.isIntegrity()) {
							result.setCalcFlag(true);
						}
					}
				}
			}
		}
	}

}
