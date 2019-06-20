
package fr.lsmbo.msda.spectra.comp.settings;

// TODO: Auto-generated Javadoc
/**
 * Builds spectra-comp properties.
 * 
 * @author Aromdhani
 *
 */
public class Version {

	/** The name. */
	private String name  ;
	
	/** The description. */
	private String description ;
	
	/** The version. */
	private String version ;
	
	/** The build. */
	private String build ;

	/**
	 * Gets the builds the date.
	 *
	 * @return the builds the date
	 */
	public String getBuildDate() {
		return build;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the builds the date.
	 *
	 * @param buildDate the new builds the date
	 */
	public void setBuildDate(String buildDate) {
		this.build = buildDate;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder spectraCompStr = new StringBuilder();
		spectraCompStr.append("\n").append("##name: ").append(this.name).append("\n").append("##description: ")
				.append(this.description).append("\n").append("##version: ").append(this.version).append("\n")
				.append("##build date: ").append(this.build);
		return spectraCompStr.toString();
	}
}
