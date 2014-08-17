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
package r2b2.odm.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

import r2b2.odm.gui.CompWeighting;
import r2b2.odm.model.base.Weighting;

import com.swtdesigner.SWTResourceManager;

/**
 * 
 * this class is used to do the weightings
 * 
 * @author Wolfgang
 * 
 */

public class WeightingScaleNew extends Composite {

	private Composite composite;
	private Weighting crit;
	private Scale scale;
	private Label lblLinks;
	private Label lblRechts;
	private Label lblUnten;
	private Label lblUntenmitte;
	private Label lblUntenlinks;
	private Label lblUntenrechts;
	private Group myGroup;
	/**
	 * depot for the button or scale If there is no weighting done, the scale is
	 * in the depot and the button "click to add weighting" is displayed If the
	 * weighting is done, the scale is displayed and the button is in the depot.
	 */
	private Composite depot;
	private Button btnDoWeighting;

	public WeightingScaleNew(Composite parent, int style) {
		super(parent, style);

		// layout components
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setLayout(new GridLayout(1, false));

		myGroup = new Group(this, SWT.NONE);
		myGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_grpZuverlsslichkeit = new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1);
		gd_grpZuverlsslichkeit.widthHint = 330;
		myGroup.setLayoutData(gd_grpZuverlsslichkeit);
		myGroup.setText("");
		myGroup.setLayout(new GridLayout(3, true));

		depot = new Composite(myGroup, SWT.NONE);
		GridData gd_depot = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_depot.heightHint = 8;
		depot.setLayoutData(gd_depot);
		depot.setVisible(false);

		lblUnten = new Label(myGroup, SWT.NONE);
		lblUnten.setAlignment(SWT.CENTER);
		lblUnten.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblUnten.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		new Label(myGroup, SWT.NONE);

		lblLinks = new Label(myGroup, SWT.NONE);
		lblLinks.setAlignment(SWT.RIGHT);
		lblLinks.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblLinks.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		lblLinks.setText("links");

		composite = new Composite(myGroup, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.horizontalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.verticalSpacing = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		GridData gd_scale2 = new GridData(SWT.FILL, SWT.CENTER, true, true, 1,
				1);
		gd_scale2.heightHint = 35;
		gd_scale2.widthHint = 101;
		composite.setLayoutData(gd_scale2);

		lblRechts = new Label(myGroup, SWT.NONE);
		lblRechts.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblRechts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		lblRechts.setText("rechts");

		lblUntenlinks = new Label(myGroup, SWT.NONE);
		lblUntenlinks.setAlignment(SWT.RIGHT);
		lblUntenlinks.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblUntenlinks.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		lblUntenmitte = new Label(myGroup, SWT.NONE);
		lblUntenmitte.setAlignment(SWT.CENTER);
		lblUntenmitte.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		lblUntenmitte.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));

		lblUntenrechts = new Label(myGroup, SWT.NONE);
		lblUntenrechts.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblUntenrechts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		scale = new Scale(depot, SWT.NONE);
		scale.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_scale = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		scale.setLayoutData(gd_scale);
		scale.setPageIncrement(1);
		scale.setSelection(8);
		scale.setMaximum(16);
		scale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (scale.getSelection() < 9) {
					crit.setValue(1 / (double) (10 - (scale.getSelection() + 1)));

				} else {
					crit.setValue(scale.getSelection() - 7);

				}
				refresh();
			}
		});

		btnDoWeighting = new Button(depot, SWT.NONE);
		btnDoWeighting.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				crit.setValue(1);
				showScale();
				refresh();
				refreshGui();
			}
		});
		btnDoWeighting.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		btnDoWeighting.setText("do weighting");

	}

	/**
	 * Set a weighting to the weighting scale
	 * 
	 * @param weight
	 */
	public void setCrit(Weighting weight) {
		this.crit = weight;
		// update labels
		lblRechts.setText(crit.getFactor1().getName());
		lblLinks.setText(crit.getFactor2().getName());

		// if there is no weighting done, show the button
		// "click here to do weighting"
		if (weight.getValue() == 0) {
			showButton();
		} else {
			// else, show the scale
			showScale();
			refresh();
		}
	}

	/**
	 * Removes button, shows scale
	 */
	private void showScale() {
		btnDoWeighting.setParent(depot);
		scale.setParent(composite);
		composite.layout();
	}

	/**
	 * Removes scale, shows button
	 */
	private void showButton() {
		lblUntenlinks.setText("");
		lblUntenmitte.setText("");
		lblUntenrechts.setText("");
		lblUnten.setText("");
		scale.setParent(depot);
		btnDoWeighting.setParent(composite);
		composite.layout();
	}

	/**
	 * Is called when the scale moves. Converts scale value into a AHP value and
	 * sets the new value Refreshes the labels
	 */
	private void refresh() {
		lblRechts.setText(crit.getFactor1().getName());
		lblLinks.setText(crit.getFactor2().getName());

		int value = 1;

		if (crit.getValue() == 1) {
			value = 10 - ((int) (1 / crit.getValue()) + 1);
			lblUnten.setText((int) (1 / crit.getValue()) + "");
			texts((int) (1 / crit.getValue()), lblUntenmitte);
		}
		if (crit.getValue() < 1) {
			value = 10 - ((int) (1 / crit.getValue()) + 1);
			lblUnten.setText((int) (1 / crit.getValue()) + "");
			texts((int) (1 / crit.getValue()), lblUntenlinks);
		}
		if (crit.getValue() > 1) {
			value = (int) crit.getValue() + 7;
			lblUnten.setText((int) crit.getValue() + "");
			texts((int) crit.getValue(), lblUntenrechts);
		}

		scale.setSelection(value);
	}

	/**
	 * Set the label texts
	 * 
	 * @param value
	 * @param label
	 */
	private void texts(int value, Label label) {
		lblUntenmitte.setText(" ");
		lblUntenlinks.setText(" ");
		lblUntenrechts.setText(" ");

		switch (value) {
		case 1:
			label.setText("equal");
			break;
		case 2:
			label.setText("equal - somewhat better");
			break;
		case 3:
			label.setText("somewhat better");
			break;
		case 4:
			label.setText("somewhat - definitely better");
			break;
		case 5:
			label.setText("definitely better");
			break;
		case 6:
			label.setText("definitely - much more better");
			break;
		case 7:
			label.setText("much more better");
			break;
		case 8:
			label.setText("much more - extremely better");
			break;
		case 9:
			label.setText("extremely better");
			break;

		}
	}

	/**
	 * Is called when the scale moves, to refresh the color of the criteria tree
	 */
	private void refreshGui() {

		((CompWeighting) this.getParent().getParent().getParent().getParent())
				.refreshMyTreeColor();
	}

}