/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.Cluster;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.utils.MetricsUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class KMeansClassifier extends Classifier {

	private static Logger log = Logger.getLogger(KMeansClassifier.class);

	List<Cluster> clusters = new ArrayList<Cluster>();

	int[] classes;

	public KMeansClassifier(Comparer comparer) {
		setComparerAndDistMx(comparer);
		classify();
	}

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
				for (Cluster c : clusters) {
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
		while (c != 10) {
			List<Cluster> clusters = classifyLoop();
			double s = MetricsUtils.getSumOfSumOfMinDistances(clusters);
			if (s < min) {
				min = s;
				i = c;
				bestClusters = clusters;
			}
//			log.debug(c + " avg of avg distances: "
//					+ MetricsUtils.getAvgAvgDistance(clusters));
//			log.debug(c + " avg of max distances: "
//					+ MetricsUtils.getAvgMaxDistance(clusters));
//			log.debug(c + " max of max distances: "
//					+ MetricsUtils.getMaxMaxDistance(clusters));
//			log.debug(c + " sum of all distances: "
//					+ MetricsUtils.getSumOfDistances(clusters));
//			log.debug(c + " sum of squared error: "
//					+ MetricsUtils.getSumOfSquaredError(clusters));
//			log.debug(c + " sum of sum of dists from centers: "
//					+ MetricsUtils.getSumOfDistsFromCentre(clusters));
			log.debug(c + " sum of sum of min distances: "
					+ MetricsUtils.getSumOfSumOfMinDistances(clusters));
			c++;
		}
		log.debug("best clusters no: " + i + " (val: " + min + ")");
		this.clusters = bestClusters;
	}

	private List<Cluster> classifyLoop() {
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (int i = 0; i < MainAnalyser.CLASS_NUM; i++) {
			int k = (int) (Math.random() * getDistMx().length);
			clusters.add(new Cluster(k, getDistMx()));
		}

		int changes = 0;
		do {
			clearClusters(clusters);
			arrangeClasses(clusters);
			changes = 0;
			for (Cluster c : clusters) {
				changes += c.calculateNewCentre();
			}
		} while (changes != 0);

		return clusters;
	}

	private void arrangeClasses(List<Cluster> clusters) {
		for (int i = 0; i < getDistMx().length; i++) {
			getClosestCluster(clusters, i).addNum(i);
		}
	}

	private void clearClusters(List<Cluster> clusters) {
		for (Cluster c : clusters) {
			c.clearCluster();
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "k-means";
	}

}
