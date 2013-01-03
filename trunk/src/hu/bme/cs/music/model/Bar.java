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
public class Bar {

	public static final double res = 16.0;

	private List<Integer> binaryValue = new ArrayList<Integer>();

	private List<Integer> interOnsetIntervals = new ArrayList<Integer>();

	private String tactSign;

	private String description;

	private int beat;

	public void printTacts() {
		// System.out.print("(" + getTactSign() + ") ");
		for (int i : binaryValue) {
			System.out.print(i + " ");
		}
		System.out.print("| ");
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the tactSign
	 */
	public String getTactSign() {
		return tactSign;
	}

	/**
	 * @param tactSign
	 *            the tactSign to set
	 */
	public void setTactSign(String tactSign) {
		this.tactSign = tactSign;
	}

	/**
	 * @return the beat
	 */
	public int getBeat() {
		return beat;
	}

	/**
	 * @param beat
	 *            the beat to set
	 */
	public void setBeat(int beat) {
		this.beat = beat;
	}

	public List<Integer> getBinaryValues() {
		return binaryValue;
	}

	public List<Integer> getInterOnsetIntervals() {
		return interOnsetIntervals;
	}

	public void setBarFromDescription() {
		String[] items = getDescription().split(" ");
		for (String item : items) {
			item = removeAddition(item);
			if (item.trim().isEmpty()) {
				continue;
			}
			if ((item.charAt(0) == 'R')) {
				// special case: R4, R16
				item = item.substring(1) + "R";
			}
			if (!Character.isDigit(item.charAt(0))) {
				continue;
			}
			if (!item.endsWith("R")) {
				binaryValue.add(1);
			} else {
				binaryValue.add(0);
			}
			for (int zeros = 1; zeros < getBarFromCharArray(item.toCharArray()); zeros++) {
				binaryValue.add(0);
			}
		}
		if (!binaryValue.isEmpty()) {
			binaryValue.set(0, 2);
		}
		calculateInterOnsetIntervals();
	}

	// tizenhatod értékét adja meg a hangnak
	private double getBarFromCharArray(char[] chars) {
		double subValue = 0.0;
		if (Character.isDigit(chars[0])) {
			if (Character.isDigit(chars[1])) {
				subValue = 1.0 / Integer.valueOf(String.valueOf(new char[] {
						chars[0], chars[1] }));
			} else {
				subValue = 1.0 / Character.getNumericValue(chars[0]);
			}
		}
		if ('.' == chars[chars.length - 1]) {
			subValue *= 1.5;
		}
		return subValue * res;
	}

	private String removeAddition(String s) {
		s = removeBrackets(s);
		s = removeHashMark(s);
		s = removeB(s);
		return s;
	}

	private String removeBrackets(String s) {
		s = removeHashMark(s);
		return s.substring((s.startsWith("[") ? 1 : 0),
				s.endsWith("]") ? s.length() - 1 : s.length());
	}

	private String removeHashMark(String s) {
		return s.substring(s.startsWith("#") ? 1 : 0);
	}

	private String removeB(String s) {
		return s.substring(s.startsWith("b") ? 1 : 0);
	}

	private void calculateInterOnsetIntervals() {
		int i = 1;
		for (int k = 1; k < binaryValue.size(); k++) {
			if (binaryValue.get(k) == 0) {
				i++;
			} else {
				interOnsetIntervals.add(i);
				i = 1;
			}
		}
		interOnsetIntervals.add(i);
	}

}
