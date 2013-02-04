/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.FileReader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * @author Jozsef
 * 
 */
public class Classifier {

	private static int[] classesByHammingMin;
	private static int[] classesByEuclidMin;
	private static int[] classesByIntervalDiffMin;
	private static int[] classesBySwapMin;
	private static int[] classesByChronotonMin;
	private static int[] classesByContinousChronotonMin;

	private static int[] classesByHammingMax;
	private static int[] classesByEuclidMax;
	private static int[] classesByIntervalDiffMax;
	private static int[] classesBySwapMax;
	private static int[] classesByChronotonMax;
	private static int[] classesByContinousChronotonMax;

	// matrix to store temporal data
	private static double[][] mx;

	public static void classify() {
		init();
		classifySpec(CompareManager.getHAMMING_MX(), classesByHammingMin,
				classesByHammingMax);
		classifySpec(CompareManager.getEUCLIDEAN_MX(), classesByEuclidMin,
				classesByEuclidMax);
		classifySpec(CompareManager.getINTERVALDIFF_MX(),
				classesByIntervalDiffMin, classesByIntervalDiffMax);
		classifySpec(CompareManager.getSWAP_MX(), classesBySwapMin,
				classesBySwapMax);
		classifySpec(CompareManager.getCHRONOTONIC_MX(), classesByChronotonMin,
				classesByChronotonMax);
		classifySpec(CompareManager.getCHRONOTONIC_MX(),
				classesByContinousChronotonMin, classesByContinousChronotonMax);

		printClasses(classesByHammingMin, "Classes by Hamming min distances: ");
		printClasses(classesByHammingMax, "Classes by Hamming max distances: ");

		printClasses(classesByEuclidMin, "Classes by Euclidean min distances: ");
		printClasses(classesByEuclidMax, "Classes by Euclidean max distances: ");

		printClasses(classesByIntervalDiffMin,
				"Classes by IntervalDiff min distances: ");
		printClasses(classesByIntervalDiffMax,
				"Classes by IntervalDiff min distances: ");

		printClasses(classesBySwapMin, "Classes by Swap min distances: ");
		printClasses(classesBySwapMax, "Classes by Swap max distances: ");

		printClasses(classesByChronotonMin,
				"Classes by Chronotonic min distances: ");
		printClasses(classesByChronotonMax,
				"Classes by Chronotonic max distances: ");

		printClasses(classesByContinousChronotonMin,
				"Classes by Continous chronotonic min distances: ");
		printClasses(classesByContinousChronotonMax,
				"Classes by Continous chronotonic max distances: ");
	}

	private static void classifySpec(double[][] specmx, int[] classesMin,
			int[] classesMax) {
		if (specmx == null || specmx.length == 0) {
			return;
		}
		classifySpecMin(specmx, classesMin);
		classifySpecMax(specmx, classesMax);
	}

	private static void classifySpecMax(double[][] specmx, int[] classes) {
		mx = specmx;
		while (getClassNum(classes) - 1 < FileReader.CLASS_NUM) {
			int[] indexes = getFurthestNeighbours();
			classes[indexes[0]] = indexes[0];
			classes[indexes[1]] = indexes[1];
		}
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == -1) {
				classes[i] = findClosestClass(classes, i);
			}
		}
	}

	private static void classifySpecMin(double[][] specmx, int[] classes) {
		mx = specmx;
		while (FileReader.CLASS_NUM < getClassNum(classes)) {
			iterate(classes);
		}

	}

	private static int findClosestClass(int[] classes, int i) {
		double min = Double.MAX_VALUE;
		int index = 0;
		for (int j = 0; j < classes.length; j++) {
			if (classes[j] != -1) {
				if (getDistance(classes[j], i) < min) {
					min = getDistance(classes[j], i);
					index = j;
				}
			}
		}
		// the class there may be already calculated
		return classes[index];
	}

	private static void init() {
		classesByHammingMin = new int[FileReader.LIMIT];
		classesByEuclidMin = new int[FileReader.LIMIT];
		classesByIntervalDiffMin = new int[FileReader.LIMIT];
		classesBySwapMin = new int[FileReader.LIMIT];
		classesByChronotonMin = new int[FileReader.LIMIT];
		classesByContinousChronotonMin = new int[FileReader.LIMIT];

		classesByHammingMax = new int[FileReader.LIMIT];
		classesByEuclidMax = new int[FileReader.LIMIT];
		classesByIntervalDiffMax = new int[FileReader.LIMIT];
		classesBySwapMax = new int[FileReader.LIMIT];
		classesByChronotonMax = new int[FileReader.LIMIT];
		classesByContinousChronotonMax = new int[FileReader.LIMIT];

		for (int i = 0; i < FileReader.LIMIT; i++) {
			classesByHammingMin[i] = i + 1;
			classesByEuclidMin[i] = i + 1;
			classesByIntervalDiffMin[i] = i + 1;
			classesBySwapMin[i] = i + 1;
			classesByChronotonMin[i] = i + 1;
			classesByContinousChronotonMin[i] = i + 1;

			classesByHammingMax[i] = -1;
			classesByEuclidMax[i] = -1;
			classesByIntervalDiffMax[i] = -1;
			classesBySwapMax[i] = -1;
			classesByChronotonMax[i] = -1;
			classesByContinousChronotonMax[i] = -1;
		}
	}

	// returns the number of actual classes
	private static int getClassNum(int[] classes) {
		return Sets.newHashSet(Ints.asList(classes)).size();
	}

	private static void iterate(int[] classes) {
		int[] indexes = getClosestNeighbours();
		if (classes[indexes[0]] == classes[indexes[1]]) {
			// already in the same class
			return;
		}
		makeEqual(classes, classes[indexes[0]], classes[indexes[1]]);
	}

	// makes equal all the occurrences of oldClass with newClass
	private static void makeEqual(int[] classes, int oldClass, int newClass) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == oldClass) {
				classes[i] = newClass;
			}
		}
	}

	// returns the indexes of the minimal element in the temporal matrix
	// and sets it to a very big number
	private static int[] getClosestNeighbours() {
		int[] result = new int[2];
		double min = Double.MAX_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] < min) {
					min = mx[i][j];
					result[0] = i;
					result[1] = j;
				}
			}
		}
		mx[result[0]][result[1]] = Double.MAX_VALUE;

		return result;
	}

	// returns the indexes of the maximal element in the temporal matrix
	// and sets it to a very low value
	private static int[] getFurthestNeighbours() {
		int[] result = new int[2];
		double max = Double.MIN_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] > max) {
					max = mx[i][j];
					result[0] = i;
					result[1] = j;
				}
			}
		}
		mx[result[0]][result[1]] = Double.MIN_VALUE;
		return result;
	}

	private static double getDistance(Integer integer, int i) {
		if (i > integer) {
			return mx[i][integer];
		} else {
			return mx[integer][i];
		}
	}

	private static void printClasses(int[] classes, String s) {
		// System.out.println(Arrays.asList(ArrayUtils.toObject(classes)));
		System.out.println(s);
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < classes.length; i++) {
			multimap.put(classes[i], i + 1);
		}
		int j = 0;
		for (Integer i : multimap.keySet()) {
			System.out.println("Class " + (++j) + ": " + multimap.get(i));
		}
	}

}
