package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;
import java.sql.SQLException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.SpectraSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.model.SpectraComparator;
import fr.lsmbo.msda.spectra.comp.settings.SpectraComparatorParams;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

// TODO: Auto-generated Javadoc
/**
 * Load spectra from Proline projects or from peak list files.
 * 
 * @author Aromdhani
 *
 */
public class PeakListProvider {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(PeakListProvider.class);

	/** The project name. */
	private static String projectName;

	/** The first pkl list. */
	private static String firstPklList;

	/** The second pkl list. */
	private static String secondPklList;

	/**
	 * Gets the ref pkl.
	 *
	 * @return the reference peaklist file path
	 */
	public static final String getRefPkl() {
		return firstPklList;
	}

	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	public static final String getProjectName() {
		return projectName;
	}

	/**
	 * Gets the tested pkl.
	 *
	 * @return the tested peaklist file path
	 */
	public static final String getTestedPkl() {
		return secondPklList;
	}

	/**
	 * Load reference peak list from a Proline project.
	 *
	 * @param dbName The database name to connect to. It's always in this form
	 *               msi_db_project_ID
	 * @param rsmIds the result_summary id from where to compute the spectra.
	 * @throws SQLException the SQL exception
	 */
	@SuppressWarnings("restriction")
	public static void loadRefSpectraFrmProline(final Long projectId, final Set<Long> rsmIds) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.DATABASE) {
			assert projectId > 0L : "Project name must not be null nor empty!";
			assert !rsmIds.isEmpty() : "Rsm Ids must not be empty!";
			logger.info("--- Start to retrieve spectra from reference peaklist from Proline project. Please wait ...");
			System.out.println(
					"INFO | Start to retrieve spectra from reference peaklist from Proline project. Please wait ...");
			// Find the msi_search_ids
			Set<Long> msiSearchIds = DBSpectraHandler.fillMsiSerachIds(projectId, rsmIds);
			DBSpectraHandler.fillSpecByPeakList(projectId, msiSearchIds);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());

		}
	}

	// test
	public static void loadTest(final Long projectId, final Long resultSetId) throws Exception {
		// TODO test
		System.out.println("INFO |  start test");
		DBSpectraHandler.fetchMSQueriesData(projectId, resultSetId);
		System.out.println("INFO |  end test");
	}

	/**
	 * Load the reference spectra from a peaklist file.
	 *
	 * @param refPklFilePath The path of the reference peaklist file.
	 * @throws Exception the exception
	 */
	@SuppressWarnings("restriction")
	public static void loadRefSpectraFromFile(String refPklFilePath) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.FILE) {
			assert (!StringsUtils.isEmpty(refPklFilePath)
					&& (new File(refPklFilePath).exists())) : "Invalid file path!";
			logger.info("Load reference spectra from the file : {} . Please wait ...", refPklFilePath);
			System.out.println(
					"INFO | --- Load reference spectra from the file  : " + refPklFilePath + " . Please wait ...");
			File firstPklListFile = new File(refPklFilePath);
			PeaklistReader.load(firstPklListFile);
		}
	}

	/**
	 * Load tested peak list from a Proline project.
	 *
	 * @param dbName The database name to connect to. It's always in this form
	 *               msi_db_project_ID
	 * @param rsmIds the result_summary ids from where to compute the spectra.
	 * @throws Exception the exception
	 */
	@SuppressWarnings("restriction")
	public static void loadTestedSpectraFrmProline(final Long projectId, Set<Long> rsmIds) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.DATABASE) {
			assert (projectId > 0L) : "Project name must not be null nor empty!";
			logger.info("--- Start to retrieve spectra from test peaklist from Proline project. Please wait ...");
			System.out.println(
					"INFO | Start to retrieve spectra from test peaklist from Proline project. Please wait ...");
			// Find the msi_search_ids
			Set<Long> msiSearchIds = DBSpectraHandler.fillMsiSerachIds(projectId, rsmIds);
			DBSpectraHandler.fillSpecByPeakList(projectId, msiSearchIds);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		}
	}

	/**
	 * Load the tested spectra from a peaklist file.
	 *
	 * @param testPklFilePath The path of the reference peaklist file.
	 * @throws Exception the exception
	 */
	@SuppressWarnings("restriction")
	public static void loadTestedSpectraFromFile(String testPklFilePath) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.FILE) {
			assert (!StringsUtils.isEmpty(testPklFilePath)
					&& (new File(testPklFilePath).exists())) : "Invalid file path !";
			logger.info("Load spectra to test from the file : {} . Please wait ...", testPklFilePath);
			System.out.println(
					"INFO | --- Load spectra to test from the file  : " + testPklFilePath + " . Please wait ...");
			File testPklFile = new File(testPklFilePath);
			PeaklistReader.load(testPklFile);
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
	 * Sets the first pkl list.
	 *
	 * @param firstPklList the first peak list to set
	 */
	public static final void setFirstPklList(String firstPklList) {
		PeakListProvider.firstPklList = firstPklList;
	}

	/**
	 * Sets the project name.
	 *
	 * @param projectName the project name to set
	 */
	public static final void setProjectName(String projectName) {
		PeakListProvider.projectName = projectName;
	}

	/**
	 * Sets the second pkl list.
	 *
	 * @param secondPklList the second peak list to set
	 */
	public static final void setSecondPklList(String secondPklList) {
		PeakListProvider.secondPklList = secondPklList;
	}

}
