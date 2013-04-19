package hu.bme.cs.music.classify;

import hu.bme.cs.music.model.Comparer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.primitives.Ints;

public class FirstRandomKMeansClassifier extends FarthestFirstKMeansClassifier {

	private static Logger log = Logger
			.getLogger(FirstRandomKMeansClassifier.class);

	public FirstRandomKMeansClassifier(Comparer comparer) {
		super(comparer);
	}

	@Override
	public int[] getFirstCenters() {
		List<Integer> indexes = new ArrayList<Integer>();
		indexes.add((int) (Math.random() * (getClassNum() + 1)));
		while (indexes.size() != getClassNum()) {
			indexes.add(getNextFarthestElement(indexes));
		}
		log.debug("farthest indexes: " + indexes);
		return Ints.toArray(indexes);
	}

	@Override
	public String getDescription() {
		return "first random k-means";
	}

}
