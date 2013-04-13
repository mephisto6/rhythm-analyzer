/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.utils.MatrixUtils;

import java.io.File;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * @author Jozsef
 * 
 */
public abstract class Classifier {

	private Comparer comparer;

	private double[][] distMx;

	public Comparer getComparer() {
		return comparer;
	}

	public void setComparerAndDistMx(Comparer comparer) {
		this.comparer = comparer;
		this.distMx = MatrixUtils.copyMx(comparer.getDistanceMx());
	}

	public double[][] getDistMx() {
		return distMx;
	}

	public void setDistMx(double[][] distMx) {
		this.distMx = distMx;
	}

	// private static Logger log = Logger.getLogger(Classifier.class);

	public abstract int[] getClasses();

	public abstract void classify();

	public String getName() {
		String description = "Classes by " + getDescription();
		if (getComparer() != null) {
			return description + " " + getComparer().getName();
		} else {
			return description;
		}
	}

	public abstract String getDescription();

	public int getClassNum(int[] classes) {
		return Sets.newHashSet(Ints.asList(classes)).size();
	}

	public void printClasses() {
		if (FileReader.getFiles() == null) {
			printClassesOld();
			return;
		}
		System.out.println(getName());
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
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

	public void printClassesOld() {
		// System.out.println(Arrays.asList(ArrayUtils.toObject(classes)));
		System.out.println(getName());
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		int j = 0;
		for (Integer i : multimap.keySet()) {
			System.out.println("Class " + (++j) + ": " + multimap.get(i));
		}
	}

}
