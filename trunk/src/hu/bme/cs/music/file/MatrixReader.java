/**
 * 
 */
package hu.bme.cs.music.file;

import hu.bme.cs.music.manage.ClassifyManager;
import hu.bme.cs.music.model.Manager;
import hu.bme.cs.music.utils.FileUtils;
import hu.bme.cs.music.utils.MatrixUtils;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class MatrixReader {

	public static final int clusterNum = 2;

	public static void main(String[] args) {
		FileUtils.initLogging();
		double[][] mx = readMx("data/adott_vekt_tav/vekttav-" + clusterNum
				+ ".dat");
		if (clusterNum <= 2) {
			FileReader.getClusterTunes(clusterNum);
		}

		MatrixUtils.normalizeMx(mx);
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
