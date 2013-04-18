/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.utils.MetricsUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public abstract class KMeansClassifier extends Classifier {

	public static int loopNum = 3;

	private static Logger log = Logger.getLogger(KMeansClassifier.class);

	List<Cluster> clusters = new ArrayList<Cluster>();

	int[] classes;

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public double getMetricToMinimize(List<Cluster> clusters) {
		return MetricsUtils.getSumOfSquaredError(clusters);
	}

	public abstract int[] getFirstCenters();

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getClasses()
	 */
	@Override
	public int[] getClasses() {
		if (classes == null) {
			int len = getDistMx().length;
			classes = new int[len];
			for (int i = 0; i < len; i++) {
				for (Cluster c : getClusters()) {
					if (c.containsNum(i)) {
						classes[i] = c.getId();
					}
				}
			}
		}
		return classes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#classify(double[][])
	 */
	@Override
	public void classify() {
		List<Cluster> bestClusters = null;
		int c = 0;
		int i = 0;
		double min = Double.MAX_VALUE;
		while (c != loopNum) {
			List<Cluster> clusters = classifyLoop();
			double s = getMetricToMinimize(clusters);
			if (s < min) {
				min = s;
				i = c;
				bestClusters = clusters;
			}
			log.debug("metric to minimize: " + getMetricToMinimize(clusters));
			c++;
		}
		log.debug("best clusters no: " + i + " (val: " + min + ")");
		setClusters(bestClusters);
	}

	protected List<Cluster> classifyLoop() {
		List<Cluster> clusters = new ArrayList<Cluster>();
		int[] rs = getFirstCenters();
		for (int i = 0; i < rs.length; i++) {
			int k = rs[i];
			clusters.add(new Cluster(k, getDistMx()));
		}
		fistArrange(clusters);

		int changes = 0;
		do {
			changes = 0;
			for (Cluster c : clusters) {
				changes += c.calculateNewCentre();
			}
			changes += arrangeClasses(clusters);
		} while (changes != 0);

		return clusters;
	}

	private int arrangeClasses(List<Cluster> clusters) {
		int k = 0;
		for (int i = 0; i < getDistMx().length; i++) {
			Cluster oldOne = getClusterOf(clusters, i);
			Cluster newOne = getClosestCluster(clusters, i);
			if (!oldOne.equals(newOne)) {
				log.debug(i + " removed from " + oldOne.getNums());
				oldOne.removeNum(i);
				newOne.addNum(i);
				log.debug("\t\t and added to " + newOne.getNums());
				k++;
			}
		}
		return k;
	}

	private void fistArrange(List<Cluster> clusters) {
		for (int i = 0; i < getDistMx().length; i++) {
			getClosestCluster(clusters, i).addNum(i);
		}
	}

	private Cluster getClusterOf(List<Cluster> clusters, int i) {
		Cluster res = null;
		for (Cluster c : clusters) {
			if (c.containsNum(i)) {
				res = c;
			}
		}
		return res;
	}

	private Cluster getClosestCluster(List<Cluster> clusters, int i) {
		Cluster res = null;
		double min = Double.MAX_VALUE;
		for (Cluster c : clusters) {
			double dist = c.getDistFromCentre(i);
			if (dist < min) {
				min = dist;
				res = c;
			}
		}
		return res;
	}

}
