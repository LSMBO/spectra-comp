package fr.lsmbo.msda.spectra.comp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import fr.lsmbo.msda.spectra.comp.Main;
import fr.lsmbo.msda.spectra.comp.settings.UserParams;

/**
 * Initialize spectra-comparator configurations
 * 
 * @author Aromdhani
 *
 */

public class Config {
	public static String applicationFile = "application.conf";
	public static String spectraCompFile = "spectra-comp.json";
	public static File DefaultParamsFile =  new File(
			Main.class.getClassLoader().getResource("default-params.json").getPath());
	public static Properties properties = null;

	/**
	 * Return object value. Example: Config.get("max.file.size")
	 * 
	 * @param the specified key to retrieve the property value as an object.
	 */
	private static Object _get(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Return a property value as String
	 * 
	 * @param key the specified key to get the property value
	 */
	public static String get(String key) {
		Object value = _get(key);
		if (value == null)
			return null;
		return value.toString();
	}

	public static void initialize() {
		loadUserParams(DefaultParamsFile);
	}

	/**
	 * Load application.conf parameters
	 */
	private synchronized static void loadApplicationConf() {

	}

	/**
	 * Load spectra-comp parameters
	 */
	private synchronized static void loadSpectraCompProps() {
		try (InputStream input = Main.class.getClassLoader().getResourceAsStream(spectraCompFile)) {
			if (input == null) {
				System.err.println("Error - Spectra-comp properties file: '" + spectraCompFile + "' does not exist!");
			} else {
				Properties recoverProperties = new Properties();
				recoverProperties.load(input);
				Session.SPECTRACOMP_RELEASE_NAME = recoverProperties.getProperty("name");
				Session.SPECTRACOMP_RELEASE_DESCRIPTION = recoverProperties.getProperty("description");
				Session.SPECTRACOMP_RELEASE_VERSION = recoverProperties.getProperty("version");
				Session.SPECTRACOMP_RELEASE_DATE = recoverProperties.getProperty("build-date");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load user parameters. Read and parse parameters file.
	 * 
	 * @param paramFile the file used to load user parameters
	 */
	public static void loadUserParams(File paramFile) {
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(paramFile));
			Session.userParams = gson.fromJson(reader, UserParams.class);
			System.out.println(Session.userParams.toString());
		} catch (Exception e) {
			// a possible error case is when param files has been generated with
			// an older version of Recover
			e.printStackTrace();
		}
	}
}
