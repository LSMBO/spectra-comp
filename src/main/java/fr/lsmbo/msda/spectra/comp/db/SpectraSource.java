package fr.lsmbo.msda.spectra.comp.db;

// TODO: Auto-generated Javadoc
/**
 * The Enum SpectraSource.
 *
 * @author Aromdhani
 */

public enum SpectraSource {

	/** From a database. */
	DATABASE("database"),
	/** FRom a file. */
	FILE("file");

	/**
	 * Gets the type.
	 *
	 * @param dataSource
	 *            the data source
	 * @return data source type
	 */
	public static SpectraSource getType(String dataSource) {
		SpectraSource ds;
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
	private SpectraSource(final String source) {
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
