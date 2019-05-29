package fr.lsmbo.msda.spectra.comp.db;

/**
 * 
 * @author Aromdhani
 *
 */

public enum DriverType {

	H2("org.h2.Driver", "h2"), POSTGRESQL("org.postgresql.Driver", "postgresql"), SQLITE("org.sqlite.JDBC", "sqlite");

	private final String m_jdbcDriver;

	private final String m_jdbcURLProtocol;

	private DriverType(final String jdbcDriver, final String jdbcURLProtocol) {
		assert (jdbcDriver != null && !jdbcDriver.trim().isEmpty()) : "DriverType() invalid jdbcDriver";
		assert (jdbcURLProtocol != null && !jdbcURLProtocol.trim().isEmpty()) : "DriverType() invalid jdbcURLProtocol";

		m_jdbcDriver = jdbcDriver;

		m_jdbcURLProtocol = jdbcURLProtocol;
	}

	public String getJdbcDriver() {
		return m_jdbcDriver;
	}

	public String getJdbcURLProtocol() {
		return m_jdbcURLProtocol;
	}

}
