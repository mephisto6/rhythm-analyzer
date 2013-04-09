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
public class SingleLinkageClassifier extends LinkageClassifier {

	public SingleLinkageClassifier(Comparer comparer) {
		init(comparer);
		classify(getDistMx());
	}

	@Override
	public String getDescription() {
		return "single linkage";
	}

	@Override
	public double getDistance(Cluster c1, Cluster c2) {
		double min = Double.MAX_VALUE;
		for (int i : c1.getNums()) {
			for (int j : c2.getNums()) {
				if (i < j) {
					if (getDistMx()[j][i] < min) {
						min = getDistMx()[j][i];
					}
				} else {
					if (getDistMx()[i][j] < min) {
						min = getDistMx()[i][j];
					}
				}
			}
		}
		return min;
	}

}
