package fr.lsmbo.msda.spectra.comp.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * Handle databases connection for Proline database.
 *
 * @author Aromdhani
 */

public class DBAccess {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(DBAccess.class);

	/** The uds_db connection. */
	static Connection udsDbConnection = null;

	/** The map of msi_db connections */
	static Map<Long, Connection> connectionByProjectIdMap = new HashMap<>();

	/**
	 * Close all active msi_db database connections
	 * 
	 */
	public static void closeAllMsiDb() {
		connectionByProjectIdMap.forEach((k, v) -> {
			try {
				if (v != null && !v.isClosed()) {
					logger.info("Close msi_db_project_{} connection", k);
					v.close();
					v = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Error while trying to close msi_db database connections");
				e.printStackTrace();
			}
		});
	}

	/**
	 * Close all active database connections
	 * 
	 */
	public static void closeAllDb() {
		logger.info("Close all database connections");
		closeAllMsiDb();
		closeUdsDb();
	}

	/**
	 * Close uds_db connection.
	 */
	public static void closeUdsDb() {
		try {
			logger.info("Close uds_db database connection");
			if (udsDbConnection != null && !udsDbConnection.isClosed()) {
				udsDbConnection.close();
				udsDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error while trying to close uds_db database connection {}", e);
			e.printStackTrace();

		}
	}

	/**
	 * Initialize msi_db and uds_db database connections.
	 */
	public static void initialize() {
		if (!connectionByProjectIdMap.isEmpty()) {
			closeAllMsiDb();
			connectionByProjectIdMap.clear();
		}
		if (udsDbConnection != null) {
			initializeUdsDb();
		}
	}

	/**
	 * Initialize uds_db.
	 */
	public static void initializeUdsDb() {
		try {
			if (udsDbConnection != null) {
				udsDbConnection.close();
				udsDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get a connection to msi_db_project_x database.
	 *
	 * @param projectId the project id to connect to
	 * @return the connection to the msi_db_project_x database connection
	 * @throws Exception
	 */

	public static Connection getMsiDBConnection(final Long projectId) throws Exception {
		Connection msiDbConnection = null;
		if (!connectionByProjectIdMap.containsKey(projectId)) {
			msiDbConnection = DBConnectionFactory.createMSIDbConnection(projectId)
					.orElseThrow(() -> new Exception("Failed to connect to msi_db_project_" + projectId));
			connectionByProjectIdMap.put(projectId, msiDbConnection);
			return msiDbConnection;
		} else {
			msiDbConnection = connectionByProjectIdMap.get(projectId);
			return msiDbConnection;
		}
	}

	/**
	 * Get a connection to uds_db database.
	 *
	 * @return the connection to uds_db
	 * @throws Exception
	 */

	public static Connection getUdsDBConnection() throws Exception {
		if (udsDbConnection == null) {
			udsDbConnection = DBConnectionFactory.createUDSDbConnection()
					.orElseThrow(() -> new Exception("Failed to connect to uds_db!"));
			return udsDbConnection;
		} else {
			return udsDbConnection;
		}
	}
}
