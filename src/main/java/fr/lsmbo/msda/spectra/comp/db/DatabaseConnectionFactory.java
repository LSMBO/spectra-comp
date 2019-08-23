package fr.lsmbo.msda.spectra.comp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;
import java.util.Properties;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * This class represents database connections
 * 
 * @author Aromdhani
 *
 */

public final class DatabaseConnectionFactory {

	private static DBConfig dbConfig = DBConfig.getInstance();

	/**
	 * @return the dbConfig
	 */
	public static final DBConfig getDbConfig() {
		return dbConfig;
	}

	/**
	 * @param dbConfig the dbConfig to set
	 */
	public static final void setDbConfig(DBConfig dbConfig) {
		DatabaseConnectionFactory.dbConfig = dbConfig;
	}

	public static Boolean isInitialized() {
		if (dbConfig != null)
			return true;
		else
			return false;
	}

	/***
	 * Create MSIDb connection
	 * 
	 * @param projectId the project id
	 * @return Optional connection
	 */
	public static Optional<Connection> createMSIDatabaseConnection(Long projectId) {
		System.out.println("INFO | Create connection to the project=#" + projectId);
		if (!isInitialized())
			return null;
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
		final String msiDbName = "msi_db_project_" + projectId;
		try {
			str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
					.append("/").append(msiDbName);
			return Optional.of(DriverManager.getConnection(str.toString(), connProperties));
		} catch (Exception e) {
			System.err.println("ERROR | Can't connect to " + msiDbName + " !" + e);
			return null;
		}
	}

	/***
	 * Create UDS_DB connection
	 * 
	 * @param projectId the project id
	 * @return Optional connection
	 */
	public static Optional<Connection> createUDSDatabaseConnection() {
		System.out.println("INFO | Create connection to UDS_DB");
		if (!isInitialized())
			return null;
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
		final String udsDbName = "uds_db";
		try {
			str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
					.append("/").append(udsDbName);
			return Optional.of(DriverManager.getConnection(str.toString(), connProperties));
		} catch (Exception e) {
			System.err.println("ERROR | Can't connect to " + udsDbName + "! " + e);
			return null;
		}
	}
}
