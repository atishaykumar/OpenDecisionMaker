/**
 * 
 * @author Bender, Blocherer, Rossmehl and Rotter
 * 
 *         This file is part of Open Decision Maker.
 * 
 *         Open Decision Maker is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU General Public
 *         License as published by the Free Software Foundation, either
 *         version 3 of the License, or (at your option) any later version.
 * 
 *         Open Decision Maker is distributed in the hope that it will be
 *         useful, but WITHOUT ANY WARRANTY; without even the implied
 *         warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *         See the GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with Open Decision Maker. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
package r2b2.odm.gui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import com.swtdesigner.SWTResourceManager;

/**
 * A composite showing the value of a given alternative in the form of a
 * progress bar.
 * 
 * @author Alex
 * 
 */
public class SaAlternativeWeightingBarComp extends Composite {

	private Label lblPercentage;
	private ProgressBar progressBar;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public SaAlternativeWeightingBarComp(Composite parent, int style,
			String altName, double altWeighting) {
		super(parent, style);

		lblPercentage = new Label(this, SWT.NONE);
		lblPercentage.setFont(SWTResourceManager.getFont("Segoe UI", 14,
				SWT.NORMAL));
		lblPercentage.setText((int) (altWeighting * 100) + "%");
		lblPercentage.setBounds(14, 53, 55, 26);

		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setSelection((int) (altWeighting * 100));
		progressBar.setBounds(75, 39, 215, 51);

		Label lblAltName = new Label(this, SWT.NONE);
		lblAltName.setFont(SWTResourceManager.getFont("Segoe UI", 14,
				SWT.NORMAL));
		lblAltName.setText(altName);
		lblAltName.setBounds(5, 7, 226, 26);

	}

	/**
	 * Update the bar to the new value
	 * 
	 * @param altWeighting
	 * @param state
	 */
	public void doUpdate(double altWeighting, int state) {
		lblPercentage.setText((int) (altWeighting * 100) + "%");
		progressBar.setState(state);
		progressBar.setSelection((int) (altWeighting * 100));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
