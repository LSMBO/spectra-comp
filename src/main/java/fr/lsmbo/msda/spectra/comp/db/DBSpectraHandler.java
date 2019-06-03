package fr.lsmbo.msda.spectra.comp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

	public static void fillSpec(String dataSet) throws SQLException {
		PreparedStatement findProjectStmt = DBAccess.createUdsDBConnection()
				.prepareStatement("SELECT * FROM external_db WHERE name=?");
		try {
			findProjectStmt.setString(1, dataSet);
			ResultSet rs = findProjectStmt.executeQuery();
			assert !rs.next() : "Project not found! Make sure that you have entered the right project name.";
		} finally {
			tryToCloseStatement(findProjectStmt);
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
			assert !rs.next() : "Peaklists are empty! Make sure that you have seleced the right Proline project.";
		} finally {
			tryToCloseStatement(allDatasetsStmt);
		}
	}

	/**
	 * Find all data set by project
	 * 
	 * @param path the peaklist path
	 * @throws SQLException
	 */
	public static void findPeakListByName(String msiName, String path) throws SQLException {
		PreparedStatement allDatasetsStmt = DBAccess.createMsiDBConnection(msiName)
				.prepareStatement(FIND_SPECTRA_BY_PEAKLIST);
		try {
			ResultSet rs = allDatasetsStmt.executeQuery();
			assert !rs.next() : "Peaklists are empty! Make sure that you have seleced the right Proline project.";
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
