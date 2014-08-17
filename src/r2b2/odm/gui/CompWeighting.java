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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import r2b2.odm.gui.components.WeightingScaleNew;
import r2b2.odm.model.base.AhpNode;
import r2b2.odm.model.base.Weighting;

import com.swtdesigner.SWTResourceManager;

/**
 * Superclass of CompWeigtingAlternatives and CopmWeightingCriteria
 * 
 * - Implements all common needed methods and variables - dictates methods which
 * have to be implemented from sub classes
 * 
 * @author Wolfgang
 * 
 */

public abstract class CompWeighting extends Comp {

	private Label lblTree;
	private Label lblComments;
	private Text textField;
	private Label weightingLabel;
	private ScrolledComposite scrolledComposite;
	private Composite weightingComposite;

	/**
	 * the scalesBuffer contains a couple of scales for a better performance
	 */
	private int scalesBuffer = 10;
	private int publicScalesCount = 0;
	private ArrayList<WeightingScaleNew> scales = new ArrayList<WeightingScaleNew>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompWeighting(Composite parent, int style) {
		super(parent, style);

		// layout
		Composite middleCopmposite = getMiddleComposite();

		lblTree = new Label(middleCopmposite, SWT.NONE);
		lblTree.setText("Select the criteria nodes.");

		lblComments = new Label(middleCopmposite, SWT.NONE);
		lblComments.setText("Comments");
		lblComments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				2, 1));

		Tree tree = new Tree(middleCopmposite, SWT.BORDER);
		setTree(tree);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		tree.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));

		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				handleTreeEvent(event);
			}
		});

		textField = new Text(middleCopmposite, SWT.BORDER | SWT.WRAP);
		GridData gridd = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gridd.heightHint = 70;
		textField.setLayoutData(gridd);

		weightingLabel = new Label(middleCopmposite, SWT.NONE);
		weightingLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12,
				SWT.NORMAL));
		weightingLabel.setText("Weightings");
		weightingLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1));

		scrolledComposite = new ScrolledComposite(middleCopmposite,
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.getVerticalBar().setPageIncrement(10);
		scrolledComposite.getVerticalBar().setIncrement(25);

		weightingComposite = new Composite(scrolledComposite, SWT.NONE);
		weightingComposite.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		weightingComposite.setBounds(0, 0, 300, 200);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setContent(weightingComposite);

		GridLayout gl_group_1 = new GridLayout(1, false);
		gl_group_1.marginWidth = 0;
		gl_group_1.horizontalSpacing = 0;
		gl_group_1.verticalSpacing = 0;
		gl_group_1.marginHeight = 0;
		weightingComposite.setLayout(gl_group_1);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));

		initializeScales();
	}

	/**
	 * Adds a new scale to the scale buffer
	 */
	protected void addNewScaleToBuffer() {
		WeightingScaleNew myScale = new WeightingScaleNew(
				getPlaceholderComposite(), SWT.NONE);
		GridData gridd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gridd.heightHint = 110;
		myScale.setLayoutData(gridd);
		scales.add(myScale);
	}

	/**
	 * Sets the number of scale objects which are visible
	 * 
	 * @param newNumberOfScales
	 */
	protected void setPublicScalesAmount(int newNumberOfScales) {
		for (int j = scalesBuffer; j < newNumberOfScales; j++) {
			addNewScaleToBuffer();
			scalesBuffer++;
		}
		if (!(publicScalesCount == newNumberOfScales)) {
			if (publicScalesCount < newNumberOfScales) {
				for (int i = publicScalesCount; i < newNumberOfScales; i++) {
					scales.get(i).setParent(getWeightingComposite());

				}

			} else {
				for (int i = newNumberOfScales; i < publicScalesCount; i++) {
					scales.get(i).setParent(getPlaceholderComposite());
				}
			}
			getScrolledComposite().setMinSize(
					getWeightingComposite().computeSize(SWT.DEFAULT,
							SWT.DEFAULT));
			getWeightingComposite().layout();
			getScrolledComposite().layout();
			publicScalesCount = newNumberOfScales;

		}
	}

	public void clearWeightings() {
		setPublicScalesAmount(0);
	}

	/**
	 * Sets the weightings
	 * 
	 * @param weights
	 */
	public void fillWeightings(ArrayList<Weighting> weights) {
		setPublicScalesAmount(weights.size());
		int i = 0;
		for (Weighting weight : weights) {
			scales.get(i).setCrit(weight);
			i++;
		}
	}

	/**
	 * 
	 * Fills the tree with the criteria
	 * 
	 * @param ahpNode
	 */
	protected void fillTree(AhpNode ahpNode) {
		clearTree();
		if (ahpNode.hasSubCriteria()) {
			setTopTreeItem(new TreeItem(getTree(), 0));
			getTopTreeItem().setText(ahpNode.getName());
			getTopTreeItem().setData(ahpNode);
			fillTreeItems(ahpNode, getTopTreeItem());
		}

	}

	/**
	 * Fills the weighting scale buffer.
	 */
	protected void initializeScales() {
		for (int i = 0; i < scalesBuffer; i++) {
			addNewScaleToBuffer();
		}
	}

	protected Text getTextField() {
		return textField;
	}

	protected void setTextField(Text textField) {
		this.textField = textField;
	}

	protected Label getLblTree() {
		return lblTree;
	}

	protected Label getWeightingLabel() {
		return weightingLabel;
	}

	protected ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

	protected Composite getWeightingComposite() {
		return weightingComposite;
	}

	/**
	 * Refreshes the tree color when a weighting is done
	 */
	public abstract void refreshMyTreeColor();

	/**
	 * Implements what is done when a TreeItem is selected
	 * 
	 * @param event
	 */
	protected abstract void handleTreeEvent(Event event);

}
