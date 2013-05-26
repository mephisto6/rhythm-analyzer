/**
 * 
 */
package hu.bme.cs.music.manage;

import hu.bme.cs.music.classify.AverageLinkageClassifier;
import hu.bme.cs.music.classify.CompleteLinkageClassifier;
import hu.bme.cs.music.classify.DBSCANClassifier;
import hu.bme.cs.music.classify.FarthestFirstKMeansClassifier;
import hu.bme.cs.music.classify.FirstRandomKMeansClassifier;
import hu.bme.cs.music.classify.MaxDistanceClassifier;
import hu.bme.cs.music.classify.MinDistanceClassifier;
import hu.bme.cs.music.classify.RandomKMeansClassifier;
import hu.bme.cs.music.classify.SingleLinkageClassifier;
import hu.bme.cs.music.model.Classifier;
import hu.bme.cs.music.model.Comparer;
import hu.bme.cs.music.model.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class ClassifyManager extends Manager {

	private List<Classifier> classifiers;

	public ClassifyManager(List<Comparer> comparers) {
		classifiers = new ArrayList<Classifier>();
		for (Comparer comparer : comparers) {
		//	classifiers.add(new SingleLinkageClassifier(comparer));
			classifiers.add(new AverageLinkageClassifier(comparer));
			classifiers.add(new CompleteLinkageClassifier(comparer));
		//	classifiers.add(new RandomKMeansClassifier(comparer));
			classifiers.add(new FarthestFirstKMeansClassifier(comparer));
		//	classifiers.add(new FirstRandomKMeansClassifier(comparer));
			classifiers.add(new DBSCANClassifier(comparer));
		}
	}

	public ClassifyManager(Comparer comparer, int id) {
		classifiers = new ArrayList<Classifier>();
		switch (id) {
		case 0:
			classifiers.add(new SingleLinkageClassifier(comparer));
			break;
		case 1:
			classifiers.add(new AverageLinkageClassifier(comparer));
			break;
		case 2:
			classifiers.add(new CompleteLinkageClassifier(comparer));
			break;
		case 3:
			classifiers.add(new RandomKMeansClassifier(comparer));
			break;
		case 4:
			classifiers.add(new FirstRandomKMeansClassifier(comparer));
			break;
		case 5:
			classifiers.add(new FarthestFirstKMeansClassifier(comparer));
			break;
		case 6:
			classifiers.add(new DBSCANClassifier(comparer));
			break;
		}
	}

	public ClassifyManager(double[][] givenMx) {
		classifiers = new ArrayList<Classifier>();
		classifiers.add(new MinDistanceClassifier(givenMx));
		classifiers.add(new MaxDistanceClassifier(givenMx));
	}

	@Override
	public void printResults() {
		for (Classifier classifier : classifiers) {
			System.out.print(classifier.printClasses());
			System.out.println("-----");
		}
	}

	@Override
	public int getNumberOfSongs() {
		int res = 0;
		if (classifiers != null && !classifiers.isEmpty()) {
			return classifiers.get(0).getClasses().length;
		}
		return res;
	}

	@Override
	public String getResults() {
		StringBuffer sb = new StringBuffer();
		for (Classifier classifier : classifiers) {
			sb.append(classifier.printClasses());
		}
		return sb.toString();
	}
	
	public String getMetrics() {
		StringBuffer sb = new StringBuffer();
		for (Classifier classifier : classifiers) {
			sb.append(classifier.getMetrics());
		}
		return sb.toString();
	}

}
