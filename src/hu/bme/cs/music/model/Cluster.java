/**
 * 
 */
package hu.bme.cs.music.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Jozsef
 * 
 */
public class Cluster {

	private static Logger log = Logger.getLogger(Cluster.class);

	private List<Integer> nums;

	private int id;

	public Cluster(int i) {
		id = i + 1;
		nums = new ArrayList<Integer>();
		nums.add(i);
	}

	public boolean containsNum(int i) {
		return nums.contains(i);
	}

	public void addNum(int i) {
		nums.add(i);
	}

	public void joinCluster(Cluster c) {
		log.debug(id + " (" + getNums() + ") joined with " + c.getId() + " ("
				+ c.getNums() + ")");
		nums.addAll(c.getNums());
	}

	public List<Integer> getNums() {
		return nums;
	}

	public int getId() {
		return id;
	}
}
