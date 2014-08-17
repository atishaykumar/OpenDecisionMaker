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

import java.security.InvalidParameterException;

/**
 * Helper class for math calculations
 * 
 * @author Alex
 * 
 */

public abstract class MathHelper {

	/**
	 * Calculates the Consistency Ratio of a given matrix
	 * 
	 * @param mm
	 * @return consistency ratio
	 */
	public static double calculateConsistencyRatio(Matrix mm) {
		// the number of criteria
		int n = mm.getData().length;

		// get eigenvector
		double[] vec = mm.getEigenvectorFinal();

		// copy eigenvector data to temp matrix
		Matrix mEvec = new Matrix(vec.length, 1);

		for (int i = 0; i < vec.length; i++) {
			mEvec.getData()[i][0] = vec[i];
		}

		// Multiply input matrix by the eigenvector-matrix
		Matrix mRes = mm.times(mEvec);

		// Divide resulting vector with eigenvector fields
		double[] mDiv = new double[n];
		for (int i = 0; i < n; i++) {
			mDiv[i] = mRes.getData()[i][0] / vec[i];
		}

		// Calculate mean of resulting vector (lambda max)
		double vals = 0;
		for (double d : mDiv) {
			vals += d;
		}
		double lambdaMax = vals / n;

		// Calculate Consistency Index
		double ci = (lambdaMax - n) / (n - 1);

		double cr;

		// Calculate Consistency Ratio

		// If there are two or less criteria, CR is always 0.
		if (n <= 2) {
			cr = 0;
		} else {
			double crFactor = getCrFactor(n);
			cr = ci / crFactor;
		}

		return cr;
	}

	/**
	 * Returns the Consistency Ratio factor for a given "n". Beware: n<=2
	 * returns 0.0
	 * 
	 * @param n
	 * @return
	 */
	private static double getCrFactor(int n) {
		switch (n) {
		case 1:
			return 0.0;
		case 2:
			return 0.0;
		case 3:
			return 0.58;
		case 4:
			return 0.90;
		case 5:
			return 1.12;
		case 6:
			return 1.24;
		case 7:
			return 1.32;
		case 8:
			return 1.41;
		case 9:
			return 1.45;
		case 10:
			return 1.49;
		case 11:
			return 1.51;
		case 12:
			return 1.48;
		case 13:
			return 1.56;
		case 14:
			return 1.57;
		case 15:
			return 1.59;
		default:
			return n / 10; // arbitrary value, unrealistic number of criteria
		}
	}

	/**
	 * Returns the highest difference between two vector values of the same
	 * level
	 * 
	 * @param v1
	 * @param v2
	 * @return max difference of 2 vectors
	 */
	public static double getBiggestVectorDifference(double[] v1, double[] v2) {
		verifySameVectorLenght(v1, v2);

		double dif = 0;

		for (int i = 0; i < v2.length; i++) {
			double curDif = Math.abs(v2[i]) - Math.abs(v1[i]);
			if (curDif > dif) {
				dif = curDif;
			}
		}

		return dif;
	}

	/**
	 * Verifies that two vectors have the same length
	 * 
	 * @param v1
	 * @param v2
	 */
	private static void verifySameVectorLenght(double[] v1, double[] v2) {
		if (v1.length != v2.length) {
			throw new InvalidParameterException(
					"Vectors have to be the same size!");
		}
	}

}
