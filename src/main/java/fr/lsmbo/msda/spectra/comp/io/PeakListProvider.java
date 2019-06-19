package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;
import java.sql.SQLException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.model.SpectraComparator;
import fr.lsmbo.msda.spectra.comp.settings.SpectraComparatorParams;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * Load spectra from Proline projects or from peak list files.
 * 
 * @author Aromdhani
 *
 */
public class PeakListProvider {
	private static final Logger logger = LogManager.getLogger(PeakListProvider.class);
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
	public static void loadFirstSpectra(String projectName, Set<Long> rsmIds) throws SQLException {
		logger.info("--- Start to retrieve spectra. Please wait ...");
		System.out.println("INFO | --- Start to retrieve spectra. Please wait ...");
		if (DataSource.getType(Session.USER_PARAMS.getDataSource()) == DataSource.DATABASE) {
			assert !StringsUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert !rsmIds.isEmpty() : "Rsm Ids must not be empty!";
			// Find the msi_search_ids
			Set<Long> msiSearchIds = DBSpectraHandler.fillMsiSerachIds(projectName, rsmIds);
			DBSpectraHandler.fillSpecByPeakList(projectName, msiSearchIds);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());

		}
	}

	@SuppressWarnings("restriction")
	public static void loadFirstSpectraFromFile(String firstPklListPath) throws Exception {
		logger.info("--- Start to retrieve spectra. Please wait ...");
		System.out.println("INFO | --- Start to retrieve spectra. Please wait ...");
		if (DataSource.getType(Session.USER_PARAMS.getDataSource()) == DataSource.FILE) {
			logger.info("Load spectra from first file: " + firstPklListPath);
			System.out.println("INFO | Load spectra from first file to set as reference : " + firstPklListPath);
			assert (!StringsUtils.isEmpty(firstPklListPath)
					&& (new File(firstPklListPath).exists())) : "Invalid file path!";
			File firstPklListFile = new File(firstPklListPath);
			PeaklistReader.load(firstPklListFile);
		}
	}

	/**
	 * Load the second peak list
	 * 
	 * @param projectName
	 *            the project name
	 * @param secondPklList
	 *            the second peak list
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	public static void loadSecondSpectra(String projectName, Set<Long> rsmIds) throws Exception {
		logger.info("--- Start to retrieve spectra. Please wait ...");
		System.out.println("INFO | --- Start to retrieve spectra. Please wait ...");
		if (DataSource.getType(Session.USER_PARAMS.getDataSource()) == DataSource.DATABASE) {
			assert !StringsUtils.isEmpty(projectName) : "Project name must not be null nor empty!";
			assert !rsmIds.isEmpty() : "Rsm Ids must not be empty!";
			// Find the msi_search_ids
			Set<Long> msiSearchIds = DBSpectraHandler.fillMsiSerachIds(projectName, rsmIds);
			DBSpectraHandler.fillSpecByPeakList(projectName, msiSearchIds);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		}
	}

	/**
	 * Load the second peak list
	 * 
	 * @param projectName
	 *            the project name
	 * @param secondPklList
	 *            the second peak list
	 * @throws SQLException
	 */
	@SuppressWarnings("restriction")
	public static void loadSecondSpectraFromFile(String secondPklListPath) throws SQLException {
		logger.info("--- Start to retrieve spectra. Please wait ...");
		System.out.println("INFO | --- Start to retrieve spectra. Please wait ...");
		if (DataSource.getType(Session.USER_PARAMS.getDataSource()) == DataSource.FILE) {
			logger.info("Load spectra from second file to test : {}", secondPklListPath);
			System.out.println("INFO | Load spectra from second file to test: " + secondPklListPath);
			assert (!StringsUtils.isEmpty(secondPklListPath)
					&& (new File(secondPklListPath).exists())) : "Invalid file path!";
			File secondPklListFile = new File(secondPklListPath);
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
		logger.info("Start to compare: {} as a reference sepctra vs  {} spectra. please wait ...",
				ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size(),
				ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size());
		logger.info(Session.USER_PARAMS.getComparison().toString());
		ListOfSpectra.getFirstSpectra().getSpectraAsObservable().forEach(sepctrum -> {
			SpectraComparator.run(sepctrum);
		});
		logger.info("{} valid spectra found.", SpectraComparator.getValidSpectra().getNbSpectra());
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
