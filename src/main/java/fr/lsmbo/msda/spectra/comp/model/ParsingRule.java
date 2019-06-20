package fr.lsmbo.msda.spectra.comp.model;

// TODO: Auto-generated Javadoc
/**
 * The Class ParsingRule.
 */
public class ParsingRule {

	/** The name. */
	private String name;
	
	/** The regex. */
	private String regex;
	
	/** The property key. */
	private String propertyKey;
	
	/** The index. */
	private Integer index;

	/**
	 * Instantiates a new parsing rule.
	 *
	 * @param name the name
	 * @param regex the regex
	 * @param key the key
	 * @param index the index
	 */
	public ParsingRule(String name, String regex, String key, Integer index) {
		this.name = name;
		this.regex = regex;
		this.propertyKey = key;
		this.index = index;
	}

	/**
	 * Gets the full description.
	 *
	 * @return the full description
	 */
	public String getFullDescription() {
		return "[" + propertyKey + " " + name + ": " + regex + "]";
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the property key.
	 *
	 * @return the property key
	 */
	public String getPropertyKey() {
		return propertyKey;
	}

	/**
	 * Gets the regex.
	 *
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the property key.
	 *
	 * @param key the new property key
	 */
	public void setPropertyKey(String key) {
		this.propertyKey = key;
	}

	/**
	 * Sets the regex.
	 *
	 * @param regex the new regex
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
