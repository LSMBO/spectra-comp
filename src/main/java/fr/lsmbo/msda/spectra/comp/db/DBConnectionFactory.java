package fr.lsmbo.msda.spectra.comp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * This class represents and wrap all active database connections. It provides
 * methods to create and close connections.
 * 
 * @author Aromdhani
 *
 */

public final class DBConnectionFactory {

	private static DBConfig dbConfig = DBConfig.getInstance();

	private static List<Optional<Connection>> connList = new ArrayList<>();

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
		DBConnectionFactory.dbConfig = dbConfig;
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
	public static Optional<Connection> createMSIDbConnection(Long projectId) throws Exception {
		try {
			System.out.println("INFO | Create connection to the project with id=#" + projectId);
			if (!isInitialized())
				throw new Exception("Error | Database configuration is not setup!");
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
			final String msiDbName = "msi_db_project_" + projectId;

			str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
					.append("/").append(msiDbName);
			Optional<Connection> dbConn = Optional.of(DriverManager.getConnection(str.toString(), connProperties));
			connList.add(dbConn);
			return dbConn;
		} catch (Exception e) {
			System.err.println("ERROR | Can't connect to msi_db_project_" + projectId + " : " + e);
			return Optional.ofNullable(null);
		}
	}

	/***
	 * Create UDS_DB connection
	 * 
	 * @param projectId the project id
	 * @return Optional connection
	 */
	public static Optional<Connection> createUDSDbConnection() {
		try {
			System.out.println("INFO | Create connection to UDS_DB");
			if (!isInitialized())
				throw new Exception("Error | Database configuration is not setup!");
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
			final String udsDbName = "uds_db";

			str.append("jdbc:postgresql://").append(dbConfig.getHost()).append(":").append(dbConfig.getPort())
					.append("/").append(udsDbName);
			Optional<Connection> dbConn = Optional
					.ofNullable(DriverManager.getConnection(str.toString(), connProperties));
			connList.add(dbConn);
			connList.add(Optional.ofNullable(DriverManager.getConnection(str.toString(), connProperties)));
			return dbConn;
		} catch (Exception e) {
			System.err.println("ERROR | Can't connect to UDS_DB: " + e);
			return Optional.ofNullable(null);
		}

	}

	/**
	 * Close all active database connections
	 * 
	 */
	public static void closeAll() {
		connList.forEach(connOptional -> {
			connOptional.ifPresent(conn -> {
				try {
					if (!conn.isClosed())
						conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});
	}
}
