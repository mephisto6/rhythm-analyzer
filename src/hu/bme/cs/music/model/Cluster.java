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
			double avgDistFromI = 0;
			for (int j : nums) {
				if (i < j) {
					avgDistFromI += distMx[j][i];
				} else {
					avgDistFromI += distMx[i][j];
				}
			}
			avgDistFromI = avgDistFromI / (nums.size() - 1);
			if (avgDistFromI < min) {
				min = avgDistFromI;
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

	public double getSumOfDistances() {
		if (nums.size() == 1) {
			return 0;
		}
		double sum = 0;
		for (int i : nums) {
			for (int j : nums) {
				if (i < j) {
					sum += distMx[j][i];
				} else {
					sum += distMx[i][j];
				}
			}
		}
		// we counted each value twice
		return sum / 2;
	}

	public double getAvgDistance() {
		if (nums.size() == 1) {
			return 0;
		}
		// n*(n-1)
		// ------- not trivial element
		// 2
		return getSumOfDistances() / (nums.size() * (nums.size() - 1) / 2);
	}

	public double getMaxDistance() {
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
	public double getAvgDistFromCentre() {
		if (nums.size() == 1) {
			return 0;
		}
		return getSumOfDistFromCentre() / (nums.size() - 1);
	}

	public double getSumOfDistFromCentre() {
		if (nums.size() == 1) {
			return 0;
		}
		double sum = 0;
		for (int i : nums) {
			if (i < id) {
				sum += distMx[id][i];
			} else {
				sum += distMx[i][id];
			}
		}
		return sum;
	}

	public double getSquaredError() {
		double s = 0;
		for (int i : nums) {
			s += Math.pow(getDistFromCentre(i), 2);
		}
		return s;
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
