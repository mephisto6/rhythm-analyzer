/**
 * 
 */
package hu.bme.cs.music.file;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.model.Bar;
import hu.bme.cs.music.model.Tune;
import hu.bme.cs.music.model.TuneLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
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

	private static Logger log = Logger.getLogger(FileReader.class);

	private static final boolean printTunes = true;

	private static String[] extensions = new String[] { "mnf", "tnf", "gnf",
			"snf" };

	private static BidiMap fileMap;
	
	private static SortedMap<Integer,Integer> sortedMap;

	private static List<File> files;
	
	public static SortedMap<Integer,Integer> getSortedMap() {
		return sortedMap;
	}

	public static BidiMap getFileMap() {
		return fileMap;
	}

	public static List<File> getFiles() {
		return files;
	}

	public static List<Tune> getTunes(String dir) {
		return getTunes(getFilesFromDir(dir));
	}

	public static List<Tune> getClusterTunes(int i) {
		return getTunes(getClusterFiles(i));
	}

	private static Collection<File> getClusterFiles(int n) {
		BufferedReader br;
		try {
			br = new BufferedReader(new java.io.FileReader(
					"data/felho-gyim-szek-e6.dat"));
			String line;
			int i = 1;
			while ((line = br.readLine()) != null) {
				if (i == n) {
					return getFilesForCluster(line.split("\t")[0]);
				}
				i++;
			}
			br.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	private static List<Tune> getTunes(Collection<File> files) {
		List<Tune> tunes = new ArrayList<Tune>();
		int i = 1;
		for (File file : files) {
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
			if (printTunes) {
				tune.printDescription();
			}
			tunes.add(tune);
			if (MainAnalyser.getLimit() < (++i)) {
				break;
			}
		}
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

	private static Collection<File> getFilesFromDir(String dir) {
		return FileUtils.listFiles(new File(dir), new SuffixFileFilter(
				extensions), TrueFileFilter.INSTANCE);
	}

	private static String getTactSignFromFirstItem(String segment) {
		String firstItem = segment.split(" ")[0];
		return firstItem.substring(1);
	}

	private static boolean hasTactSign(String segment) {
		return segment.startsWith("T");
	}

	private static Collection<File> getFilesForCluster(String fileIdsLine) {
		files = new ArrayList<File>();
		fileMap = new DualHashBidiMap();
		sortedMap = new TreeMap<Integer,Integer>();
		String[] ids = fileIdsLine.split(" ");
		List<Integer> lineNums = new ArrayList<Integer>();
		for (String id : ids) {
			if (id.length() > 0) {
				lineNums.add(Integer.parseInt(id.trim()));
			}
		}
		BufferedReader br;
		try {
			br = new BufferedReader(new java.io.FileReader(
					"data/katalgyim-szek.dat"));
			String line;
			int i = 1;
			int n = 1;
			while ((line = br.readLine()) != null) {
				if (lineNums.contains(i)) {
					File f = new File(line.split(" ")[0].trim());
					if (f.exists()) {
						files.add(f);
						fileMap.put(i, f);
						sortedMap.put(n++, i);
					} else {
						System.out.println(f.getName() + " does not exist.");
					}
				}
				i++;
			}
			br.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		// System.out.println(Arrays.toString(files.toArray()));
		return files;
	}

}
