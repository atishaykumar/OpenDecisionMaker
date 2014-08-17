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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import r2b2.odm.model.Criterion;
import r2b2.odm.model.base.AhpNode;

/**
 * Tab insert criteria. 
 * @author Wolfgang
 *
 */
public class CompInsertCriteria extends CompInsert {

	private Shell shell;
	private Button btnNewCriterion;
	private Button btnNewSubCriterion;
	private Button btnEditCriterionName;
	private Button btnEdit;
	private Button btnRemoveCriteria;
	private Label lblComments;
	private Text descriptionText;

	public CompInsertCriteria(Composite parent, int style) {
		super(parent, style);
		shell = this.getShell();

		// layout components
		getHelpLabel().setToolTipText(
				"Make sure that each criterion has at least two subcriteria.");
		getStepLabel().setText("Step 3: The criteria.");

		getLblPleaseInsertThe().setText("Please insert the criteria.");
		Composite composite = getButtonComposite();

		// new criterion
		btnNewCriterion = new Button(composite, SWT.NONE);
		btnNewCriterion.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewCriterion();
			}
		});
		btnNewCriterion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		btnNewCriterion.setToolTipText("press +");
		btnNewCriterion.setText("Add criterion...");

		// new sub criteria
		btnNewSubCriterion = new Button(composite, SWT.NONE);
		btnNewSubCriterion.setToolTipText("press -");
		btnNewSubCriterion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		btnNewSubCriterion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewSubCriterion();

			}
		});
		btnNewSubCriterion.setText("Add sub-criterion...");

		// edit crietion name
		btnEditCriterionName = new Button(composite, SWT.NONE);
		btnEditCriterionName.setToolTipText("press F2");
		btnEditCriterionName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editName(getTree(), getEditor());
			}
		});
		btnEditCriterionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		btnEditCriterionName.setText("Edit criterion name...");

		// edit description
		btnEdit = new Button(composite, SWT.NONE);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editDescription(getTree(), getEditor());
			}
		});
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		btnEdit.setToolTipText("");
		btnEdit.setText("Add/Edit description...");

		// button remove criterion
		btnRemoveCriteria = new Button(composite, SWT.NONE);
		btnRemoveCriteria.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeCriterion();
			}
		});
		btnRemoveCriteria.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		btnRemoveCriteria.setToolTipText("press -");
		btnRemoveCriteria.setText("Remove criterion.");

		lblComments = new Label(getButtonComposite(), SWT.NONE);
		lblComments.setText("Comments");

		descriptionText = new Text(getButtonComposite(), SWT.BORDER | SWT.WRAP);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		descriptionText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (isPreCondFulfilled()) {
					getAhpModel().setTabDescCriteria(descriptionText.getText());
				}
			}
		});
		// key listener for usability
		getTree().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.F2) {
					editName(getTree(), getEditor());
				}
				// 43 keyCode for +
				if (event.keyCode == 43) {
					addNewSubCriterion();
				}
				// 45 keyCode for -
				if (event.keyCode == 45) {
					removeCriterion();
				}
			}
		});
	}

	/**
	 * Puts the criteria into the tree
	 * 
	 * @param ahpNode
	 * @param tree
	 */
	public void fillTree(AhpNode ahpNode, Tree tree) {
		tree.removeAll();
		for (AhpNode criterion : ahpNode.getCriteria()) {
			TreeItem treeItem = new TreeItem(tree, 0);
			treeItem.setText(new String[] { criterion.getName(),
					criterion.getDescription() });
			treeItem.setData(criterion);
			fillTreeItems(criterion, treeItem);
		}
	}

	protected void refreshIntegrity() {
		if (getAhpModel().getGoal().hasSubCriteria()
				&& checkInegrity(getAhpModel().getGoal())) {
			setIntegrity(true);
			getBtnNext().setEnabled(true);
		} else {
			setIntegrity(false);
			getBtnNext().setEnabled(false);
		}
	}

	/**
	 * Returns false if one criterion has only one sub criterion
	 * 
	 * @param ahpNode
	 * @return
	 */
	protected boolean checkInegrity(AhpNode ahpNode) {
		if (ahpNode.hasSubCriteria() && ahpNode.getCriteria().size() == 1) {
			return false;
		}
		if (ahpNode.hasSubCriteria()) {
			for (AhpNode ahpNode2 : ahpNode.getCriteria()) {
				if (!checkInegrity(ahpNode2)) {
					return false;
				}
			}
		}
		return true;
	}

	private void addNewCriterion() {
		TreeItem newItem;
		try {
			// if a treeItem is selected
			TreeItem item = getTree().getSelection()[0].getParentItem();
			newItem = new TreeItem(item, 0);
			newItem.setText("new criterion");
			Criterion crit1 = new Criterion((Criterion) item.getData(),
					"new criterion");
			newItem.setData(crit1);
			getAhpModel().addCriterion(crit1);

		} catch (Exception ex) {
			// if no teeItem is selected
			newItem = new TreeItem(getTree(), 0);
			newItem.setText("new criterion");
			Criterion newCrit = new Criterion(getAhpModel().getGoal(),
					"new criterion");
			newItem.setData(newCrit);
			getAhpModel().addCriterion(newCrit);
		}
		refreshIntegrity();
		updatePreConditions();
		getTree().select(newItem);
		editName(getTree(), getEditor());
	}

	private void addNewSubCriterion() {
		if (getTree().getSelectionCount() == 1) {
			final TreeItem item = getTree().getSelection()[0];
			TreeItem newItem = new TreeItem(item, 0);
			newItem.setText("new criterion");
			Criterion crit1 = new Criterion((AhpNode) item.getData(),
					"new criterion");
			newItem.setData(crit1);
			getAhpModel().addCriterion(crit1);
			refreshIntegrity();
			updatePreConditions();
			getTree().select(newItem);
			editName(getTree(), getEditor());
		}

	}

	private void removeCriterion() {
		if (getTree().getSelectionCount() == 1) {

			boolean answer = MessageDialog
					.openQuestion(
							shell,
							"Remove criterion",
							"Do you really want to remove this criterion? \n\nRemember that deleating a criterion causes the loss of the weightings of this criterion.");

			if (answer) {
				final TreeItem item = getTree().getSelection()[0];
				Criterion criterion = (Criterion) item.getData();
				criterion.getParent().getCriteria().remove(criterion);
				item.dispose();
			}
		}
		refreshIntegrity();
		updatePreConditions();
	}

	protected void next() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(3);
	}

	protected void back() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(1);
	}

	@Override
	protected void refresh() {
		if (isPreCondFulfilled()) {
			// enable all components
			getTree().setEnabled(true);
			getBtnNext().setEnabled(true);
			getBtnBack().setEnabled(true);
			btnNewCriterion.setEnabled(true);
			btnNewSubCriterion.setEnabled(true);
			btnEditCriterionName.setEnabled(true);
			btnEdit.setEnabled(true);
			btnRemoveCriteria.setEnabled(true);
			descriptionText.setEnabled(true);

			// set content
			descriptionText.setText(getAhpModel().getTabDescCriteria());
			fillTree(getAhpModel().getGoal(), getTree());

			refreshIntegrity();
			updatePreConditions();
		}

		else {
			// remove contents
			clearTree();
			descriptionText.setText("");
			// enable components
			getBtnNext().setEnabled(false);
			getBtnBack().setEnabled(false);
			descriptionText.setEnabled(false);
			btnNewCriterion.setEnabled(false);
			btnNewSubCriterion.setEnabled(false);
			btnEditCriterionName.setEnabled(false);
			btnEdit.setEnabled(false);
			btnRemoveCriteria.setEnabled(false);
			getTree().setEnabled(false);
		}

	}
}
