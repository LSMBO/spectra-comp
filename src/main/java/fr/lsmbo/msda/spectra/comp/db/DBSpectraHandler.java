package fr.lsmbo.msda.spectra.comp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.list.Spectra;
import fr.lsmbo.msda.spectra.comp.model.DMsQuery;
import fr.lsmbo.msda.spectra.comp.model.DPeptide;
import fr.lsmbo.msda.spectra.comp.model.Dataset;
import fr.lsmbo.msda.spectra.comp.model.Dataset.DatasetType;
import fr.lsmbo.msda.spectra.comp.model.Fragment;
import fr.lsmbo.msda.spectra.comp.model.Project;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handle databases request. It retrieves the data from Proline databases.
 * Example : Retrieve the projects , the ms queries ...
 * 
 * @author Aromdhani
 */

public class DBSpectraHandler {
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(DBSpectraHandler.class);

	/** The Constant USER. */
	private static final String USER = "SELECT login FROM user_account WHERE login=?";

	/** The Constant PROJECT. */
	private static final String PROJECT = "SELECT * FROM external_db WHERE name=?";

	/** The Constant PEAKLIST. */
	private static final String PEAKLIST = "SELECT * FROM peaklist";

	/** The Constant PROJECTS_BY_OWNER. */
	private static final String PROJECTS_BY_OWNER = "SELECT project.id as id ,project.name as name,project.description as description FROM user_account ,project WHERE user_account.id=project.owner_id AND user_account.login=?";

	/** The Constant UDS_DATASET. */
	private static final String UDS_DATASET = "SELECT DISTINCT(ds.result_summary_id) AS rsm_id,ds.* ,proj.name proj_name FROM data_set ds,project proj WHERE  proj.id=ds.project_id AND ds.type IN ('AGGREGATE','IDENTIFICATION') AND ds.result_summary_id>0 AND ds.project_id=? order by ds.result_summary_id";

	/** The Constant ALL_DATASET. */
	private static final String ALL_DATASET = "SELECT * FROM data_set WHERE project_id =? ORDER BY parent_dataset_id DESC";
	/** The Constant VALIDATED_MSI_SEARCH_IDS. */

	private static final String VALIDATED_MSI_SEARCH_IDS = "SELECT distinct(msi.id) as msi_search_id FROM result_summary rsm, result_set rs, msi_search msi, peaklist pkl, protein_set ps WHERE rsm.result_set_id=rs.id AND rs.msi_search_id=msi.id AND msi.peaklist_id= pkl.id AND ps.result_summary_id=rsm.id AND ps.is_validated=true AND rs.type IN('SEARCH','USER') AND rsm.id=?";

	/** The Constant SPECTRA_BY_MSI_SEARCH. */
	private static final String SPECTRA_BY_MSI_SEARCH = "SELECT spec.*,pklsof.name as pkl_software FROM msi_search msi,peaklist pkl,peaklist_software pklsof,spectrum spec WHERE spec.peaklist_id=pkl.id AND  pkl.id=msi.peaklist_id AND pkl.peaklist_software_id=pklsof.id AND msi.id=?";
	// TODO
	/** The constant MSI_SEARCH_ID_QUERY */
	private static final String MSI_SEARCH_ID_QUERY = "SELECT msi_search_id FROM result_set WHERE id=?";

	/** The constant MSI_MSQ_QUERY */
	private static final String MSI_MSQ_QUERY = "SELECT msq.* FROM ms_query msq, spectrum sp, msi_search msi, peaklist pkl "
			+ "WHERE msi.peaklist_id=pkl.id AND pkl.id=sp.peaklist_id AND sp.id=msq.spectrum_id AND msi.id=? AND msq.msi_search_id=?";

	/** The constant of msi spectra */
	private static final String MSI_SPECTRA = "SELECT spec.* FROM spectrum spec, ms_query msq WHERE spec.id=msq.spectrum_id AND msq.id IN(?)";

	/** The constant of protein match by ms queries */
	private static final String QUERY_PSM = "SELECT pepm.ms_query_id ,pepm.peptide_id  FROM peptide_match pepm WHERE pepm.result_set_id in(?) AND pepm.ms_query_id IN(?) order by pepm.ms_query_id ";

