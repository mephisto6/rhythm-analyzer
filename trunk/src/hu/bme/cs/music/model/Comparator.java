/**
 * 
 */
package hu.bme.cs.music.model;

import java.util.List;

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

	public static double compareEuclidean(TuneLine t1, TuneLine t2) {
		List<Integer> e1 = t1.getAllInterOnsetIntervalBarElement();
		List<Integer> e2 = t2.getAllInterOnsetIntervalBarElement();

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

}
