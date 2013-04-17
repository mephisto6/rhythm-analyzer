/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.model.Cluster;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.KMeansClassifier;
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
public class RandomKMeansClassifier extends KMeansClassifier {

	private static Logger log = Logger.getLogger(RandomKMeansClassifier.class);

	public RandomKMeansClassifier(Comparer comparer) {
		setComparerAndDistMx(comparer);
		classify();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "random k-means";
	}

	@Override
	public double getMetricToMinimize(List<Cluster> clusters) {
		return MetricsUtils.getSumOfSquaredError(clusters);
	}

	@Override
	public int[] getFirstCenters() {
		int n = getDistMx().length;
		List<Integer> list = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++) {
			list.add(i);
		}
		Collections.shuffle(list, new Random());
		log.debug("first centers: " + list.subList(0, MainAnalyser.CLASS_NUM));
		return Ints.toArray(list.subList(0, MainAnalyser.CLASS_NUM));
	}

}
