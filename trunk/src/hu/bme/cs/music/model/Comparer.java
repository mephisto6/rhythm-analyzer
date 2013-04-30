/**
 * 
 */
package hu.bme.cs.music.model;

import hu.bme.cs.music.utils.FileUtils;
import hu.bme.cs.music.utils.MatrixUtils;

import java.util.List;
import java.util.Locale;

/**
 * @author Jozsef
 * 
 */
public abstract class Comparer {

	private List<Tune> tunes;

	private double[][] distMx;

	public double[][] getDistanceMx() {
		if (distMx == null) {
			distMx = init();
		}
		return distMx;
	}

	protected void setTunes(List<Tune> tunes) {
		this.tunes = tunes;
	}

	public abstract String getName();

	public abstract double compare(TuneLine t1, TuneLine t2);

	protected double[][] init() {
		int size = tunes.size();
		double[][] distMx = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				distMx[j][i] = compare(tunes.get(j).getFirstTuneLine(), tunes
						.get(i).getFirstTuneLine());
			}
		}
		MatrixUtils.normalizeMx(distMx);
		return distMx;
	}

	public void generateFile() {
		String fileName = getName().replace(" ", "_");
		FileUtils.gerenateNexFile(fileName, getDistanceMx());
	}

	public void printMX() {
		double[][] mx = getDistanceMx();
		System.out.print("     --------------------     " + getName()
				+ "     ---------------------     \n");
		for (int i = 0; i < mx.length; i++) {
			System.out.print(i + 1 + "\t");
			for (int j = 0; j <= i; j++) {
				System.out.printf(Locale.US, "%.3f%s", mx[i][j], "\t");
			}
			System.out.println();
		}
		System.out.print("\t");
		for (int i = 0; i < mx.length; i++) {
			System.out.print(i + 1 + "\t");
		}
		System.out.println();
	}

}
