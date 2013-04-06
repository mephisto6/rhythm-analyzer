/**
 * 
 */
package hu.bme.cs.music;

import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.manage.ClassifyManager;
import hu.bme.cs.music.manage.CompareManager;
import hu.bme.cs.music.model.Manager;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Jozsef
 * 
 */
@SuppressWarnings("unused")
public class MainAnalyser {

	public static int LIMIT = 100;

	public static final int CLASS_NUM = 8;

	private static Logger log = Logger.getLogger(MainAnalyser.class);

	private static String directory = "data/szeke/vo";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure(new File(".").getAbsolutePath()
				+ "/resources/log4j.xml");
		long startTime = System.currentTimeMillis();
		CompareManager compareManager = new CompareManager(
				FileReader.getClusterTunes(2));
		compareManager.printResults();
		Manager classifyManager = new ClassifyManager(
				compareManager.getComparers());
		classifyManager.printResults();
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Analysed " + LIMIT + " songs in " + elapsedTime
				+ " ms");
	}

}
