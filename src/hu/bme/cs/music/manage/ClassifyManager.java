/**
 * 
 */
package hu.bme.cs.music.manage;

import hu.bme.cs.music.classify.MaxDistanceClassifier;
import hu.bme.cs.music.classify.MinDistanceClassifier;
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
			classifiers.add(new MinDistanceClassifier(comparer));
			classifiers.add(new MaxDistanceClassifier(comparer));
		}
	}

	@Override
	public void printResults() {
		for (Classifier classifier : classifiers) {
			classifier.printClasses();
			System.out.println("-----");
		}
	}

}
