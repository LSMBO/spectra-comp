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
	static Connection firstMsiDbConnection = null;
	static Connection secondMsiDbConnection = null;

	/**
	 * Close all active connection
	 */
	public static void closeAll() {
		closeFirstMsiDb();
		closeSecondMsiDb();
		closeUdsDb();
	}

	/**
	 * Close first msi_db connection
	 * 
	 */
	public static void closeFirstMsiDb() {
		try {
			if (firstMsiDbConnection != null && !firstMsiDbConnection.isClosed()) {
				firstMsiDbConnection.close();
				firstMsiDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Close second msi_db connection
	 * 
	 */
	public static void closeSecondMsiDb() {
		try {
			if (secondMsiDbConnection != null && !secondMsiDbConnection.isClosed()) {
				secondMsiDbConnection.close();
				secondMsiDbConnection = null;
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
			if (firstMsiDbConnection != null || udsDbConnection != null) {
				firstMsiDbConnection.close();
				firstMsiDbConnection = null;
				udsDbConnection.close();
				udsDbConnection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialize first msi_db
	 * 
	 */
	public static void initializeFirstMsiDb() {
		try {
			if (firstMsiDbConnection != null) {
				firstMsiDbConnection.close();
				firstMsiDbConnection = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialize second msi_db
	 * 
	 */
	public static void initializeSecondMsiDb() {
		try {
			if (secondMsiDbConnection != null) {
				secondMsiDbConnection.close();
				secondMsiDbConnection = null;
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
	 * @return the connection to the first database connection
	 */

	public static Connection openFirstMsiDBConnection(final String msiDbName) {
		if (firstMsiDbConnection == null) {
			DBConfig dbConfig = DBConfig.getInstance();
			System.out.println("INFO | --- Connection config: " + dbConfig.toString() + "");
			System.out.println("INFO | Open connection to " + msiDbName + "");
			StringBuilder str = new StringBuilder();
			Properties connProperties = new Properties();
			assert !StringsUtils.isEmpty(dbConfig.getUser()) : "User name must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getPassword()) : "Password must not be null nor empty!";
			assert !StringsUtils
					.isEmpty(dbConfig.getDriverType().getJdbcDriver()) : "Driver must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getHost()) : "Host must not be null nor empty!";
			assert dbConfig.getPort() > 0 : "Port number is not valid!";
			connProperties.setProperty("user", dbConfig.getUser());
			connProperties.setProperty("password", dbConfig.getPassword());
			connProperties.setProperty("driver", dbConfig.getDriverType().getJdbcDriver());
			try {
				str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
						.append("/").append(msiDbName);
				firstMsiDbConnection = DriverManager.getConnection(str.toString(), connProperties);
				return firstMsiDbConnection;
			} catch (Exception e) {
				System.out.println("ERROR | Can't connect to " + msiDbName + " !" + e);
				return firstMsiDbConnection;
			}
		} else {
			return firstMsiDbConnection;
		}
	}

	/**
	 * Create connection to uds_db database
	 * 
	 * @return the connection to the first database connection
	 */

	public static Connection openSecondMsiDBConnection(final String msiDbName) {
		if (secondMsiDbConnection == null) {
			DBConfig dbConfig = DBConfig.getInstance();
			System.out.println("INFO | --- Connection config: " + dbConfig.toString() + "");
			System.out.println("INFO | Open connection to " + msiDbName + "");
			StringBuilder str = new StringBuilder();
			Properties connProperties = new Properties();
			assert !StringsUtils.isEmpty(dbConfig.getUser()) : "User name must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getPassword()) : "Password must not be null nor empty!";
			assert !StringsUtils
					.isEmpty(dbConfig.getDriverType().getJdbcDriver()) : "Driver must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getHost()) : "Host must not be null nor empty!";
			assert dbConfig.getPort() > 0 : "Port number is not valid!";
			connProperties.setProperty("user", dbConfig.getUser());
			connProperties.setProperty("password", dbConfig.getPassword());
			connProperties.setProperty("driver", dbConfig.getDriverType().getJdbcDriver());
			try {
				str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
						.append("/").append(msiDbName);
				secondMsiDbConnection = DriverManager.getConnection(str.toString(), connProperties);
				return secondMsiDbConnection;
			} catch (Exception e) {
				System.out.println("ERROR |Can't connect to " + msiDbName + " !" + e);
				return secondMsiDbConnection;
			}
		} else {
			return secondMsiDbConnection;
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
			System.out.println("INFO | --- Connection config: " + dbConfig.toString() + "");
			System.out.println("INFO | Open connection to uds_db");
			StringBuilder str = new StringBuilder();
			Properties connProperties = new Properties();
			assert !StringsUtils.isEmpty(dbConfig.getUser()) : "User name must not be null nor empty!";
			assert !StringsUtils.isEmpty(dbConfig.getPassword()) : "Password must not be null nor empty!";
			assert !StringsUtils
					.isEmpty(dbConfig.getDriverType().getJdbcDriver()) : "Driver must not be null nor empty!";
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
				System.out.println("ERROR | Can't connect to UDS DB!" + e);
				return udsDbConnection;
			}
		} else {
			return udsDbConnection;
		}
	}
}
