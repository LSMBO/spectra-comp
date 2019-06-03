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

	private static final String DATASETS_BY_USER_AND_PROJECT = "SELECT ds.name,pr.name,ow.id FROM data_set ds,"
			+ "project pr, " + "user_account ow " + "WHERE " + "ds.TYPE='IDENTIFICATION' AND " + "ds.project_id=pr.id "
			+ "AND pr.owner_id=ow.id " + "AND pr.name=? AND ow.id=?";
	private static final String ALL_USERS = "SELECT login FROM user_account";
	private static final String FIND_USER = "SELECT * FROM user_account WHERE login=?";
	private static final String FIND_PROECT = "SELECT * FROM external_db WHERE name=?";
	private static final String FIND_SPECTRA_BY_PEAKLIST = "SELECT spec.* FROM peaklist pkl," + "spectrum spec WHERE "
			+ "spec.peaklist_id=pkl.id " + " AND pkl.path=?";

	/**
	 * List all users
	 * 
	 * @throws SQLException
	 */
	public static void findAllUser() throws SQLException {
		PreparedStatement allUserStmt = DBAccess.createUdsDBConnection().prepareStatement(ALL_USERS);
		try {
			ResultSet rs = allUserStmt.executeQuery();
			assert !rs.next() : "User list is empty! Make sure that you have already created a Proline account.";
		} finally {
			tryToCloseStatement(allUserStmt);
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
			assert !rs.next() : "Dataset owner not found! Make sure that you have an account.";
		} finally {
			tryToCloseStatement(findUserStmt);
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
	 * Find all data set by project
	 * 
	 * @param path
	 *            the peaklist path
	 * @throws SQLException
	 */
	public static void findPeakListByUserAndroject(String path) throws SQLException {
		PreparedStatement allDatasetsStmt = DBAccess.createUdsDBConnection()
				.prepareStatement(FIND_SPECTRA_BY_PEAKLIST);
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
	 * @param projectName
	 *            the project name
	 * @throws SQLException
	 */
	public static void findDataSetByUserAndroject(String projectName, String login) throws SQLException {
		PreparedStatement allDatasetsStmt = DBAccess.createUdsDBConnection()
				.prepareStatement(DATASETS_BY_USER_AND_PROJECT);
		try {
			ResultSet rs = allDatasetsStmt.executeQuery();
			assert !rs.next() : "Datasets are empty! Make sure that you have already created a Proline project.";
		} finally {
			tryToCloseStatement(allDatasetsStmt);
		}
	}

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
