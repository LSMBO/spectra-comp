package fr.lsmbo.msda.spectra.comp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.list.Spectra;
import fr.lsmbo.msda.spectra.comp.model.Dataset;
import fr.lsmbo.msda.spectra.comp.model.Fragment;
import fr.lsmbo.msda.spectra.comp.model.Project;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handle spectra from database
 * 
 * @author Aromdhani
 *
 */

public class DBSpectraHandler {

	private static final String USER = "SELECT login FROM user_account WHERE login=?";
	private static final String PROJECT = "SELECT * FROM external_db WHERE name=?";
	private static final String PEAKLIST = "SELECT * FROM peaklist";
	private static final String SPECTRA_BY_PEAKLIST = "SELECT spec.*,pklsof.name as pkl_software FROM peaklist pkl,peaklist_software pklsof,spectrum spec WHERE spec.peaklist_id=pkl.id AND pklsof.id=pkl.peaklist_software_id AND pkl.path=?";
	private static final String PROJECTS_BY_OWNER = "SELECT project.id as id ,project.name as name,project.description as description FROM user_account ,project WHERE user_account.id=project.owner_id AND user_account.login=?";
	private static final String DATASET = "SELECT ds.* ,proj.name FROM data_set ds,project proj WHERE  proj.id=ds.project_id AND ds.type IN ('AGGREGATE','IDENTIFICATION') AND ds.result_summary_id>0 AND ds.project_id=? order by ds.result_summary_id;";
	private static Spectra spectra = new Spectra();

	/**
	 * Find PeakList
	 * 
	 * @throws SQLException
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
	 * Find all dataset by project
	 * 
	 * @param path
	 *            the peaklist path
	 * @throws SQLException
	 */
	public static ObservableList<Dataset> fillDataSetByProject(Long projectId) throws SQLException {
		PreparedStatement datasetStmt = null;
		ResultSet rs = null;
		ObservableList<Dataset> list = FXCollections.observableArrayList();
		try {
			datasetStmt = DBAccess.openUdsDBConnection().prepareStatement(DATASET);
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
	 * Find all spectra set by project
	 * 
	 * @param path
	 *            the peaklist path
	 * @throws SQLException
	 */
	public static void fillSpecByPeakList(String msiName, String path) throws SQLException {
		PreparedStatement peakListStmt = null;
		ResultSet rs = null;
		try {
			spectra.initialize();
			peakListStmt = DBAccess.openFirstMsiDBConnection(msiName).prepareStatement(SPECTRA_BY_PEAKLIST);
			System.out.println("INFO | Load spectra from '" + msiName + "' whith the peaklist path: '" + path + "'.");
			peakListStmt.setString(1, path);
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
				// Retrieve the used peak list software to determine the parsing
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
							System.out.println("Invalid fragment! moz must be greater than 0!");
						}
					}
					// Update the current regex
					Session.CURRENT_REGEX_RT = pklSoftwareName;
					spectrum.setRetentionTimeFromTitle();
					spectra.addSpectrum(spectrum);
				}

			}
			System.out.println("--- Retrieve spectra has finished. " + spectra.getSpectraAsObservable().size()
					+ " spectra found.");
		} finally

		{
			tryToCloseResultSet(rs);
			tryToCloseStatement(peakListStmt);
		}
	}

	/**
	 * Find a project by name
	 * 
	 * @param login
	 * @throws SQLException
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
	 * Find an user by login
	 * 
	 * @param login
	 * @throws Exception
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
				throw new Exception("The user does not exist! Make sure that you have a Proline account.");
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findUserStmt);
		}
	}

	/**
	 * Find an user by login
	 * 
	 * @param login
	 * @throws Exception
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
					System.out.println(proj.toString());
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findUserStmt);
		}
		return list;
	}

	/**
	 * @return the spectra
	 */
	public static Spectra getSpectra() {
		return spectra;
	}

	/**
	 * @param spectra
	 *            the spectra to set
	 */
	public static void setSpectra(Spectra spectra) {
		DBSpectraHandler.spectra = spectra;
	}

	/**
	 * Close statement
	 * 
	 * @param stmt
	 *            the statement to close
	 */
	private static void tryToCloseResultSet(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e) {
			System.out.println("Error while trying to close rs: " + e);
			// logger.error("Error while trying to close Statement", e)
		}
	}

	/**
	 * Close statement
	 * 
	 * @param stmt
	 *            the statement to close
	 */
	private static void tryToCloseStatement(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (Exception e) {
			System.out.println("Error while trying to close Statement" + e);
			// logger.error("Error while trying to close Statement", e)
		}
	}

}
