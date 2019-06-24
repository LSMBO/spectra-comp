package fr.lsmbo.msda.spectra.comp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUtils.
 *
 * @author Aromdhani
 */
public class FileUtils {

	/**
	 * Read file bytes via input stream and close correctly the input stream.
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

	/**
	 * Open peak list file.
	 *
	 * @param peakListConsumer
	 *            a Consumer of peak list file when the file is loaded.
	 * @param stage
	 *            the stage
	 */
	public static void openPeakListFile(Consumer<File> peakListConsumer, Stage stage) {
		// Default folder is 'Documents'
		File initialDirectory = new File(System.getProperty("user.home"));
		FileChooser fileChooser = new FileChooser();
		// Set extension filter
		fileChooser.setInitialDirectory(initialDirectory);
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Mgf files (*.mgf)", "*.mgf"),
				new FileChooser.ExtensionFilter("PeakList files (*.pkl)", "*.pkl"));
		fileChooser.setTitle("Open file");
		// Show open file dialog
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			peakListConsumer.accept(file);
		}
	}

	/**
	 * Browse and show a file
	 * 
	 * @param path
	 *            the path of file to browse and to show.
	 */
	public static void showFile(String path) {
		if (java.awt.Desktop.isDesktopSupported()) {
			try {
				java.awt.Desktop.getDesktop().browse(new File(path).toURI());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
