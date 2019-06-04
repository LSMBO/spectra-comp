package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;
import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.utils.StringUtils;

public class PeakListReader {

	private static File firstFile = null;
	private static File secondFile = null;
	private static String projectName;
	private static String firstDataset;
	private static String secondDataset;

	/**
	 * @return the firstDataset
	 */
	public static final String getFirstDataset() {
		return firstDataset;
	}

	/**
	 * @return the firstFile
	 */
	public static File getFirstFile() {
		return firstFile;
	}

	/**
	 * @return the projectName
	 */
	public static final String getProjectName() {
		return projectName;
	}

	/**
	 * @return the secondDataset
	 */
	public static final String getSecondDataset() {
		return secondDataset;
	}

	/**
	 * @return the secondFile
	 */
	public static File getSecondFile() {
		return secondFile;
	}

	/**
	 * Load spectra from file
	 * 
	 * @param file
	 */

	private static void loadFile(File file) {
	}

	/**
	 * Load first spectra
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("restriction")
	public static void loadFirstSpectra(String projectName, String firstDataset) throws SQLException {
		if (Session.USER_PARAMS.getDataSource() == DataSource.DATABASE) {
			assert StringUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert StringUtils.isEmpty(firstDataset) : "Dataset name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, firstDataset);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			loadFile(firstFile);
		}
	}

	/**
	 * Load second spectra
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("restriction")
	public static void loadSecondSpectra(String projectName, String secondDataset) throws SQLException {
		if (Session.USER_PARAMS.getDataSource() == DataSource.DATABASE) {
			assert StringUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert StringUtils.isEmpty(secondDataset) : "Dataset name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, secondDataset);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			loadFile(secondFile);
		}
	}

	/**
	 * @param firstDataset the firstDataset to set
	 */
	public static final void setFirstDataset(String firstDataset) {
		PeakListReader.firstDataset = firstDataset;
	}

	/**
	 * @param firstFile the firstFile to set
	 */
	public static void setFirstFile(File firstFile) {
		PeakListReader.firstFile = firstFile;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public static final void setProjectName(String projectName) {
		PeakListReader.projectName = projectName;
	}

	/**
	 * @param secondDataset the secondDataset to set
	 */
	public static final void setSecondDataset(String secondDataset) {
		PeakListReader.secondDataset = secondDataset;
	}

	/**
	 * @param secondFile the secondFile to set
	 */
	public static void setSecondFile(File secondFile) {
		PeakListReader.secondFile = secondFile;
	}

}
