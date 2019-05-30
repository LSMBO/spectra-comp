package fr.lsmbo.msda.spectra.comp.db;

/**
 * 
 * @author Aromdhani
 *
 */

public enum DataSource {

	DATABASE("database"), FILE("file");

	private final String d_source;
	private DataSource(final String source) {
		assert (source != null && !source.trim().isEmpty()) : "invalid source!";
		d_source = source;
	}
	public String getSource() {
		return d_source;
	}
	
}
