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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

	private static final boolean printTunes = false;

	private static String[] extensions = new String[] { "mnf", "tnf", "gnf",
			"pnf", "knf", "snf", "vnf" };

	private static BidiMap fileMap;

	private static List<File> files;

	public static BidiMap getFileMap() {
		return fileMap;
	}

	public static List<File> getFiles() {
		return files;
	}

	public static List<Tune> getTunes(String dir) {
		createFileMap();
		files = Arrays.asList(FileUtils
				.convertFileCollectionToFileArray(getFilesFromDir(dir)));
		return getTunes(files);
	}

	private static void createFileMap() {
		fileMap = new DualHashBidiMap();
		BufferedReader br;
		try {
			br = new BufferedReader(new java.io.FileReader(
					"data/katalgyim-szek.dat"));
			String line;
			int i = 1;
			while ((line = br.readLine()) != null) {
				File f = new File(line.split(" ")[0].trim());
				if (f.exists()) {
					fileMap.put(i, f);
				} else {
					System.out.println(f.getName() + " does not exist.");
				}
				i++;
			}
			br.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public static List<Tune> getClusterTunes(int i) {
		createFileMap();
		return getTunes(getClusterFiles(i));
	}

	public static String getFileIdsLine(String nn) {
		int n = Integer.valueOf(nn);
		BufferedReader br;
		try {
			br = new BufferedReader(new java.io.FileReader(
					"data/felho-gyim-szek-e6.dat"));
			String line;
			int i = 1;
			while ((line = br.readLine()) != null) {
				if (i == n) {
					return line.split("\t")[0];
				}
				i++;
			}
			br.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
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
					// return
					// getFilesForCluster("88 145 169 172 175 182 211 212 213 228 229 277 288 299 376 377 378 392 455 575 760 764 1030 1090 1379 1476 1594 1609 1610 13 122 123 151 163 204 224 255 264 307 386 394 397 460 465 799 1012 1023 1038 1039 1097 1147 1162 1331 1405 1418 1419 1448 1449 1475 1496 1498 1586 1594 121 21  71  82 149 206 208 300 433 998 1019 1065 1066 1093 1270 1422 207 210 273 463 509 584 948 1052 1069 1133 1187 1188 1189 1190 209 162 597 614 616 619 623 634 635 637 640 663 666 668 688 689 691 697 699 1199 1206 1208 1210 1216 1225 1254 1281 1303 1304 1306 1414 1479 1480 615 250 278 279 287 1033 1462 249 174 592 595 598 601 603 605 621 624 653 654 657 671 679 680 693 778 1088 1089 1203 1218 1220 1305 1412 1544 1548 1549 658 40 41 42 45 49 721 728 1081 1 38  40 237 238 240 241 339 375 388 433 435 491 494 766 1006 1008 1165 1423 1424 1425 296 149 407 522 526 529 531 532 533 537 540 547 586 587 590 746 791 1396 1518 521 100 101 102 104 105 107 111 138 139 171 1136 1295 106 89 244 303 447 841 969 972 1356 1358 1384 843 147 148 293 308 358 432 1036 1160 1273 1592 1593 1364 415 444 445 498 565 781 786 1031 1138 1553 1566 1567 1594 795 10 313 361 453 1002 1005 1164 1547 454 406 458 461 489 556 799 808 1418 1419 1529 1617 412 331 604 616 620 660 697 1211 1214 1224 1229 1230 682 600 617 625 631 632 636 641 646 655 694 700 1197 1198 1283 1201 259 327 383 790 1450 272 35  36  37 225 409 414 423 759 793 400 55 315 352 723 724 1163 1453 1074 976 1086 1431 1433 1434 1432 274 1113 1117 1118 1123 14  15  39 141 183 233 761 995 1098 1162 1182 1318 1472 205 87 809 1301 1571 1572 1573 158 945 1333 944 203 281 571 777 223 262 554 1243 1493 1509 1447");
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

	public static List<Tune> getTunes(Collection<File> files) {
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

	public static Collection<File> getFilesForCluster(String fileIdsLine) {
		if (fileMap == null) {
			fileMap = new DualHashBidiMap();
		}
		files = new ArrayList<File>();
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
			while ((line = br.readLine()) != null) {
				if (lineNums.contains(i)) {
					File f = new File(line.split(" ")[0].trim());
					if (f.exists()) {
						files.add(f);
						fileMap.put(i, f);
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
