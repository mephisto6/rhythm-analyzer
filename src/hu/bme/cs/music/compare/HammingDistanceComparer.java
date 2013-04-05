/**
 * 
 */
package hu.bme.cs.music.compare;

import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.Tune;
import hu.bme.cs.music.model.TuneLine;

import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class HammingDistanceComparer extends Comparer {
	
	private double[][] distMx;

	public HammingDistanceComparer(List<Tune> tunes) {
		distMx = init(tunes);
	}
	
	@Override
	public String getName() {
		return "Weighted Hamming distances";
	}

	@Override
	public double[][] getDistanceMx() {
		return distMx;
	}

	@Override
	public double compare(TuneLine t1, TuneLine t2) {
		List<Integer> e1 = t1.getAllBinaryBarElement();
		List<Integer> e2 = t2.getAllBinaryBarElement();

		int size1 = e1.size();
		int size2 = e2.size();

		double w = 0.5;
		double syncOnsets = 0.0;

		for (int i = 0; (i < size1 && i < size2); i++) {
			if (e1.get(i) == e2.get(i)) {
				syncOnsets++;
			}
		}

		double diff = w * (1 - (syncOnsets / size1)) + (1 - w)
				* (1 - (syncOnsets / size2));

		return diff;
	}
}
