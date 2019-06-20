package fr.lsmbo.msda.spectra.comp.model;

import java.util.HashMap;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.db.DataSource;

// TODO: Auto-generated Javadoc
/**
 * The Class SpectraParams.
 */
public class SpectraParams {
	
	/** The ref pkl by data source map. */
	// Return reference Pkl in map
	private Map<DataSource, Object> refPklByDataSourceMap = new HashMap<>();
	
	/** The tested pkl by data source map. */
	// Return tested Pkl in map
	private Map<DataSource, Object> testedPklByDataSourceMap = new HashMap<>();
	
	/** The ref db name. */
	// Database name
	private String refDbName = null;
	
	/** The test db name. */
	private String testDbName = null;

	/**
	 * Default constructor.
	 */
	public SpectraParams() {
	}

	/**
	 * Instantiates a new spectra params.
	 *
	 * @param refPklByDataSourceMap the ref pkl by data source map
	 * @param testedPklByDataSourceMap the tested pkl by data source map
	 * @param refDbName the ref db name
	 * @param testDbName the test db name
	 */
	public SpectraParams(Map<DataSource, Object> refPklByDataSourceMap,
			Map<DataSource, Object> testedPklByDataSourceMap, String refDbName, String testDbName) {
		super();
		this.refPklByDataSourceMap = refPklByDataSourceMap;
		this.testedPklByDataSourceMap = testedPklByDataSourceMap;
		this.refDbName = refDbName;
		this.testDbName = testDbName;
	}

	/**
	 * Gets the ref pkl by data source map.
	 *
	 * @return the refPklByDataSourceMap
	 */
	public final Map<DataSource, Object> getRefPklByDataSourceMap() {
		return refPklByDataSourceMap;
	}

	/**
	 * Sets the ref pkl by data source map.
	 *
	 * @param refPklByDataSourceMap            the refPklByDataSourceMap to set
	 */
	public final void setRefPklByDataSourceMap(Map<DataSource, Object> refPklByDataSourceMap) {
		this.refPklByDataSourceMap = refPklByDataSourceMap;
	}

	/**
	 * Gets the tested pkl by data source map.
	 *
	 * @return the testedPklByDataSourceMap
	 */
	public final Map<DataSource, Object> getTestedPklByDataSourceMap() {
		return testedPklByDataSourceMap;
	}

	/**
	 * Sets the tested pkl by data source map.
	 *
	 * @param testedPklByDataSourceMap            the testedPklByDataSourceMap to set
	 */
	public final void setTestedPklByDataSourceMap(Map<DataSource, Object> testedPklByDataSourceMap) {
		this.testedPklByDataSourceMap = testedPklByDataSourceMap;
	}

	/**
	 * Gets the ref db name.
	 *
	 * @return the refDbName
	 */
	public final String getRefDbName() {
		return refDbName;
	}

	/**
	 * Sets the ref db name.
	 *
	 * @param refDbName            the refDbName to set
	 */
	public final void setRefDbName(String refDbName) {
		this.refDbName = refDbName;
	}

	/**
	 * Gets the test db name.
	 *
	 * @return the testDbName
	 */
	public final String getTestDbName() {
		return testDbName;
	}

	/**
	 * Sets the test db name.
	 *
	 * @param testDbName            the testDbName to set
	 */
	public final void setTestDbName(String testDbName) {
		this.testDbName = testDbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Spectra Params [reference pkl by data source=" + refPklByDataSourceMap
				+ ", tested Pkl by data source =" + testedPklByDataSourceMap + ", reference db name=" + refDbName
				+ ", test db name=" + testDbName + "]";
	}

}
