package fr.lsmbo.msda.spectra.comp.model;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

// TODO: Auto-generated Javadoc
/**
 * The Enum SoftwareType.
 *
 * @author Aromdhani
 */

public enum SoftwareType {

	/** The ms convert. */
	MS_CONVERT("MSConvert", "RTmin:\"(\\d+\\.\\d+)\""),
	
	/** The proline. */
	PROLINE("Proline", "first_time:(\\d+);"),
	
	/** The mascot dll. */
	MASCOT_DLL("mascot.dll", "Elution: (.+?) to .+? min|Elution: (.+?) min"),
	
	/** The analysis 4 0. */
	ANALYSIS_4_0("Data Analysis 4.0","Cmpd.+MSn.+, (\\d+\\.\\d+) min"),
	
	/** The analysis 4 1. */
	ANALYSIS_4_1("Data Analysis 4.1","Cmpd.+MS\\d.+, (\\d+\\.\\d+) min"),
	
	/** The mascot distiller. */
	MASCOT_DISTILLER("Mascot Distiller","in range \\d+ \\(rt=(\\d+.\\d+)\\)||\\(rt=(\\d+.\\d+)\\)"),
	
	/** The spectrum mill rule. */
	SPECTRUM_MILL_RULE("Spectrum Mill", "Cmpd.+MSn.+, (\\d+\\.\\d+) min");

	/**
	 * Return the parsing rule by the software name. this format has been taken
	 * from Proline databases.
	 * 
	 * @param softwareName
	 *            the software name
	 * @return the parsing rule
	 */
	public static SoftwareType getParsingRule(String softwareName) {
		SoftwareType parsingRuleType = SoftwareType.PROLINE;
		switch (softwareName) {
		case "MSConvert":
			parsingRuleType = SoftwareType.MS_CONVERT;
			break;
		case "Proline":
			parsingRuleType = SoftwareType.PROLINE;
			break;
		case "mascot.dll":
			parsingRuleType = SoftwareType.MASCOT_DLL;
			break;
		case "Data Analysis 4.0":
			parsingRuleType = SoftwareType.ANALYSIS_4_0;
			break;
		case "Data Analysis 4.1":
			parsingRuleType = SoftwareType.ANALYSIS_4_1;
			break;
		case "Spectrum Mill":
			parsingRuleType = SoftwareType.SPECTRUM_MILL_RULE;
			break;
		case "Mascot Distiller":
			parsingRuleType = SoftwareType.MASCOT_DISTILLER;
			break;
		default:
			break;
		}
		return parsingRuleType;
	}

	/** The m regex name. */
	private final String m_regexName;

	/** The m regex value. */
	private final String m_regexValue;

	/**
	 * Instantiates a new software type.
	 *
	 * @param regexName the regex name
	 * @param regexValue the regex value
	 */
	private SoftwareType(final String regexName, final String regexValue) {
		assert (StringsUtils.isEmpty(regexName)) : "invalid regex name";
		assert (StringsUtils.isEmpty(regexValue)) : "invalid regex value";

		m_regexValue = regexValue;
		m_regexName = regexName;
	}

	/**
	 * Gets the regex name.
	 *
	 * @return the regex name
	 */
	public String getRegexName() {
		return m_regexName;
	}

	/**
	 * Gets the regex value.
	 *
	 * @return the regex value
	 */
	public String getRegexValue() {
		return m_regexValue;
	}

}
