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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

/**
 * Goal tab.
 * 
 * @author Wolfgang
 * 
 */

public class CompGoal extends Comp {

	// variables
	private Text textGoal;
	private Text descriptionText;
	private Label lblGoal;

	/**
	 * @param parent
	 * @param style
	 */
	public CompGoal(Composite parent, int style) {
		super(parent, style);

		// layout components
		getStepLabel().setText("Step 1: The Goal.");
		getBtnBack().dispose();
		getHelpLabel()
				.setToolTipText(
						"For the next step, make sure that your goal has at least one character");

		Composite middleComposite = getMiddleComposite();

		lblGoal = new Label(getMiddleComposite(), SWT.NONE);
		lblGoal.setText("Please insert the Goal.");
		new Label(getMiddleComposite(), SWT.NONE);
		new Label(getMiddleComposite(), SWT.NONE);

		textGoal = new Text(middleComposite, SWT.BORDER);
		textGoal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		Label lblComment = new Label(getMiddleComposite(), SWT.NONE);
		lblComment.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 3, 1));
		lblComment.setText("Comments");

		descriptionText = new Text(getMiddleComposite(), SWT.BORDER | SWT.WRAP);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_text.heightHint = 119;
		descriptionText.setLayoutData(gd_text);

		textGoal.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				refreshIntegrity();
				updatePreConditions();
			}
		});

		descriptionText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				getAhpModel().getGoal().setDescription(
						descriptionText.getText());
			}
		});

	}

	protected void refreshIntegrity() {
		if (checkTextIntegrity(textGoal.getText())) {
			getAhpModel().getGoal().setName(textGoal.getText());
			getBtnNext().setEnabled(true);
			((TabFolder) this.getParent()).getItem(0).setText("Goal");
			setIntegrity(true);
		} else {
			getAhpModel().getGoal().setName("");
			getBtnNext().setEnabled(false);
			((TabFolder) this.getParent()).getItem(0).setText("*Goal");
			setIntegrity(false);
		}
	}

	public void next() {
		TabFolder tabFolder = (TabFolder) this.getParent();
		tabFolder.setSelection(1);
	}

	protected void refresh() {
		textGoal.setText(getAhpModel().getGoal().getName());
		descriptionText.setText(getAhpModel().getGoal().getDescription());
	}

	protected void back() {
		// no back button in the first tab

	}

}
