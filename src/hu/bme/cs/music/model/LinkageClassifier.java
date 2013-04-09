/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.utils.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public abstract class LinkageClassifier extends Classifier {

	List<Cluster> clusters;

	private double[][] distMx;

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public double[][] getDistMx() {
		return distMx;
	}

	public void setDistMx(double[][] distMx) {
		this.distMx = distMx;
	}

	public abstract double getDistance(Cluster c1, Cluster c2);

	public void init(Comparer comparer) {
		setClusters(new ArrayList<Cluster>());
		setComparer(comparer);
		setDistMx(MatrixUtils.copyMx(comparer.getDistanceMx()));
		classify(getDistMx());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getClasses()
	 */
	@Override
	public int[] getClasses() {
		int len = getComparer().getDistanceMx().length;
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
	public void classify(double[][] distMx) {
		for (int i = 0; i < distMx.length; i++) {
			getClusters().add(new Cluster(i));
		}
		while (getClusters().size() > MainAnalyser.CLASS_NUM) {
			findClosestClusters();
		}

	}

	protected void findClosestClusters() {
		double min = Double.MAX_VALUE;
		Cluster c1 = null;
		Cluster c2 = null;
		for (int i = 0; i < getClusters().size(); i++) {
			for (int j = i + 1; j < getClusters().size(); j++) {
				double d = getDistance(getClusters().get(i),
						getClusters().get(j));
				if (min > d) {
					min = d;
					c1 = getClusters().get(i);
					c2 = getClusters().get(j);
				}
			}
		}
		c1.joinCluster(c2);
		getClusters().remove(c2);
	}

}
