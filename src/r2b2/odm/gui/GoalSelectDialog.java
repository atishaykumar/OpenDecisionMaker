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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import r2b2.odm.model.Goal;

/**
 * Lets the user select which goal he wants to load into the GUI.
 * 
 * @author Alex
 * 
 */

public class GoalSelectDialog extends Dialog {

	protected int result = 0;
	protected Shell shell;
	private ArrayList<Goal> goals;
	private Label lblDescription;
	protected Goal goalSelected;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public GoalSelectDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result, "0" means cancel
	 */
	public int open(ArrayList<Goal> goals) {

		this.goals = goals;
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(450, 300);
		shell.setText(getText());

		lblDescription = new Label(shell, SWT.WRAP);
		lblDescription.setBounds(245, 10, 189, 164);
		lblDescription.setText("Description");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = 0;
				shell.close();
			}
		});
		btnCancel.setBounds(359, 237, 75, 25);
		btnCancel.setText("Cancel");

		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = goalSelected.getId();
				shell.close();
			}
		});
		btnOk.setBounds(278, 237, 75, 25);
		btnOk.setText("OK");

		List list = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List source = (List) e.widget;
				int index = source.getSelectionIndex();
				if (index != -1) {
					goalSelected = goals.get(index);
					lblDescription.setText(goalSelected.getDescription());
				} else {
					goalSelected = null;
					lblDescription.setText("");
				}
			}
		});
		list.setBounds(10, 10, 229, 252);

		for (Goal goal : goals) {
			list.add(goal.getId() + ": " + goal.toString());
		}
	}
}
