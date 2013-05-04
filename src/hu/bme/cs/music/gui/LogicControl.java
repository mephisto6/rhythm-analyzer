/**
 * 
 */
package hu.bme.cs.music.gui;

import hu.bme.cs.music.classify.DBSCANClassifier;
import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.manage.ClassifyManager;
import hu.bme.cs.music.manage.CompareManager;
import hu.bme.cs.music.model.KMeansClassifier;
import hu.bme.cs.music.model.Manager;

/**
 * @author Jozsef
 * 
 */
public class LogicControl {

	public static int k;

	private CompareManager compareManager;

	private Manager classifyManager;

	private int comparerId;

	private int classifierId;

	public void run() {

		getParams();

		switch (MainWindow.getMode()) {
		case 1:
			compareManager = new CompareManager(FileReader.getTunes(MainWindow
					.getDirectory()), comparerId);
			break;
		case 2:
			compareManager = new CompareManager(FileReader.getTunes(FileReader
					.getFilesForCluster(MainWindow.getFileIdsLine())),
					comparerId);
			break;
		}

		KMeansClassifier.loopNum = MainWindow.getKMeansLoop();
		DBSCANClassifier.eps = MainWindow.getEps();
		DBSCANClassifier.minpts = MainWindow.getMinPts();

		classifyManager = new ClassifyManager(compareManager.getComparer(),
				classifierId);

		MainWindow.setResultText(classifyManager.getResults());
	}

	private void getParams() {
		comparerId = MainWindow.getComparerId();
		classifierId = MainWindow.getClassifierId();
		k = MainWindow.getK();
	}
}
