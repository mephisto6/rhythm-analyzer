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

	private List<Bar> bars;

	public void printBars() {
		for (Bar bar : bars) {
			bar.printTacts();
		}
		// printIntervalDifferenceElements();
		// printOnsetVector();
		// printChronoticChain();
		System.out.println();
	}

	public void printIntervalDifferenceElements() {
		System.out.print("<--- ");
		for (Double d : getAllIntervalDifferenceElement()) {
			System.out.printf("%.2f", d);
			System.out.print(" ");
		}
	}

	public void printOnsetVector() {
		System.out.print("<--- ");
		for (Integer d : getOnsetVector()) {
			System.out.print(d + " ");
		}
	}

	public void printChronoticChain() {
		System.out.print("<--- ");
		for (Integer d : getChronotonicChain()) {
			System.out.print(d + " ");
		}
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
		List<Integer> chronoticChain = new ArrayList<Integer>();
		for (Integer unit : getAllInterOnsetIntervalBarElement()) {
			for (int i = 0; i < unit; i++) {
				chronoticChain.add(unit);
			}
		}
		return chronoticChain;
	}

}
