/*
 * 
 */
package fr.lsmbo.msda.spectra.comp;

import java.io.File;

import fr.lsmbo.msda.spectra.comp.settings.UserParams;
import fr.lsmbo.msda.spectra.comp.settings.Version;

/**
 * Holds session parameters.
 * 
 * @author Aromdhani
 *
 */
public class Session {
	
	/** The spectracomp release name. */
	// List of session values
	public static String SPECTRACOMP_RELEASE_NAME = "";
	
	/** The spectracomp release description. */
	public static String SPECTRACOMP_RELEASE_DESCRIPTION = "";
	
	/** The spectracomp release version. */
	public static String SPECTRACOMP_RELEASE_VERSION = "";
	
	/** The spectracomp release date. */
	public static String SPECTRACOMP_RELEASE_DATE = "";
	
	/** The spectracomp version. */
	public static Version SPECTRACOMP_VERSION = null;
	
	/** The file header. */
	public static String FILE_HEADER = "";
	
	/** The highest fragment mz. */
	public static Double HIGHEST_FRAGMENT_MZ = 0d;
	
	/** The highest fragment intensity. */
	public static Float  HIGHEST_FRAGMENT_INTENSITY=0f;
	
	/** The current regex rt. */
	public static String CURRENT_REGEX_RT = "msconvert";
	
	/** The reference pklist file. */
	public static File REFERENCE_PKLIST_FILE = null;
	
	/** The test pklist file. */
	public static File TEST_PKLIST_FILE = null;
	
	/** The user params. */
	public static UserParams USER_PARAMS = null;

}
