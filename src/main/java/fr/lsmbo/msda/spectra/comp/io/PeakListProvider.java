package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.SpectraSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.model.SpectraComparator;
import fr.lsmbo.msda.spectra.comp.settings.SpectraComparatorParams;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * Load spectra from Proline project or from peaklist file.
 * 
 * @author Aromdhani
 *
 */
public class PeakListProvider {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(PeakListProvider.class);

	/** The project name. */
	private static String projectName;

	/** The reference peaklist. */
	private static String referencePeakList;

	/** The test peaklist. */
	private static String testPeakList;

	/**
	 * Gets the reference peaklist.
	 *
	 * @return the reference peaklist file path
	 */
	public static final String getReferencePeakList() {
		return referencePeakList;
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
	 * Gets the tested peaklist.
	 *
	 * @return the tested peaklist file path
	 */
	public static final String getTestPeakList() {
		return testPeakList;
	}

	/**
	 * Sets the reference peaklist.
	 *
	 * @param referencePeakList the reference peaklist to set
	 */
	public static final void setReferencePeakList(String referencePeakList) {
		PeakListProvider.referencePeakList = referencePeakList;
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
	 * Sets the test peaklist.
	 *
	 * @param testPeakList the second peak list to set
	 */
	public static final void setSecondPklList(String testPeakList) {
		PeakListProvider.testPeakList = testPeakList;
	}

	/**
	 * Load reference spectra from proline project.
	 * 
	 * @param projectId   the project id
	 * @param resultSetId the result set id from where the spectra will be
	 *                    retrieved.
	 * @throws Exception the exception to throw
	 */
	public static void loadRefSpectra(final Long projectId, final Long resultSetId) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.DATABASE) {
			logger.info("Start to retrieve reference spectra from a proline project with id=#{}. Please wait ...",
					projectId);
			System.out.println("INFO | Start to retrieve reference spectra from a proline project with id=#" + projectId
					+ ". Please wait ...");
			DBSpectraHandler.fetchMSQueriesData(projectId, resultSetId);
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		}
	}

	/**
	 * Load test spectra from proline project.
	 * 
	 * @param projectId the project id
	 * @param the       result set id from where the spectra will be retrieved.
	 * @throws Exception the exception to throw
	 */
	public static void loadTestSpectra(final Long projectId, final Long resultSetId) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.DATABASE) {
			logger.info("Start to retrieve test spectra from a proline project with id=#{}. Please wait ...",
					projectId);
			System.out.println("INFO | Start to retrieve test spectra from a proline project with id=#" + projectId
					+ ". Please wait...");
			DBSpectraHandler.fetchMSQueriesData(projectId, resultSetId);
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		}
	}

	/**
	 * Load the reference spectra from a peaklist file(.mgf or .pkl).
	 *
	 * @param refPklFilePath The path of the reference peaklist file.
	 * @throws Exception the exception tro throw
	 */
	@SuppressWarnings("restriction")
	public static void loadRefSpectraFromFile(String refPklFilePath) throws Exception {
		if (SpectraSource.getType(Session.USER_PARAMS.getDataSource()) == SpectraSource.FILE) {
			assert (!StringsUtils.isEmpty(refPklFilePath)
					&& (new File(refPklFilePath).exists())) : "Invalid file path!";
			logger.info("Load reference spectra from file: {} . Please wait ...", refPklFilePath);
			System.out.println("INFO | --- Load reference spectra from file: " + refPklFilePath + ". Please wait ...");
			File firstPklListFile = new File(refPklFilePath);
			PeaklistReader.load(firstPklListFile);
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
			logger.info("Load spectra to test from the file: {} . Please wait ...", testPklFilePath);
			System.out.println("INFO | --- Load test spectra from file: " + testPklFilePath + ". Please wait ...");
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
		logger.info(
				"Start comparing: {} as reference spectra with {} as test spectra. The used parameters in comparison are: {}. Please wait...",
				ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size(),
				ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size(),
				Session.USER_PARAMS.getComparison().toString());
		ListOfSpectra.getFirstSpectra().getSpectraAsObservable().forEach(sepctrum -> {
			SpectraComparator.run(sepctrum);
		});
		logger.info("{} valid spectra were found.", SpectraComparator.getValidSpectra().getNbSpectra());
	}

}
