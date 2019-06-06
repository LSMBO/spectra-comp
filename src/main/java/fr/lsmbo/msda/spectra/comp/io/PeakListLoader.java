package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;
import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

public class PeakListLoader {

	private static File firstFile = null;
	private static File secondFile = null;
	private static String projectName;
	private static String firstPklList;
	private static String secondPklList;

	/**
	 * @return the first peak list
	 */
	public static final String getFirstDataset() {
		return firstPklList;
	}

	/**
	 * @return the first file
	 */
	public static File getFirstFile() {
		return firstFile;
	}

	/**
	 * @return the project name
	 */
	public static final String getProjectName() {
		return projectName;
	}

	/**
	 * @return the second peak list
	 */
	public static final String getSecondDataset() {
		return secondPklList;
	}

	/**
	 * @return the second file
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
	 * Load first peak list
	 * 
	 * @param projectName
	 *            the project name
	 * @param firstPklList
	 *            the first peak list
	 * @throws SQLException
	 */
	@SuppressWarnings("restriction")
	public static void loadFirstSpectra(String projectName, String firstPklList) throws SQLException {
		if (Session.USER_PARAMS.getDataSource() == DataSource.DATABASE) {
			assert StringsUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert StringsUtils.isEmpty(firstPklList) : "Dataset name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, firstPklList);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			loadFile(firstFile);
		}
	}

	/**
	 * Load the second peaklist
	 * 
	 * @param projectName
	 *            the project name
	 * @param secondPklList
	 *            the second peak list
	 * @throws SQLException
	 */
	@SuppressWarnings("restriction")
	public static void loadSecondSpectra(String projectName, String secondPklList) throws SQLException {
		if (Session.USER_PARAMS.getDataSource() == DataSource.DATABASE) {
			assert StringsUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert StringsUtils.isEmpty(secondPklList) : "Dataset name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, secondPklList);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			loadFile(secondFile);
		}
	}

	/**
	 * @param firstDataset
	 *            the first peak list to set
	 */
	public static final void setFirstDataset(String firstDataset) {
		PeakListLoader.firstPklList = firstDataset;
	}

	/**
	 * @param firstFile
	 *            the first file to set
	 */
	public static void setFirstFile(File firstFile) {
		PeakListLoader.firstFile = firstFile;
	}

	/**
	 * @param projectName
	 *            the project name to set
	 */
	public static final void setProjectName(String projectName) {
		PeakListLoader.projectName = projectName;
	}

	/**
	 * @param secondDataset
	 *            the second peak list to set
	 */
	public static final void setSecondDataset(String secondDataset) {
		PeakListLoader.secondPklList = secondDataset;
	}

	/**
	 * @param secondFile
	 *            the second file to set
	 */
	public static void setSecondFile(File secondFile) {
		PeakListLoader.secondFile = secondFile;
	}

}
