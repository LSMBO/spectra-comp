package fr.lsmbo.msda.spectra.comp.db;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import fr.lsmbo.msda.spectra.comp.utils.FileUtils;

// TODO: Auto-generated Javadoc
/**
 * Handle database connection for spectra-comp.
 *
 * @author Aromdhani
 */
public class DBConfig {
	
	/** The Constant CONFIGURATION_LOCK. */
	private static final Object CONFIGURATION_LOCK = new Object();
	
	/** The application. */
	private static String application = "application.conf";
	
	/** The instance. */
	private static DBConfig instance;

	/**
	 * Force properties file reload.
	 */
	public static void forcePropertiesFileReload() {
		synchronized (CONFIGURATION_LOCK) {
			if (instance != null) {
				instance = null;
			}
		}
	}

	/**
	 * Return an instance of DBConfig.
	 *
	 * @return an instance of DBConfig
	 */
	public static DBConfig getInstance() {
		if (instance == null) {
			instance = new DBConfig();
		}
		return instance;
	}

	/** The max pool connection. */
	private Integer maxPoolConnection = null;
	
	/** The user. */
	private String user = null;
	
	/** The password. */
	private String password = null;
	
	/** The port. */
	private Integer port = null;
	
	/** The host. */
	private String host = null;
	
	/** The db name. */
	private String dbName = null;
	
	/** The driver type. */
	DriverType driverType = null;

	/** The connection properties. */
	private Properties connectionProperties = null;

	/**
	 * Instantiates a new DB config.
	 */
	private DBConfig() {
		loadProperties();
	}

	/**
	 * Gets the db name.
	 *
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * Gets the driver type.
	 *
	 * @return the driverType
	 */
	public final DriverType getDriverType() {
		return driverType;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the max pool connection.
	 *
	 * @return the maxPoolConnection
	 */
	public Integer getMaxPoolConnection() {
		return maxPoolConnection;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Load application.conf properties
	 *
	 * @return the path
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
	 * Gets the port.
	 *
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Load properties.
	 */
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
	 * Sets the db name.
	 *
	 * @param dbName            the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Sets the driver type.
	 *
	 * @param driverType            the driverType to set
	 */
	public final void setDriverType(DriverType driverType) {
		this.driverType = driverType;
	}

	/**
	 * Sets the host.
	 *
	 * @param host            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Sets the max pool connection.
	 *
	 * @param maxPoolConnection            the maxPoolConnection to set
	 */
	public void setMaxPoolConnection(Integer maxPoolConnection) {
		this.maxPoolConnection = maxPoolConnection;
	}

	/**
	 * Sets the password.
	 *
	 * @param password            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the port.
	 *
	 * @param port            the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Sets the user.
	 *
	 * @param user            the user to set
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
