/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.utils.MatrixUtils;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class MaxDistanceClassifier extends Classifier {

	private static Logger log = Logger.getLogger(MaxDistanceClassifier.class);

	// put elements to separate clusters if distance between them is above this
	// value
	private static double maxThreshold =
	// 0.6;
	0;

	public MaxDistanceClassifier(Comparer comparer) {
		setComparer(comparer);
		classify(comparer.getDistanceMx());
	}

	public MaxDistanceClassifier(double[][] distMx) {
		classify(distMx);
	}

	@Override
	public void classify(double[][] distMx) {
		int limit = Math.min(MainAnalyser.LIMIT, distMx.length);
		classes = new int[limit];
		for (int i = 0; i < limit; i++) {
			classes[i] = -1;
		}
		double[][] mx = MatrixUtils.copyMx(distMx);
		while (getClassNum(classes) - 1 < MainAnalyser.CLASS_NUM
				&& getMaximal(mx) > maxThreshold) {
			int[] indexes = getFarthestNeighbours(mx);
			log.debug("Class of " + (indexes[0] + 1) + " is set to "
					+ (indexes[0] + 1));
			log.debug("Class of " + (indexes[1] + 1) + " is set to "
					+ (indexes[1] + 1));
			classes[indexes[0]] = indexes[0];
			classes[indexes[1]] = indexes[1];
		}
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == -1) {
				log.debug("Class of " + (i + 1) + " (" + classes[i]
						+ ") is set to "
						+ (findClosestClass(mx, classes, i) + 1));
				classes[i] = findClosestClass(mx, classes, i);
			}
		}
	}

	private int[] classes;

	@Override
	public int[] getClasses() {
		return classes;
	}

	private double getMaximal(double[][] mx) {
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] > max) {
					max = mx[i][j];
				}
			}
		}
		return max;
	}

	private int findClosestClass(double[][] mx, int[] classes, int i) {
		double min = Double.MAX_VALUE;
		int index = 0;
		for (int j = 0; j < classes.length; j++) {
			if (classes[j] != -1) {
				if (getDistance(mx, classes[j], i) < min) {
					min = getDistance(mx, classes[j], i);
					index = j;
				}
			}
		}
		// the class there may be already calculated
		return classes[index];
	}

	// returns the indexes of the maximal element in the temporal matrix
	// and sets it to a very low value
	private int[] getFarthestNeighbours(double[][] mx) {
		int[] result = new int[2];
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] > max) {
					max = mx[i][j];
					result[0] = i;
					result[1] = j;
				}
			}
		}
		log.debug(mx[result[0]][result[1]] + " is the highest value between "
				+ (result[0] + 1) + " and " + (result[1] + 1) + ".");

		mx[result[0]][result[1]] = -Double.MAX_VALUE;
		return result;
	}

	private double getDistance(double[][] mx, Integer integer, int i) {
		if (i > integer) {
			return mx[i][integer];
		} else {
			return mx[integer][i];
		}
	}

	@Override
	public String getDescription() {
		return "max";
	}

}
