/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.utils.MatrixUtils;

import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class MinDistanceClassifier extends Classifier {

	private static Logger log = Logger.getLogger(MinDistanceClassifier.class);

	// put elements to the same cluster if distance between them is below this
	// value
	private static double minThreshold =
	// 0.2;
	1000;

	public MinDistanceClassifier(Comparer comparer) {
		setComparerAndDistMx(comparer);
		classify();
	}

	public MinDistanceClassifier(double[][] distMx) {
		setDistMx(distMx);
		classify();
	}

	private int[] classes;

	@Override
	public int[] getClasses() {
		return classes;
	}

	@Override
	public void classify() {
		int limit = Math.min(MainAnalyser.LIMIT, getDistMx().length);
		classes = new int[limit];
		for (int i = 0; i < limit; i++) {
			classes[i] = i + 1;
		}
		double[][] mx = MatrixUtils.copyMx(getDistMx());
		while (MainAnalyser.CLASS_NUM < getClassNum(classes)
				&& getMinimal(mx) < minThreshold) {
			int[] indexes = getClosestNeighbours(mx);
			if (classes[indexes[0]] != classes[indexes[1]]) {
				makeEqual(classes[indexes[0]], classes[indexes[1]]);
			}
		}
		log.debug("Calculated classes: " + Arrays.toString(getClasses()));
	}

	// returns the indexes of the minimal element in the temporal matrix
	// and sets it to a very big number
	private static int[] getClosestNeighbours(double[][] mx) {
		int[] result = new int[2];
		double min = Double.MAX_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] < min) {
					min = mx[i][j];
					result[0] = i;
					result[1] = j;
				}
			}
		}
		log.debug(mx[result[0]][result[1]] + " is the lowest value between "
				+ (result[0] + 1) + " and " + (result[1] + 1) + ".");

		mx[result[0]][result[1]] = Double.MAX_VALUE;

		return result;
	}

	// return the minimal element of the temporal matrix
	private double getMinimal(double[][] mx) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] < min) {
					min = mx[i][j];
				}
			}
		}
		return min;
	}

	// makes equal all the occurrences of oldClass with newClass
	private void makeEqual(int oldClass, int newClass) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == oldClass) {
				log.debug("Class of " + (i + 1) + " (" + getClasses()[i]
						+ ") is set to " + newClass);
				classes[i] = newClass;
			}
		}
	}

	@Override
	public String getDescription() {
		return "min";
	}

}
