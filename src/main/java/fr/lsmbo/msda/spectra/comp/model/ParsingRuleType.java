package fr.lsmbo.msda.spectra.comp.model;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

/**
 * 
 * @author Aromdhani
 *
 */

public enum ParsingRuleType {

	MSCONVERT("MSConvert", "RTmin:\"(\\d+\\.\\d+)\""), PROLINE("Proline", "first_time:(\\d+);"), MASCOT_DLL(
			"mascot.dll", "Elution: (.+?) to .+? min|Elution: (.+?) min"), ANALYSIS("Data Analysis",
					"Cmpd.+MS.+, (\\d+\\.\\d+) min"), MASCOT_DISTILLER("Mascot Distiller",
							"in range \\d+ \\(rt=(\\d+.\\d+)\\)|\\(rt=(\\d+.\\d+)\\)");
	private final String m_regexName;
	private final String m_regexValue;

	private ParsingRuleType(final String regexName, final String regexValue) {
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

	public static ParsingRuleType getSoftwareType(String softwareName) {
		ParsingRuleType parsingRuleType = ParsingRuleType.MSCONVERT;
		switch (softwareName) {
		case "MSConvert":
			parsingRuleType = ParsingRuleType.MSCONVERT;
			break;
		case "Proline":
			parsingRuleType = ParsingRuleType.PROLINE;
			break;
		case "mascot.dll":
			parsingRuleType = ParsingRuleType.MASCOT_DLL;
			break;
		case "Data Analysis":
			parsingRuleType = ParsingRuleType.ANALYSIS;
			break;
		case "Mascot Distiller":
			parsingRuleType = ParsingRuleType.MASCOT_DISTILLER;
			break;
		default:
			break;
		}
		return parsingRuleType;
	}

}
