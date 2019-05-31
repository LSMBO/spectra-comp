
package fr.lsmbo.msda.spectra.comp.settings;

/**
 * Builds spectra-comp properties.
 * 
 * @author Aromdhani
 *
 */
public class Version {

	private String name  ;
	private String description ;
	private String version ;
	private String build ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildDate() {
		return build;
	}

	public void setBuildDate(String buildDate) {
		this.build = buildDate;
	}

	@Override
	public String toString() {
		StringBuilder spectraCompStr = new StringBuilder();
		spectraCompStr.append("\n").append("##name: ").append(this.name).append("\n").append("##description: ")
				.append(this.description).append("\n").append("##version: ").append(this.version).append("\n")
				.append("##build date: ").append(this.build);
		return spectraCompStr.toString();
	}
}
