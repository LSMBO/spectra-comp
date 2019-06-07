package fr.lsmbo.msda.spectra.comp.db;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import fr.lsmbo.msda.spectra.comp.utils.FileUtils;

/**
 * Handle database connection for spectra-comp
 * 
 * @author Aromdhani
 *
 */
public class DBConfig {
	private static final Object CONFIGURATION_LOCK = new Object();
	private static String application = "application.conf";
	private static DBConfig instance;

	public static void forcePropertiesFileReload() {
		synchronized (CONFIGURATION_LOCK) {
			if (instance != null) {
				instance = null;
			}
		}
	}

	/**
	 * Return an instance of DBConfig
	 * 
	 * @return an instance of DBConfig
	 */
	public static DBConfig getInstance() {
		if (instance == null) {
			instance = new DBConfig();
		}
		return instance;
	}

	private Integer maxPoolConnection = null;
	private String user = null;
	private String password = null;
	private Integer port = null;
	private String host = null;
	private String dbName = null;
	DriverType driverType = null;

	private Properties connectionProperties = null;

	private DBConfig() {
		loadProperties();
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @return the driverType
	 */
	public final DriverType getDriverType() {
		return driverType;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the maxPoolConnection
	 */
	public Integer getMaxPoolConnection() {
		return maxPoolConnection;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Load application.conf properties
	 * 
	 * @throws URISyntaxException
	 */
	private String getPath() {
		URI srcPath;
		try {
			srcPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
			String path = new File(srcPath).getParent().replaceAll("\\\\", "/");
			String applicationConf = path + File.separator + "config" + File.separator + application;
			System.out.println("INFO - Load properties from: " + applicationConf + "");
			return applicationConf;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	private void loadProperties() {
		FileUtils.readBytesFrmFile(getPath(), properties -> {
			maxPoolConnection = Integer.valueOf(properties.getProperty("db-config.max-pool-connection"));
			user = properties.getProperty("auth-config.user");
			password = properties.getProperty("auth-config.password");
			host = properties.getProperty("host-config.host");
			port = Integer.valueOf(properties.getProperty("host-config.port"));
			dbName = properties.getProperty("db-name");
			if (properties.getProperty("db-config.driver-type").equals("postgresql")) {
				driverType = DriverType.POSTGRESQL;
			} else if (properties.getProperty("db-config.driver-type").equals("sqlite")) {
				driverType = DriverType.SQLITE;
			} else {
				driverType = DriverType.H2;
			}
		});

	}

	/**
	 * @param dbName
	 *            the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @param driverType
	 *            the driverType to set
	 */
	public final void setDriverType(DriverType driverType) {
		this.driverType = driverType;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param maxPoolConnection
	 *            the maxPoolConnection to set
	 */
	public void setMaxPoolConnection(Integer maxPoolConnection) {
		this.maxPoolConnection = maxPoolConnection;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		str.append("max pool connection: ").append(maxPoolConnection).append(" ;user: ").append(user).append(" ;host: ")
				.append(host).append(" ;port: ").append(port);
		return str.toString();

	}

}
