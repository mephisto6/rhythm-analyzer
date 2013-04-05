/**
 * 
 */
package hu.bme.cs.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Jozsef
 * 
 */
public class Comparator {

	public static double compareHamming(TuneLine t1, TuneLine t2) {
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

	public static double compareEuclidean(List<Integer> e1, List<Integer> e2) {
		int size1 = e1.size();
		int size2 = e2.size();

		double s = 0.0;

		for (int i = 0; (i < size1 && i < size2); i++) {
			s += Math.pow(e1.get(i) - e2.get(i), 2);
		}

		return Math.sqrt(s);
	}

	public static double compareIntervalDifference(TuneLine t1, TuneLine t2) {
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

	public static double compareSwap(TuneLine t1, TuneLine t2) {
		List<Integer> e1 = t1.getOnsetVector();
		List<Integer> e2 = t2.getOnsetVector();

		int size1 = e1.size();
		int size2 = e2.size();

		double s = 0.0;

		for (int i = 0; (i < size1 && i < size2); i++) {
			s += Math.abs(e1.get(i) - e2.get(i));
		}

		return s;
	}

	public static double compareChronotonic(TuneLine t1, TuneLine t2) {
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

	public static double compareContinousChronotonic(TuneLine t1, TuneLine t2) {
		List<Integer> chain1 = t1.getChronotonicChain();
		List<Integer> chain2 = t2.getChronotonicChain();
		List<Double> co1 = t1.getChronotonicCoordinates();
		List<Double> co2 = t2.getChronotonicCoordinates();
		int s1 = chain1.size();
		int s2 = chain2.size();
		double res = TuneLine.chronotonicResolution;
		Set<Double> coords = new TreeSet<Double>(co1);
		coords.addAll(co2);

		double s = 0.0;
		double prev = 0.0;
		for (Double d : coords) {
			int i1 = (int) Math.floor((d / res) * s1);
			int i2 = (int) Math.floor((d / res) * s2);
			if (co1.contains(d)) {
				i1--;
			}
			if (co2.contains(d)) {
				i2--;
			}
			double diff = Math.abs(chain1.get(i1) - chain2.get(i2));
			s += diff * (d - prev);
			prev = d;
		}

		double max = Collections.max(chain1) > Collections.max(chain2) ? Collections
				.max(chain1) : Collections.max(chain2);

		return s / (res * max);
	}

	public static double compareEuclideanShift(TuneLine t1, TuneLine t2) {
		List<Double> diffs = new ArrayList<Double>();

		List<Integer> e1 = t1.getAllInterOnsetIntervalBarElement();
		List<Integer> e2 = t2.getAllInterOnsetIntervalBarElement();

		int s1 = e1.size();
		int s2 = e2.size();

		if (s1 < s2) {
			for (int i = 1; i <= s2 - s1; i++) {
				diffs.add(compareEuclidean(e2.subList(i, s1 + i), e1));
			}
		} else if (s1 > s2) {
			for (int i = 1; i <= s1 - s2; i++) {
				diffs.add(compareEuclidean(e1.subList(i, s2 + i), e2));
			}
		} else {
			return compareEuclidean(e1, e2);
		}

		return Collections.min(diffs);
	}
}