/*
 * 
 */
package fr.lsmbo.msda.spectra.comp;

import java.io.File;

import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.settings.UserParams;

/**
 * Holds session parameters .
 * 
 * @author Aromdhani
 *
 */
public class Session {
	// List of session values
	public static String SPECTRACOMP_RELEASE_NAME = "";
	public static String SPECTRACOMP_RELEASE_DESCRIPTION = "";
	public static String SPECTRACOMP_RELEASE_VERSION = "";
	public static String SPECTRACOMP_RELEASE_DATE = "";

	public static File REFERENCE_PKLIST_FILE = null;
	public static File TEST_PKLIST_FILE = null;
	public static UserParams userParams = null;
	public static String CURRENT_REGEX_RT = "title.regex.data-analysis.rt";

}
