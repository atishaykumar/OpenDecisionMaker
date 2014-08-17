/** 
 * @author Bender, Blocherer, Rossmehl and Rotter
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

/**
 * The "About" dialog in the GUI providing additional information and links for
 * the program.
 * 
 * @author Rotter, Bender
 */
public class AboutDialog extends Dialog {

	protected Object result;
	protected Shell shlAbout;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAbout.open();
		shlAbout.layout();
		Display display = getParent().getDisplay();
		while (!shlAbout.isDisposed()) {
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
		shlAbout = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN);
		shlAbout.setSize(450, 209);
		shlAbout.setText("About");

		Label lblAuthors = new Label(shlAbout, SWT.NONE);
		lblAuthors.setBounds(10, 52, 55, 15);
		lblAuthors.setText("Authors:");

		Label lblVersion = new Label(shlAbout, SWT.NONE);
		lblVersion.setBounds(10, 10, 55, 15);
		lblVersion.setText("Version:");

		Label label = new Label(shlAbout, SWT.NONE);
		label.setBounds(155, 10, 76, 15);
		label.setText("1.0.1");

		Label lblBlocher = new Label(shlAbout, SWT.NONE);
		lblBlocher.setBounds(155, 52, 231, 15);
		lblBlocher.setText("Bender, Blocher, Rossmehl, Rotter");

		Label lblSourceForgeProject = new Label(shlAbout, SWT.NONE);
		lblSourceForgeProject.setBounds(10, 94, 137, 15);
		lblSourceForgeProject.setText("Source Forge Project URL");

		Label lblLinceseInfo = new Label(shlAbout, SWT.NONE);
		lblLinceseInfo.setBounds(10, 136, 114, 15);
		lblLinceseInfo.setText("Lincese Info:");

		Label lblGnuGplV = new Label(shlAbout, SWT.NONE);

		lblGnuGplV.setBounds(155, 136, 81, 15);
		lblGnuGplV.setText("GNU GPL v3.0");

		Link link = new Link(shlAbout, SWT.NONE);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.launch(e.text);
			}
		});
		link.setBounds(155, 94, 279, 15);
		link.setText("<a>http://sourceforge.net/projects/opendecisionmak/</a>");

		Link link_1 = new Link(shlAbout, SWT.NONE);
		link_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.launch(e.text);
			}
		});
		link_1.setBounds(155, 157, 279, 15);
		link_1.setText("<a>http://www.gnu.org/licenses/gpl.html</a>");

	}
}
