package fr.lsmbo.msda.spectra.comp.model;

import java.util.HashMap;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

public class SpectraParams {
	// Return reference Pkl in map
	private Map<DataSource, Object> refPklByDataSourceMap = new HashMap<>();
	// Return tested Pkl in map
	private Map<DataSource, Object> testedPklByDataSourceMap = new HashMap<>();
	// Database name
	private String refDbName = null;
	private String testDbName = null;

	/**
	 * Default constructor
	 */
	public SpectraParams() {
	}

	/**
	 * @param refPklByDataSourceMap
	 * @param testedPklByDataSourceMap
	 * @param refDbName
	 * @param testDbName
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
	 * @return the refPklByDataSourceMap
	 */
	public final Map<DataSource, Object> getRefPklByDataSourceMap() {
		return refPklByDataSourceMap;
	}

	/**
	 * @param refPklByDataSourceMap
	 *            the refPklByDataSourceMap to set
	 */
	public final void setRefPklByDataSourceMap(Map<DataSource, Object> refPklByDataSourceMap) {
		this.refPklByDataSourceMap = refPklByDataSourceMap;
	}

	/**
	 * @return the testedPklByDataSourceMap
	 */
	public final Map<DataSource, Object> getTestedPklByDataSourceMap() {
		return testedPklByDataSourceMap;
	}

	/**
	 * @param testedPklByDataSourceMap
	 *            the testedPklByDataSourceMap to set
	 */
	public final void setTestedPklByDataSourceMap(Map<DataSource, Object> testedPklByDataSourceMap) {
		this.testedPklByDataSourceMap = testedPklByDataSourceMap;
	}

	/**
	 * @return the refDbName
	 */
	public final String getRefDbName() {
		return refDbName;
	}

	/**
	 * @param refDbName
	 *            the refDbName to set
	 */
	public final void setRefDbName(String refDbName) {
		this.refDbName = refDbName;
	}

	/**
	 * @return the testDbName
	 */
	public final String getTestDbName() {
		return testDbName;
	}

	/**
	 * @param testDbName
	 *            the testDbName to set
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