	/** The constant of result set id */
	private static final String RESULTSET_ID = "SELECT id,decoy_result_set_id FROM result_set WHERE id=?";

	/** The constant of peptide query */
	private static final String PEPTIDE_QUERY = "SELECT id, sequence, ptm_string, calculated_mass FROM peptide WHERE id=?";

	/** The constant of msi spectra to retreive peaklist */
	private static final String MSI_SPECTRA_PKL = "SELECT spec.* FROM spectrum spec, ms_query msq WHERE spec.id=msq.spectrum_id AND msq.id IN(?) limit 5";

	/** The constant of peaklist software */
	private static final String PKL_SOFTWARE = "SELECT software.name, software.version FROM peaklist pkl, peaklist_software software WHERE pkl.peaklist_software_id=software.id AND pkl.id=?";

	/** Initial parameters */
	// the resultsets id (include decoy result set id) to compute
	private static List<Long> resultSetIds = new ArrayList<>();
	// The ms queries ids
	private static List<Long> msQueriesIds = new ArrayList<>();
	// The ms queries
	private static List<DMsQuery> listMsQueries = new ArrayList<>();
	// The ms queries map
	private static Map<Long, DMsQuery> msqQueryMap = new HashMap<>();
	// the PSMs by ms queries ids
	private static Map<Long, Set<Long>> peptideMatchesByMsQueryIdMap = new HashMap<>();

	/** Initialize parameters */
	private static void initialize() {
		// TODO : Initialize spectra in another level to compute many selected result
		// set
		spectra.initialize();
		msQueriesIds.clear();
		listMsQueries.clear();
		msqQueryMap.clear();
		peptideMatchesByMsQueryIdMap.clear();

	}

