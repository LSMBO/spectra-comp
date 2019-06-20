package fr.lsmbo.msda.spectra.comp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.list.Spectra;
import fr.lsmbo.msda.spectra.comp.model.Dataset;
import fr.lsmbo.msda.spectra.comp.model.Fragment;
import fr.lsmbo.msda.spectra.comp.model.Project;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// TODO: Auto-generated Javadoc
/**
 * Handle spectra from database.
 */

public class DBSpectraHandler {

	/** The Constant USER. */
	private static final String USER = "SELECT login FROM user_account WHERE login=?";
	
	/** The Constant PROJECT. */
	private static final String PROJECT = "SELECT * FROM external_db WHERE name=?";
	
	/** The Constant PEAKLIST. */
	private static final String PEAKLIST = "SELECT * FROM peaklist";
	
	/** The Constant SPECTRA_BY_PEAKLIST. */
	private static final String SPECTRA_BY_PEAKLIST = "SELECT spec.*,pklsof.name as pkl_software FROM peaklist pkl,peaklist_software pklsof,spectrum spec WHERE spec.peaklist_id=pkl.id AND pklsof.id=pkl.peaklist_software_id AND pkl.path=?";
	
	/** The Constant PROJECTS_BY_OWNER. */
	private static final String PROJECTS_BY_OWNER = "SELECT project.id as id ,project.name as name,project.description as description FROM user_account ,project WHERE user_account.id=project.owner_id AND user_account.login=?";
	
	/** The Constant UDS_DATASET. */
	private static final String UDS_DATASET = "SELECT DISTINCT(ds.result_summary_id) AS rsm_id,ds.* ,proj.name proj_name FROM data_set ds,project proj WHERE  proj.id=ds.project_id AND ds.type IN ('AGGREGATE','IDENTIFICATION') AND ds.result_summary_id>0 AND ds.project_id=? order by ds.result_summary_id";
	
	/** The Constant PKL_BY_RSM. */
	private static final String PKL_BY_RSM = "SELECT distinct(pkl.*) FROM result_summary rsm, result_set rs,msi_search msi,peaklist pkl WHERE rsm.result_set_id=rs.id AND rs.msi_search_id=msi.id AND msi.peaklist_id= pkl.id AND rsm.id=? ";
	
	/** The Constant VALIDATED_MSI_SEARCH_IDS. */
	private static final String VALIDATED_MSI_SEARCH_IDS = "SELECT distinct(msi.id) as msi_search_id FROM result_summary rsm, result_set rs, msi_search msi, peaklist pkl, protein_set ps WHERE rsm.result_set_id=rs.id AND rs.msi_search_id=msi.id AND msi.peaklist_id= pkl.id AND ps.result_summary_id=rsm.id AND ps.is_validated=true AND rs.type IN('SEARCH','USER') AND rsm.id=?";
	
	/** The Constant SPECTRA_BY_MSI_SEARCH. */
	private static final String SPECTRA_BY_MSI_SEARCH = "SELECT spec.*,pklsof.name as pkl_software FROM msi_search msi,peaklist pkl,peaklist_software pklsof,spectrum spec WHERE spec.peaklist_id=pkl.id AND  pkl.id=msi.peaklist_id AND pkl.peaklist_software_id=pklsof.id AND msi.id=?";
	
	/** The spectra. */
	private static Spectra spectra = new Spectra();

	/**
	 * Find PeakList.
	 *
	 * @throws SQLException the SQL exception
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
	 * Find all dataset by project.
	 *
	 * @param projectId the project id
	 * @return the observable list
	 * @throws SQLException the SQL exception
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
	 * @param msiName            the database name
	 * @param rsmIds            the rsm ids
	 * @return the sets the
	 * @throws SQLException the SQL exception
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
	 * @param msiName the msi name
	 * @param msiIds the msi ids
	 * @throws SQLException the SQL exception
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
					// rule
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
	 * @param name the name
	 * @throws Exception the exception
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
	 * @param login the login
	 * @throws Exception the exception
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
	 * @param login the login
	 * @return the observable list
	 * @throws Exception the exception
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
	 * @param spectra            the spectra to set
	 */
	public static void setSpectra(Spectra spectra) {
		DBSpectraHandler.spectra = spectra;
	}

	/**
	 * Close statement.
	 *
	 * @param rs the rs
	 */
	private static void tryToCloseResultSet(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e) {
			System.out.println("ERROR | Error while trying to close rs " + e);
			// logger.error("Error while trying to close Statement", e)
		}
	}

	/**
	 * Close statement.
	 *
	 * @param stmt            the statement to close
	 */
	private static void tryToCloseStatement(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (Exception e) {
			System.err.println("ERROR | Error while trying to close Statement " + e);
			// logger.error("Error while trying to close Statement", e)
		}
	}

}
