/**
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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import r2b2.odm.gui.components.SaAlternativeWeightingBarComp;
import r2b2.odm.gui.components.SaCriterionWeightingChangedEvent;
import r2b2.odm.gui.components.SaCriterionWeightingChangedListener;
import r2b2.odm.gui.components.SaCriterionWeightingSelectComp;
import r2b2.odm.model.Alternative;
import r2b2.odm.model.Criterion;
import r2b2.odm.model.base.AhpNode;

/**
 * A dialog providing basic sensitivity analysis functionality to the user. All
 * actions of the user leave the original data used as basis for the analysis
 * unchanged.
 * 
 * @author Bender
 * 
 */
public class SensitivityAnalysisDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * The criteria weightings
	 */
	private double[] c;
	/**
	 * The alternative values
	 */
	private double[] a;
	/**
	 * The top node for the analysis
	 */
	private AhpNode parentNode;
	/**
	 * The alternatives
	 */
	ArrayList<Alternative> alternatives;

	private SashForm sashForm;
	private SaCriterionWeightingSelectComp[] weightingSelectComponents;
	private SaAlternativeWeightingBarComp[] alternativeBarComponents;
	private ScrolledComposite scrolledComposite;
	private Composite SaCriterionListComp;
	private ScrolledComposite scrolledComposite_1;
	private Composite SaAlternativeWeighingComp;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SensitivityAnalysisDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open(AhpNode node, ArrayList<Alternative> alternatives) {
		parentNode = node;
		this.alternatives = alternatives;

		// Load the weightings and values, do not calculate new values.
		c = node.getCriteriaWeighting(false);
		a = node.getAlternativeValues(false);

		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(750, 450);
		shell.setText("Sensitivity Analysis for: " + parentNode.getName());
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		sashForm = new SashForm(shell, SWT.NONE);

		scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		SaCriterionListComp = new Composite(scrolledComposite, SWT.NONE);
		SaCriterionListComp.setLayout(new RowLayout(SWT.VERTICAL));

		// Create a criterion weighting SaCriterionListComp for every criterion
		weightingSelectComponents = new SaCriterionWeightingSelectComp[c.length];

		ArrayList<Criterion> criteria = parentNode.getCriteria();
		for (int i = 0; i < c.length; i++) {

			SaCriterionWeightingSelectComp scwsc = new SaCriterionWeightingSelectComp(
					SaCriterionListComp, SWT.None, criteria.get(i), c[i], i);

			scwsc.addWeightingChangedListener(new SaCriterionWeightingChangedListener() {

				@Override
				public void doCriterionWeightingChanged(
						SaCriterionWeightingChangedEvent e) {
					updateCriteriaWeightings(e.getCriterionIndex(),
							e.getNewValue());
				}
			});

			weightingSelectComponents[i] = scwsc;
		}

		scrolledComposite.setContent(SaCriterionListComp);
		scrolledComposite.setMinSize(SaCriterionListComp.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));

		scrolledComposite_1 = new ScrolledComposite(sashForm, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);

		SaAlternativeWeighingComp = new Composite(scrolledComposite_1, SWT.NONE);
		SaAlternativeWeighingComp.setLayout(new RowLayout(SWT.VERTICAL));

		// Create a bar component for every alternative
		alternativeBarComponents = new SaAlternativeWeightingBarComp[a.length];

		for (int i = 0; i < alternatives.size(); i++) {
			SaAlternativeWeightingBarComp sawc = new SaAlternativeWeightingBarComp(
					SaAlternativeWeighingComp, SWT.NONE, alternatives.get(i)
							.getName(), a[i]);

			alternativeBarComponents[i] = sawc;
		}

		scrolledComposite_1.setContent(SaAlternativeWeighingComp);
		scrolledComposite_1.setMinSize(SaAlternativeWeighingComp.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));

		sashForm.setWeights(new int[] { 1, 1 });

		setBarValues();
	}

	/**
	 * @param cXi
	 *            the index of the criterion that changed
	 * @param cXval
	 *            the new value of the changed criterion
	 */
	public void updateCriteriaWeightings(int cXi, double cXval) {
		double remainingSum = 0;

		// Get difference
		double diff = c[cXi] - cXval;

		// Update value of changed criterion
		c[cXi] = cXval;

		// Calculate remaining criteria sum
		// Add all criteria
		for (int i = 0; i < c.length; i++) {
			remainingSum += c[i];
		}
		// Subtract changed criterion
		remainingSum -= c[cXi];

		// Get factors
		double[] factors = new double[c.length];
		for (int i = 0; i < factors.length; i++) {
			factors[i] = c[i] / remainingSum;
		}
		factors[cXi] = 1;

		// New values
		for (int i = 0; i < factors.length; i++) {
			if (i != cXi) {
				c[i] = c[i] + diff * factors[i];
				weightingSelectComponents[i].doUpdate(c[i]);
			}
		}

		a = parentNode.getAlternativeValues(c);

		setBarValues();

	}

	/**
	 * Updates the bar components to the current values of the alternatives
	 */
	private void setBarValues() {
		// Get highest value
		int iMax = 0;
		double dMax = 0;
		for (int i = 0; i < a.length; i++) {
			if (dMax < a[i]) {
				iMax = i;
				dMax = a[i];
			}
		}

		for (int i = 0; i < a.length; i++) {
			// Highlight leading alternative
			if (i == iMax)
				alternativeBarComponents[i].doUpdate(a[i], SWT.NORMAL);
			else
				alternativeBarComponents[i].doUpdate(a[i], SWT.PAUSED);
		}
	}
}
