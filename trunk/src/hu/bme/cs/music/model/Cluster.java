/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.utils.MatrixUtils;

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

	double[][] distMx;

	public Cluster(int i, double[][] distMx) {
		this.distMx = MatrixUtils.copyMx(distMx);
		id = i;
		nums = new ArrayList<Integer>();
		nums.add(i);
	}

	public double getDistFromCentre(int i) {
		if (i < id) {
			return distMx[id][i];
		} else {
			return distMx[i][id];
		}
	}

	public int calculateNewCentre() {
		if (nums.size() == 1) {
			return 0;
		}
		int newId = id;
		double min = Double.MAX_VALUE;
		for (int i : nums) {
			double avg = 0;
			for (int j : nums) {
				if (i < j) {
					avg += distMx[j][i];
				} else {
					avg += distMx[i][j];
				}
			}
			avg = avg / (nums.size() - 1);
			if (avg < min) {
				min = avg;
				newId = i;
			}
		}
		if (id != newId) {
			log.debug("old centre: " + id + ", new centre: " + newId);
			id = newId;
			return 1;
		}
		// return 0 if no changes were made
		return 0;
	}

	public double getMaxDistanceWithinCluster() {
		if (nums.size() == 1) {
			return 0;
		}
		double max = -Double.MAX_VALUE;
		for (int i : nums) {
			for (int j : nums) {
				if (i < j) {
					if (distMx[j][i] > max) {
						max = distMx[j][i];
					}
				} else {
					if (distMx[i][j] > max) {
						max = distMx[i][j];
					}
				}
			}
		}
		return max;
	}

	/**
	 * return avg dist from centre
	 * 
	 * @param distMx
	 * @return
	 */
	public double getDiameter() {
		if (nums.size() == 1) {
			return 0;
		}
		double avg = 0;
		for (int i : nums) {
			if (i < id) {
				avg += distMx[id][i];
			} else {
				avg += distMx[i][id];
			}
		}
		return avg / (nums.size() - 1);
	}

	public boolean containsNum(int i) {
		return nums.contains(i);
	}

	public void addNum(int i) {
		if (!containsNum(i)) {
			nums.add(i);
		}
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

	public int getId1() {
		return id + 1;
	}

	public void clearCluster() {
		nums.clear();
		nums.add(id);
	}
}
