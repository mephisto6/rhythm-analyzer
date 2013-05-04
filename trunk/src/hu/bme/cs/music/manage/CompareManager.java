/**
 * 
 */
package hu.bme.cs.music.manage;

import hu.bme.cs.music.compare.ChronotonicDistanceComparer;
import hu.bme.cs.music.compare.ContinousChronotonicDistanceComparer;
import hu.bme.cs.music.compare.EuclideanDistanceComparer;
import hu.bme.cs.music.compare.HammingDistanceComparer;
import hu.bme.cs.music.compare.IntervalDiffDistanceComparer;
import hu.bme.cs.music.compare.ManhattanDistanceComparer;
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
		comparers.add(new ManhattanDistanceComparer(tunes));
	}

	public CompareManager(List<Tune> tunes, int id) {
		comparers = new ArrayList<Comparer>();
		switch (id) {
		case 0:
			comparers.add(new HammingDistanceComparer(tunes));
			break;
		case 1:
			comparers.add(new ManhattanDistanceComparer(tunes));
			break;
		case 2:
			comparers.add(new EuclideanDistanceComparer(tunes));
			break;
		case 3:
			comparers.add(new IntervalDiffDistanceComparer(tunes));
			break;
		case 4:
			comparers.add(new SwapDistanceComparer(tunes));
			break;
		case 5:
			comparers.add(new ChronotonicDistanceComparer(tunes));
			break;
		case 6:
			comparers.add(new ContinousChronotonicDistanceComparer(tunes));
			break;
		}
	}

	public Comparer getComparer() {
		return comparers.get(0);
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

	@Override
	public String getResults() {
		printResults();
		return "result printed to console";
	}
}
