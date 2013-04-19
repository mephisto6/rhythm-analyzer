/**
 * 
 */
package hu.bme.cs.music;

import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.manage.ClassifyManager;
import hu.bme.cs.music.manage.CompareManager;
import hu.bme.cs.music.model.Manager;
import hu.bme.cs.music.utils.FileUtils;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class MainAnalyser {

	private static int classNum;

	private static Logger log = Logger.getLogger(MainAnalyser.class);

	private static String directory = "data/szeke/nya";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtils.initLogging();
		long startTime = System.currentTimeMillis();
		CompareManager compareManager = new CompareManager(
				FileReader.getTunes(directory));
		// FileReader.getClusterTunes(2));
		compareManager.printResults();
		// compareManager.generateFiles();
		setClassNum((int) Math.sqrt(compareManager.getNumberOfSongs()));
		Manager classifyManager = new ClassifyManager(
				compareManager.getComparers());
		classifyManager.printResults();
		long elapsedTime = System.currentTimeMillis() - startTime;
		log.info("Analysed " + compareManager.getNumberOfSongs() + " songs in "
				+ elapsedTime + " ms");
	}

	public static int getLimit() {
		return 1000000;
	}

	public static int getClassNum() {
		return classNum;
	}

	public static void setClassNum(int classNum) {
		log.debug("Default class num is set to: " + classNum);
		MainAnalyser.classNum = classNum;
	}

}
