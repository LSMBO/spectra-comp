package fr.lsmbo.msda.spectra.comp;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import fr.lsmbo.msda.spectra.comp.db.DBConfig;
import fr.lsmbo.msda.spectra.comp.settings.UserParams;
import fr.lsmbo.msda.spectra.comp.settings.Version;

/**
 * Initialize spectra-comp configurations values
 * 
 * @author Aromdhani
 *
 */

public class Config {

	private static String spectraCompFileName = "spectra-comp.json";
	private static String defaultParamsFileName = "default-params.json";
	private static Config instance = null;

	/**
	 * Force to load configuration properties
	 */
	public static void forceToLoadProperties() {
		instance = null;
		getInstance();
	}

	/**
	 * Return an instance of Config
	 * 
	 * @return the instance of Config
	 */
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	/**
	 * Private constructor
	 */
	private Config() {
		loadProperties();
	}

	/**
	 * Return the file path
	 * 
	 * @param fileName
	 *            the file name to get its path
	 * @return the file path
	 */
	public String getConfigFilePath(String fileName) {
		URI srcPath;
		try {
			srcPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
			String path = new File(srcPath).getParent().replaceAll("\\\\", "/");
			String filePath = path + File.separator + "config" + File.separator + fileName;
			System.out.println("--- Load properties from: " + fileName + "");
			return filePath;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load configuration files and initialize the values
	 * 
	 */

	private void loadProperties() {
		if (instance == null) {
			// Load spectra-comp version
			loadSpectraCompProps();
			// Load Spectra-comp default parameters
			loadUserParams();
			// Load database connection properties
			DBConfig.getInstance();
		}
	}

	/**
	 * Load spectra-comp version properties
	 * 
	 */
	private synchronized void loadSpectraCompProps() {
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(new File(getConfigFilePath(spectraCompFileName))));
			Session.SPECTRACOMP_VERSION = gson.fromJson(reader, Version.class);
			System.out.println(Session.SPECTRACOMP_VERSION.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load user parameters. Read and parse parameters file.
	 * 
	 */
	public synchronized void loadUserParams() {
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(getConfigFilePath(defaultParamsFileName)));
			// Update default user parameters
			Session.USER_PARAMS = gson.fromJson(reader, UserParams.class);
			// Update Regex
			Session.CURRENT_REGEX_RT = Session.USER_PARAMS.getParsingRules().getParsingRuleName();
			System.out.println(Session.USER_PARAMS.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
