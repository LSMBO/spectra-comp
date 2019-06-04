package fr.lsmbo.msda.spectra.comp.db;

import fr.lsmbo.msda.spectra.comp.utils.StringUtils;

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
		assert (StringUtils.isEmpty(jdbcDriver)) : "DriverType() invalid jdbcDriver";
		assert (StringUtils.isEmpty(jdbcURLProtocol)) : "DriverType() invalid jdbcURLProtocol";

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
