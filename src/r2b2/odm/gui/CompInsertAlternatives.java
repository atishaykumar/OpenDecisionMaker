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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import r2b2.odm.model.Alternative;
import r2b2.odm.model.base.AhpObject;

/**
 * Tab insert alternatives.
 * @author Wolfgang
 *
 */
public class CompInsertAlternatives extends CompInsert {

	private Button btnAddAlternative;
	private Button btnEdit;
	private Button btnEditComment;
	private Button btnRemoveAlternative;
	private Label lblComments;
	private Text descriptionText;
	private Shell shell;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompInsertAlternatives(Composite parent, int style) {
		super(parent, style);
		shell = this.getShell();

		// layout components
		getHelpLabel().setToolTipText(
				"Make sure that there are at least two alternatives.");
		getStepLabel().setText("Step 2: The alternatives.");
		Composite composite = getButtonComposite();

		getLblPleaseInsertThe().setText("Please insert the alternatives.");

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		composite.setLayout(new GridLayout(1, false));

		// add Alternative
		btnAddAlternative = new Button(composite, SWT.NONE);
		btnAddAlternative.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewAlternative();
			}
		});
		btnAddAlternative.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		btnAddAlternative.setToolTipText("press +");
		btnAddAlternative.setText("Add alternative...");

		// edit alternative
		btnEdit = new Button(composite, SWT.NONE);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editName(getTree(), getEditor());
			}
		});
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		btnEdit.setToolTipText("press F2");
		btnEdit.setText("Edit alternative name...");

		// edit comment
		btnEditComment = new Button(composite, SWT.NONE);
		btnEditComment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				editDescription(getTree(), getEditor());
			}
		});
		btnEditComment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		btnEditComment.setText("Add/Edit description...");

		// remove alternative
		btnRemoveAlternative = new Button(composite, SWT.NONE);
		btnRemoveAlternative.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeAlternative();
			}
		});
		btnRemoveAlternative.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		btnRemoveAlternative.setToolTipText("press -");
		btnRemoveAlternative.setText("Remove alternative.");

		lblComments = new Label(getButtonComposite(), SWT.NONE);
		lblComments.setText("Comments");

		descriptionText = new Text(getButtonComposite(), SWT.BORDER | SWT.WRAP);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		descriptionText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (isPreCondFulfilled()) {
					getAhpModel().setTabDescAlternatives(
							descriptionText.getText());
				}
			}
		});

		// additional for usability: key listener
		getTree().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.F2) {
					editName(getTree(), getEditor());
				}
				// 43 keyCode for +
				if (event.keyCode == 43) {
					addNewAlternative();
				}
				// 45 keyCode for -
				if (event.keyCode == 45) {
					removeAlternative();
				}
			}
		});

	}

	/**
	 * puts the alternatives to the tree
	 */
	private void fillTree() {
		clearTree();
		for (AhpObject alternative : getAhpModel().getAlternatives()) {
			TreeItem treeItem = new TreeItem(getTree(), 0);
			treeItem.setText(new String[] { alternative.getName(),
					alternative.getDescription() });
			treeItem.setData(alternative);
		}
	}

	protected void refreshIntegrity() {
		if (getAhpModel().getAlternatives().size() > 1) {
			setIntegrity(true);
			getBtnNext().setEnabled(true);
			((TabFolder) this.getParent()).getItem(1).setText("Alternatives");

		} else {
			setIntegrity(false);
			getBtnNext().setEnabled(false);
			((TabFolder) this.getParent()).getItem(1).setText("*Alternatives");
		}
	}

	private void addNewAlternative() {
		TreeItem treeItem = new TreeItem(getTree(), 0);
		treeItem.setText("new alternative");
		Alternative alternative = new Alternative(getAhpModel().getGoal(),
				"new alternative");
		getAhpModel().addAlternative(alternative);
		treeItem.setData(alternative);
		refreshIntegrity();
		updatePreConditions();
		getTree().select(treeItem);
		editName(getTree(), getEditor());
	}

	private void removeAlternative() {

		if (getTree().getSelectionCount() == 1) {
			boolean answer = MessageDialog
					.openQuestion(
							shell,
							"Remove alternative",
							"Do you really want to remove this alternative? \n\nRemember that deleating a alternative causes the loss of the weightings of this alternative.");

			if (answer) {
				final TreeItem item = getTree().getSelection()[0];
				Alternative alternative = (Alternative) item.getData();
				getAhpModel().removeAlternative(alternative);
				item.dispose();
				refreshIntegrity();
				updatePreConditions();
			}
		}
	}

	protected void next() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(2);
	}

	protected void back() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(0);
	}

	@Override
	protected void refresh() {
		if (isPreCondFulfilled()) {
			// enable all components
			getTree().setEnabled(true);
			getBtnNext().setEnabled(true);
			getBtnBack().setEnabled(true);
			btnEdit.setEnabled(true);
			btnEditComment.setEnabled(true);
			btnAddAlternative.setEnabled(true);
			btnRemoveAlternative.setEnabled(true);
			descriptionText.setEnabled(true);
			// set data to GUI
			fillTree();
			descriptionText.setText(getAhpModel().getTabDescAlternatives());
			// refreshes
			refreshIntegrity();
			updatePreConditions();
		} else {
			// remove all content
			clearTree();
			descriptionText.setText("");
			// disable all GUI components
			getTree().setEnabled(false);
			descriptionText.setEnabled(false);
			getBtnNext().setEnabled(false);
			getBtnBack().setEnabled(false);
			btnEdit.setEnabled(false);
			btnEditComment.setEnabled(false);
			btnAddAlternative.setEnabled(false);
			btnRemoveAlternative.setEnabled(false);
		}

	}

}
