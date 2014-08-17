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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TreeItem;

import r2b2.odm.model.Criterion;
import r2b2.odm.model.base.AhpNode;
import r2b2.odm.model.base.Weighting;

import com.swtdesigner.SWTResourceManager;



/**
 * tab weigting criteria
 * 
 * @author Wolfgang
 *
 */
public class CompWeightingCriteria extends CompWeighting {

	public CompWeightingCriteria(Composite parent, int style) {
		super(parent, style);
		// layout components
		getHelpLabel()
				.setToolTipText(
						"Bevor switching to the result tab, make sure that all weightings are done.");
		getStepLabel().setText("Step 4: Weighting the criteria.");
		getLblTree().setText("Select the criteria nodes.");

		getTextField().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (isPreCondFulfilled()) {
					getAhpModel().setTabDescWeightingsCrit(
							getTextField().getText());
				}
			}
		});
	}

	protected void handleTreeEvent(Event event) {
		if (((AhpNode) event.item.getData()).hasSubCriteria()) {
			fillWeightings(((AhpNode) event.item.getData()).getWeightings());
			getWeightingLabel().setText(
					"Weighting: " + ((AhpNode) event.item.getData()).getName());
		} else {
			fillWeightings(((Criterion) event.item.getData()).getParent()
					.getWeightings());
			getWeightingLabel().setText(
					"Weighting: "
							+ ((Criterion) event.item.getData()).getParent()
									.getName());

		}
	}

	public void refreshMyTreeColor() {
		refreshTreeColor(getTopTreeItem());
		refreshIntegrity();
	}

	private void refreshTreeColor(TreeItem item) {
		AhpNode ahpNode = (AhpNode) item.getData();
		if (ahpNode.hasSubCriteria()) {
			if (!weightingCheck(ahpNode)) {
				item.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			} else {
				item.setForeground(SWTResourceManager
						.getColor(SWT.COLOR_DARK_GREEN));
			}

			for (TreeItem items : item.getItems()) {
				refreshTreeColor(items);
			}
		}
	}

	/**
	 * Checks if all weightings are done
	 * 
	 * @param parentNode
	 * @return
	 */
	private boolean weightingCheck(AhpNode parentNode) {
		if (parentNode.hasSubCriteria()) {
			for (Weighting weighting : parentNode.getWeightings()) {
				if (weighting.getValue() == 0) {
					return false;
				}
			}
			for (Criterion criterion : parentNode.getCriteria()) {
				if (!weightingCheck(criterion)) {
					return false;
				}

			}

		} else {
			return true;
		}
		return true;

	}

	protected void refreshIntegrity() {

		if (weightingCheck(getAhpModel().getGoal())) {
			((TabFolder) this.getParent()).getItem(3).setText(
					"Weighting Criteria");

			setIntegrity(true);
		} else {
			((TabFolder) this.getParent()).getItem(3).setText(
					"*Weighting Criteria");

			setIntegrity(false);
		}

	}

	protected void refresh() {
		if (isPreCondFulfilled()) {
			// create weightings
			getAhpModel().createCriteriaWeightings();

			// enable gui components
			getTree().setEnabled(true);
			getBtnNext().setEnabled(true);
			getBtnBack().setEnabled(true);
			getTextField().setEditable(true);

			// set contents
			getTextField().setText(getAhpModel().getTabDescWeightingsCrit());
			fillTree(getAhpModel().getGoal());
			refreshMyTreeColor();

			// set as default the top level criteria weightings
			fillWeightings(getAhpModel().getGoal().getWeightings());
			getWeightingLabel().setText(
					"Weighting: " + getAhpModel().getGoal().getName());

			// refreshes
			refreshIntegrity();
			updatePreConditions();

		} else {
			// remove content
			clearTree();
			clearWeightings();
			getWeightingLabel().setText("Weightings");
			getTextField().setText("");

			// enable components
			getBtnNext().setEnabled(false);
			getBtnBack().setEnabled(false);
			getTextField().setEditable(false);
			getTree().setEnabled(false);

		}

	}

	protected void next() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(4);
	}

	protected void back() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(2);
	}

}
