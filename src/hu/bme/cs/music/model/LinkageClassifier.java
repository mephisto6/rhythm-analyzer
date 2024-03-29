/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.utils.MetricsUtils;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public abstract class LinkageClassifier extends Classifier {

	private static Logger log = Logger.getLogger(LinkageClassifier.class);

	public abstract double getDistance(Cluster c1, Cluster c2);

	public void init(Comparer comparer) {
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
		while (getClusters().size() > getClassNum()) {
			findClosestClusters();
		}
		log.debug("sum of squared error: "
				+ MetricsUtils.getSumOfSquaredError(clusters));
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
