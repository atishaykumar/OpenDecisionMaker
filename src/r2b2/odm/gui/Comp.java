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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import r2b2.odm.model.AhpModel;
import r2b2.odm.model.Criterion;
import r2b2.odm.model.base.AhpNode;

import com.swtdesigner.SWTResourceManager;

/**
 * Superclass of all gui tabs except the tab result.
 * 
 * @author Wolfgang
 *
 */
public abstract class Comp extends Composite {

	// variables
	private AhpModel ahpModel;

	private Label stepLabel;
	private Tree tree;
	private TreeItem topTreeItem;
	private Label treeLabel;
	private Composite middleComposite;
	private Composite placeholderComposite;
	private boolean preCondFulfilled = false;
	private boolean integrity = false;
	private Button btnNext;
	private Button btnBack;
	private Label helpLabel;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public Comp(Composite parent, int style) {
		super(parent, style);

		// layout
		setLayout(new GridLayout(3, true));

		// layout components
		stepLabel = new Label(this, SWT.NONE);
		stepLabel.setText("Step 4: Weighting the criteria.");

		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3,
				1));

		middleComposite = new Composite(this, SWT.NONE);
		middleComposite.setLayout(new GridLayout(3, true));
		middleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));

		Label label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		placeholderComposite = new Composite(composite, SWT.NONE);
		GridData gd_composite_t = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_composite_t.heightHint = 10;
		placeholderComposite.setLayoutData(gd_composite_t);

		btnBack = new Button(composite, SWT.NONE);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				back();
			}
		});

		GridData gd_btnBack = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_btnBack.widthHint = 90;
		btnBack.setLayoutData(gd_btnBack);
		btnBack.setText("Back");

		btnNext = new Button(composite, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				next();
			}
		});

		GridData gd_btnNext = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_btnNext.widthHint = 90;
		btnNext.setLayoutData(gd_btnNext);
		btnNext.setText("Next");

		helpLabel = new Label(composite, SWT.CENTER);
		helpLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		GridData gd_helpLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);

		gd_helpLabel.widthHint = 20;
		helpLabel.setLayoutData(gd_helpLabel);
		helpLabel.setText("?");

		// calls the method refresh of a tab if the user gets in the tab
		this.addListener(SWT.Show, new Listener() {
			@Override
			public void handleEvent(Event event) {
				refresh();
			}
		});

	}

	/**
	 * 
	 * Puts the criteria structure into a tree, beginning with the delivered
	 * TreeItem
	 * 
	 * @param parentNode
	 * @param item
	 */
	protected void fillTreeItems(AhpNode parentNode, TreeItem item) {
		ArrayList<Criterion> subCriteria = parentNode.getCriteria();
		for (Criterion criterion : subCriteria) {
			TreeItem treeItem = new TreeItem(item, 0);
			treeItem.setText(new String[] { criterion.getName(),
				     criterion.getDescription() });
			treeItem.setData(criterion);
			if (criterion.hasSubCriteria()) {
				fillTreeItems(criterion, treeItem);
			}
		}
	}

	/**
	 * Is called when the preConditons of all tabs have to be updated. This is
	 * necessary when the integrity of a single tab changes.
	 */
	public void updatePreConditions() {
		((GuiTabFolder) this.getParent().getParent()).refreshPreConditons();

	}

	/**
	 * 
	 * Helper method. Checks if there is at least one character except space in
	 * the String
	 * 
	 * @param text
	 * @return boolean for Empty Field
	 */
	public boolean checkTextIntegrity(String text) {
		String test = text.replaceAll(" ", "");
		if (test.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isIntegrity() {
		return integrity;
	}

	public void setIntegrity(boolean integrity) {
		this.integrity = integrity;
	}

	public boolean isPreCondFulfilled() {
		return preCondFulfilled;
	}

	public void setPreCondIsFulfilled(boolean preCondIsFulfilled) {
		this.preCondFulfilled = preCondIsFulfilled;
	}

	public Composite getPlaceholderComposite() {
		return placeholderComposite;
	}

	public Composite getMiddleComposite() {
		return middleComposite;
	}

	public Button getBtnBack() {
		return btnBack;
	}

	public Button getBtnNext() {
		return btnNext;
	}

	public Label getHelpLabel() {
		return helpLabel;
	}

	public Label getStepLabel() {
		return stepLabel;
	}

	public void setStepLabel(Label stepLabel) {
		this.stepLabel = stepLabel;
	}

	public Label getTreeLabel() {
		return treeLabel;
	}

	public void setTreeLabel(Label treeLabel) {
		this.treeLabel = treeLabel;
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public void clearTree() {
		tree.removeAll();
	}

	public void setAhpModel(AhpModel ahpModel) {
		this.ahpModel = ahpModel;
	}

	public AhpModel getAhpModel() {
		return ahpModel;
	}

	protected TreeItem getTopTreeItem() {
		return topTreeItem;
	}

	protected void setTopTreeItem(TreeItem topTreeItem) {
		this.topTreeItem = topTreeItem;
	}

	/**
	 * Is called when the user selects this tab.
	 * 
	 * The method does all necessary refreshes.
	 */
	protected abstract void refresh();

	/**
	 * This methods checks all integrity conditions of the tab. It sets the
	 * integrity variables and enables or disables the next button.
	 */
	protected abstract void refreshIntegrity();

	/**
	 * Simple switch to the next tab
	 */
	protected abstract void next();

	/**
	 * Simple switch to the previous tab.
	 */
	protected abstract void back();
}
