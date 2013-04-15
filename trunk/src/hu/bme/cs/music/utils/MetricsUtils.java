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
	 * 
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

	/**
	 * sum of sum of distances from centre within each cluster
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getSumOfDistsFromCentre(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getSumOfDistFromCentre();

		}
		return d;
	}

	/**
	 * sum of avg of distances from centre within each cluster
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getSumOfAvgDistFromCentre(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getAvgDistFromCentre();

		}
		return d;
	}

	/***
	 * sum of squared errors of the elements and the centers
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getSumOfSquaredError(List<Cluster> clusters) {
		double s = 0;
		for (Cluster c : clusters) {
			s += c.getSquaredError();
		}
		return s;
	}

	/***
	 * sum of sum of min distances
	 * 
	 * @param clusters
	 * @return
	 */
	public static double getSumOfSumOfMinDistances(List<Cluster> clusters) {
		double s = 0;
		for (Cluster c : clusters) {
			s += c.getSumOfMinDistances();
		}
		return s;
	}
}
