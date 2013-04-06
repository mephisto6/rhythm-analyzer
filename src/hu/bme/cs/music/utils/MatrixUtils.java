/**
 * 
 */
package hu.bme.cs.music.utils;

import com.google.common.primitives.Doubles;

/**
 * @author Jozsef
 * 
 */
public class MatrixUtils {

	public static void normalizeMx(double[][] mx) {
		int size = mx.length;
		double max = Doubles.max(mx[0]);
		for (int i = 1; i < size; i++) {
			double locMax = Doubles.max(mx[i]);
			if (locMax > max) {
				max = locMax;
			}
		}
		if (max > 1.0) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					mx[j][i] = mx[j][i] / max;
				}
			}
		}
	}
	
	public static double[][] copyMx(double[][] specmx) {
		double[][] newmx = new double[specmx.length][];
		for (int i = 0; i < specmx.length; i++)
			newmx[i] = specmx[i].clone();
		return newmx;
	}

}
