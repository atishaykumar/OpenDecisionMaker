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
package r2b2.odm.gui.components;

import javax.swing.event.EventListenerList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

import r2b2.odm.model.Criterion;

import com.swtdesigner.SWTResourceManager;

/**
 * A composite where the weighting of a criterion can be changed by hand
 * 
 * @author Alex
 */
public class SaCriterionWeightingSelectComp extends Composite {

	private double value;
	private int criterionIndex;
	private Criterion criterion;

	private Slider slider;
	private Label lblPercentage;

	/**
	 * Creates the object
	 * 
	 * @param parent
	 * @param style
	 * @param crit
	 * @param initialValue
	 * @param critIndex
	 */
	public SaCriterionWeightingSelectComp(Composite parent, int style,
			Criterion crit, double initialValue, int critIndex) {
		super(parent, SWT.NONE);

		this.value = initialValue;
		this.criterionIndex = critIndex;
		this.criterion = crit;

		Label lblCriterionName = new Label(this, SWT.NONE);
		lblCriterionName.setFont(SWTResourceManager.getFont("Segoe UI", 14,
				SWT.NORMAL));
		lblCriterionName.setText(criterion.getName());
		lblCriterionName.setBounds(0, 10, 216, 25);

		slider = new Slider(this, SWT.NONE);
		slider.setThumb(1000);
		slider.setPageIncrement(1000);
		slider.setMaximum(10000);
		slider.setMinimum(1);
		slider.setIncrement(100);

		slider.setSelection((int) (value * 10000));

		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doChanged(e);
			}
		});
		slider.setBounds(74, 42, 196, 25);

		lblPercentage = new Label(this, SWT.NONE);
		lblPercentage.setFont(SWTResourceManager.getFont("Segoe UI", 14,
				SWT.NORMAL));
		lblPercentage.setBounds(23, 44, 43, 25);
		lblPercentage.setText((int) (value * 100) + "%");
	}

	/**
	 * Handles changes in the slider selection. Fires event to signal change.
	 * 
	 * @param e
	 */
	void doChanged(TypedEvent e) {
		Slider source = (Slider) e.widget;
		int value = source.getSelection();
		double valueNew = value / 10000d;
		fireChangedEvent(new SaCriterionWeightingChangedEvent(this, criterion,
				criterionIndex, valueNew));
		doUpdate(valueNew);
	}

	/**
	 * Updates the slider to the new value.
	 * 
	 * @param valueNew
	 */
	public void doUpdate(double valueNew) {
		value = valueNew;
		slider.setSelection((int) (valueNew * 10000));
		lblPercentage.setText((int) (valueNew * 100) + "%");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	// Create the listener list
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * Adds a listener to the listener list.
	 * 
	 * @param listener
	 */
	public void addWeightingChangedListener(
			SaCriterionWeightingChangedListener listener) {
		listenerList.add(SaCriterionWeightingChangedListener.class, listener);
	}

	/**
	 * Removes a listener from the list
	 * 
	 * @param listener
	 */
	public void removeListener(SaCriterionWeightingChangedListener listener) {
		listenerList
				.remove(SaCriterionWeightingChangedListener.class, listener);
	}

	/**
	 * Notifies the listeners that the event occurred
	 * 
	 * @param evt
	 */
	protected void fireChangedEvent(SaCriterionWeightingChangedEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == SaCriterionWeightingChangedListener.class) {
				((SaCriterionWeightingChangedListener) listeners[i + 1])
						.doCriterionWeightingChanged(evt);
			}
		}
	}
}
