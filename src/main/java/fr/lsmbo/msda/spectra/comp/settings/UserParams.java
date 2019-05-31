
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
	private DataSource dataSource = DataSource.DATABASE;
	private ComparisonSettings comparison;
	private ParsingRulesSettings parsingRules;

	public UserParams() {
		this("", "", "", DataSource.DATABASE, new ComparisonSettings(), new ParsingRulesSettings());
	}

	public UserParams(ComparisonSettings comparison, ParsingRulesSettings parsingRules) {
		this("", "", "", DataSource.DATABASE, comparison, parsingRules);
	}

	public UserParams(String userName, String timestamp, String spectraCompVersion, DataSource dataSource,
			ComparisonSettings comparison, ParsingRulesSettings parsingRules) {
		super();
		this.userName = userName;
		this.timestamp = timestamp;
		this.spectraCompVersion = spectraCompVersion;
		this.dataSource = dataSource;
		this.comparison = comparison;
		this.parsingRules = parsingRules;

	}

	public ComparisonSettings getComparison() {
		return comparison;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getSpectraCompVersion() {
		return spectraCompVersion;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getUserName() {
		return userName;
	}

	public void setComparison(ComparisonSettings comparison) {
		this.comparison = comparison;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setSpectraCompVersion(String spectraCompVersion) {
		this.spectraCompVersion = spectraCompVersion;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ParsingRulesSettings getParsingRules() {
		return parsingRules;
	}

	public void setParsingRules(ParsingRulesSettings parsingRules) {
		this.parsingRules = parsingRules;
	}

	@Override
	public String toString() {
		StringBuilder userParamsStr = new StringBuilder();
		return userParamsStr.toString();
	}
}
