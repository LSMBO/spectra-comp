package fr.lsmbo.msda.spectra.comp.model;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * 
 * @author Aromdhani
 *
 */

public enum SoftwareType {

	MS_CONVERT("MSConvert", "RTmin:\"(\\d+\\.\\d+)\""),
	PROLINE("Proline", "first_time:(\\d+);"),
	MASCOT_DLL("mascot.dll", "Elution: (.+?) to .+? min|Elution: (.+?) min"),
	ANALYSIS_4_0("Data Analysis 4.0","Cmpd.+MSn.+, (\\d+\\.\\d+) min"),
	ANALYSIS_4_1("Data Analysis 4.1","Cmpd.+MS\\d.+, (\\d+\\.\\d+) min"),
	MASCOT_DISTILLER("Mascot Distiller","in range \\d+ \\(rt=(\\d+.\\d+)\\)||\\(rt=(\\d+.\\d+)\\)"),
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

	private final String m_regexName;

	private final String m_regexValue;

	private SoftwareType(final String regexName, final String regexValue) {
		assert (StringsUtils.isEmpty(regexName)) : "invalid regex name";
		assert (StringsUtils.isEmpty(regexValue)) : "invalid regex value";

		m_regexValue = regexValue;
		m_regexName = regexName;
	}

	public String getRegexName() {
		return m_regexName;
	}

	public String getRegexValue() {
		return m_regexValue;
	}

}
