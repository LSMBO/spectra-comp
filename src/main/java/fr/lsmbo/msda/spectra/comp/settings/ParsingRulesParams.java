
package fr.lsmbo.msda.spectra.comp.settings;

// TODO: Auto-generated Javadoc
/**
 * Wrap Parsing rule settings.
 * 
 * @author Aromdhani
 *
 */

public class ParsingRulesParams {

	/** The parsing rule name. */
	private String parsingRuleName;
	
	/** The parsing rule value. */
	private String parsingRuleValue;

	/**
	 * Instantiates a new parsing rules params.
	 */
	public ParsingRulesParams() {
	}

	/**
	 * Instantiates a new parsing rules params.
	 *
	 * @param parsingRuleName the parsing rule name
	 * @param parsingRuleValue the parsing rule value
	 */
	public ParsingRulesParams(String parsingRuleName, String parsingRuleValue) {
		super();
		this.parsingRuleName = parsingRuleName;
		this.parsingRuleValue = parsingRuleValue;

	}

	/**
	 * Gets the parsing rule name.
	 *
	 * @return the parsing rule name
	 */
	public String getParsingRuleName() {
		return parsingRuleName;
	}

	/**
	 * Gets the parsing rule value.
	 *
	 * @return the parsing rule value
	 */
	public String getParsingRuleValue() {
		return parsingRuleValue;
	}

	/**
	 * Sets the parsing rule name.
	 *
	 * @param parsingRuleName the new parsing rule name
	 */
	public void setParsingRuleName(String parsingRuleName) {
		this.parsingRuleName = parsingRuleName;
	}

	/**
	 * Sets the parsing rule value.
	 *
	 * @param parsingRuleValue the new parsing rule value
	 */
	public void setParsingRuleValue(String parsingRuleValue) {
		this.parsingRuleValue = parsingRuleValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Parsing Rule Name:" + parsingRuleName + " ; " + "parsingRuleValue: " + parsingRuleValue;
	}

}
