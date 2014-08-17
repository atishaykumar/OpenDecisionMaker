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
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import r2b2.odm.model.base.AhpObject;

import com.swtdesigner.SWTResourceManager;

/**
 * Superclass of the tabs CompInsertAlternative and CompInsertCriteria
 * 
 * - Implements all common needed methods and variables - dictates methods which
 * have to be implemented from sub classes
 * 
 * @author Wolfgang
 * 
 */

public abstract class CompInsert extends Comp {

	// variables
	private final TreeEditor editor;
	private TreeColumn column1;
	private TreeColumn column2;
	private Composite buttonComposite;
	private Label lblPleaseInsertThe;

	public CompInsert(Composite parent, int style) {
		super(parent, style);

		// layout components
		Composite middleCopmposite = getMiddleComposite();

		lblPleaseInsertThe = new Label(middleCopmposite, SWT.NONE);
		lblPleaseInsertThe.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 3, 1));

		// tree
		Tree tree = new Tree(middleCopmposite, SWT.BORDER);
		setTree(tree);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tree.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		tree.setHeaderVisible(true);
		column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setWidth(250);
		column2 = new TreeColumn(tree, SWT.LEFT);
		column2.setWidth(250);

		// composite in which the buttons are
		buttonComposite = new Composite(middleCopmposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		// tree editor
		editor = new TreeEditor(tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}

	/**
	 * Opens the text editor to edit the description of the selected criterion
	 * 
	 * @param tree
	 * @param editor
	 */
	public void editDescription(Tree tree, TreeEditor editor) {
		if (tree.getSelectionCount() == 1) {
			final TreeItem item = tree.getSelection()[0];
			final Text text = new Text(tree, SWT.NONE);
			text.setText(((AhpObject) item.getData()).getDescription());
			text.selectAll();
			text.setFocus();
			text.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent event) {
					setDescription(item, text.getText());
					text.dispose();
				}
			});

			text.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					switch (event.keyCode) {
					case SWT.CR:
						setDescription(item, text.getText());
					case SWT.ESC:
						text.dispose();
						break;
					}
				}
			});
			editor.setEditor(text, item, 1);
		}
	}

	/**
	 * Opens the text editor to edit the name of the selected criterion in the
	 * tree
	 * 
	 * @param tree
	 * @param editor
	 */
	public void editName(Tree tree, TreeEditor editor) {
		if (tree.getSelectionCount() == 1) {
			final TreeItem item = tree.getSelection()[0];
			final Text text = new Text(tree, SWT.NONE);
			text.setText(item.getText());
			text.selectAll();
			text.setFocus();

			text.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent event) {
					setName(item, text.getText());
					text.dispose();
				}
			});

			text.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					switch (event.keyCode) {
					case SWT.CR:
						setName(item, text.getText());
					case SWT.ESC:
						text.dispose();
						break;
					}
				}
			});

			editor.setEditor(text, item, 0);

		}

	}

	/**
	 * private function for editName/Description
	 * 
	 * @param item
	 * @param ahpNode
	 */
	private void setItemText(TreeItem item, AhpObject ahpNode) {
		item.setText(new String[] { ahpNode.getName(), ahpNode.getDescription() });
	}

	/**
	 * private function for editName/Description
	 * 
	 * @param item
	 * @param name
	 */
	private void setName(TreeItem item, String name) {
		AhpObject ahpNode = (AhpObject) item.getData();
		if (checkTextIntegrity(name)) {
			ahpNode.setName(name);
		} else {
			ahpNode.setName("new");
		}
		setItemText(item, ahpNode);
	}

	/**
	 * private function for editName/Description
	 * 
	 * @param item
	 * @param description
	 */
	private void setDescription(TreeItem item, String description) {
		AhpObject ahpNode = (AhpObject) item.getData();
		if (checkTextIntegrity(description)) {
			ahpNode.setDescription(description);
		} else {
			ahpNode.setDescription("");
		}
		setItemText(item, ahpNode);
	}

	public TreeEditor getEditor() {
		return editor;
	}

	public Composite getButtonComposite() {
		return buttonComposite;
	}

	public Label getLblPleaseInsertThe() {
		return lblPleaseInsertThe;
	}

	public TreeColumn getColumn1() {
		return column1;
	}

	public TreeColumn getColumn2() {
		return column2;
	}

}
