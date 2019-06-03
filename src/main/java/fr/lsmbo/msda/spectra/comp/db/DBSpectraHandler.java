package fr.lsmbo.msda.spectra.comp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.lsmbo.msda.spectra.comp.list.Spectra;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;

/**
 * Handle spectra from database
 * 
 * @author Aromdhani
 *
 */

public class DBSpectraHandler {

	private static final String FIND_USER = "SELECT * FROM user_account WHERE login=?";
	private static final String FIND_PROECT = "SELECT * FROM external_db WHERE name=?";
	private static final String FIND_PEAKLIST = "SELECT * FROM peaklist";
	private static final String FIND_SPECTRA_BY_PEAKLIST = "SELECT spec.* FROM peaklist pkl," + "spectrum spec WHERE "
			+ "spec.peaklist_id=pkl.id " + " AND pkl.path=?";
	private static Spectra spectra = new Spectra();

	/**
	 * Find all data set by project
	 * 
	 * @param path the peaklist path
	 * @throws SQLException
	 */
	public static void fill(String msiName, String path) throws SQLException {
		PreparedStatement peakListStmt = DBAccess.createMsiDBConnection(msiName)
				.prepareStatement(FIND_SPECTRA_BY_PEAKLIST);
		try {
			spectra.initialize();
			ResultSet rs = peakListStmt.executeQuery();
			while (rs.next()) {
				int initialId = rs.getInt("initial_id");
				Integer precursorCharge = rs.getInt("precursor_charge");
				Float precursorIntensity = rs.getFloat("precursor_intensity");
				Double precursorMoz = rs.getDouble("precursor_moz");
				if (precursorMoz != null && precursorMoz > 0 && precursorIntensity != null && precursorIntensity > 0) {
					Spectrum spectrum = new Spectrum(initialId, precursorCharge, precursorMoz, precursorIntensity);
					spectra.addSpectrum(spectrum);
				}
			}
		} finally {
			tryToCloseStatement(peakListStmt);
		}
	}

	/**
	 * Find PeakList
	 * 
	 * @throws SQLException
	 */
	public static void findPeakList() throws SQLException {
		PreparedStatement allDatasetsStmt = DBAccess.createUdsDBConnection().prepareStatement(FIND_PEAKLIST);
		try {
			ResultSet rs = allDatasetsStmt.executeQuery();

		} finally {
			tryToCloseStatement(allDatasetsStmt);
		}
	}

	/**
	 * Find a project by name
	 * 
	 * @param login
	 * @throws SQLException
	 */
	public static void findProject(String name) throws SQLException {
		PreparedStatement findProjectStmt = DBAccess.createUdsDBConnection().prepareStatement(FIND_PROECT);
		try {
			findProjectStmt.setString(1, name);
			ResultSet rs = findProjectStmt.executeQuery();
			assert !rs.next() : "Project not found! Make sure that you have entered the right project name.";
		} finally {
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
		PreparedStatement findUserStmt = DBAccess.createUdsDBConnection().prepareStatement(FIND_USER);
		try {
			findUserStmt.setString(1, login);
			ResultSet rs = findUserStmt.executeQuery();
			assert !rs.next() : "The user does not exist! Make sure that you have a Proline account.";
		} finally {
			tryToCloseStatement(findUserStmt);
		}
	}

	/**
	 * Close statement
	 * 
	 * @param stmt the statement to close
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
