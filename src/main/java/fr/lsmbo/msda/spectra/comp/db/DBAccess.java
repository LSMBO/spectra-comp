package fr.lsmbo.msda.spectra.comp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Handle database connection for spectra-comp
 * 
 * @author Aromdhani
 *
 */
public class DBAccess {
	static Connection udsDbConnection = null;
	static Connection msiDbConnection = null;

	/**
	 * Create connection to uds_db database
	 * 
	 * @return the connection to uds_db
	 */

	public static Connection createUdsDBConnection() {
		DBConfig dbConfig = DBConfig.getInstance();
		StringBuilder str = new StringBuilder();
		Properties connProperties = new Properties();
		connProperties.setProperty("user", dbConfig.getUser());
		connProperties.setProperty("password", dbConfig.getPassword());
		connProperties.setProperty("driver", dbConfig.getDriverType().getJdbcDriver());
		try {
			str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
					.append("/").append(dbConfig.getDbName());
			udsDbConnection = DriverManager.getConnection(str.toString(), connProperties);
			return udsDbConnection;
		} catch (Exception e) {
			System.out.println("--- Can't connect to UDS DB!" + e);
			return udsDbConnection;
		}
	}

	/**
	 * Create connection to uds_db database
	 * 
	 * @return the connection to uds_db
	 */

	public static Connection createMsiDBConnection(final String msiDbName) {
		DBConfig dbConfig = DBConfig.getInstance();
		StringBuilder str = new StringBuilder();
		Properties connProperties = new Properties();
		connProperties.setProperty("user", dbConfig.getUser());
		connProperties.setProperty("password", dbConfig.getPassword());
		connProperties.setProperty("driver", dbConfig.getDriverType().getJdbcDriver());
		try {
			str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
					.append("/").append(msiDbName);
			msiDbConnection = DriverManager.getConnection(str.toString(), connProperties);
			return msiDbConnection;
		} catch (Exception e) {
			System.out.println("--- Can't connect to UDS DB!" + e);
			return msiDbConnection;
		}
	}

	/**
	 * Close all active connection
	 */
	public static void closeAll() {
		closeMsiDb();
		closeUdsDb();
	}

	/**
	 * Close uds_db connection
	 * 
	 */
	public static void closeUdsDb() {
		try {
			if (udsDbConnection != null && !udsDbConnection.isClosed()) {
				udsDbConnection.close();
				udsDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Close msi_db connection
	 * 
	 */
	public static void closeMsiDb() {
		try {
			if (msiDbConnection != null && !msiDbConnection.isClosed()) {
				msiDbConnection.close();
				msiDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
