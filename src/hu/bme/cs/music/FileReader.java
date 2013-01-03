/**
 * 
 */
package hu.bme.cs.music;

import hu.bme.cs.music.model.Bar;
import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.CompareManager;
import hu.bme.cs.music.model.Tune;
import hu.bme.cs.music.model.TuneLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * @author Jozsef
 * 
 */
public class FileReader {

	public static int LIMIT = 3;

	public static final int CLASS_NUM = 8;

	private static Logger log = Logger.getLogger(FileReader.class);

	private static String directory = "data/szeke/vo";

	private static String[] extensions = new String[] { "mnf", "tnf", "gnf",
			"snf" };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		CompareManager.manage(readFile());
	//	Classifier.classify();
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Analysed " + LIMIT + " songs in " + estimatedTime
				+ " ms");
	}

	private static List<Tune> readFile() {
		List<Tune> tunes = new ArrayList<Tune>();
		int i = 1;
		for (File file : FileUtils.listFiles(new File(directory),
				new SuffixFileFilter(extensions), TrueFileFilter.INSTANCE)) {
			Tune tune = new Tune();
			tune.setFile(file);
			List<TuneLine> tuneLines = new ArrayList<TuneLine>();
			try {
				for (String line : Files.readLines(file, Charsets.UTF_8)) {
					tuneLines.add(processTuneLine(line));
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			tune.setTuneLines(tuneLines);
			tune.printDescription();
			tunes.add(tune);
			if (LIMIT < (++i)) {
				break;
			}
		}
		LIMIT = i - 1;
		return tunes;

	}

	private static TuneLine processTuneLine(String line) {

		TuneLine tuneLine = new TuneLine();
		List<Bar> bars = new ArrayList<Bar>();

		String tactSign = "";

		for (String segment : line.split("\\|")) {
			if (segment.trim().isEmpty()) {
				continue;
			}
			Bar bar = new Bar();
			bar.setDescription(segment);
			if (hasTactSign(segment)) {
				tactSign = getTactSignFromFirstItem(segment);
			}
			bar.setTactSign(tactSign);
			bar.setBarFromDescription();
			bars.add(bar);
		}

		tuneLine.setTacts(bars);
		return tuneLine;
	}

	private static String getTactSignFromFirstItem(String segment) {
		String firstItem = segment.split(" ")[0];
		return firstItem.substring(1);
	}

	private static boolean hasTactSign(String segment) {
		return segment.startsWith("T");
	}

}
