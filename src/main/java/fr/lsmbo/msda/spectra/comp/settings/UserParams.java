/*
 * 
 */
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

	public UserParams() {
		this("", "", "", DataSource.DATABASE, new ComparisonSettings());
	}

	public UserParams(ComparisonSettings comparison) {
		this("", "", "", DataSource.DATABASE, comparison);
	}

	public UserParams(String userName, String timestamp, String spectraCompVersion, DataSource dataSource,
			ComparisonSettings comparison) {
		super();
		this.userName = userName;
		this.timestamp = timestamp;
		this.spectraCompVersion = spectraCompVersion;
		this.dataSource = dataSource;
		this.comparison = comparison;

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

	@Override
	public String toString() {
		StringBuilder userParamsStr = new StringBuilder();
		userParamsStr.append("\n").append("##Version: ").append(this.spectraCompVersion);
		return userParamsStr.toString();
	}
}
