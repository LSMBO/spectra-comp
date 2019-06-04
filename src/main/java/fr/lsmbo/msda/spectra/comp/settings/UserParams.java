
package fr.lsmbo.msda.spectra.comp.settings;

import fr.lsmbo.msda.spectra.comp.db.DataSource;

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
	private String firstDataset;
	private String secondDataset;

	private DataSource dataSource = DataSource.DATABASE;
	private ComparisonSettings comparison;
	private ParsingRulesSettings parsingRules;

	public UserParams() {
		this("", "", "", "", "", "", DataSource.DATABASE, new ComparisonSettings(), new ParsingRulesSettings());
	}

	public UserParams(ComparisonSettings comparison, ParsingRulesSettings parsingRules) {
		this("", "", "", "", "", "", DataSource.DATABASE, comparison, parsingRules);
	}

	public UserParams(String userName, String timestamp, String spectraCompVersion, String projectName,
			String firstDataset, String secondDataset, DataSource dataSource,

			ComparisonSettings comparison, ParsingRulesSettings parsingRules) {
		super();
		this.userName = userName;
		this.timestamp = timestamp;
		this.spectraCompVersion = spectraCompVersion;
		this.projectName = projectName;
		this.firstDataset = firstDataset;
		this.secondDataset = secondDataset;
		this.dataSource = dataSource;
		this.comparison = comparison;
		this.parsingRules = parsingRules;

	}

	/**
	 * @return the comparison
	 */
	public ComparisonSettings getComparison() {
		return comparison;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @return the firstDataset
	 */
	public String getFirstDataset() {
		return firstDataset;
	}

	/**
	 * @return the parsingRules
	 */
	public ParsingRulesSettings getParsingRules() {
		return parsingRules;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @return the secondDataset
	 */
	public String getSecondDataset() {
		return secondDataset;
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
	 * @param comparison the comparison to set
	 */
	public void setComparison(ComparisonSettings comparison) {
		this.comparison = comparison;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param firstDataset the firstDataset to set
	 */
	public void setFirstDataset(String firstDataset) {
		this.firstDataset = firstDataset;
	}

	/**
	 * @param parsingRules the parsingRules to set
	 */
	public void setParsingRules(ParsingRulesSettings parsingRules) {
		this.parsingRules = parsingRules;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @param secondDataset the secondDataset to set
	 */
	public void setSecondDataset(String secondDataset) {
		this.secondDataset = secondDataset;
	}

	/**
	 * @param spectraCompVersion the spectraCompVersion to set
	 */
	public void setSpectraCompVersion(String spectraCompVersion) {
		this.spectraCompVersion = spectraCompVersion;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param userName the userName to set
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
