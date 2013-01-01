/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.FileReader;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * @author Jozsef
 * 
 */
public class Classifier {

	private static int[] classesByHamming;
	private static int[] classesByEuclid;
	private static int[] classesByIntervalDiff;
	private static int[] classesBySwap;
	private static int[] classesByChronoton;

	private static double[][] mx;

	public static void classify() {
		init();
		classifySpec(CompareManager.getHAMMING_MX(), classesByHamming);
		classifySpec(CompareManager.getEUCLIDEAN_MX(), classesByEuclid);
		classifySpec(CompareManager.getINTERVALDIFF_MX(), classesByIntervalDiff);
		classifySpec(CompareManager.getSWAP_MX(), classesBySwap);
		classifySpec(CompareManager.getCHRONOTONIC_MX(), classesByChronoton);

//		printResult(classesByHamming, "Classes by Hamming distances: ");
//		printResult(classesByEuclid, "Classes by Euclidean distances: ");
//		printResult(classesByIntervalDiff,
//				"Classes by IntervalDiff distances: ");
//		printResult(classesBySwap, "Classes by Swap distances: ");
//		printResult(classesByChronoton, "Classes by Chronotonic distances: ");

		printClasses(classesByHamming, "Classes by Hamming distances: ");
		printClasses(classesByEuclid, "Classes by Euclidean distances: ");
		printClasses(classesByIntervalDiff,
				"Classes by IntervalDiff distances: ");
		printClasses(classesBySwap, "Classes by Swap distances: ");
		printClasses(classesByChronoton, "Classes by Chronotonic distances: ");
	}

	private static void classifySpec(double[][] specmx, int[] classes) {
		mx = specmx;
		if (mx == null || mx.length == 0) {
			return;
		}
		while (FileReader.CLASS_NUM < getClassNum(classes)) {
			iterate(classes);
		}
	}

	private static void init() {
		classesByHamming = new int[FileReader.LIMIT];
		classesByEuclid = new int[FileReader.LIMIT];
		classesByIntervalDiff = new int[FileReader.LIMIT];
		classesBySwap = new int[FileReader.LIMIT];
		classesByChronoton = new int[FileReader.LIMIT];
		for (int i = 0; i < FileReader.LIMIT; i++) {
			classesByHamming[i] = i + 1;
			classesByEuclid[i] = i + 1;
			classesByIntervalDiff[i] = i + 1;
			classesBySwap[i] = i + 1;
			classesByChronoton[i] = i + 1;
		}
	}

	private static void printResult(int[] classes, String s) {
		System.out.print(s);
		System.out.println(Arrays.asList(ArrayUtils.toObject(classes)));
	}

	private static int getClassNum(int[] classes) {
		return Sets.newHashSet(Ints.asList(classes)).size();
	}

	private static void iterate(int[] classes) {
		int[] indexes = getClosestNeighbours();
		if (classes[indexes[0]] == classes[indexes[1]]) {
			return;
		}
		makeEqual(classes, classes[indexes[0]], classes[indexes[1]]);
	}

	private static void makeEqual(int[] classes, int oldClass, int newClass) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == oldClass) {
				classes[i] = newClass;
			}
		}
	}

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

	private static void printClasses(int[] classes, String s) {
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
