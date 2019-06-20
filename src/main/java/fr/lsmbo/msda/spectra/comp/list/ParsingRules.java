/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.list;

import java.util.ArrayList;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.model.ParsingRule;
import fr.lsmbo.msda.spectra.comp.model.SoftwareType;

// TODO: Auto-generated Javadoc
/**
 * The Class ParsingRules.
 */
public class ParsingRules {

	/** The parsing rules. */
	private static ArrayList<ParsingRule> parsingRules = new ArrayList<ParsingRule>();

	/** The current parsing rule index. */
	private static Integer currentParsingRuleIndex = -1;

	/**
	 * Add a new parsing rule.
	 *
	 * @param rule
	 *            the rule
	 */
	public static void add(ParsingRule rule) {
		parsingRules.add(rule);
	}

	/**
	 * Gets the.
	 *
	 * @return the array list
	 */
	public static ArrayList<ParsingRule> get() {
		initialize();
		return parsingRules;
	}

	/**
	 * Return a parsing rule via its key.
	 *
	 * @param key
	 *            the key of parsing rule to retrieve
	 * @return the parsing rule
	 */
	public static ParsingRule get(String key) {
		for (ParsingRule pr : parsingRules) {
			if (pr.getName().equals(key))
				return pr;
		}
		return null;
	}

	/**
	 * Return the current parsing rule.
	 *
	 * @return the current parsing rule
	 */
	public static ParsingRule getCurrentParsingRule() {
		if (currentParsingRuleIndex != -1)
			return parsingRules.get(currentParsingRuleIndex);
		return null;
	}

	/**
	 * Initialize.
	 */
	private static void initialize() {
		if (parsingRules.isEmpty()) {
			// lazy loading
			try {
				int i = 0;
				for (SoftwareType type : SoftwareType.values()) {
					parsingRules
							.add(new ParsingRule(type.getRegexName(), type.getRegexValue(), type.getRegexName(), i));
					i++;
				}

			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Set a new current parsing rule.
	 *
	 * @param pr
	 *            the new new current parsing rule
	 */
	public static void setNewCurrentParsingRule(ParsingRule pr) {
		if (pr != null) {
			Session.CURRENT_REGEX_RT = pr.getPropertyKey();
			currentParsingRuleIndex = pr.getIndex();
		}
	}
}
