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
public class ChronotonicDistanceComparer extends Comparer {

	private double[][] distMx;

	public ChronotonicDistanceComparer(List<Tune> tunes) {
		distMx = init(tunes);
	}
	
	@Override
	public String getName() {
		return "Chronotonic distances";
	}

	@Override
	public double[][] getDistanceMx() {
		return distMx;
	}

	@Override
	public double compare(TuneLine t1, TuneLine t2) {
		List<Integer> e1 = t1.getChronotonicChain();
		List<Integer> e2 = t2.getChronotonicChain();

		int size1 = e1.size();
		int size2 = e2.size();

		double s = 0.0;

		for (int i = 0; (i < size1 && i < size2); i++) {
			s += Math.abs(e1.get(i) - e2.get(i));
		}

		return s;
	}

}
