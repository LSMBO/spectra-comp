package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;
import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.utils.FileUtils;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * Load peak lists to compare from database(Proline) or from files.
 * 
 * @author Aromdhani
 *
 */
public class PeakListProvider {

	private static String projectName;
	private static String firstPklList;
	private static String secondPklList;
	public static boolean isSecondPeakList = false;

	/**
	 * @return the first peak list
	 */
	public static final String getFirstPklList() {
		return firstPklList;
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
	public static final String getSecondPklList() {
		return secondPklList;
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
			assert StringsUtils.isEmpty(firstPklList) : "First peak list name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, firstPklList);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			if (FileUtils.isValidMgf(firstPklList)) {
				File firstPklListFile = new File(firstPklList);
				PeaklistReader.load(firstPklListFile);
			}
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
			assert StringsUtils.isEmpty(secondPklList) : "Second peak list name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, secondPklList);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			if (FileUtils.isValidMgf(secondPklList)) {
				File secondPklListFile = new File(secondPklList);
				PeaklistReader.load(secondPklListFile);
			}
		}
	}

	/**
	 * @param firstPklList
	 *            the first peak list to set
	 */
	public static final void setFirstPklList(String firstPklList) {
		PeakListProvider.firstPklList = firstPklList;
	}

	/**
	 * @param projectName
	 *            the project name to set
	 */
	public static final void setProjectName(String projectName) {
		PeakListProvider.projectName = projectName;
	}

	/**
	 * @param secondPklList
	 *            the second peak list to set
	 */
	public static final void setSecondPklList(String secondPklList) {
		PeakListProvider.secondPklList = secondPklList;
	}

}
