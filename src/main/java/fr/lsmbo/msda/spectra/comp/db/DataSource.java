package fr.lsmbo.msda.spectra.comp.db;

/**
 * 
 * @author Aromdhani
 *
 */

public enum DataSource {

	DATABASE("database"), FILE("file");

	/**
	 * 
	 * @param dataSource
	 * @return data source type
	 */
	public static DataSource getType(String dataSource) {
		DataSource ds;
		switch (dataSource) {
		case "database":
			ds = DATABASE;
			break;
		case "file":
			ds = FILE;
			break;
		default:
			ds = DATABASE;
			break;
		}
		return ds;
	}

	private final String d_source;

	private DataSource(final String source) {
		assert (source != null && !source.trim().isEmpty()) : "invalid source!";
		d_source = source;
	}

	public String getSource() {
		return d_source;
	}
}
