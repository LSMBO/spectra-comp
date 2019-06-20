
package fr.lsmbo.msda.spectra.comp.settings;

// TODO: Auto-generated Javadoc
/**
 * Builds user parameters.
 * 
 * @author Aromdhani
 *
 */
public class UserParams {

	/** The user name. */
	private String userName;
	
	/** The timestamp. */
	private String timestamp;
	
	/** The spectra comp version. */
	private String spectraCompVersion;
	
	/** The project name. */
	private String projectName;
	
	/** The first pkl list. */
	private String firstPklList;
	
	/** The second pkl list. */
	private String secondPklList;

	/** The data source. */
	private String dataSource;
	
	/** The comparison. */
	private SpectraComparatorParams comparison;
	
	/** The parsing rules. */
	private ParsingRulesParams parsingRules;

	/**
	 * Instantiates a new user params.
	 */
	public UserParams() {
		this("", "", "", "", "", "", "", new SpectraComparatorParams(), new ParsingRulesParams());
	}

	/**
	 * Instantiates a new user params.
	 *
	 * @param comparison the comparison
	 * @param parsingRules the parsing rules
	 */
	public UserParams(SpectraComparatorParams comparison, ParsingRulesParams parsingRules) {
		this("", "", "", "", "", "", "", comparison, parsingRules);
	}

	/**
	 * Instantiates a new user params.
	 *
	 * @param userName the user name
	 * @param timestamp the timestamp
	 * @param spectraCompVersion the spectra comp version
	 * @param projectName the project name
	 * @param firstPklList the first pkl list
	 * @param secondPklList the second pkl list
	 * @param dataSource the data source
	 * @param comparison the comparison
	 * @param parsingRules the parsing rules
	 */
	public UserParams(String userName, String timestamp, String spectraCompVersion, String projectName,
			String firstPklList, String secondPklList, String dataSource, SpectraComparatorParams comparison,
			ParsingRulesParams parsingRules) {
		super();
		this.userName = userName;
		this.timestamp = timestamp;
		this.spectraCompVersion = spectraCompVersion;
		this.projectName = projectName;
		this.firstPklList = firstPklList;
		this.secondPklList = secondPklList;
		this.dataSource = dataSource;
		this.comparison = comparison;
		this.parsingRules = parsingRules;
	}

	/**
	 * Gets the comparison.
	 *
	 * @return the comparison
	 */
	public SpectraComparatorParams getComparison() {
		return comparison;
	}

	/**
	 * Gets the data source.
	 *
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * Gets the first pkl list.
	 *
	 * @return the first peak list
	 */
	public String getFirstPklList() {
		return firstPklList;
	}

	/**
	 * Gets the parsing rules.
	 *
	 * @return the parsingRules
	 */
	public ParsingRulesParams getParsingRules() {
		return parsingRules;
	}

	/**
	 * Gets the project name.
	 *
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Gets the second pkl list.
	 *
	 * @return the second peak list
	 */
	public String getSecondPklList() {
		return secondPklList;
	}

	/**
	 * Gets the spectra comp version.
	 *
	 * @return the spectraCompVersion
	 */
	public String getSpectraCompVersion() {
		return spectraCompVersion;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the comparison.
	 *
	 * @param comparison            the comparison to set
	 */
	public void setComparison(SpectraComparatorParams comparison) {
		this.comparison = comparison;
	}

	/**
	 * Sets the data source.
	 *
	 * @param dataSource            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Sets the first pkl list.
	 *
	 * @param firstPklList            the first peak list to set
	 */
	public void setFirstPklList(String firstPklList) {
		this.firstPklList = firstPklList;
	}

	/**
	 * Sets the parsing rules.
	 *
	 * @param parsingRules            the parsingRules to set
	 */
	public void setParsingRules(ParsingRulesParams parsingRules) {
		this.parsingRules = parsingRules;
	}

	/**
	 * Sets the project name.
	 *
	 * @param projectName            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Sets the second pkl list.
	 *
	 * @param secondPklList            the second peak list to set
	 */
	public void setSecondPklList(String secondPklList) {
		this.secondPklList = secondPklList;
	}

	/**
	 * Sets the spectra comp version.
	 *
	 * @param spectraCompVersion            the spectraCompVersion to set
	 */
	public void setSpectraCompVersion(String spectraCompVersion) {
		this.spectraCompVersion = spectraCompVersion;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder userParamsStr = new StringBuilder();
		return userParamsStr.toString();
	}
}
