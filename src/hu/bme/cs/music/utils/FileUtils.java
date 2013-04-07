/**
 * 
 */
package hu.bme.cs.music.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.google.common.io.Files;

/**
 * @author Jozsef
 * 
 */
public class FileUtils {

	private static Logger log = Logger.getLogger(FileUtils.class);

	public static void initLogging() {
		DOMConfigurator.configure(new File(".").getAbsolutePath()
				+ "/resources/log4j.xml");
	}

	public static void gerenateNexFile(String name, double[][] mx) {
		int size = mx.length;
		StringBuilder sb = new StringBuilder();
		sb.append("#NEXUS\n\nBEGIN taxa;\n\tDIMENSIONS ntax=" + size
				+ ";\nTAXLABELS\n");
		for (int i = 1; i <= size; i++) {
			sb.append("\t" + i + "\n");
		}
		sb.append(";\nEND;\n\nBEGIN distances;\n\tDIMENSIONS ntax=" + size
				+ ";\n");
		sb.append("\tFORMAT\n\t\ttriangle=LOWER\n\t\tdiagonal\n\t\tlabels\n\t\tmissing=?\n\t;\n");
		sb.append("MATRIX\n");
		for (int i = 0; i < mx.length; i++) {
			sb.append(i + 1 + "\t");
			for (int j = 0; j <= i; j++) {
				sb.append(String.format(Locale.US, "%.3f%s", mx[i][j], "\t"));
			}
			sb.append("\n");
		}
		sb.append("\n;\nEND;");

		File newFile = new File("nex/" + name + ".nex");
		try {
			Files.write(sb.toString().getBytes(), newFile);
		} catch (IOException fileIoEx) {
			log.error("Error while writing " + name + ". "
					+ fileIoEx.getMessage());
		}
	}

}
