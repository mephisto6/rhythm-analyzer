/**
 * 
 */
package hu.bme.cs.music.manage;

import hu.bme.cs.music.compare.ChronotonicDistanceComparer;
import hu.bme.cs.music.compare.ContinousChronotonicDistanceComparer;
import hu.bme.cs.music.compare.EuclideanDistanceComparer;
import hu.bme.cs.music.compare.HammingDistanceComparer;
import hu.bme.cs.music.compare.IntervalDiffDistanceComparer;
import hu.bme.cs.music.compare.SwapDistanceComparer;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.Manager;
import hu.bme.cs.music.model.Tune;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class CompareManager extends Manager {

	private List<Comparer> comparers;

	public CompareManager(List<Tune> tunes) {
		comparers = new ArrayList<Comparer>();

		comparers.add(new HammingDistanceComparer(tunes));
//		comparers.add(new EuclideanDistanceComparer(tunes));
//		comparers.add(new IntervalDiffDistanceComparer(tunes));
//		comparers.add(new SwapDistanceComparer(tunes));
//		comparers.add(new ChronotonicDistanceComparer(tunes));
//		comparers.add(new ContinousChronotonicDistanceComparer(tunes));
	}

	public List<Comparer> getComparers() {
		return comparers;
	}

	@Override
	public void printResults() {
		for (Comparer c : getComparers()) {
			c.printMX();
		}
	}

	public void generateFiles() {
		for (Comparer c : getComparers()) {
			c.generateFile();
		}
	}

	@Override
	public int getNumberOfSongs() {
		int res = 0;
		if (comparers != null && !comparers.isEmpty()) {
			return comparers.get(0).getDistanceMx().length;
		}
		return res;
	}
}
