/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.utils.MetricsUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public abstract class LinkageClassifier extends Classifier {

	private static Logger log = Logger.getLogger(LinkageClassifier.class);

	List<Cluster> clusters;

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public abstract double getDistance(Cluster c1, Cluster c2);

	public void init(Comparer comparer) {
		setClusters(new ArrayList<Cluster>());
		setComparerAndDistMx(comparer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getClasses()
	 */
	@Override
	public int[] getClasses() {
		int len = getDistMx().length;
		int[] classes = new int[len];
		for (int i = 0; i < len; i++) {
			for (Cluster c : getClusters()) {
				if (c.containsNum(i)) {
					classes[i] = c.getId();
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
		for (int i = 0; i < getDistMx().length; i++) {
			getClusters().add(new Cluster(i, getDistMx()));
		}
		while (getClusters().size() > MainAnalyser.CLASS_NUM) {
			findClosestClusters();
		}
		log.debug("avg of avg distances: "
				+ MetricsUtils.getAvgAvgDistance(clusters));
		log.debug("avg of max distances: "
				+ MetricsUtils.getAvgMaxDistance(clusters));
		log.debug("max of max distances: "
				+ MetricsUtils.getMaxMaxDistance(clusters));
		log.debug("sum of all distances: "
				+ MetricsUtils.getSumOfDistances(clusters));
		log.debug("sum of squared error: "
				+ MetricsUtils.getSquaredError(clusters));
	}

	protected void findClosestClusters() {
		double min = Double.MAX_VALUE;
		Cluster c1 = null;
		Cluster c2 = null;
		for (int i = 0; i < getClusters().size(); i++) {
			for (int j = i + 1; j < getClusters().size(); j++) {
				double d = getDistance(getClusters().get(i),
						getClusters().get(j));
				if (d < min) {
					min = d;
					c1 = getClusters().get(i);
					c2 = getClusters().get(j);
				}
			}
		}
		log.debug(c1.getId1() + " (" + c1.getNums() + ") joined with "
				+ c2.getId1() + " (" + c2.getNums() + "), min: " + min);
		c1.joinCluster(c2);
		getClusters().remove(c2);
	}

}
