package fr.lsmbo.msda.spectra.comp.model;

import java.util.HashMap;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.db.SpectraSource;

// TODO: Auto-generated Javadoc
/**
 * The Class SpectraParams.
 */
public class Parameters {

	/** The ref pkl by data source map. */
	// Return reference Pkl in map
	private Map<SpectraSource, Object> refPklByDataSourceMap = new HashMap<>();

	/** The tested pkl by data source map. */
	// Return tested Pkl in map
	private Map<SpectraSource, Object> testedPklByDataSourceMap = new HashMap<>();

	/** The ref db name. */
	// Database name
	private Long refProjectId = -1L;

	/** The test db name. */
	private Long testProjectId = -1L;

	/**
	 * Default constructor.
	 */
	public Parameters() {
	}

	/**
	 * Instantiates a new spectra params.
	 *
	 * @param refPklByDataSourceMap    the ref pkl by data source map
	 * @param testedPklByDataSourceMap the tested pkl by data source map
	 * @param refDbName                the ref db name
	 * @param testDbName               the test db name
	 */
	public Parameters(Map<SpectraSource, Object> refPklByDataSourceMap,
			Map<SpectraSource, Object> testedPklByDataSourceMap, Long refProjectId, Long testProjectId) {
		super();
		this.refPklByDataSourceMap = refPklByDataSourceMap;
		this.testedPklByDataSourceMap = testedPklByDataSourceMap;
		this.refProjectId = refProjectId;
		this.testProjectId = testProjectId;
	}

	/**
	 * Gets the ref pkl by data source map.
	 *
	 * @return the refPklByDataSourceMap
	 */
	public final Map<SpectraSource, Object> getRefPklByDataSourceMap() {
		return refPklByDataSourceMap;
	}

	/**
	 * Sets the ref pkl by data source map.
	 *
	 * @param refPklByDataSourceMap the refPklByDataSourceMap to set
	 */
	public final void setRefPklByDataSourceMap(Map<SpectraSource, Object> refPklByDataSourceMap) {
		this.refPklByDataSourceMap = refPklByDataSourceMap;
	}

	/**
	 * Gets the tested pkl by data source map.
	 *
	 * @return the testedPklByDataSourceMap
	 */
	public final Map<SpectraSource, Object> getTestedPklByDataSourceMap() {
		return testedPklByDataSourceMap;
	}

	/**
	 * Sets the tested pkl by data source map.
	 *
	 * @param testedPklByDataSourceMap the testedPklByDataSourceMap to set
	 */
	public final void setTestedPklByDataSourceMap(Map<SpectraSource, Object> testedPklByDataSourceMap) {
		this.testedPklByDataSourceMap = testedPklByDataSourceMap;
	}

	/**
	 * Gets the ref project id.
	 *
	 * @return the reference project id
	 */
	public final Long getRefProjectId() {
		return refProjectId;
	}

	/**
	 * Sets the ref project id.
	 *
	 * @param refDbName he reference project id
	 */
	public final void setRefProjectId(Long refProjectId) {
		this.refProjectId = refProjectId;
	}

	/**
	 * Gets the test project id.
	 *
	 * @return the test project id
	 */
	public final Long getTestProjectId() {
		return testProjectId;
	}

	/**
	 * Sets the test test project id.
	 *
	 * @param testProjectId the test project id
	 */
	public final void setTestDbName(Long testProjectId) {
		this.testProjectId = testProjectId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Spectra parameters: [reference peaklist by data source=" + refPklByDataSourceMap
				+ ", tested peaklist by data source =" + testedPklByDataSourceMap + ", reference project id=#"
				+ refProjectId + ", test project id=#" + testProjectId + "]";
	}

}