	/**
	 * Search peptides by ms query id
	 * 
	 * @param projectId the project id
	 * @param mqId      the ms query id to search
	 * @return a list of peptide
	 * @throws Exception
	 */
	public static ObservableList<DPeptide> getPeptideByMsqId(final Long projectId, Long msqId) throws Exception {
		PreparedStatement peptidesByMsqIdStmt = null;
		ResultSet rs = null;
		ObservableList<DPeptide> peptideList = FXCollections.observableArrayList();
		try {
			System.out.println("INFO | The initial MS Query id=#" + msqQueryMap.get(msqId));
			Set<Long> peptideIdSet = peptideMatchesByMsQueryIdMap.get(msqId);
			if (!peptideIdSet.isEmpty()) {
				for (Long peptideId : peptideIdSet) {
					peptidesByMsqIdStmt = DBAccess.getMsiDBConnection(projectId).prepareStatement(PEPTIDE_QUERY);
					peptidesByMsqIdStmt.setLong(1, peptideId);
					rs = peptidesByMsqIdStmt.executeQuery();
					while (rs.next()) {
						Long pepId = rs.getLong("id");
						String sequence = rs.getString("sequence");
						String ptmAsString = rs.getString("ptm_string");
						Double calcMass = rs.getDouble("calculated_mass");
						if (pepId > 0L) {
							DPeptide pep = new DPeptide(pepId);
							if (!StringsUtils.isEmpty(sequence))
								pep.setM_sequence(sequence);
							if (!StringsUtils.isEmpty(ptmAsString))
								pep.setM_ptm(ptmAsString);
							if (calcMass != null && calcMass > 0D)
								pep.setM_calculatedMass(calcMass);
							peptideList.add(pep);
						}
					}
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(peptidesByMsqIdStmt);
		}
		return peptideList;
	}

	/**
	 * Fetch ms queries data
	 * 
	 * @param projectId   the project id
	 * @param resultSetId the resultSetId
	 * @throws Exception the exception to throw.
	 */
	public static void fetchMSQueriesData(final Long projectId, final Long resultSetId) throws Exception {
		Optional<Connection> msiDbConnectionOpt = Optional.ofNullable(DBAccess.getMsiDBConnection(projectId));
		PreparedStatement msiSearchIdStmt = null;
		ResultSet rs = null;
		Long msiSearchId = -1L;
		try {
			assert projectId > 0L : "Project id must be exists!";
			assert resultSetId > 0L : "Result set id must be exists!";
			assert msiDbConnectionOpt.isPresent() : "Database connection must not be null nor empty!";
			final Connection msiDbConnection = msiDbConnectionOpt.get();
			initialize();
			msiSearchIdStmt = msiDbConnection.prepareStatement(MSI_SEARCH_ID_QUERY);
			msiSearchIdStmt.setLong(1, resultSetId);
			rs = msiSearchIdStmt.executeQuery();
			while (rs.next()) {
				msiSearchId = rs.getLong("msi_search_id");
			}
			// Fetch main data
			if (msiSearchId > 0L) {
				fetchData(msiDbConnection, msiSearchId);
				// Fetch ms queries
				if (!msQueriesIds.isEmpty()) {
					// fetchPklSoftware(msiDbConnection, msQueriesIds);
					fetchMSQueries(msiDbConnection, msQueriesIds);
					// Get decoy result set id
					addResultSetIds(msiDbConnection, resultSetId);
					// fetch PSMs grouped by ms query id
					fetchPSM(msiDbConnection, resultSetIds, msQueriesIds);
				} else {
					logger.warn("No ms queries were found!");
					System.err.println("WARN | No ms queries were found!");
				}
			} else {
				logger.warn("msi_search id does not exist!");
				System.out.println("WARN | msi_search id does not exist!");
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(msiSearchIdStmt);
		}

	}

	/**
	 * Fetch data for a ms_search_id
	 * 
	 * @param projectId   the project id
	 * @param dbName      the database name
	 * @param msiSearchId the msi search id
	 * @throws Exception
	 */
	private static void fetchData(final Connection msiDbConnection, final Long msiSearchId) throws Exception {
		PreparedStatement msQueryStmt = null;
		ResultSet rs = null;
		try {
			System.out.println("INFO | Retrieve data from msi_search_id= #" + msiSearchId);
			logger.info("Retrieve data from msi_search_id= #{}", msiSearchId);
			msQueryStmt = msiDbConnection.prepareStatement(MSI_MSQ_QUERY);
			msQueryStmt.setLong(1, msiSearchId);
			msQueryStmt.setLong(2, msiSearchId);
			rs = msQueryStmt.executeQuery();
			while (rs.next()) {
				Long msqId = rs.getLong("id");
				Integer msqInitialId = rs.getInt("initial_id");
				Integer charge = rs.getInt("charge");
				Double moz = rs.getDouble("moz");
				DMsQuery msQuery = new DMsQuery(-1, msqId, msqInitialId, null);
				Set<Long> peptideMatcheIds = new HashSet<>();
				msQuery.setCharge(charge);
				msQuery.setMoz(moz);
				listMsQueries.add(msQuery);
				msQueriesIds.add(msqId);
				msqQueryMap.put(msqId, msQuery);
				peptideMatchesByMsQueryIdMap.put(msqId, peptideMatcheIds);
			}
			logger.info("Retrieve ms queries has finished. {} ms queries were found.", msQueriesIds.size());
			System.out.println(
					"INFO | Retrieve ms queries has finished." + msQueriesIds.size() + " ms queries were found.");
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(msQueryStmt);
		}
	}

	/***
	 * Retrieve the used peaklist sofwatre
	 * 
	 * @param msiDbConnection
	 * @param msQueriesIdList
	 * @throws Exception
	 */
	private static void fetchPklSoftware(final Connection msiDbConnection, List<Long> msQueriesIdList)
			throws Exception {
		PreparedStatement querySpecStmt = null;
		ResultSet rs = null;
		final List<String> softwareList = new ArrayList<String>();
		try {
			querySpecStmt = msiDbConnection.prepareStatement(MSI_SPECTRA_PKL);
			for (Long msqId : msQueriesIdList) {
				querySpecStmt.setLong(1, msqId);
				rs = querySpecStmt.executeQuery();
				while (rs.next()) {
					Long id = rs.getLong("id");
					getSoftwareName(msiDbConnection, id).ifPresent(sofwatreName -> {
						softwareList.add(sofwatreName);
					});
				}
			}
			if (!softwareList.isEmpty() && softwareList.stream().allMatch(e -> e.equals(softwareList.get(0)))) {
				Session.CURRENT_REGEX_RT = softwareList.get(0);
				logger.warn("Current regex: {}", Session.CURRENT_REGEX_RT);
				System.out.println("INFO | Current regex: " + Session.CURRENT_REGEX_RT);
			}
			{
				logger.warn("Current regex to retrieve spectra are heteregenous. The default value will be used!");
				System.out.println(
						"WARN | Current regex to retrieve spectra are heteregenous. The default value will be used!");
			}

		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(querySpecStmt);
		}
	}

	/***
	 * 
	 * @param msiDbConnection the msi database connection
	 * @param msQueriesIdList the list of ms queries id
	 * @return Boolean <code>true</code> when it's succeeded otherwise
	 *         <code>false</code>
	 * @throws Exception
	 */
	private static boolean fetchMSQueries(final Connection msiDbConnection, List<Long> msQueriesIdList)
			throws Exception {
		PreparedStatement querySpecStmt = null;
		ResultSet rs = null;
		Boolean isSuccess = false;
		try {
			querySpecStmt = msiDbConnection.prepareStatement(MSI_SPECTRA);
			for (Long msqId : msQueriesIdList) {
				querySpecStmt.setLong(1, msqId);
				rs = querySpecStmt.executeQuery();
				while (rs.next()) {
					Long id = rs.getLong("id");
					Integer firstScan = rs.getInt("first_scan");
					Float firstTime = rs.getFloat("first_time");
					Float lastTime = rs.getFloat("last_time");
					byte[] intensityList = rs.getBytes("intensity_list");
					byte[] mozeList = rs.getBytes("moz_list");
					Integer precursorCharge = rs.getInt("precursor_charge");
					Float precursorIntensity = rs.getFloat("precursor_intensity");
					Double precursorMoz = rs.getDouble("precursor_moz");
					String title = rs.getString("title");
					if (id > 0L && (!StringsUtils.isEmpty(title))) {
						Spectrum spectrum = new Spectrum(id, firstScan, firstTime, lastTime, intensityList, mozeList,
								precursorCharge, precursorIntensity, precursorMoz, title);
						// Create the fragments
						for (int i = 0; i < spectrum.getMasses().length; i++) {
							double mz = spectrum.getMasses()[i];
							float intensity = (float) spectrum.getIntensities()[i];
							if (mz >= 0) {
								Fragment fragment = new Fragment(i, mz, intensity);
								spectrum.addFragment(fragment);
							} else {
								logger.error("Invalid fragment! moz must be greater than 0!");
								System.err.println("Error | Invalid fragment! moz must be greater than 0!");
							}
						}
						spectra.addSpectrum(spectrum);
					}
				}
			}
			logger.error("Retrieve spectra from database has finished. {} spectra were found.",
					spectra.getSpectraAsObservable().size());
			System.out.println("INFO | Retrieve spectra from database has finished. "
					+ spectra.getSpectraAsObservable().size() + " spectra were found.");

		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(querySpecStmt);
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * Retrieve peaklist software name
	 * 
	 * @param conenction the SQL connection to the msi_database
	 * @param spectrumId the spectrum id
	 * @return software name
	 * @throws Exception
	 * @throws SQLException
	 */

	private static Optional<String> getSoftwareName(final Connection msiDbconnection, final Long spectrumId)
			throws SQLException, Exception {
		PreparedStatement pklSoftwareStmt = null;
		ResultSet rs = null;
		Optional<String> softwareName = Optional.empty();
		try {
			pklSoftwareStmt = msiDbconnection.prepareStatement(PKL_SOFTWARE);
			pklSoftwareStmt.setLong(1, spectrumId);
			rs = pklSoftwareStmt.executeQuery();
			while (rs.next()) {
				softwareName = Optional.ofNullable(rs.getString("software.name"));
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(pklSoftwareStmt);
		}
		return softwareName;
	}

	/**
	 * Add result set id to the list of Result set ids and its decoy result set id
	 * id
	 * 
	 * @param projectId   the project id
	 * @param resultSetId the result set id to add
	 * @throws Exception
	 */
	private static void addResultSetIds(final Connection msiDbConnection, Long resultSetId) throws Exception {
		PreparedStatement resultSetIdsStmt = null;
		ResultSet rs = null;
		Long id = -1L;
		Long decoyId = -1L;
		try {
			resultSetIdsStmt = msiDbConnection.prepareStatement(RESULTSET_ID);
			resultSetIdsStmt.setLong(1, resultSetId);
			rs = resultSetIdsStmt.executeQuery();
			while (rs.next()) {
				id = rs.getLong("id");
				decoyId = rs.getLong("decoy_result_set_id");
			}
			if (id > 0) {
				resultSetIds.add(id);
			}
			if (decoyId > 0) {
				resultSetIds.add(decoyId);
			}
			logger.info("Retrieve peptide match for the result set with id=#{} and the decoy result set id=#{} ", id,
					decoyId);
			System.out.println("INFO | Retrieve peptide match for the result set with id=# " + id
					+ " and the decoy result set id=# " + decoyId + "");

		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(resultSetIdsStmt);
		}
	}

	/**
	 * Fetch PSMs grouped by ms query id
	 * 
	 * @param projectId   the project id
	 * @param msQueryList the list of ms queries id
	 * @param rsIdList    the list of result set id ( include decoy result set id )
	 * @throws Exception
	 */
	private static void fetchPSM(final Connection msiDbConnection, List<Long> rsIdList, List<Long> msQueryList)
			throws Exception {
		PreparedStatement psmStmt = null;
		ResultSet rs = null;
		try {
			psmStmt = msiDbConnection.prepareStatement(QUERY_PSM);
			for (Long rsId : rsIdList) {
				psmStmt.setLong(1, rsId);
				for (Long msqId : msQueryList) {
					Set<Long> pepIdsSet = peptideMatchesByMsQueryIdMap.get(msqId);
					psmStmt.setLong(2, msqId);
					rs = psmStmt.executeQuery();
					while (rs.next()) {
						Long pepId = rs.getLong("peptide_id");
						pepIdsSet.add(pepId);
					}
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(psmStmt);
		}
	}

	/** The spectra. */
	private static Spectra spectra = new Spectra();

	/**
	 * Find all peakList.
	 *
	 * @throws SQLException the SQL exception
	 */
	public static void allPeakList() throws Exception {
		PreparedStatement allPklistStmt = null;
		ResultSet rs = null;
		try {
			allPklistStmt = DBAccess.getUdsDBConnection().prepareStatement(PEAKLIST);
			rs = allPklistStmt.executeQuery();

		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(allPklistStmt);
		}
	}

	/**
	 * Find uds dataset by project.
	 *
	 * @param projectId the project id
	 * @return the observable list of dataset
	 * @throws Exception
	 */
	public static ObservableList<Dataset> fillDataSetByProject(Long projectId) throws Exception {
		PreparedStatement datasetStmt = null;
		ResultSet rs = null;
		ObservableList<Dataset> list = FXCollections.observableArrayList();
		try {
			datasetStmt = DBAccess.getUdsDBConnection().prepareStatement(UDS_DATASET);
			datasetStmt.setLong(1, projectId);
			rs = datasetStmt.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong("id");
				String description = rs.getString("description");
				int childCount = rs.getInt("child_count");
				int number = rs.getInt("number");
				String name = rs.getString("name");
				String type = rs.getString("type");
				Long resultSummaryId = rs.getLong("result_summary_id");
				Long resultSetId = rs.getLong("result_set_id");
				Long parentDatasetId = rs.getLong("parent_dataset_id");
				Integer projId = rs.getInt("project_id");
				if (id > 0L) {
					Dataset dataset = new Dataset(id, description, childCount, name, number, parentDatasetId,
							resultSetId, resultSummaryId);
					dataset.setType(DatasetType.valueOf(type));
					list.add(dataset);
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(datasetStmt);
		}
		return list;
	}

	/**
	 * Find all dataset by project.
	 *
	 * @param projectId the project id
	 * @return the observable list
	 * @throws SQLException the SQL exception
	 */
	public static ObservableList<Dataset> fillAllDataSetByProject(Long projectId) throws Exception {
		PreparedStatement datasetStmt = null;
		ResultSet rs = null;
		ObservableList<Dataset> list = FXCollections.observableArrayList();
		try {
			datasetStmt = DBAccess.getUdsDBConnection().prepareStatement(ALL_DATASET);
			datasetStmt.setLong(1, projectId);
			rs = datasetStmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				Long id = rs.getLong("id");
				String description = rs.getString("description");
				int childCount = rs.getInt("child_count");
				int number = rs.getInt("number");
				String name = rs.getString("name");
				String type = rs.getString("type");
				Long resultSummaryId = rs.getLong("result_summary_id");
				Long resultSetId = rs.getLong("result_set_id");
				Long parentDatasetId = rs.getLong("parent_dataset_id");
				Integer projId = rs.getInt("project_id");
				if (id > 0L) {
					Dataset dataset = new Dataset(id, description, childCount, name, number, parentDatasetId,
							resultSetId, resultSummaryId);
					dataset.setType(DatasetType.valueOf(type));
					if (parentDatasetId != null && parentDatasetId > 0L) {
						dataset.addChild(list.get(i));
					}
					list.add(dataset);
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(datasetStmt);
		}
		return list;
	}

	/**
	 * Find all msi_search ids by set of rsm ids.
	 *
	 * @param projectId the project id
	 * @param rsmIds    the set of rsms id
	 * @return the sets the
	 * @throws Exception the exception to throw
	 */
	public static Set<Long> fillMsiSerachIds(final Long projectId, Set<Long> rsmIds) throws Exception {
		PreparedStatement peakListStmt = null;
		ResultSet rs = null;
		Set<Long> msiIds = new HashSet<Long>();
		spectra.initialize();
		try {
			for (Long rsmId : rsmIds) {
				System.out.println("INFO | Retrieve msi_search ids from rsmId= #" + rsmId);
				logger.info("Retrieve msi_search ids from rsmId=#{}", rsmId);
				peakListStmt = DBAccess.getMsiDBConnection(projectId).prepareStatement(VALIDATED_MSI_SEARCH_IDS);
				peakListStmt.setLong(1, rsmId);
				rs = peakListStmt.executeQuery();
				while (rs.next()) {
					Long id = rs.getLong("msi_search_id");
					msiIds.add(id);
				}
				logger.info("Retrieve msi_search ids has finished. {} msi_search were found", msiIds.size());
				System.out.println(
						"INFO | Retrieve msi_search ids has finished. " + msiIds.size() + " msi_search were found.");
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(peakListStmt);
		}
		return msiIds;
	}

	/**
	 * Find all spectra set by project.
	 *
	 * @param projectId the project id
	 * @param msiIds    the set of msi_id
	 * @throws @throws Exception the exception to throw
	 */
	public static void fillSpecByPeakList(final Long projectId, Set<Long> msiIds) throws Exception {
		PreparedStatement peakListStmt = null;
		ResultSet rs = null;
		try {
			for (Long msiSearchId : msiIds) {
				peakListStmt = DBAccess.getMsiDBConnection(projectId).prepareStatement(SPECTRA_BY_MSI_SEARCH);
				System.out.println("INFO | Load spectra from msi_search= #" + msiSearchId);
				logger.info("Load spectra from msi_search=#{}", msiSearchId);
				peakListStmt.setLong(1, msiSearchId);
				rs = peakListStmt.executeQuery();
				while (rs.next()) {
					Long id = rs.getLong("id");
					Integer firstScan = rs.getInt("first_scan");
					Float firstTime = rs.getFloat("first_time");
					Float lastTime = rs.getFloat("last_time");
					byte[] intensityList = rs.getBytes("intensity_list");
					byte[] mozeList = rs.getBytes("moz_list");
					Integer precursorCharge = rs.getInt("precursor_charge");
					Float precursorIntensity = rs.getFloat("precursor_intensity");
					Double precursorMoz = rs.getDouble("precursor_moz");
					String title = rs.getString("title");
					// Retrieve the used peak list software to determine the
					// parsing
					// rules
					String pklSoftwareName = rs.getString("pkl_software");
					if (id > 0L && (!StringsUtils.isEmpty(title))) {
						Spectrum spectrum = new Spectrum(id, firstScan, firstTime, lastTime, intensityList, mozeList,
								precursorCharge, precursorIntensity, precursorMoz, title);
						// Create fragment
						for (int i = 0; i < spectrum.getMasses().length; i++) {
							double mz = spectrum.getMasses()[i];
							float intensity = (float) spectrum.getIntensities()[i];
							if (mz > 0) {
								Fragment fragment = new Fragment(i, mz, intensity);
								spectrum.addFragment(fragment);
							} else {
								logger.error("Invalid fragment! moz must be greater than 0!");
								System.err.println("ERROR | Invalid fragment! moz must be greater than 0!");
							}
						}
						// Update the current regex
						Session.CURRENT_REGEX_RT = pklSoftwareName;
						spectrum.setRetentionTimeFromTitle();
						spectra.addSpectrum(spectrum);
					}

				}
				logger.info("Retrieve spectra has finished. {} spectra were found",
						spectra.getSpectraAsObservable().size());
				System.out.println("INFO | Retrieve spectra has finished. " + spectra.getSpectraAsObservable().size()
						+ " spectra were found.");
			}

		} finally

		{
			tryToCloseResultSet(rs);
			tryToCloseStatement(peakListStmt);
		}
	}

	/**
	 * Find a project by name.
	 *
	 * @param name the name of the project
	 * @throws Exception the exception to throw
	 */
	public static void findProject(String name) throws Exception {
		PreparedStatement findProjectStmt = null;
		ResultSet rs = null;
		try {
			assert DBAccess.getUdsDBConnection() != null : "Can't connect to uds_db!";
			findProjectStmt = DBAccess.getUdsDBConnection().prepareStatement(PROJECT);
			findProjectStmt.setString(1, name);
			rs = findProjectStmt.executeQuery();
			assert !rs.next() : "Project not found! Make sure that you have entered the right project name.";
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findProjectStmt);
		}
	}

	/**
	 * Find an user by login.
	 *
	 * @param login the login
	 * @throws Exception the exception to throw
	 */
	public static void findUser(String login) throws Exception {
		PreparedStatement findUserStmt = null;
		ResultSet rs = null;
		String userLogin = null;
		try {
			assert DBAccess.getUdsDBConnection() != null : "Can't connect to uds_db!";
			findUserStmt = DBAccess.getUdsDBConnection().prepareStatement(USER);
			findUserStmt.setString(1, login);
			rs = findUserStmt.executeQuery();
			while (rs.next()) {
				userLogin = rs.getString("login");
			}
			if (userLogin == null)
				throw new Exception("ERROR | The user does not exist! Make sure that you have a Proline account.");
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findUserStmt);
		}
	}

	/**
	 * Find an user by login.
	 *
	 * @param login the login
	 * @return the observable list
	 * @throws Exception the exception to throw
	 */
	public static ObservableList<Project> findProjects(String login) throws Exception {
		PreparedStatement findUserStmt = null;
		ResultSet rs = null;
		ObservableList<Project> projectList = FXCollections.observableArrayList();
		try {
			assert DBAccess.getUdsDBConnection() != null : "Can't connect to uds_db!";
			findUserStmt = DBAccess.getUdsDBConnection().prepareStatement(PROJECTS_BY_OWNER);
			findUserStmt.setString(1, login);
			rs = findUserStmt.executeQuery();
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				String desc = rs.getString("description");
				if (id > 0L && !StringsUtils.isEmpty(name)) {
					Project proj = new Project(id, name, desc);
					projectList.add(proj);
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findUserStmt);
		}
		return projectList;
	}

	/**
	 * Gets the spectra.
	 *
	 * @return the spectra
	 */
	public static Spectra getSpectra() {
		return spectra;
	}

	/**
	 * Sets the spectra.
	 *
	 * @param spectra the spectra to set
	 */
	public static void setSpectra(Spectra spectra) {
		DBSpectraHandler.spectra = spectra;
	}

	/**
	 * Close resultset.
	 *
	 * @param rs the resultset to close
	 * 
	 */
	private static void tryToCloseResultSet(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e) {
			System.err.println("ERROR | Error while trying to close the resultset " + e);
			logger.error("Error while trying to close the resultset {}", e);
		}
	}

	/**
	 * Close statement.
	 *
	 * @param stmt the statement to close
	 */

	private static void tryToCloseStatement(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (Exception e) {
			System.err.println("ERROR | Error while trying to close the statement " + e);
			logger.error("Error while trying to close the statement {}", e);
		}
	}

}
