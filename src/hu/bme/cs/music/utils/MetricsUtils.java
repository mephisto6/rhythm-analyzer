/**
 * 
 */
package hu.bme.cs.music.utils;

import hu.bme.cs.music.model.Cluster;

import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class MetricsUtils {

	/**
	 * avg of avg distances within each cluster
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getAvgAvgDistance(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getAvgDistance();
		}
		return d / clusters.size();
	}

	/**
	 * avg of max distances within each cluster
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getAvgMaxDistance(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getMaxDistance();
		}
		return d / clusters.size();
	}

	/**
	 * max of max distances within each cluster
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getMaxMaxDistance(List<Cluster> clusters) {
		double d = -Double.MAX_VALUE;
		for (Cluster c : clusters) {
			double k = c.getMaxDistance();
			if (k > d) {
				d = k;
			}
		}
		return d;
	}

	/**
	 * sum of all distances within each cluster
	 * @param clusters
	 * @return
	 */
	public static double getSumOfDistances(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getSumOfDistances();

		}
		return d;
	}

	/***
	 * sum of squared errors of the elements and the centers
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getSquaredError(List<Cluster> clusters) {
		double s = 0;
		for (Cluster c : clusters) {
			for (int i : c.getNums()) {
				s += Math.pow(c.getDistFromCentre(i), 2);
			}
		}
		return s;
	}
}
