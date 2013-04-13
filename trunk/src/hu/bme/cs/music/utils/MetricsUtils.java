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

	public static double getAvgDiameter(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getDiameter();
		}
		return d / clusters.size();
	}

	public static double getAvgMaxDistance(List<Cluster> clusters) {
		double d = 0;
		for (Cluster c : clusters) {
			d += c.getMaxDistanceWithinCluster();
		}
		return d / clusters.size();
	}

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
