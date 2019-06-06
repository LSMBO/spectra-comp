package fr.lsmbo.msda.spectra.comp.model;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * 
 * @author Aromdhani
 *
 */

public enum SoftwareType {

	MSCONVERT("MSConvert", "RTmin:\"(\\d+\\.\\d+)\""), PROLINE("Proline", "first_time:(\\d+);"), MASCOT_DLL(
			"mascot.dll", "Elution: (.+?) to .+? min|Elution: (.+?) min"), ANALYSIS("Data Analysis",
					"Cmpd.+MS.+, (\\d+\\.\\d+) min"), MASCOT_DISTILLER("Mascot Distiller",
							"in range \\d+ \\(rt=(\\d+.\\d+)\\)|\\(rt=(\\d+.\\d+)\\)");
	/**
	 * Return the parsing rule by the software name
	 * 
	 * @param softwareName
	 *            the software name
	 * @return the parsing rule
	 */
	public static SoftwareType getParsingRule(String softwareName) {
		SoftwareType parsingRuleType = SoftwareType.MSCONVERT;
		switch (softwareName) {
		case "MSConvert":
			parsingRuleType = SoftwareType.MSCONVERT;
			break;
		case "Proline":
			parsingRuleType = SoftwareType.PROLINE;
			break;
		case "mascot.dll":
			parsingRuleType = SoftwareType.MASCOT_DLL;
			break;
		case "Data Analysis":
			parsingRuleType = SoftwareType.ANALYSIS;
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
