/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.list;

import java.util.ArrayList;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.model.ParsingRule;

public class ParsingRules {

	private static ArrayList<ParsingRule> parsingRules = new ArrayList<ParsingRule>();
	private static Integer currentParsingRuleIndex = -1;

	/** Add a new parsing rule */
	public static void add(ParsingRule rule) {
		parsingRules.add(rule);
	}

	public static ArrayList<ParsingRule> get() {
		initialize();
		return parsingRules;
	}

	/**
	 * Return a parsing rule via its key
	 * 
	 * @param key
	 *            the key of parsing rule to retrieve
	 */
	public static ParsingRule get(String key) {
		for (ParsingRule pr : parsingRules) {
			if (pr.getName().equals(key))
				return pr;
		}
		return null;
	}

	/** Return the current parsing rule */
	public static ParsingRule getCurrentParsingRule() {
		if (currentParsingRuleIndex != -1)
			return parsingRules.get(currentParsingRuleIndex);
		return null;
	}

	private static void initialize() {
		if (parsingRules.isEmpty()) {
			// lazy loading
			try {
				parsingRules.add(new ParsingRule(Session.USER_PARAMS.getParsingRules().getParsingRuleName(),
						Session.USER_PARAMS.getParsingRules().getParsingRuleValue(),
						Session.USER_PARAMS.getParsingRules().getParsingRuleName(), 0));

			} catch (Exception ex) {
			}

		}
	}

	/** Set a new current parsing rule */
	public static void setNewCurrentParsingRule(ParsingRule pr) {
		if (pr != null) {
			Session.CURRENT_REGEX_RT = pr.getPropertyKey();
			currentParsingRuleIndex = pr.getIndex();
		}
	}
}
