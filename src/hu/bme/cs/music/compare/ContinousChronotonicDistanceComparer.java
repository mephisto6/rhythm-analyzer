/**
 * 
 */
package hu.bme.cs.music.compare;

import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.Tune;
import hu.bme.cs.music.model.TuneLine;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Jozsef
 * 
 */
public class ContinousChronotonicDistanceComparer extends Comparer {

	public ContinousChronotonicDistanceComparer(List<Tune> tunes) {
		setTunes(tunes);
	}
	
	@Override
	public String getName() {
		return "Continous chronotonic distances";
	}

	@Override
	public double compare(TuneLine t1, TuneLine t2) {
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

}
