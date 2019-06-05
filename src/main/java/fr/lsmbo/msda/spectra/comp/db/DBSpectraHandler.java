package fr.lsmbo.msda.spectra.comp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.lsmbo.msda.spectra.comp.list.Spectra;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * Handle spectra from database
 * 
 * @author Aromdhani
 *
 */

public class DBSpectraHandler {

	private static final String USER = "SELECT * FROM user_account WHERE login=?";
	private static final String PROJECT = "SELECT * FROM external_db WHERE name=?";
	private static final String PEAKLIST = "SELECT * FROM peaklist";
	private static final String SPECTRA_BY_PEAKLIST = "SELECT spec.* FROM peaklist pkl,spectrum spec WHERE spec.peaklist_id=pkl.id AND pkl.path=?";
	private static Spectra spectra = new Spectra();

	/**
	 * Find all data set by project
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
			peakListStmt = DBAccess.openMsiDBConnection(msiName).prepareStatement(SPECTRA_BY_PEAKLIST);
			System.out.println("--- Start to retrieve spectra from '" + msiName + "' whith the peaklist path:'" + path
					+ "'. Please wait ...");
			peakListStmt.setString(1, path);
			rs = peakListStmt.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong("id");
				String title = rs.getString("title");
				Integer precursorCharge = rs.getInt("precursor_charge");
				Float precursorIntensity = rs.getFloat("precursor_intensity");
				Double precursorMoz = rs.getDouble("precursor_moz");
				Integer peakCount = rs.getInt("peak_count");
				if ((!StringsUtils.isEmpty(title))) {
					Spectrum spectrum = new Spectrum(id, title, precursorCharge, precursorMoz, precursorIntensity,
							peakCount);
					spectrum.setRetentionTimeFromTitle();
					spectra.addSpectrum(spectrum);
				}
			}
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(peakListStmt);
		}
	}

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
	 * Find a project by name
	 * 
	 * @param login
	 * @throws SQLException
	 */
	public static void findProject(String name) throws SQLException {
		PreparedStatement findProjectStmt = null;
		ResultSet rs = null;
		try {
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
	 * @throws SQLException
	 */
	public static void findUser(String login) throws SQLException {
		PreparedStatement findUserStmt = null;
		ResultSet rs = null;
		try {
			findUserStmt = DBAccess.openUdsDBConnection().prepareStatement(USER);
			findUserStmt.setString(1, login);
			rs = findUserStmt.executeQuery();
			assert !rs.next() : "The user does not exist! Make sure that you have a Proline account.";
		} finally {
			tryToCloseResultSet(rs);
			tryToCloseStatement(findUserStmt);
		}
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
