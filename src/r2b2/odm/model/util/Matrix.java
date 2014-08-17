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
package r2b2.odm.model.util;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * Offers the various matrix operations required in an AHP process
 * 
 * @author Alex
 */

public class Matrix implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7704090836145215786L;
	/**
	 * The raw matrix data
	 */
	private double[][] data;
	private final int rows, columns;

	/**
	 * 
	 * @param rows
	 * @param columns
	 */
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		data = new double[rows][columns];
	}

	/**
	 * 
	 * @param data
	 */
	public Matrix(double[][] data) {

		rows = data.length;
		if (rows < 1) {
			throw new InvalidParameterException(
					"Cannot create matrix from empty array.");
		}
		columns = data[0].length;

		// Verify that all rows have the same lenght
		for (int i = 0; i < rows; i++) {
			if (data[i].length != columns) {
				throw new IllegalArgumentException(
						"All rows must have the same length.");
			}
		}

		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(this.data, other.data)) {
			return false;
		}
		if (this.rows != other.rows) {
			return false;
		}
		if (this.columns != other.columns) {
			return false;
		}
		return true;
	}

	/**
	 * Calculates the eigenvector of the matrix
	 * 
	 * @return eigenvector of the matrix
	 */
	public double[] getEigenvector() {
		double[] vector = new double[rows];

		// Calculate sum of each row
		double[] rowSums = new double[rows];
		for (int i = 0; i < rows; i++) {
			double rowSum = 0;
			for (int j = 0; j < columns; j++) {
				rowSum += data[i][j];
			}
			rowSums[i] = rowSum;
		}

		// Calculate total sum
		double totalSum = 0;
		for (double d : rowSums) {
			totalSum += d;
		}

		// Calculate portion of row
		for (int i = 0; i < rows; i++) {
			vector[i] = rowSums[i] / totalSum;
		}

		return vector;
	}

	/**
	 * Deep copy of the matrix
	 * 
	 * @return real copy of a matrix
	 */
	public Matrix copy() {

		Matrix copy = new Matrix(rows, columns);

		double[][] dataCopy = copy.getData();

		for (int rowi = 0; rowi < rows; rowi++) {
			for (int columnj = 0; columnj < columns; columnj++) {
				dataCopy[rowi][columnj] = data[rowi][columnj];
			}
		}

		return copy;
	}

	/**
	 * Produces the square of a matrix until the eigenvector does not longer
	 * change
	 * 
	 * @return the eigenvector of the matrix
	 */
	public double[] getEigenvectorFinal() {

		Matrix mTemp = this;
		Matrix mSquared;
		double matrixDif = 0;

		do {
			// Calculate next vector
			mSquared = mTemp.square();

			// Calculate vector difference
			matrixDif = MathHelper.getBiggestVectorDifference(
					mSquared.getEigenvector(), mTemp.getEigenvector());

			// Override input variable with new values
			mTemp = mSquared;

			// Stop when difference is below 0.0001
		} while (!(matrixDif < 0.0001));

		return mTemp.getEigenvector();
	}

	/**
	 * Calculates the square of a matrix
	 * 
	 * @return squared Matrix
	 */
	public Matrix square() {
		return this.times(this);
	}

	/**
	 * Multiplies the matrix with another matrix
	 * 
	 * @param b
	 * @return
	 * result Matrix
	 */
	public Matrix times(Matrix b) {
		int rowCount = Math.min(rows, b.rows);
		int colCount = Math.min(b.columns, columns);
		Matrix result = new Matrix(rowCount, colCount);

		double[][] resultData = result.getData();

		double[] currentColumn = new double[columns];

		// Iterate over target columns
		for (int iTargetColNo = 0; iTargetColNo < b.columns; iTargetColNo++) {

			// Copy data of current target column
			for (int jRow = 0; jRow < columns; jRow++) {
				currentColumn[jRow] = b.data[jRow][iTargetColNo];
			}

			// Go through source rows
			for (int iRowNo = 0; iRowNo < rowCount; iRowNo++) {
				double[] currentRow = data[iRowNo];
				double scalarProduct = 0;

				for (int iColNo = 0; iColNo < columns; iColNo++) {
					scalarProduct += currentRow[iColNo] * currentColumn[iColNo];
				}
				resultData[iRowNo][iTargetColNo] = scalarProduct;
			}

		}

		return result;
	}

	/**
	 * Gets the column count
	 * 
	 * @return column count of a matrix
	 */
	public int getColumncount() {
		return columns;
	}

	/**
	 * Gets the raw data
	 * 
	 * @return data of a matrix
	 */
	public double[][] getData() {
		return data;
	}

	/**
	 * Gets the row count
	 * 
	 * @return row count of the matrix
	 */
	public int getRowcount() {
		return rows;
	}
}
