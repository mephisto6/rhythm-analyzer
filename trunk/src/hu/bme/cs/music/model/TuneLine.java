/**
 * 
 */
package hu.bme.cs.music.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class TuneLine {
	
	public static final double chronotonicResolution = 100.0;

	private List<Bar> bars;

	public void printBars() {
		for (Bar bar : bars) {
			bar.printTacts();
		}
		printInterOnsetIntervals();
		printIntervalDifferenceElements();
		printOnsetVector();
		printChronoticChain();
		System.out.println();
	}

	private void printInterOnsetIntervals() {
		System.out.print("\nInter onset intervals: ");
		for (Bar bar : bars) {
			System.out.print(bar.getInterOnsetIntervals() + " ");
		}
	}

	public void printIntervalDifferenceElements() {
		System.out.print("\nInterval Differences: [");
		for (Double d : getAllIntervalDifferenceElement()) {
			System.out.printf("%.2f ", d);
		}
		System.out.print("]");
	}

	public void printOnsetVector() {
		System.out.print("\nOnsets: " + getOnsetVector());
	}

	public void printChronoticChain() {
		System.out.print("\nChronotonic chain: " + getChronotonicChain());
		System.out.print("\nChronotonic coordinates: [");
		for (Double d : getChronotonicCoordinates()) {
			System.out.printf("%.2f ", d);
		}
		System.out.print("] Size: " + getChronotonicCoordinates().size());
	}

	/**
	 * @return the tacts
	 */
	public List<Bar> getBars() {
		return bars;
	}

	/**
	 * @param bars
	 *            the tacts to set
	 */
	public void setTacts(List<Bar> bars) {
		this.bars = bars;
	}

	public List<Integer> getAllBinaryBarElement() {
		List<Integer> tacts = new ArrayList<Integer>();
		for (Bar bar : getBars()) {
			tacts.addAll(bar.getBinaryValues());
		}
		return tacts;
	}

	public List<Integer> getAllInterOnsetIntervalBarElement() {
		List<Integer> tacts = new ArrayList<Integer>();
		for (Bar bar : getBars()) {
			tacts.addAll(bar.getInterOnsetIntervals());
		}
		return tacts;
	}

	public List<Double> getAllIntervalDifferenceElement() {
		List<Double> tacts = new ArrayList<Double>();
		int i = 0;
		List<Integer> interOnsetIntervals = getAllInterOnsetIntervalBarElement();
		for (; i < interOnsetIntervals.size() - 1; i++) {
			tacts.add((interOnsetIntervals.get(i + 1)).doubleValue()
					/ interOnsetIntervals.get(i).doubleValue());
		}
		tacts.add(interOnsetIntervals.get(0).doubleValue()
				/ interOnsetIntervals.get(i).doubleValue());
		return tacts;
	}

	public List<Integer> getOnsetVector() {
		List<Integer> onsetVector = new ArrayList<Integer>();
		int i = 0;
		for (Integer bin : getAllBinaryBarElement()) {
			i++;
			if (bin != 0) {
				onsetVector.add(i);
			}
		}
		return onsetVector;
	}

	public List<Integer> getChronotonicChain() {
		List<Integer> chronotonicChain = new ArrayList<Integer>();
		for (Integer unit : getAllInterOnsetIntervalBarElement()) {
			for (int i = 0; i < unit; i++) {
				chronotonicChain.add(unit);
			}
		}
		return chronotonicChain;
	}
	
	public List<Double> getChronotonicCoordinates() {
		int chainSize = getChronotonicChain().size();
		List<Double> chronotonicCoordinates = new ArrayList<Double>();
			for (int i = 1; i <= chainSize; i++) {
				chronotonicCoordinates.add(i*chronotonicResolution/chainSize);
			}

		return chronotonicCoordinates;
	}

}
