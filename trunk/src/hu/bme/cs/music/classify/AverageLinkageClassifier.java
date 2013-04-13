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
public class AverageLinkageClassifier extends LinkageClassifier {

	public AverageLinkageClassifier(Comparer comparer) {
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
		double avg = 0;
		int n = 0;
		for (int i : c1.getNums()) {
			for (int j : c2.getNums()) {
				if (i < j) {
					avg += getDistMx()[j][i];
				} else {
					avg += getDistMx()[i][j];
				}
				n++;
			}
		}
		return avg / n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Classifier#getDescription()
	 */
	@Override
	public String getDescription() {
		return "average linkage";
	}

}
