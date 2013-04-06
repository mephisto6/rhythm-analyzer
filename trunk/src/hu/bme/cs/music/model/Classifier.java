/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.MainAnalyser;
import hu.bme.cs.music.file.FileReader;

import java.io.File;

import org.apache.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * @author Jozsef
 * 
 */
public abstract class Classifier {

	private static Logger log = Logger.getLogger(Classifier.class);

	public int getClassNum(int[] classes) {
		return Sets.newHashSet(Ints.asList(classes)).size();
	}

	public abstract int[] getClasses();

	public abstract String getName();

	public void printClasses() {
		System.out.println(getName());
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		Object[] files = FileReader.getFiles().toArray();
		for (Integer i : multimap.keySet()) {
			for (Object o : multimap.get(i)) {
				File f = (File) files[(Integer) o - 1];
				System.out.print(FileReader.getFileMap().inverseBidiMap()
						.get(f)
						+ " ");
			}
			System.out.println();
		}
	}

	public void printClassesOld() {
		// System.out.println(Arrays.asList(ArrayUtils.toObject(classes)));
		System.out.println(getName());
		Multimap<Integer, Object> multimap = ArrayListMultimap.create();
		for (int i = 0; i < getClasses().length; i++) {
			multimap.put(getClasses()[i], i + 1);
		}
		int j = 0;
		for (Integer i : multimap.keySet()) {
			System.out.println("Class " + (++j) + ": " + multimap.get(i));
		}
	}
	
    public void classify(double[][] givenMx) {
        int limit = Math.min(MainAnalyser.LIMIT, givenMx.length);
        int[] classesMin = new int[limit];
        int[] classesMax = new int[limit];
        for (int i = 0; i < limit; i++) {
                classesMin[i] = i + 1;
                classesMax[i] = -1;
        }
        //TODO
//        classifySpec(givenMx, classesMin, classesMax);
//        printClasses(classesMin, "Classes by given distance matrix (min): ");
//        printClasses(classesMax, "Classes by given distance matrix (max): ");
}

	// makes equal all the occurrences of oldClass with newClass
	protected  void makeEqual(int[] classes, int oldClass, int newClass) {
		for (int i = 0; i < classes.length; i++) {
			if (classes[i] == oldClass) {
				log.debug("Class of " + (i + 1) + " (" + classes[i]
						+ ") is set to " + newClass);
				classes[i] = newClass;
			}
		}
	}

}
