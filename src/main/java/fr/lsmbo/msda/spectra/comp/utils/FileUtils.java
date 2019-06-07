package fr.lsmbo.msda.spectra.comp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * 
 * @author Aromdhani
 *
 */
public class FileUtils {
	/**
	 * Read file bytes via input stream and close correctly the input stream
	 * 
	 * @param path
	 *            the file path to load
	 * @param fileConsumer
	 *            the action
	 */
	public static void readBytesFrmFile(String path, Consumer<Properties> fileConsumer) {
		InputStream fileInputStream = null;
		try {
			if (path != null && new File(path).exists()) {
				fileInputStream = new FileInputStream(path);
				Properties connectionProperties = new Properties();
				connectionProperties.load(fileInputStream);
				fileConsumer.accept(connectionProperties);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
