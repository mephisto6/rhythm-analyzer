/**
 * 
 */
package hu.bme.cs.music.classify;

import hu.bme.cs.music.model.Cluster;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.LinkageClassifier;

/**
 * @author Jozsef
 * 
 */
public class CompleteLinkageClassifier extends LinkageClassifier {

	public CompleteLinkageClassifier(Comparer comparer) {
		init(comparer);
		classify();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.bme.cs.music.model.LinkageClassifier#getDistance(hu.bme.cs.music.model
	 * .Cluster, hu.bme.cs.music.model.Cluster)
	 */
	@Override
	public double getDistance(Cluster c1, Cluster c2) {
		double max = -Double.MAX_VALUE;
		for (int i : c1.getNums()) {
			for (int j : c2.getNums()) {
				if (i < j) {
					if (getDistMx()[j][i] > max) {
						max = getDistMx()[j][i];
					}
				} else {
					if (getDistMx()[i][j] > max) {
						max = getDistMx()[i][j];
					}
				}
			}
		}
		return max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "complete linkage";
	}

}
