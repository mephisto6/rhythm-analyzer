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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.google.common.primitives.Ints;

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
			double s = MetricsUtils.getSumOfSquaredError(clusters);
			if (s < min) {
				min = s;
				i = c;
				bestClusters = clusters;
			}
			// log.debug(c + " avg of avg distances: "
			// + MetricsUtils.getAvgAvgDistance(clusters));
			// log.debug(c + " avg of max distances: "
			// + MetricsUtils.getAvgMaxDistance(clusters));
			// log.debug(c + " max of max distances: "
			// + MetricsUtils.getMaxMaxDistance(clusters));
			// log.debug(c + " sum of all distances: "
			// + MetricsUtils.getSumOfDistances(clusters));
			// log.debug(c + " sum of squared error: "
			// + MetricsUtils.getSumOfSquaredError(clusters));
			// log.debug(c + " sum of sum of dists from centers: "
			// + MetricsUtils.getSumOfDistsFromCentre(clusters));
			log.debug(c + " sum squared error: "
					+ MetricsUtils.getSumOfSquaredError(clusters));
			c++;
		}
		log.debug("best clusters no: " + i + " (val: " + min + ")");
		this.clusters = bestClusters;
	}

	private List<Cluster> classifyLoop() {
		List<Cluster> clusters = new ArrayList<Cluster>();
		String s = "";
		int[] rs = getRandomNums();
		for (int i = 0; i < rs.length; i++) {
			// int k = (int) (Math.random() * getDistMx().length);
			// int k = new int[] { 6, 4, 4, 5 }[i];
			int k = rs[i];
			s += k + " ";
			clusters.add(new Cluster(k, getDistMx()));
		}
		System.out.println(s);
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

	private int[] getRandomNums() {
		int n = getDistMx().length;
		List<Integer> list = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++) {
			list.add(i);
		}
		Collections.shuffle(list, new Random());
		int[] ints = Ints.toArray(list.subList(0, MainAnalyser.CLASS_NUM));
		return ints;
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

	@SuppressWarnings("unused")
	private void printClusters(List<Cluster> clusters) {
		for (Cluster c : clusters) {
			System.out.println(c.getNums());
		}
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
