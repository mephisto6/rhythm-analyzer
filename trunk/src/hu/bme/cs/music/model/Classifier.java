/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.gui.LogicControl;
import hu.bme.cs.music.utils.MatrixUtils;
import hu.bme.cs.music.utils.MetricsUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

	List<Cluster> clusters = new ArrayList<Cluster>();

	private double[][] distMx;

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public Comparer getComparer() {
		return comparer;
	}

	public int getClassNum() {
		if (MainAnalyser.getClassNum() != 0) {
			return MainAnalyser.getClassNum();
		}
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

	public String getMetrics() {
		StringBuffer sb = new StringBuffer();
		DecimalFormat df = new DecimalFormat("#.###");
		sb.append("SSE:\n");
		sb.append(df.format(MetricsUtils.getSumOfSquaredError(getClusters()))
				+ "\n");
		sb.append("Avg avg dist:\n");
		sb.append(df.format(MetricsUtils.getAvgAvgDistance(getClusters()))
				+ "\n");
		sb.append("Avg max dist:\n");
		sb.append(df.format(MetricsUtils.getAvgMaxDistance(getClusters()))
				+ "\n");
		sb.append("Max max dist:\n");
		sb.append(df.format(MetricsUtils.getMaxMaxDistance(getClusters()))
				+ "\n");
		sb.append("Sum of dists:\n");
		sb.append(df.format(MetricsUtils.getSumOfDistances(getClusters()))
				+ "\n");
		sb.append("Sum of dists from centre:\n");
		sb.append(df.format(MetricsUtils.getSumOfDistsFromCentre(getClusters()))
				+ "\n");
		return sb.toString();
	}

	public int getClassNum(int[] classes) {
		return Sets.newHashSet(Ints.asList(classes)).size();
	}

	public String printClasses() {
		//if (FileReader.getFiles() == null) {
		//	return printClassesOld();
		//}
		StringBuffer sb = new StringBuffer(getName()
				+ System.getProperty("line.separator"));
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		Object[] files = FileReader.getFiles().toArray();
		for (Integer i : multimap.keySet()) {
			for (Object o : multimap.get(i)) {
				File f = (File) files[(Integer) o - 1];
				sb.append(FileReader.getFileMap().inverseBidiMap().get(f) + " ");
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	public String printClassesOld() {
		// System.out.println(Arrays.asList(ArrayUtils.toObject(classes)));
		StringBuffer sb = new StringBuffer(getName()
				+ System.getProperty("line.separator"));
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		int j = 0;
		for (Integer i : multimap.keySet()) {
			sb.append("Class " + (++j) + ": " + multimap.get(i)
					+ System.getProperty("line.separator"));
		}
		return sb.toString();
	}

}
