/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.FileWriter;

import java.util.List;
import java.util.Locale;

import com.google.common.primitives.Doubles;

/**
 * @author Jozsef
 * 
 */
public class CompareManager {

	private static double[][] HAMMING_MX;
	private static double[][] EUCLIDEAN_MX;
	private static double[][] INTERVALDIFF_MX;
	private static double[][] SWAP_MX;
	private static double[][] CHRONOTONIC_MX;
	private static double[][] CONTINOUSCHRONOTONIC_MX;

	/**
	 * @return the hAMMING_MX
	 */
	public static double[][] getHAMMING_MX() {
		return HAMMING_MX;
	}

	/**
	 * @return the eUCLIDEAN_MX
	 */
	public static double[][] getEUCLIDEAN_MX() {
		return EUCLIDEAN_MX;
	}

	/**
	 * @return the iNTERVALDIFF_MX
	 */
	public static double[][] getINTERVALDIFF_MX() {
		return INTERVALDIFF_MX;
	}

	/**
	 * @return the sWAP_MX
	 */
	public static double[][] getSWAP_MX() {
		return SWAP_MX;
	}

	/**
	 * @return the cHRONOTONIC_MX
	 */
	public static double[][] getCHRONOTONIC_MX() {
		return CHRONOTONIC_MX;
	}
	
	/**
	 * @return the cONTINOUSCHRONOTONIC_MX
	 */
	public static double[][] getCONTINOUSCHRONOTONIC_MX() {
		return CONTINOUSCHRONOTONIC_MX;
	}

	public static void manage(List<Tune> tunes) {
		int size = tunes.size();
		if (size == 0) {
			return;
		}
		HAMMING_MX = new double[size][size];
		EUCLIDEAN_MX = new double[size][size];
		INTERVALDIFF_MX = new double[size][size];
		SWAP_MX = new double[size][size];
		CHRONOTONIC_MX = new double[size][size];
		CONTINOUSCHRONOTONIC_MX= new double[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				HAMMING_MX[j][i] = Comparator.compareHamming(tunes.get(j)
						.getFirstTuneLine(), tunes.get(i).getFirstTuneLine());
				EUCLIDEAN_MX[j][i] = Comparator.compareEuclidean(tunes.get(j)
						.getFirstTuneLine(), tunes.get(i).getFirstTuneLine());
				INTERVALDIFF_MX[j][i] = Comparator.compareIntervalDifference(
						tunes.get(j).getFirstTuneLine(), tunes.get(i)
								.getFirstTuneLine());
				SWAP_MX[j][i] = Comparator.compareSwap(tunes.get(j)
						.getFirstTuneLine(), tunes.get(i).getFirstTuneLine());
				CHRONOTONIC_MX[j][i] = Comparator.compareChronotonic(
						tunes.get(j).getFirstTuneLine(), tunes.get(i)
								.getFirstTuneLine());
				CONTINOUSCHRONOTONIC_MX[j][i] = Comparator.compareContinousChronotonic(
						tunes.get(j).getFirstTuneLine(), tunes.get(i)
								.getFirstTuneLine());
			}
		}
	//	normalizeMxs();

	 printMxs();

		// generateFiles();
	}

	public static void printMxs() {
		printMX("Weighted Hamming distances", HAMMING_MX);
		printMX("Euclidean interval vector distances", EUCLIDEAN_MX);
		printMX("Interval difference vector distances", INTERVALDIFF_MX);
		printMX("Swap distances", SWAP_MX);
		printMX("Chronotonic distances", CHRONOTONIC_MX);
		printMX("Continous chronotonic distances", CONTINOUSCHRONOTONIC_MX);
	}

	public static void normalizeMxs() {
		normalizeMx(EUCLIDEAN_MX);
		normalizeMx(INTERVALDIFF_MX);
		normalizeMx(SWAP_MX);
		normalizeMx(CHRONOTONIC_MX);
		normalizeMx(CONTINOUSCHRONOTONIC_MX);
	}

	public static void generateFiles() {
		FileWriter.gerenateNexFile("Hamming", HAMMING_MX);
		FileWriter.gerenateNexFile("Euclidean", EUCLIDEAN_MX);
		FileWriter.gerenateNexFile("IntervalDiff", INTERVALDIFF_MX);
		FileWriter.gerenateNexFile("SwapDistance", SWAP_MX);
		FileWriter.gerenateNexFile("ChronotonicChain", CHRONOTONIC_MX);
		FileWriter.gerenateNexFile("ContinousChronotonicChain", CONTINOUSCHRONOTONIC_MX);
	}

	private static void printMX(String title, double[][] mx) {
		System.out.print("     --------------------     " + title
				+ "     ---------------------     \n");
		for (int i = 0; i < mx.length; i++) {
			System.out.print(i + 1 + "\t");
			for (int j = 0; j <= i; j++) {
				System.out.printf(Locale.US, "%.3f%s", mx[i][j], "\t");
			}
			System.out.println();
		}
		System.out.print("\t");
		for (int i = 0; i < mx.length; i++) {
			System.out.print(i + 1 + "\t");
		}
		System.out.println();
	}

	private static void normalizeMx(double[][] mx) {
		int size = mx.length;
		double max = Doubles.max(mx[0]);
		for (int i = 1; i < size; i++) {
			double locMax = Doubles.max(mx[i]);
			if (locMax > max) {
				max = locMax;
			}
		}
		if (max > 1.0) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					mx[j][i] = mx[j][i] / max;
				}
			}
		}
	}

}
