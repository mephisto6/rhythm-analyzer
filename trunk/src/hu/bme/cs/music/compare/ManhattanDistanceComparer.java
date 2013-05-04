/**
 * 
 */
package hu.bme.cs.music.compare;

import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.Tune;
import hu.bme.cs.music.model.TuneLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class ManhattanDistanceComparer extends Comparer {

	public ManhattanDistanceComparer(List<Tune> tunes) {
		setTunes(tunes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.bme.cs.music.model.Comparer#getName()
	 */
	@Override
	public String getName() {
		return "Shifted Manhattan interval distances";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.bme.cs.music.model.Comparer#compare(hu.bme.cs.music.model.TuneLine,
	 * hu.bme.cs.music.model.TuneLine)
	 */
	@Override
	public double compare(TuneLine t1, TuneLine t2) {
		List<Double> diffs = new ArrayList<Double>();

		List<Integer> e1 = t1.getAllInterOnsetIntervalBarElement();
		List<Integer> e2 = t2.getAllInterOnsetIntervalBarElement();

		int s1 = e1.size();
		int s2 = e2.size();

		if (s1 < s2) {
			for (int i = 0; i <= s2 - s1; i++) {
				diffs.add(compareManhattan(e2.subList(i, s1 + i), e1));
			}
		} else if (s1 > s2) {
			for (int i = 0; i <= s1 - s2; i++) {
				diffs.add(compareManhattan(e1.subList(i, s2 + i), e2));
			}
		} else {
			return compareManhattan(e1, e2);
		}

		return Collections.min(diffs);
	}

	private double compareManhattan(List<Integer> e1, List<Integer> e2) {
		int size1 = e1.size();
		int size2 = e2.size();

		double s = 0.0;

		for (int i = 0; (i < size1 && i < size2); i++) {
			s += Math.abs(e1.get(i) - e2.get(i));
		}
		return s;
	}

}
