package fr.lsmbo.msda.spectra.comp.db;

// TODO: Auto-generated Javadoc
/**
 * The Enum DataSource.
 *
 * @author Aromdhani
 */

public enum DataSource {

	/** The database. */
	DATABASE("database"), /** The file. */
 FILE("file");

	/**
	 * Gets the type.
	 *
	 * @param dataSource
	 *            the data source
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

	/** The data source. */
	private final String d_source;

	/**
	 * Instantiates a new data source.
	 *
	 * @param source
	 *            the source
	 */
	private DataSource(final String source) {
		assert (source != null && !source.trim().isEmpty()) : "invalid source!";
		d_source = source;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public String getSource() {
		return d_source;
	}
}
