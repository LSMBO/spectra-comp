package fr.lsmbo.msda.spectra.comp.db;

import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

// TODO: Auto-generated Javadoc
/**
 * The Enum DriverType.
 *
 * @author Aromdhani
 */

public enum DriverType {

	/** The h2. */
	H2("org.h2.Driver", "h2"), /** The postgresql. */
 POSTGRESQL("org.postgresql.Driver", "postgresql"), /** The sqlite. */
 SQLITE("org.sqlite.JDBC", "sqlite");

	/** The m jdbc driver. */
	private final String m_jdbcDriver;

	/** The m jdbc URL protocol. */
	private final String m_jdbcURLProtocol;

	/**
	 * Instantiates a new driver type.
	 *
	 * @param jdbcDriver the jdbc driver
	 * @param jdbcURLProtocol the jdbc URL protocol
	 */
	private DriverType(final String jdbcDriver, final String jdbcURLProtocol) {
		assert (StringsUtils.isEmpty(jdbcDriver)) : "DriverType() invalid jdbcDriver";
		assert (StringsUtils.isEmpty(jdbcURLProtocol)) : "DriverType() invalid jdbcURLProtocol";

		m_jdbcDriver = jdbcDriver;

		m_jdbcURLProtocol = jdbcURLProtocol;
	}

	/**
	 * Gets the jdbc driver.
	 *
	 * @return the jdbc driver
	 */
	public String getJdbcDriver() {
		return m_jdbcDriver;
	}

	/**
	 * Gets the jdbc URL protocol.
	 *
	 * @return the jdbc URL protocol
	 */
	public String getJdbcURLProtocol() {
		return m_jdbcURLProtocol;
	}

}
