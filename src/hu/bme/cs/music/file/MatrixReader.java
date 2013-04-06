/**
 * 
 */
package hu.bme.cs.music.file;

import hu.bme.cs.music.manage.ClassifyManager;
import hu.bme.cs.music.model.Manager;
import hu.bme.cs.music.utils.MatrixUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Jozsef
 * 
 */
public class MatrixReader {

	public static final int clusterNum = 2;

	public static void main(String[] args) {
		DOMConfigurator.configure(new File(".").getAbsolutePath()
				+ "/resources/log4j.xml");

		double[][] mx = readMx("data/vekttav-" + clusterNum + ".dat");
		FileReader.getClusterTunes(clusterNum);
		MatrixUtils.normalizeMx(mx);
		// FileWriter.gerenateNexFile("Given2", mx);
		Manager classifyManager = new ClassifyManager(mx);
		classifyManager.printResults();
	}

	private static Logger log = Logger.getLogger(MatrixReader.class);

	public static double[][] readMx(String fileName) {
		double[][] mx = null;
		BufferedReader br;
		try {
			br = new BufferedReader(new java.io.FileReader(fileName));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] nums = line.split("\u0000");
				if (i == 0) {
					mx = new double[nums.length - 1][nums.length - 1];
				}
				double[] mxLine = new double[nums.length - 1];
				for (int j = 0; j < nums.length - 1; j++) {
					if (!nums[j].trim().isEmpty()) {
						mxLine[j] = Double.valueOf(nums[j].trim());
					}
				}
				mx[i++] = mxLine;
			}
			br.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return mx;
	}

}
