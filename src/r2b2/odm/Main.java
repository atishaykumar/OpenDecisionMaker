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
package r2b2.odm;

import r2b2.odm.gui.Main_Gui;

/**
 * Starts the Application
 * @author Rotter
 */
public class Main {

	/**
	 * start the application
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		try {
			Main_Gui application = new Main_Gui();

			application.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
