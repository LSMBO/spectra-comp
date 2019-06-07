package fr.lsmbo.msda.spectra.comp.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

public class Spectrum {

	public static final int MASSES_INDEX = 0;
	public static final int INTENSITIES_INDEX = 1;

	private long m_id;

	private Integer m_firstScan;
	private Integer m_lastScan;

	private Float m_firstTime;
	private Float m_lastTime;

	private byte[] m_intensityList = null;
	private byte[] m_mozList = null;
	private double[][] m_massIntensitiesValues = null;

	private Integer m_precursorCharge;
	private Float m_precursorIntensity;
	private Double m_precursorMoz;

	private String m_title = null;
	private double cosTheta;
	private Integer nbMatch;
	private Boolean identified;
	private Float retentionTime;
	// The index of the line start in file of the spectrum
	private Integer lineStart = 0;
	// The index of the line stop in file of the spectrum
	private Integer lineStop = 0;
	// May be empty if file is too big
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	public Spectrum() {
	}

	public Spectrum(long id, Integer firstScan, Float firstTime, Float lastTime, byte[] intensityList, byte[] mozeList,
			Integer precursorCharge, Float precursorIntensity, Double precursorMoz, String title) {
		m_id = id;
		m_firstScan = firstScan;
		m_firstTime = firstTime;
		m_lastTime = lastTime;

		m_intensityList = intensityList;
		m_mozList = mozeList;

		m_precursorCharge = precursorCharge;
		m_precursorIntensity = precursorIntensity;
		m_precursorMoz = precursorMoz;
		m_title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	public double getCosTheta() {
		return cosTheta;
	}

	public Boolean getIdentified() {
		return identified;
	}

	public Float getRetentionTime() {
		return retentionTime;
	}

	public void setCosTheta(double cosTheta) {
		this.cosTheta = cosTheta;
	}

	public void setRetentionTime(Float retentionTime) {
		this.retentionTime = retentionTime;
	}

	/**
	 * Set the retention time from title
	 */
	public void setRetentionTimeFromTitle() {
		if ((!StringsUtils.isEmpty(m_title)) && (!StringsUtils.isEmpty(Session.CURRENT_REGEX_RT))) {
			setRetentionTimeFromTitle(SoftwareType.getParsingRule(Session.CURRENT_REGEX_RT).getRegexValue());
		}
	}

	/**
	 * Set the retention from title
	 * 
	 * @param regex
	 *            the used regex to retrieve the retention time from title
	 */
	public void setRetentionTimeFromTitle(String regex) {
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(this.m_title);
			Float rt = 0F;
			if (m.find())
				rt = new Float(m.group(1));
			this.retentionTime = rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the m_id
	 */
	public final long getM_id() {
		return m_id;
	}

	/**
	 * @param m_id the m_id to set
	 */
	public final void setM_id(long m_id) {
		this.m_id = m_id;
	}

	/**
	 * @return the m_firstScan
	 */
	public final Integer getM_firstScan() {
		return m_firstScan;
	}

	/**
	 * @param m_firstScan the m_firstScan to set
	 */
	public final void setM_firstScan(Integer m_firstScan) {
		this.m_firstScan = m_firstScan;
	}

	/**
	 * @return the m_lastScan
	 */
	public final Integer getM_lastScan() {
		return m_lastScan;
	}

	/**
	 * @param m_lastScan the m_lastScan to set
	 */
	public final void setM_lastScan(Integer m_lastScan) {
		this.m_lastScan = m_lastScan;
	}

	/**
	 * @return the m_firstTime
	 */
	public final Float getM_firstTime() {
		return m_firstTime;
	}

	/**
	 * @param m_firstTime the m_firstTime to set
	 */
	public final void setM_firstTime(Float m_firstTime) {
		this.m_firstTime = m_firstTime;
	}

	/**
	 * @return the m_lastTime
	 */
	public final Float getM_lastTime() {
		return m_lastTime;
	}

	/**
	 * @param m_lastTime the m_lastTime to set
	 */
	public final void setM_lastTime(Float m_lastTime) {
		this.m_lastTime = m_lastTime;
	}

	/**
	 * @return the m_intensityList
	 */
	public final byte[] getM_intensityList() {
		return m_intensityList;
	}

	/**
	 * @param m_intensityList the m_intensityList to set
	 */
	public final void setM_intensityList(byte[] m_intensityList) {
		this.m_intensityList = m_intensityList;
	}

	/**
	 * @return the m_mozList
	 */
	public final byte[] getM_mozList() {
		return m_mozList;
	}

	/**
	 * @param m_mozList the m_mozList to set
	 */
	public final void setM_mozList(byte[] m_mozList) {
		this.m_mozList = m_mozList;
	}

	/**
	 * @return the m_massIntensitiesValues
	 */
	public final double[][] getM_massIntensitiesValues() {
		return m_massIntensitiesValues;
	}

	/**
	 * @param m_massIntensitiesValues the m_massIntensitiesValues to set
	 */
	public final void setM_massIntensitiesValues(double[][] m_massIntensitiesValues) {
		this.m_massIntensitiesValues = m_massIntensitiesValues;
	}

	/**
	 * @return the m_precursorCharge
	 */
	public final Integer getM_precursorCharge() {
		return m_precursorCharge;
	}

	/**
	 * @param m_precursorCharge the m_precursorCharge to set
	 */
	public final void setM_precursorCharge(Integer m_precursorCharge) {
		this.m_precursorCharge = m_precursorCharge;
	}

	/**
	 * @return the m_precursorIntensity
	 */
	public final Float getM_precursorIntensity() {
		return m_precursorIntensity;
	}

	/**
	 * @param m_precursorIntensity the m_precursorIntensity to set
	 */
	public final void setM_precursorIntensity(Float m_precursorIntensity) {
		this.m_precursorIntensity = m_precursorIntensity;
	}

	/**
	 * @return the m_precursorMoz
	 */
	public final Double getM_precursorMoz() {
		return m_precursorMoz;
	}

	/**
	 * @param m_precursorMoz the m_precursorMoz to set
	 */
	public final void setM_precursorMoz(Double m_precursorMoz) {
		this.m_precursorMoz = m_precursorMoz;
	}

	/**
	 * @return the m_title
	 */
	public final String getM_title() {
		return m_title;
	}

	/**
	 * @param m_title the m_title to set
	 */
	public final void setM_title(String m_title) {
		this.m_title = m_title;
	}

	/**
	 * @return the nbMatch
	 */
	public final Integer getNbMatch() {
		return nbMatch;
	}

	/**
	 * @param nbMatch the nbMatch to set
	 */
	public final void setNbMatch(Integer nbMatch) {
		this.nbMatch = nbMatch;
	}

	/**
	 * @param identified the identified to set
	 */
	public final void setIdentified(Boolean identified) {
		this.identified = identified;
	}

	/**
	 * @return the lineStart
	 */
	public final Integer getLineStart() {
		return lineStart;
	}

	/**
	 * @param lineStart
	 *            the lineStart to set
	 */
	public final void setLineStart(Integer lineStart) {
		this.lineStart = lineStart;
	}

	/**
	 * @return the lineStop
	 */
	public final Integer getLineStop() {
		return lineStop;
	}

	/**
	 * @param lineStop
	 *            the lineStop to set
	 */
	public final void setLineStop(Integer lineStop) {
		this.lineStop = lineStop;
	}

	/**
	 * @param fragment
	 *            the fragment to set
	 */
	public void addFragment(Fragment fragment) {
		this.fragments.add(fragment);
	}

	/**
	 * @param fragments
	 *            the list of fragment to set
	 */
	public void addFragments(ArrayList<Fragment> fragments) {
		this.fragments.addAll(fragments);
	}

	public double[] getMasses() {
		return getMassIntensityValues()[MASSES_INDEX];
	}

	public double[] getIntensities() {
		return getMassIntensityValues()[INTENSITIES_INDEX];
	}

	public double[][] getMassIntensityValues() {
		if (m_massIntensitiesValues == null) {
			if ((m_intensityList == null) || (m_mozList == null)) {
				return null;
			}
			ByteBuffer intensityByteBuffer = ByteBuffer.wrap(m_intensityList).order(ByteOrder.LITTLE_ENDIAN);
			FloatBuffer intensityFloatBuffer = intensityByteBuffer.asFloatBuffer();
			double[] intensityDoubleArray = new double[intensityFloatBuffer.remaining()];
			for (int i = 0; i < intensityDoubleArray.length; i++) {
				intensityDoubleArray[i] = (double) intensityFloatBuffer.get();
			}

			ByteBuffer massByteBuffer = ByteBuffer.wrap(m_mozList).order(ByteOrder.LITTLE_ENDIAN);
			DoubleBuffer massDoubleBuffer = massByteBuffer.asDoubleBuffer();
			double[] massDoubleArray = new double[massDoubleBuffer.remaining()];
			for (int i = 0; i < massDoubleArray.length; i++) {
				massDoubleArray[i] = massDoubleBuffer.get();
			}

			int size = intensityDoubleArray.length;
			m_massIntensitiesValues = new double[2][size];
			for (int i = 0; i < size; i++) {
				m_massIntensitiesValues[MASSES_INDEX][i] = massDoubleArray[i];
				m_massIntensitiesValues[INTENSITIES_INDEX][i] = intensityDoubleArray[i];
			}
		}
		return m_massIntensitiesValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		str.append("id: ").append(m_id).append("; title: ").append(m_title).append(" ;Retentio time: ")
				.append(retentionTime).append(" ;precursor charge: ").append(m_precursorCharge)
				.append(" ;precursor moz: ").append(m_precursorMoz).append(" ;precursor intensity: ")
				.append(m_precursorIntensity);
		return str.toString();
	}

}
