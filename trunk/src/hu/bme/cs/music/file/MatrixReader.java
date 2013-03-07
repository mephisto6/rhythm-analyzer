/**
 * 
 */
package hu.bme.cs.music.file;

import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.CompareManager;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class MatrixReader {

	public static void main(String[] args) {
		double[][] mx = readMx("data/vekttav-2.dat");
		CompareManager.normalizeMx(mx);
		// FileWriter.gerenateNexFile("Given2", mx);
		Classifier.classify(mx);
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
