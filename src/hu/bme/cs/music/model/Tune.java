/**
 * 
 */
package hu.bme.cs.music.model;

import java.io.File;
import java.util.List;

/**
 * @author Jozsef
 * 
 */
public class Tune {

	private File file;

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	private List<TuneLine> tuneLines;

	/**
	 * @return the tuneLines
	 */
	public List<TuneLine> getTuneLines() {
		return tuneLines;
	}

	public TuneLine getFirstTuneLine() {
		return tuneLines.get(0);
	}

	/**
	 * @param tuneLines
	 *            the tuneLines to set
	 */
	public void setTuneLines(List<TuneLine> tuneLines) {
		this.tuneLines = tuneLines;
	}

	public void printDescription() {
		System.out.println(">>> File: " + getFile().getAbsolutePath() + " <<<");
		// for (TuneLine tuneLine : tuneLines) {
		// tuneLine.printBars();
		// }
		getFirstTuneLine().printBars();
	}

}
