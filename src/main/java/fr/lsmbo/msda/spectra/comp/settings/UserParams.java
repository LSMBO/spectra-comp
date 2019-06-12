
package fr.lsmbo.msda.spectra.comp.settings;

/**
 * Builds user parameters.
 * 
 * @author Aromdhani
 *
 */
public class UserParams {

	private String userName;
	private String timestamp;
	private String spectraCompVersion;
	private String projectName;
	private String firstPklList;
	private String secondPklList;

	private String dataSource;
	private SpectraComparatorParams comparison;
	private ParsingRulesParams parsingRules;

	public UserParams() {
		this("", "", "", "", "", "", "", new SpectraComparatorParams(), new ParsingRulesParams());
	}

	public UserParams(SpectraComparatorParams comparison, ParsingRulesParams parsingRules) {
		this("", "", "", "", "", "", "", comparison, parsingRules);
	}

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
	 * @return the comparison
	 */
	public SpectraComparatorParams getComparison() {
		return comparison;
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @return the first peak list
	 */
	public String getFirstPklList() {
		return firstPklList;
	}

	/**
	 * @return the parsingRules
	 */
	public ParsingRulesParams getParsingRules() {
		return parsingRules;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @return the second peak list
	 */
	public String getSecondPklList() {
		return secondPklList;
	}

	/**
	 * @return the spectraCompVersion
	 */
	public String getSpectraCompVersion() {
		return spectraCompVersion;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param comparison
	 *            the comparison to set
	 */
	public void setComparison(SpectraComparatorParams comparison) {
		this.comparison = comparison;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param firstPklList
	 *            the first peak list to set
	 */
	public void setFirstPklList(String firstPklList) {
		this.firstPklList = firstPklList;
	}

	/**
	 * @param parsingRules
	 *            the parsingRules to set
	 */
	public void setParsingRules(ParsingRulesParams parsingRules) {
		this.parsingRules = parsingRules;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @param secondPklList
	 *            the second peak list to set
	 */
	public void setSecondPklList(String secondPklList) {
		this.secondPklList = secondPklList;
	}

	/**
	 * @param spectraCompVersion
	 *            the spectraCompVersion to set
	 */
	public void setSpectraCompVersion(String spectraCompVersion) {
		this.spectraCompVersion = spectraCompVersion;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Override
	public String toString() {
		StringBuilder userParamsStr = new StringBuilder();
		return userParamsStr.toString();
	}
}
