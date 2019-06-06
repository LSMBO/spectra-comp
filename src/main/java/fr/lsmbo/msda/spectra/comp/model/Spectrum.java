package fr.lsmbo.msda.spectra.comp.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;

public class Spectrum {

	private long id;
	private Integer firstCycle;
	private Integer firstScan;
	private Float firstTime;
	private Long fragmentationRuleSetId;
	private byte[] intensityList;
	private boolean isSummed;
	private Integer lastCycle;
	private Integer lastScan;
	private Float lastTime;
	private byte[] mozList;
	private int peakCount;
	private long peaklistId;
	private Integer precursorCharge;
	private Float precursorIntensity;
	private Double precursorMoz;
	private String serializedProperties;
	private String title;
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

	public Spectrum(Long id, String title, Integer precursorCharge, Double precursorMoz, Float precursorIntensity,
			Integer peakCount) {
		this.id = id;
		this.title = title;
		this.precursorCharge = precursorCharge;
		this.precursorMoz = precursorMoz;
		this.precursorIntensity = precursorIntensity;
		this.peakCount = peakCount;
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

	public Integer getFirstCycle() {
		return this.firstCycle;
	}

	public Integer getFirstScan() {
		return this.firstScan;
	}

	public Float getFirstTime() {
		return firstTime;
	}

	public long getFragmentationRuleSetId() {
		return fragmentationRuleSetId;
	}

	public long getId() {
		return id;
	}

	public Boolean getIdentified() {
		return identified;
	}

	public byte[] getIntensityList() {
		return this.intensityList;
	}

	public boolean getIsSummed() {
		return this.isSummed;
	}

	public Integer getLastCycle() {
		return this.lastCycle;
	}

	public Integer getLastScan() {
		return this.lastScan;
	}

	public Float getLastTime() {
		return lastTime;
	}

	public byte[] getMozList() {
		return this.mozList;
	}

	public Integer getNbMatch() {
		return nbMatch;
	}

	public int getPeakCount() {
		return peakCount;
	}

	public long getPeaklistId() {
		return peaklistId;
	}

	public Integer getPrecursorCharge() {
		return this.precursorCharge;
	}

	public Float getPrecursorIntensity() {
		return this.precursorIntensity;
	}

	public Double getPrecursorMoz() {
		return precursorMoz;
	}

	public Float getRetentionTime() {
		return retentionTime;
	}

	public String getSerializedProperties() {
		return this.serializedProperties;
	}

	public String getTitle() {
		return this.title;
	}

	public void setCosTheta(double cosTheta) {
		this.cosTheta = cosTheta;
	}

	public void setFirstCycle(Integer firstCycle) {
		this.firstCycle = firstCycle;
	}

	public void setFirstScan(Integer firstScan) {
		this.firstScan = firstScan;
	}

	public void setFirstTime(final Float pFirstTime) {
		firstTime = pFirstTime;
	}

	public void setFragmentationRuleSetId(Long pFragmentationRuleSetId) {
		fragmentationRuleSetId = pFragmentationRuleSetId;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public void setIdentified(Boolean identified) {
		this.identified = identified;
	}

	public void setIntensityList(byte[] intensityList) {
		this.intensityList = intensityList;
	}

	public void setIsSummed(boolean isSummed) {
		this.isSummed = isSummed;
	}

	public void setLastCycle(Integer lastCycle) {
		this.lastCycle = lastCycle;
	}

	public void setLastScan(Integer lastScan) {
		this.lastScan = lastScan;
	}

	public void setLastTime(final Float pLastTime) {
		lastTime = pLastTime;
	}

	public void setMozList(byte[] mozList) {
		this.mozList = mozList;
	}

	public void setNbMatch(Integer nbMatch) {
		this.nbMatch = nbMatch;
	}

	public void setPeakCount(final int pPeakCount) {
		peakCount = pPeakCount;
	}

	public void setPeaklistId(final long pPeaklistId) {
		peaklistId = pPeaklistId;
	}

	public void setPrecursorCharge(Integer precursorCharge) {
		this.precursorCharge = precursorCharge;
	}

	public void setPrecursorIntensity(Float precursorIntensity) {
		this.precursorIntensity = precursorIntensity;
	}

	public void setPrecursorMoz(final Double pPrecursorMoz) {
		precursorMoz = pPrecursorMoz;
	}

	public void setRetentionTime(Float retentionTime) {
		this.retentionTime = retentionTime;
	}

	/**
	 * Set the retention time from title
	 */
	public void setRetentionTimeFromTitle() {
		if ((!StringsUtils.isEmpty(title)) && (!StringsUtils.isEmpty(Session.CURRENT_REGEX_RT))) {
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
			Matcher m = p.matcher(this.title);
			Float rt = 0F;
			if (m.find())
				rt = new Float(m.group(1));
			this.retentionTime = rt;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSerializedProperties(String serializedProperties) {
		this.serializedProperties = serializedProperties;
	}

	public void setTitle(String title) {
		this.title = title;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		str.append("id: ").append(id).append("; title: ").append(title).append(" ;Retentio time: ")
				.append(retentionTime).append(" ;precursor charge: ").append(precursorCharge)
				.append(" ;precursor moz: ").append(precursorMoz).append(" ;precursor intensity: ")
				.append(precursorIntensity).append(" ;peaks count: ").append(peakCount);
		return str.toString();
	}

}
