/**
 * 
 */
package hu.bme.cs.music.gui;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.manage.ClassifyManager;
import hu.bme.cs.music.manage.CompareManager;
import hu.bme.cs.music.model.Manager;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class LogicControl {

	private static Logger log = Logger.getLogger(LogicControl.class);

	private int k;

	private int comparerId;

	private int classifierId;

	private double minPts;

	private double eps;

	public void setComparerId(int comparerId) {
		this.comparerId = comparerId;
	}

	public void setClassifierId(int classifierId) {
		this.classifierId = classifierId;
	}

	public void setK(int k) {
		this.k = k;
	}

	public void setMinPts(double minPts) {
		this.minPts = minPts;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}

	public void run() {
		long startTime = System.currentTimeMillis();
		CompareManager compareManager = new CompareManager(
				FileReader.getClusterTunes(2));
		MainAnalyser.setClassNum((int) Math.sqrt(compareManager.getNumberOfSongs()));
		Manager classifyManager = new ClassifyManager(
				compareManager.getComparerForId(comparerId), classifierId);

		classifyManager.printResults();
		long elapsedTime = System.currentTimeMillis() - startTime;
		log.info("Analysed " + compareManager.getNumberOfSongs() + " songs in "
				+ elapsedTime + " ms");
	}

}
