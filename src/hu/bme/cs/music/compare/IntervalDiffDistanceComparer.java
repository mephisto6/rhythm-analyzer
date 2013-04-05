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
public class IntervalDiffDistanceComparer extends Comparer {
	
	private double[][] distMx;

	public IntervalDiffDistanceComparer(List<Tune> tunes) {
		distMx = init(tunes);
	}
	
	@Override
	public String getName() {
		return "Interval difference distances";
	}

	@Override
	public double[][] getDistanceMx() {
		return distMx;
	}

	@Override
	public double compare(TuneLine t1, TuneLine t2) {
		List<Double> e1 = t1.getAllIntervalDifferenceElement();
		List<Double> e2 = t2.getAllIntervalDifferenceElement();

		int size1 = e1.size();
		int size2 = e2.size();

		double s = 0.0;
		int i = 0;
		for (; (i < size1 && i < size2); i++) {
			s += Math.max(e1.get(i), e2.get(i))
					/ Math.min(e1.get(i), e2.get(i));
		}
		s -= i;

		return s;
	}

}
