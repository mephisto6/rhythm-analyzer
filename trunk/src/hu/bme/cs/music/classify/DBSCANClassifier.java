/**
 * 
 */
package hu.bme.cs.music.classify;

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
public class DBSCANClassifier extends Classifier {

	private static Logger log = Logger.getLogger(DBSCANClassifier.class);

	//cluster1: 0.17
	//cluster2: 0.21
	public static double eps = 0.21;

	public static int minpts = 2;

	int[] classes;

	public DBSCANClassifier(Comparer comparer) {
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
				for (Cluster c : getClusters()) {
					if (c.containsNum(i)) {
						classes[i] = c.getId();
					}
				}
			}
		}
		return classes;
	}

	//https://github.com/KanwarBhajneek/DBSCAN/blob/master/src/dbscan/dbscan.java
	
	@Override
	public void classify() {
		List<Integer> visited = new ArrayList<Integer>();
		double[][] mx = getDistMx();
		for (int i = 0; i < mx.length; i++) {
			if (!visited.contains(i)) {
				visited.add(i);
				List<Integer> neighbours = getNeighbours(i);
				if (neighbours.size() >= minpts) {
					for (int j = 0; j < neighbours.size(); j++) {
						int akt = neighbours.get(j);
						if (!visited.contains(akt)) {
							visited.add(akt);
							List<Integer> neighbours2 = getNeighbours(akt);
							if (neighbours2.size() >= minpts) {
								for (int b : neighbours2) {
									if (!neighbours.contains(b)) {
										neighbours.add(b);
									}
								}
							}
						}
					}
					log.debug("Cluster " + i + " -> " + neighbours);
					getClusters().add(new Cluster(i, neighbours, mx));
				}
			}
		}
		List<Integer> noise = new ArrayList<Integer>();
		for (int i = 0; i < mx.length; i++) {
			if (notMember(i)) {
				noise.add(i);
				getClusters().add(new Cluster(i, mx));
			}
		}
		log.debug("Considered to be noise: " + noise);
		log.debug("sum of sum of min distances: "
				+ MetricsUtils.getSumOfSumOfMinDistances(getClusters()));
	}

	private List<Integer> getNeighbours(int p) {
		List<Integer> neighbours = new ArrayList<Integer>();
		// neighbours should contain the element itself
		neighbours.add(p);
		double[][] mx = getDistMx();
		for (int k = 0; k < mx.length; k++) {
			if (k < p && eps >= mx[p][k]) {
				neighbours.add(k);
			} else if (k > p && eps >= mx[k][p]) {
				neighbours.add(k);
			}
		}
		return neighbours;
	}

	private boolean notMember(int k) {
		for (Cluster c : getClusters()) {
			if (c.containsNum(k)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "DBSCAN";
	}

}
