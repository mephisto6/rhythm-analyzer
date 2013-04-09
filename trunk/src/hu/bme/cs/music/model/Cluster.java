/**
 * 
 */
package hu.bme.cs.music.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class Cluster {

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
		nums.addAll(c.getNums());
	}

	public List<Integer> getNums() {
		return nums;
	}

	public int getId() {
		return id;
	}
}
