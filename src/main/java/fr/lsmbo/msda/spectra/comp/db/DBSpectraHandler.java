package fr.lsmbo.msda.spectra.comp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

// TODO: Auto-generated Javadoc
/**
 * Handle spectra from databases.
 */

public class DBSpectraHandler {

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

	/** Initial parameters */
	private static List<Long> resultSetIds = new ArrayList<>();
	private static List<Long> msQueriesIds = new ArrayList<>();
	private static List<DMsQuery> listMsQueries = new ArrayList<>();
	private static Map<Long, DMsQuery> msqQueryMap = new HashMap<>();
	private static Map<Long, Set<Long>> peptideMatchesByMsQueryIdMap = new HashMap<>();

	/**
	 * Fetch PSMs grouped by ms query id
	 * 
	 * @param dbName
	 *            the database name
	 * @param msQueryList
	 *            the list of ms queries id
	 * @param rsIdList
	 *            the list of result set id /decoy result set id
	 * @throws Exception
	 */
	private static void fetchPSM(String dbName, List<Long> rsIdList, List<Long> msQueryList) throws Exception {
		PreparedStatement psmStmt = null;
		ResultSet rs = null;
		try {
			psmStmt = DBAccess.openFirstMsiDBConnection(dbName).prepareStatement(QUERY_PSM);
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

	/**
	 * Search peptides by ms query id
	 * 
	 * @param dbName
	 *            the database name
	 * @param mqId
	 *            the ms query id to search
	 * @return a list of peptide
	 * @throws SQLException
	 */
	public static List<DPeptide> getPeptideByMsqId(String dbName, Long msqId) throws SQLException {
		PreparedStatement peptidesByMsqIdStmt = null;
		ResultSet rs = null;
		List<DPeptide> peptideList = new ArrayList<>();
		try {
			peptidesByMsqIdStmt = DBAccess.openFirstMsiDBConnection(dbName).prepareStatement(PEPTIDE_QUERY);
			peptidesByMsqIdStmt.setLong(2, msqId);
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
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(peptidesByMsqIdStmt);
		}
		return peptideList;
	}

	/**
	 * Return the msi_search_id
	 * 
	 * @param resultSetId
	 *            the resultSetId
	 * @return msi_search_id
	 * @throws SQLException
	 */
	public static void fetchMSQueriesData(final String dbName, final Long resultSetId) throws Exception {
		PreparedStatement msiSearchIdStmt = null;
		ResultSet rs = null;
		Long msiSearchId = -1L;
		try {
			assert !StringsUtils.isEmpty(dbName) : "Database name must not be null nor empty!";
			assert resultSetId > 0L : "Result set id must not be null nor empty!";
			initialize();
			msiSearchIdStmt = DBAccess.openFirstMsiDBConnection(dbName).prepareStatement(MSI_SEARCH_ID_QUERY);
			msiSearchIdStmt.setLong(1, resultSetId);
			rs = msiSearchIdStmt.executeQuery();
			while (rs.next()) {
				msiSearchId = rs.getLong("msi_search_id");
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(msiSearchIdStmt);
		}
		// Fetch main data
		assert (msiSearchId > 0L) : "msi_search id must not be empty nor null!";
		fetchData(dbName, msiSearchId);
		// Fetch ms queries
		if (!msQueriesIds.isEmpty() && fetchMSQueries(dbName, msQueriesIds)) {
			// Get decoy result set id
			addResultSetIds(dbName, resultSetId);
			// fetch PSMs grouped by ms query id
			fetchPSM(dbName, resultSetIds, msQueriesIds);
		} else {
			System.err.println("WARN | No ms queries were found!");
		}
	}

	/**
	 * Add result set id to the list of Result set ids and its decoy result set
	 * id id
	 * 
	 * @param resultSetId
	 *            the result set id to add
	 * @throws SQLException
	 */
	private static void addResultSetIds(String dbName, Long resultSetId) throws Exception {
		PreparedStatement resultSetIdsStmt = null;
		ResultSet rs = null;
		Long id = -1L;
		Long decoyId = -1L;
		try {
			resultSetIdsStmt = DBAccess.openFirstMsiDBConnection(dbName).prepareStatement(RESULTSET_ID);
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
			System.out.println("INFO | Retrieve peptide match for the result set with id=# " + id
					+ " and the decoy result set id=# " + decoyId + "");
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(resultSetIdsStmt);
		}
	}

	/**
	 * Fetch data for a ms_search_id
	 * 
	 * @param dbName
	 *            the database name
	 * @param msiSearchId
	 *            the msi search id
	 * @throws SQLException
	 */
	private static void fetchData(final String dbName, final Long msiSearchId) throws SQLException {
		PreparedStatement msQueryStmt = null;
		ResultSet rs = null;
		try {
			System.out.println("INFO | Retrieve data from msi_search_id= #" + msiSearchId);
			msQueryStmt = DBAccess.openFirstMsiDBConnection(dbName).prepareStatement(MSI_MSQ_QUERY);
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
			System.out.println(
					"INFO | Retrieve ms queries has finished." + msQueriesIds.size() + " ms queries were found.");
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(msQueryStmt);
		}
	}

	/**
	 * Fetch Ms Queries data
	 * 
	 * @throws SQLException
	 */
	private static boolean fetchMSQueries(final String dbName, List<Long> msQueriesIdList) throws SQLException {
		PreparedStatement querySpecStmt = null;
		ResultSet rs = null;
		Boolean isSuccess = false;
		try {
			querySpecStmt = DBAccess.openFirstMsiDBConnection(dbName).prepareStatement(MSI_SPECTRA);
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
								System.err.println("INFO | Invalid fragment! moz must be greater than 0!");
							}
						}
						// Update the current regex
						spectra.addSpectrum(spectrum);
					}
				}
			}
			System.out.println("INFO | Retrieve spectra from database has finished. "
					+ spectra.getSpectraAsObservable().size() + " spectra were found.");

		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(querySpecStmt);
			isSuccess = true;
		}
		return isSuccess;
	}

	/** Initialize parameters */
	private static void initialize() {
		msQueriesIds.clear();
		listMsQueries.clear();
		msqQueryMap.clear();
		peptideMatchesByMsQueryIdMap.clear();
	}

	//
	/** The spectra. */
	private static Spectra spectra = new Spectra();

	/**
	 * Find PeakList.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static void allPeakList() throws SQLException {
		PreparedStatement allPklistStmt = null;
		ResultSet rs = null;
		try {
			allPklistStmt = DBAccess.openUdsDBConnection().prepareStatement(PEAKLIST);
			rs = allPklistStmt.executeQuery();

		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(allPklistStmt);
		}
	}

	/**
	 * Find uds dataset by project.
	 *
	 * @param projectId
	 *            the project id
	 * @return the observable list
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static ObservableList<Dataset> fillDataSetByProject(Long projectId) throws SQLException {
		PreparedStatement datasetStmt = null;
		ResultSet rs = null;
		ObservableList<Dataset> list = FXCollections.observableArrayList();
		try {
			datasetStmt = DBAccess.openUdsDBConnection().prepareStatement(UDS_DATASET);
			System.out.println("INFO | Load datasets from project=" + projectId + ".");
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
	 * @param projectId
	 *            the project id
	 * @return the observable list
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static ObservableList<Dataset> fillAllDataSetByProject(Long projectId) throws SQLException {
		PreparedStatement datasetStmt = null;
		ResultSet rs = null;
		ObservableList<Dataset> list = FXCollections.observableArrayList();
		try {
			datasetStmt = DBAccess.openUdsDBConnection().prepareStatement(ALL_DATASET);
			System.out.println("INFO | Load datasets from project=" + projectId + ".");
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
	 * Find all msi_search _ids by set of rsm ids.
	 *
	 * @param msiName
	 *            the database name
	 * @param rsmIds
	 *            the rsm ids
	 * @return the sets the
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static Set<Long> fillMsiSerachIds(String msiName, Set<Long> rsmIds) throws SQLException {
		PreparedStatement peakListStmt = null;
		ResultSet rs = null;
		Set<Long> msiIds = new HashSet<Long>();
		spectra.initialize();
		try {
			for (Long rsmId : rsmIds) {
				peakListStmt = DBAccess.openFirstMsiDBConnection(msiName).prepareStatement(VALIDATED_MSI_SEARCH_IDS);
				System.out.println("INFO | Retrieve msi_search ids from rsmId= #'" + rsmId + "'.");
				peakListStmt.setLong(1, rsmId);
				rs = peakListStmt.executeQuery();
				while (rs.next()) {
					Long id = rs.getLong("msi_search_id");
					msiIds.add(id);
				}
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
	 * @param msiName
	 *            the msi name
	 * @param msiIds
	 *            the msi ids
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static void fillSpecByPeakList(String msiName, Set<Long> msiIds) throws SQLException {
		PreparedStatement peakListStmt = null;
		ResultSet rs = null;
		try {
			for (Long msiSearchId : msiIds) {
				peakListStmt = DBAccess.openFirstMsiDBConnection(msiName).prepareStatement(SPECTRA_BY_MSI_SEARCH);
				System.out.println("INFO | Load spectra from msi_search= #'" + msiSearchId + "'.");
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
								System.err.println("INFO | Invalid fragment! moz must be greater than 0!");
							}
						}
						// Update the current regex
						Session.CURRENT_REGEX_RT = pklSoftwareName;
						spectrum.setRetentionTimeFromTitle();
						spectra.addSpectrum(spectrum);
					}

				}
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
	 * @param name
	 *            the name
	 * @throws Exception
	 *             the exception
	 */
	public static void findProject(String name) throws Exception {
		PreparedStatement findProjectStmt = null;
		ResultSet rs = null;
		try {
			assert DBAccess.openUdsDBConnection() != null : "Can't connect to uds_db!";
			findProjectStmt = DBAccess.openUdsDBConnection().prepareStatement(PROJECT);
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
	 * @param login
	 *            the login
	 * @throws Exception
	 *             the exception
	 */
	public static void findUser(String login) throws Exception {
		PreparedStatement findUserStmt = null;
		ResultSet rs = null;
		String userLogin = null;
		try {
			assert DBAccess.openUdsDBConnection() != null : "Can't connect to uds_db!";
			findUserStmt = DBAccess.openUdsDBConnection().prepareStatement(USER);
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
	 * @param login
	 *            the login
	 * @return the observable list
	 * @throws Exception
	 *             the exception
	 */
	public static ObservableList<Project> findProjects(String login) throws Exception {
		PreparedStatement findUserStmt = null;
		ResultSet rs = null;
		ObservableList<Project> list = FXCollections.observableArrayList();
		try {
			assert DBAccess.openUdsDBConnection() != null : "Can't connect to uds_db!";
			findUserStmt = DBAccess.openUdsDBConnection().prepareStatement(PROJECTS_BY_OWNER);
			findUserStmt.setString(1, login);
			rs = findUserStmt.executeQuery();
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				String desc = rs.getString("description");
				if (id > 0L && !StringsUtils.isEmpty(name)) {
					Project proj = new Project(id, name, desc);
					list.add(proj);
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findUserStmt);
		}
		return list;
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
	 * @param spectra
	 *            the spectra to set
	 */
	public static void setSpectra(Spectra spectra) {
		DBSpectraHandler.spectra = spectra;
	}

	/**
	 * Close statement.
	 *
	 * @param rs
	 *            the resultset to close
	 * 
	 */
	private static void tryToCloseResultSet(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e) {
			System.err.println("ERROR | Error while trying to close the resultset " + e);
		}
	}

	/**
	 * Close statement.
	 *
	 * @param stmt
	 *            the statement to close
	 */

	private static void tryToCloseStatement(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (Exception e) {
			System.err.println("ERROR | Error while trying to close the statement " + e);
		}
	}

}
