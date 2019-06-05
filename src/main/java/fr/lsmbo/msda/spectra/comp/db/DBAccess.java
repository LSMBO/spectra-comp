package fr.lsmbo.msda.spectra.comp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * Handle databases connection for spectra-comp
 * 
 * @author Aromdhani
 *
 */
public class DBAccess {
	static Connection udsDbConnection = null;
	static Connection msiDbConnection = null;

	/**
	 * Close all active connection
	 */
	public static void closeAll() {
		closeMsiDb();
		closeUdsDb();
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
	 * initialize msi_db and uds_db connection
	 * 
	 */
	public static void initialize() {
		try {
			if (msiDbConnection != null || udsDbConnection != null) {
				msiDbConnection.close();
				msiDbConnection = null;
				udsDbConnection.close();
				udsDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialize msi_db
	 * 
	 */
	public static void initializeMsiDb() {
		try {
			if (msiDbConnection != null) {
				msiDbConnection.close();
				msiDbConnection = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialize uds_db
	 * 
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
	 * Create connection to uds_db database
	 * 
	 * @return the connection to uds_db
	 */

	public static Connection openMsiDBConnection(final String msiDbName) {
		if (msiDbConnection == null) {
			DBConfig dbConfig = DBConfig.getInstance();
			System.out.println("--- Connection config: " + dbConfig.toString() + "");
			System.out.println("--- Open connection to " + msiDbName + "");
			StringBuilder str = new StringBuilder();
			Properties connProperties = new Properties();
			assert !StringsUtils.isEmpty(dbConfig.getUser()) : "User name must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getPassword()) : "Password must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getDriverType().getJdbcDriver()) : "Driver must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getHost()) : "Host must not be null nor empty!";
			assert dbConfig.getPort() > 0 : "Port number is not valid!";
			connProperties.setProperty("user", dbConfig.getUser());
			connProperties.setProperty("password", dbConfig.getPassword());
			connProperties.setProperty("driver", dbConfig.getDriverType().getJdbcDriver());
			try {
				str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
						.append("/").append(msiDbName);
				msiDbConnection = DriverManager.getConnection(str.toString(), connProperties);
				return msiDbConnection;
			} catch (Exception e) {
				System.out.println("--- Can't connect to " + msiDbName + " !" + e);
				return msiDbConnection;
			}
		} else {
			return msiDbConnection;
		}
	}

	/**
	 * Create connection to uds_db database
	 * 
	 * @return the connection to uds_db
	 */

	public static Connection openUdsDBConnection() {
		if (udsDbConnection == null) {
			DBConfig dbConfig = DBConfig.getInstance();
			System.out.println("--- Connection config: " + dbConfig.toString() + "");
			System.out.println("--- Open connection to uds_db");
			StringBuilder str = new StringBuilder();
			Properties connProperties = new Properties();
			assert !StringsUtils.isEmpty(dbConfig.getUser()) : "User name must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getPassword()) : "Password must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getDriverType().getJdbcDriver()) : "Driver must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getHost()) : "Host must not be null nor empty!";
			assert dbConfig.getPort() > 0 : "Port number is not valid!";
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
		} else {
			return udsDbConnection;
		}
	}
}
