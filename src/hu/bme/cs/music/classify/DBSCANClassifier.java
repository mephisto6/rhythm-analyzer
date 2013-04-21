/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.Cluster;
import hu.bme.cs.music.model.Comparer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class DBSCANClassifier extends Classifier {

	private static Logger log = Logger.getLogger(DBSCANClassifier.class);

	private List<Cluster> clusters = new ArrayList<Cluster>();

	private static double eps = 0.2;

	private static int minpts = 5;

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
	 * @see hu.bme.cs.music.model.Classifier#classify()
	 */
	@Override
	public void classify() {
		List<Integer> visited = new ArrayList<Integer>();
		double[][] mx = getDistMx();
		for (int k = 0; k < mx.length; k++) {
			visited.add(k);
			Set<Integer> neigbours = getNeighbours(k);
			if (neigbours.size() <= minpts) {
				log.debug(k + " has only " + neigbours.size()
						+ " neighbours -> NOISE");
			} else {
				Cluster newCluster = new Cluster(k, mx);
				expandCluster(k, neigbours, visited, newCluster);
			}
		}
	}

	private void expandCluster(int p, Set<Integer> neigbours,
			List<Integer> visited, Cluster newCluster) {
		newCluster.addNum(p);
		for (int k = 0; k < neigbours.size(); k++) {
			if (!visited.contains(k)) {
				visited.add(k);
				Set<Integer> kNeigbours = getNeighbours(k);
				if (kNeigbours.size() >= minpts) {
					neigbours.addAll(kNeigbours);
				}
			}
			if (notYetMember(k)) {
				newCluster.addNum(k);
			}
		}
	}

	private boolean notYetMember(int k) {
		for (Cluster c : clusters) {
			if (c.containsNum(k)) {
				return true;
			}
		}
		return false;
	}

	private Set<Integer> getNeighbours(int p) {
		Set<Integer> elements = new HashSet<Integer>();
		double[][] mx = getDistMx();
		for (int k = 0; k < mx.length; k++) {
			if (k < p && eps >= mx[p][k]) {
				elements.add(k);
			} else if (k > p && eps >= mx[k][p]) {
				elements.add(k);
			}
		}
		return elements;
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
