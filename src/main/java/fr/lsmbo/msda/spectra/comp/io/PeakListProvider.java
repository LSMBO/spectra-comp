package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;
import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.model.SpectraComparator;
import fr.lsmbo.msda.spectra.comp.model.SpectraComparatorParams;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * Load peak lists from databases(Proline) or from files.
 * 
 * @author Aromdhani
 *
 */
public class PeakListProvider {

	private static String projectName;
	private static String firstPklList;
	private static String secondPklList;

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
		System.out.println("--- Start to retrieve spectra. Please wait ...");
		if (DataSource.getType(Session.USER_PARAMS.getDataSource()) == DataSource.DATABASE) {
			assert StringsUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert StringsUtils.isEmpty(firstPklList) : "First peak list name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, firstPklList);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());

		} else {
			System.out.println("INFO - Load spectra from file: " + firstPklList);
			assert (!StringsUtils.isEmpty(firstPklList) && (new File(firstPklList).exists())) : "Invalid file path!";
			File firstPklListFile = new File(firstPklList);
			PeaklistReader.load(firstPklListFile);
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
		System.out.println("--- Start to retrieve spectra. Please wait ...");
		if (DataSource.getType(Session.USER_PARAMS.getDataSource()) == DataSource.DATABASE) {
			assert StringsUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert StringsUtils.isEmpty(secondPklList) : "Second peak list name must not be null nor empty!";
			DBSpectraHandler.fillSpecByPeakList(projectName, secondPklList);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			System.out.println("INFO - Load spectra from file: " + secondPklList);
			PeaklistReader.isSecondPeakList = true;
			assert (!StringsUtils.isEmpty(secondPklList) && (new File(secondPklList).exists())) : "Invalid file path!";
			File secondPklListFile = new File(secondPklList);
			PeaklistReader.load(secondPklListFile);
		}
	}

	/**
	 * Compare spectra using dot product method.
	 * 
	 * @see SpectraComparator
	 * @see SpectraComparatorParams
	 */
	public static void compareSpectra() {
		System.out.println("INFO - Start to compare: " + ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size()
				+ " as a reference sepctra vs " + ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size()
				+ " spectra. please wait ...");
		ListOfSpectra.getFirstSpectra().getSpectraAsObservable().forEach(sepctrum -> {
			SpectraComparator.run(sepctrum);
		});
		System.out.println("INFO - " + SpectraComparator.getValidSpectra().getNbSpectra()+" valid spectra found.");
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
