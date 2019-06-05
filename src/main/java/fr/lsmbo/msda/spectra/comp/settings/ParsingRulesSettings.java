
package fr.lsmbo.msda.spectra.comp.settings;

/**
 * Wrap Parsing rule settings.
 * 
 * @author Aromdhani
 *
 */

public class ParsingRulesSettings {

	private String parsingRuleName;
	private String parsingRuleValue;

	public ParsingRulesSettings() {
	}

	public ParsingRulesSettings(String parsingRuleName, String parsingRuleValue) {
		super();
		this.parsingRuleName = parsingRuleName;
		this.parsingRuleValue = parsingRuleValue;

	}

	public String getParsingRuleName() {
		return parsingRuleName;
	}

	public String getParsingRuleValue() {
		return parsingRuleValue;
	}

	public void setParsingRuleName(String parsingRuleName) {
		this.parsingRuleName = parsingRuleName;
	}

	public void setParsingRuleValue(String parsingRuleValue) {
		this.parsingRuleValue = parsingRuleValue;
	}

	@Override
	public String toString() {
		return "Parsing Rule Name:" + parsingRuleName + " ; " + "parsingRuleValue: " + parsingRuleValue;
	}

}
