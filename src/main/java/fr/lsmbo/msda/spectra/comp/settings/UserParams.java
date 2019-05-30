/*
 * 
 */
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
	private ComparisonSettings comparison;
	
	public UserParams() {
		this("", "", "", new ComparisonSettings());
	}

	public UserParams(ComparisonSettings comparison) {
		this("", "", "", comparison);
	}

	public UserParams(String userName, String timestamp, String spectraCompVersion, ComparisonSettings comparison) {
		super();
		this.userName = userName;
		this.timestamp = timestamp;
		this.spectraCompVersion = spectraCompVersion;
		this.comparison = comparison;
	
	}

	public ComparisonSettings getComparison() {
		return comparison;
	}

	public String getRecoverVersion() {
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


	public void setRecoverVersion(String recoverVersion) {
		this.spectraCompVersion = recoverVersion;
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
