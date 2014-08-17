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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import r2b2.odm.AhpConfig;
import r2b2.odm.AhpController;
import r2b2.odm.model.AhpModel;
import r2b2.odm.model.Goal;

import com.swtdesigner.SWTResourceManager;

/**
 * The base component for all other components
 * 
 * @author Bender, Blocherer, Rossmehl and Rotter
 */

public class Main_Gui {

	protected Shell shell;

	AhpController controller;

	private AhpModel ahpModel;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		// Create controller
		try {
			controller = new AhpController(false);
		} catch (IOException e) {
			MessageDialog.openError(shell, "Error loading configuration file",
					"The following error occured while trying to load the configuration file: \n"
							+ e.getLocalizedMessage());
			try {
				controller = new AhpController(true);
			} catch (IOException e1) {
				MessageDialog.openError(shell,
						"Error loading default configuration file",
						"The following error occured while trying to load the configuration file: \n"
								+ e.getLocalizedMessage());
			}
		}

		createContents();

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(906, 636);
		shell.setText("Open Decision Maker");
		shell.setLayout(new GridLayout(1, false));

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setImage(SWTResourceManager.getImage(Main_Gui.class,
				"/r2b2/odm/gui/resources/Folder_32.png"));
		/**
		 * Icon: Folder_32.png Author:Leomx Company:Studiomx.eu ©2007 License:
		 * Free ====================================================
		 * 
		 * All the icons contained in this archive are free. If you like them
		 * just send an email at "studiomx@studiomx.eu" or visit the website
		 * "www.studiomx.eu" . They can be given out provided that they are
		 * neither altered nor separated from this "Read me" file and from a
		 * reference to the site "www.studiomx.eu".
		 * 
		 * ====================================================
		 * 
		 * Artwork by Studiomx.eu studiomx@studiomx.eu www.studiomx.eu
		 */
		mntmFile.setText("&File");
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmNew = new MenuItem(menu_1, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ahpModel = new AhpModel();
				ahpModel.setGoal(new Goal(""));
				controller.setModel(ahpModel);
				updateAhpModel();
			}
		});
		mntmNew.setText("New");

		MenuItem mntmLoad = new MenuItem(menu_1, SWT.NONE);
		mntmLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int goalId = 0;

				if (!verifySqlConfigured())
					return;

				try {
					ArrayList<Goal> goals = controller.getGoalsFromDataBase();

					// make selection between goals
					GoalSelectDialog gsd = new GoalSelectDialog(shell, SWT.NONE);
					goalId = gsd.open(goals);

					if (goalId != 0) {
						controller.loadModelFromDatabase(goalId);
						updateAhpModel();
						MessageDialog.openInformation(shell,
								"Operation Successfull", "Load successful.");
					}

				} catch (SQLException e1) {
					MessageDialog
							.openError(
									shell,
									"Error loading data",
									"The following error occured: \n"
											+ e1.getMessage());
				}

			}
		});
		mntmLoad.setText("Load From Database");

		MenuItem mntmLoadFromFile = new MenuItem(menu_1, SWT.NONE);
		mntmLoadFromFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setFilterExtensions(new String[] {
						"*" + AhpController.ODMEXT, "*.*" });
				shell.setEnabled(false);
				String fileName = fd.open();
				shell.setEnabled(true);
				if (fileName == null)
					return;

				try {
					controller.loadModelFromFile(fileName);
					updateAhpModel();

					MessageDialog.openInformation(shell,
							"Operation Successfull", "Load successful.");

				} catch (IOException e1) {
					MessageDialog.openError(shell, "Error loading file",
							"Could not load file. The following error occured: \n"
									+ e1.getLocalizedMessage());
				}

			}
		});
		mntmLoadFromFile.setText("Load From File");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmSaveToDb = new MenuItem(menu_1, SWT.NONE);
		mntmSaveToDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!verifySqlConfigured())
					return;

				try {
					controller.saveModelToDataBase();

					MessageDialog.openInformation(shell,
							"Operation Successfull", "Save successful.");
				} catch (SQLException e1) {
					MessageDialog
							.openError(
									shell,
									"Error saving data",
									"The following error occured: \n"
											+ e1.getMessage());
				}
			}
		});
		mntmSaveToDb.setText("Save To Database");

		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!verifySqlConfigured())
					return;

				// Reset goal id to enforce new id from data base
				controller.getModel().getGoal().setId(0);
				// Save to DB
				try {
					controller.saveModelToDataBase();

					MessageDialog.openInformation(shell,
							"Operation Successfull", "Save successful.");
				} catch (SQLException e1) {
					MessageDialog
							.openError(
									shell,
									"Error saving data",
									"The following error occured: \n"
											+ e1.getMessage());
				}
			}
		});
		mntmSaveAs.setText("Save Copy To Database");

		MenuItem mntmSaveAsFile = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAsFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell, SWT.SAVE);

				fd.setFilterExtensions(new String[] {
						"*" + AhpController.ODMEXT, "*.*" });
				shell.setEnabled(false);
				String fileName = fd.open();
				shell.setEnabled(true);
				if (fileName == null)
					return;

				try {
					controller.saveModelToFile(fileName);

					MessageDialog.openInformation(shell,
							"Operation Successfull", "Save successful.");
				} catch (IOException e1) {
					MessageDialog
							.openError(
									shell,
									"Error saving data",
									"The following error occured: \n"
											+ e1.getMessage());
				}

			}
		});
		mntmSaveAsFile.setText("Save To File");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmConfiguration = new MenuItem(menu_1, SWT.NONE);
		mntmConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openConfiguration();
			}
		});
		mntmConfiguration.setText("Configuration");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setImage(SWTResourceManager.getImage(Main_Gui.class,
				"/r2b2/odm/gui/resources/Help-16.png"));
		/**
		 * Help-16.png Icon out of --Quartz Icon Pack -- Designer: Andy Gongea
		 * Website: http://www.graphicrating.com/ Twitter: @andygongea
		 * 
		 * The icons are free to use for any personal or commercial projects.
		 * Cheers!
		 * 
		 * contact: http://www.graphicrating.com/387
		 */
		mntmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Program.launch(openFile("UserManual.pdf", "User Manual", ".pdf")
						.getAbsolutePath());
				;

			}
		});
		mntmHelp.setText("Help");

		MenuItem mntmAbout = new MenuItem(menu, SWT.NONE);
		mntmAbout.setImage(SWTResourceManager.getImage(Main_Gui.class,
				"/r2b2/odm/gui/resources/Info-32.png"));
		/**
		 * --- Quartz Icon Pack ---
		 * 
		 * 
		 * Designer: Andy Gongea Website: http://www.graphicrating.com/ Twitter: @andygongea
		 * 
		 * The icons are free to use for any personal or commercial projects.
		 * Cheers!
		 * 
		 * 
		 * I am looking for new projects on web design, graphic design and web
		 * development. So feel free to contact me for any projects or
		 * collaborations.
		 * 
		 * http://www.graphicrating.com/387
		 */
		mntmAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new AboutDialog(shell, SWT.NONE).open();
			}
		});
		mntmAbout.setText("About");

		guiTabFolder = new GuiTabFolder(shell, SWT.NONE, controller);
		guiTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		guiTabFolder.setLayout(new GridLayout(1, false));

		AhpModel ahpModel = new AhpModel();
		ahpModel.setGoal(new Goal(""));
		// Model has to be set at one point during the initialization
		controller.setModel(ahpModel);
		updateAhpModel();

	}

	protected void openConfiguration() {
		ConfigView config = new ConfigView(shell, 0, controller.getConfig());
		shell.setEnabled(false);
		AhpConfig conf = config.open();
		if (conf != null) {

			controller.setConfig(conf);
			try {
				controller.saveConfig();
			} catch (Exception e1) {
				MessageDialog.openError(shell,
						"Error saving the configuration",
						"The following error occured: \n" + e1.getMessage());
			}
		}
		shell.setEnabled(true);
	}

	/**
	 * Verifies that the SQL connection has been configured. Opens the
	 * configuration dialog if the connection is not configured yet.
	 */
	private boolean verifySqlConfigured() {
		if (!controller.isSqlConfigured()) {
			MessageDialog
					.openError(
							shell,
							"",
							"The database connection has not been configured yet, please do the configuration and try again.");
			openConfiguration();

			// try again after configuration dialog. if connection is
			// still not
			// configured, cancel process.
			if (!controller.isSqlConfigured()) {
				return false;
			}

		}
		return true;
	}

	GuiTabFolder guiTabFolder;

	public void updateAhpModel() {
		AhpModel ahpModel = controller.getModel();
		guiTabFolder.setAhpModel(ahpModel);

	}

	public AhpModel getAhpModel() {

		return ahpModel;
	}

	/**
	 * Opens a file
	 * 
	 * @param pathIn
	 *            location of the file in the jar
	 * @param fileName
	 *            file name of the generated file
	 * @param fileExtention
	 *            type of the file which is generated
	 */
	public static File openFile(String pathIn, String fileName,
			String fileExtention) {
		File tempFile = null;
		try {
			// generate tempfile with data

			InputStream fileIn = Main_Gui.class.getResourceAsStream(pathIn);
			tempFile = File.createTempFile(fileName, fileExtention);
			tempFile.deleteOnExit();
			FileOutputStream pdfOut = new FileOutputStream(tempFile);
			byte[] buffer = new byte[32768];

			if (fileIn != null) {
				int len;
				while ((len = fileIn.read(buffer)) > 0) {
					pdfOut.write(buffer, 0, len);
				}

			}
			pdfOut.close();
			fileIn.close();

		} catch (IOException e1) {

			JOptionPane
					.showMessageDialog(null, "Failure while generating file");
		}
		return tempFile;

	}

}
