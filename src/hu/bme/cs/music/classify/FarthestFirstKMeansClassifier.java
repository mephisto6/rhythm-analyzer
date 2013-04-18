/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.KMeansClassifier;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.primitives.Ints;

/**
 * @author Jozsef
 * 
 */
public class FarthestFirstKMeansClassifier extends KMeansClassifier {

	private static Logger log = Logger
			.getLogger(FarthestFirstKMeansClassifier.class);

	public FarthestFirstKMeansClassifier(Comparer comparer) {
		setComparerAndDistMx(comparer);
		classify();
	}

	@Override
	public void classify() {
		setClusters(classifyLoop());
		log.debug("metric to minimize: " + getMetricToMinimize(getClusters()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.KMeansClassifier#getFirstCenters()
	 */
	@Override
	public int[] getFirstCenters() {
		List<Integer> indexes = getFarthestNeighbours();
		while (indexes.size() != MainAnalyser.CLASS_NUM) {
			indexes.add(getNextFarthestElement(indexes));
		}
		log.debug("farthest indexes: " + indexes);
		return Ints.toArray(indexes);
	}

	protected Integer getNextFarthestElement(List<Integer> indexes) {
		int res = 0;
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < getDistMx().length; i++) {
			if (!indexes.contains(i)) {
				double d = getSumOfDistFromCenters(i, indexes);
				if (d > max) {
					max = d;
					res = i;
				}
			}
		}
		log.debug(res + " is " + max + " away from " + indexes);
		return res;
	}

	private double getSumOfDistFromCenters(int i, List<Integer> indexes) {
		double s = 0;
		for (int k : indexes) {
			if (i < k) {
				s += getDistMx()[k][i];
			} else {
				s += getDistMx()[i][k];
			}
		}
		return s;
	}

	private List<Integer> getFarthestNeighbours() {
		Integer[] result = new Integer[2];
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < getDistMx().length; i++) {
			for (int j = 0; j < i; j++) {
				if (getDistMx()[i][j] > max) {
					max = getDistMx()[i][j];
					result[0] = i;
					result[1] = j;
				}
			}
		}
		log.debug(getDistMx()[result[0]][result[1]]
				+ " is the highest value between " + (result[0] + 1) + " and "
				+ (result[1] + 1) + ".");

		List<Integer> res = new ArrayList<Integer>();
		res.add(result[0]);
		res.add(result[1]);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "farthest first k-means";
	}

}
