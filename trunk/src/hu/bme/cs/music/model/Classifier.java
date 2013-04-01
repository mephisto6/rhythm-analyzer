/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.FileReader;

import java.io.File;

import org.apache.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * @author Jozsef
 * 
 */
public class Classifier {

	private static Logger log = Logger.getLogger(Classifier.class);

	// put elements to the same cluster if distance between them is below this
	// value
	private static double minThreshold =
	// 0.2;
	1000;
	// put elements to separate clusters if distance between them is above this
	// value
	private static double maxThreshold =
	// 0.6;
	0;

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

	public static void classify(double[][] givenMx) {
		int limit = Math.min(FileReader.LIMIT, givenMx.length);
		int[] classesMin = new int[limit];
		int[] classesMax = new int[limit];
		for (int i = 0; i < limit; i++) {
			classesMin[i] = i + 1;
			classesMax[i] = -1;
		}
		classifySpec(givenMx, classesMin, classesMax);
		printClassesOld(classesMin, "Classes by given distance matrix (min): ");
		printClassesOld(classesMax, "Classes by given distance matrix (max): ");
	}

	public static void classify() {
		init();
		classifySpec(CompareManager.getHAMMING_MX(), classesByHammingMin,
				classesByHammingMax);
		// classifySpec(CompareManager.getEUCLIDEAN_MX(), classesByEuclidMin,
		// classesByEuclidMax);
		// classifySpec(CompareManager.getINTERVALDIFF_MX(),
		// classesByIntervalDiffMin, classesByIntervalDiffMax);
		// classifySpec(CompareManager.getSWAP_MX(), classesBySwapMin,
		// classesBySwapMax);
		// classifySpec(CompareManager.getCHRONOTONIC_MX(),
		// classesByChronotonMin,
		// classesByChronotonMax);
		classifySpec(CompareManager.getCONTINOUSCHRONOTONIC_MX(),
				classesByContinousChronotonMin, classesByContinousChronotonMax);

		printClassesOld(classesByHammingMin, "Classes by Hamming min distances: ");
		printClassesOld(classesByHammingMax, "Classes by Hamming max distances: ");
		//
		// printClasses(classesByEuclidMin,
		// "Classes by Euclidean min distances: ");
		// printClasses(classesByEuclidMax,
		// "Classes by Euclidean max distances: ");
		//
		// printClasses(classesByIntervalDiffMin,
		// "Classes by IntervalDiff min distances: ");
		// printClasses(classesByIntervalDiffMax,
		// "Classes by IntervalDiff max distances: ");
		//
		// printClasses(classesBySwapMin, "Classes by Swap min distances: ");
		// printClasses(classesBySwapMax, "Classes by Swap max distances: ");
		//
		// printClasses(classesByChronotonMin,
		// "Classes by Chronotonic min distances: ");
		// printClasses(classesByChronotonMax,
		// "Classes by Chronotonic max distances: ");
		//
		printClassesOld(classesByContinousChronotonMin,
				"Classes by Continous chronotonic min distances: ");
		printClassesOld(classesByContinousChronotonMax,
				"Classes by Continous chronotonic max distances: ");
	}

	private static void classifySpec(double[][] specmx, int[] classesMin,
			int[] classesMax) {
		if (specmx == null || specmx.length == 0) {
			return;
		}
		classifySpecMin(specmx, classesMin);
		System.out.println("-----");
		classifySpecMax(specmx, classesMax);
		System.out.println("-----");
	}

	private static void classifySpecMax(double[][] specmx, int[] classes) {
		mx = copyMx(specmx);
		while (getClassNum(classes) - 1 < FileReader.CLASS_NUM
				&& getMaximal() > Classifier.maxThreshold) {
			int[] indexes = getFurthestNeighbours();
			log.debug("Class of " + (indexes[0]+1) + " is set to " + (indexes[0]+1));
			log.debug("Class of " + (indexes[1]+1) + " is set to " + (indexes[1]+1));
			classes[indexes[0]] = indexes[0];
			classes[indexes[1]] = indexes[1];
		}
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == -1) {
				log.debug("Class of " + (i+1) + " (" + classes[i] + ") is set to " + (findClosestClass(classes, i)+1));
				classes[i] = findClosestClass(classes, i);
			}
		}
	}

	private static double[][] copyMx(double[][] specmx) {
		double[][] newmx = new double[specmx.length][];
		for (int i = 0; i < specmx.length; i++)
			newmx[i] = specmx[i].clone();
		return newmx;
	}

	private static void classifySpecMin(double[][] specmx, int[] classes) {
		mx = copyMx(specmx);
		while (FileReader.CLASS_NUM < getClassNum(classes)
				&& getMinimal() < Classifier.minThreshold) {
			int[] indexes = getClosestNeighbours();
			if (classes[indexes[0]] != classes[indexes[1]]) {
				makeEqual(classes, classes[indexes[0]], classes[indexes[1]]);
			}
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

	// makes equal all the occurrences of oldClass with newClass
	private static void makeEqual(int[] classes, int oldClass, int newClass) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == oldClass) {
				log.debug("Class of " + (i+1) + " (" + classes[i] + ") is set to " + newClass);
				classes[i] = newClass;
			}
		}
	}

	// return the minimal element of the temporal matrix
	private static double getMinimal() {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] < min) {
					min = mx[i][j];
				}
			}
		}
		return min;
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
		log.debug(mx[result[0]][result[1]] + " is the lowest value between "
				+ (result[0]+1) + " and " + (result[1]+1) + ".");

		mx[result[0]][result[1]] = Double.MAX_VALUE;

		return result;
	}

	private static double getMaximal() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < mx.length; i++) {
			for (int j = 0; j < i; j++) {
				if (mx[i][j] > max) {
					max = mx[i][j];
				}
			}
		}
		return max;
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
		log.debug(mx[result[0]][result[1]] + " is the highest value between "
				+ (result[0]+1) + " and " + (result[1]+1) + ".");

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
		Object[] files = FileReader.getFiles().toArray();
		for (Integer i : multimap.keySet()) {
			for (Object o : multimap.get(i)) {
				File f = (File) files[(Integer) o - 1];
				System.out.print(FileReader.getFileMap().inverseBidiMap()
						.get(f)
						+ " ");
			}
			System.out.println();
		}
	}

	private static void printClassesOld(int[] classes, String s) {
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
