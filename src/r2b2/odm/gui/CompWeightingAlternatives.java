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
 * Tab weighting alternatives
 * 
 * @author Wolfgang
 *
 */
public class CompWeightingAlternatives extends CompWeighting {

	public CompWeightingAlternatives(Composite parent, int style) {
		super(parent, style);

		// layout components
		getHelpLabel()
				.setToolTipText(
						"Bevor switching to the result tab, make sure that all weightings are done.");
		getStepLabel().setText("Step 5: Weighting the alternatives.");
		getLblTree().setText("Select only the lowest level criteria.");

		getTextField().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (isPreCondFulfilled()) {
					getAhpModel().setTabDescWeightingsAlt(
							getTextField().getText());
				}
			}
		});
	}

	/**
	 * This method drills down the first criterion which has no sub criteria,
	 * because only lowest level criteria have alternative weightings.
	 * 
	 * @param item
	 */
	private void itemDrillDown(TreeItem item) {
		AhpNode node = (AhpNode) item.getData();
		if (node.hasSubCriteria()) {
			itemDrillDown(item.getItem(0));

		} else {
			fillWeightings(node.getWeightings());
			getTree().setSelection(item);
			getWeightingLabel().setText("Weighting: " + node.getName());
		}
	}

	@Override
	protected void handleTreeEvent(Event event) {
		itemDrillDown((TreeItem) event.item);

	}

	/**
	 * checks if all weightings are done.
	 * 
	 * @param parentNode
	 * @return
	 */
	private boolean weightingCheck(AhpNode parentNode) {
		if (parentNode.hasSubCriteria()) {
			for (Criterion criterion : parentNode.getCriteria()) {
				if (!weightingCheck(criterion)) {
					return false;
				}
			}
		} else {
			if (parentNode.getWeightings().isEmpty()) {
				return false;
			}
			for (Weighting weighting : parentNode.getWeightings()) {
				if (weighting.getValue() == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void refreshMyTreeColor() {
		refreshTreeColor(getTopTreeItem());
		refreshIntegrity();
		updatePreConditions();
	}

	private void refreshTreeColor(TreeItem item) {
		AhpNode ahpNode = (AhpNode) item.getData();
		if (!weightingCheck(ahpNode)) {
			if (ahpNode.hasSubCriteria()) {
				item.setForeground(SWTResourceManager
						.getColor(SWT.COLOR_MAGENTA));
			} else {
				item.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			}
		} else {
			item.setForeground(SWTResourceManager
					.getColor(SWT.COLOR_DARK_GREEN));
		}

		for (TreeItem items : item.getItems()) {
			refreshTreeColor(items);
		}
	}

	public boolean integrity2 = false;

	/**
	 * The integrity2 of WeightingAlternatives is the integrity of Weighting
	 * Criteria
	 * 
	 * @return boolean weightingIntegrety
	 */
	public boolean isIntegrity2() {
		return integrity2;
	}

	public void setIntegrity2(boolean integrity2) {
		this.integrity2 = integrity2;
	}

	protected void refreshIntegrity() {
		if (weightingCheck(getAhpModel().getGoal())) {
			setIntegrity(true);
		} else {

			setIntegrity(false);
		}
		if (integrity2 && isIntegrity()) {
			getBtnNext().setEnabled(true);
		} else {
			getBtnNext().setEnabled(false);
		}

	}

	protected void refresh() {
		if (isPreCondFulfilled()) {
			// create weightings
			getAhpModel().createAlternativeWeightings();

			// enable components
			getTree().setEnabled(true);
			getBtnBack().setEnabled(true);
			getTextField().setEnabled(true);

			// set contents
			getTextField().setText(getAhpModel().getTabDescWeightingsAlt());
			fillTree(getAhpModel().getGoal());
			itemDrillDown(getTopTreeItem());
			refreshTreeColor(getTopTreeItem());

			refreshIntegrity();
			updatePreConditions();
		} else {
			// remove content
			clearTree();
			clearWeightings();
			getTextField().setText("");
			getWeightingLabel().setText("Weightings");

			// disable components
			getBtnNext().setEnabled(false);
			getBtnBack().setEnabled(false);
			getTextField().setEnabled(false);
			getTree().setEnabled(false);

		}

	}

	protected void next() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(5);
	}

	protected void back() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(3);
	}
}