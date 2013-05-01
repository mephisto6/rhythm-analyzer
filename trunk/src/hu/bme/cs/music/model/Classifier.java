/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.gui.LogicControl;
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

	public int getClassNum() {
		//MainAnalyser.getClassNum();
		return LogicControl.k;
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
			return description + " using " + getComparer().getName();
		} else {
			return description;
		}
	}

	public abstract String getDescription();

	public int getClassNum(int[] classes) {
		return Sets.newHashSet(Ints.asList(classes)).size();
	}

	public String printClasses() {
		if (FileReader.getFiles() == null) {
			return printClassesOld();
		}
		StringBuffer sb = new StringBuffer(getName() + System.getProperty("line.separator"));
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		Object[] files = FileReader.getFiles().toArray();
		for (Integer i : multimap.keySet()) {
			for (Object o : multimap.get(i)) {
				File f = (File) files[(Integer) o - 1];
				sb.append(FileReader.getFileMap().inverseBidiMap()
						.get(f)
						+ " ");
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	public String printClassesOld() {
		// System.out.println(Arrays.asList(ArrayUtils.toObject(classes)));
		StringBuffer sb = new StringBuffer(getName() + System.getProperty("line.separator"));
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		int j = 0;
		for (Integer i : multimap.keySet()) {
			sb.append("Class " + (++j) + ": " + multimap.get(i) + System.getProperty("line.separator"));
		}
		return sb.toString();
	}

}
