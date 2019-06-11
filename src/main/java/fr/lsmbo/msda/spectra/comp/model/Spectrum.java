package fr.lsmbo.msda.spectra.comp.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
	private Integer nbMatch;
	private Boolean identified;
	private float retentionTime;

	// The index of the line start in file of the spectrum
	private Integer lineStart = 0;
	// The index of the line stop in file of the spectrum
	private Integer lineStop = 0;
	// May be empty if file is too big
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	// May be empty if file is too big
	private ArrayList<Fragment> sortedFragments = new ArrayList<Fragment>();
	private ArrayList<Fragment> fragmentEqualsToChart = new ArrayList<Fragment>();
	private Fragment[] nbIntensePeaks;
	private Double[] squareRootnbIntensePeaks;
	private Integer nbFragments = 0;
	// The index of the most intense fragment.
	private Integer indexOfMostIntenseFragment = 0;
	// The maximum of Moz
	private double fragmentMaxMoz = 0;
	// The fragment maximum intensity
	private float fragmentMaxIntensity = 0;
	// Median of fragment intensity
	private float medianFragmentsIntensities = 0;
	// The average of fragment intensity
	private float averageFragmentsIntensities = 0;
	// The number of peaks
	private int nbPeaks;
	// TODO More about the reference spectrum
	private double cosTheta = 0D;
	private double deltaMozWithReferenceSpectrum = 0F;
	private int deltaRetentionTimeWithReferenceSpectrum = 0;
	private int nbPeaksIdenticalWithReferenceSpectrum = 0;
	private String titleReferenceSpectrum;

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

	/**
	 * @param fragment the fragment to set
	 */
	public void addFragment(Fragment fragment) {
		this.fragments.add(fragment);
	}

	/**
	 * @param fragments the list of fragment to set
	 */
	public void addFragments(ArrayList<Fragment> fragments) {
		this.fragments.addAll(fragments);
	}

	/**
	 * Compute fragment values . It computes the average and the median of fragment
	 * intensities.
	 */
	private void computeFragmentValues() {
		// TODO Compute here the sortedFragments array, order by intensity
		// ascendant
		if (this.averageFragmentsIntensities == 0 && this.medianFragmentsIntensities == 0 && this.fragmentMaxMoz == 0
				&& this.fragmentMaxIntensity == 0) {
			// lazy computing
			this.nbFragments = this.fragments.size();
			if (this.nbFragments > 0) {
				Float sum = 0F;
				Float[] intensities = new Float[this.nbFragments];
				for (Integer i = 0; i < this.nbFragments; i++) {
					Fragment fragment = fragments.get(i);
					// for(Fragment fragment: fragments) {
					sum += fragment.getIntensity();
					// intensities[fragment.getId()-1] =
					// fragment.getIntensity();
					intensities[i] = fragment.getIntensity();
					if (fragment.getIntensity() > this.fragmentMaxIntensity) {
						this.fragmentMaxIntensity = fragment.getIntensity();
						// this.indexOfMostIntenseFragment = fragment.getId();
						this.indexOfMostIntenseFragment = i;
					}
				}
				this.averageFragmentsIntensities = sum / this.nbFragments;
				Arrays.sort(intensities);
				if (intensities.length % 2 == 0)
					this.medianFragmentsIntensities = (intensities[intensities.length / 2]
							+ intensities[intensities.length / 2 - 1]) / 2;
				else
					this.medianFragmentsIntensities = intensities[intensities.length / 2];
				this.fragmentMaxMoz = fragments.get(this.nbFragments - 1).getMz();
			}
		}
	}

	/**
	 * Compute the square root for the nbIntensePeaks compute in
	 * computeNbIntensePeaks()
	 */
	private void computeListSquareRootNbIntensePeaks() {
		nbPeaks = SpectraComparatorParams.getNbPeaks();
		squareRootnbIntensePeaks = new Double[nbPeaks];

		for (int i = 0; i < nbPeaks; i++) {
			Fragment fragment = nbIntensePeaks[i];
			float intensity = fragment.getIntensity();
			squareRootnbIntensePeaks[i] = Math.sqrt(intensity);
		}
	}

	/**
	 * Compute the number of intense peaks. It get the nbPeak last fragment of the
	 * spectrum ( sorted by intensity) and put them in an array. they are the most
	 * intense peaks of the spectrum
	 */
	private void computeNbIntensePeaks() {
		nbPeaks = SpectraComparatorParams.getNbPeaks();
		nbIntensePeaks = new Fragment[nbPeaks];
		if (getNbFragments() >= nbPeaks) {
			int firstValue = getNbFragments() - 1;
			int lastValue = firstValue - nbPeaks;
			for (int i = firstValue; i > lastValue; i--) {
				Fragment fragment = getSortedFragments().get(i);
				nbIntensePeaks[firstValue - i] = fragment;
			}
		}
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

	/**
	 * @return the deltaMozWithReferenceSpectrum
	 */
	public final double getDeltaMozWithReferenceSpectrum() {
		return deltaMozWithReferenceSpectrum;
	}

	/**
	 * @return the deltaRetentionTimeWithReferenceSpectrum
	 */
	public final int getDeltaRetentionTimeWithReferenceSpectrum() {
		return deltaRetentionTimeWithReferenceSpectrum;
	}

	/**
	 * 
	 * @return the list of fragment that equals to chart
	 */
	public ArrayList<Fragment> getFragmentEqualsToChart() {
		return fragmentEqualsToChart;
	}

	/**
	 * @return the fragments
	 */
	public final ArrayList<Fragment> getFragments() {
		return fragments;
	}

	public Boolean getIdentified() {
		return identified;
	}

	public double[] getIntensities() {
		return getMassIntensityValues()[INTENSITIES_INDEX];
	}

	/**
	 * @return the lineStart
	 */
	public final Integer getLineStart() {
		return lineStart;
	}

	/**
	 * @return the lineStop
	 */
	public final Integer getLineStop() {
		return lineStop;
	}

	/**
	 * @return the list of the square root of the intense peaks.
	 */
	public Double[] getListSquareRootNbIntensePeaks() {
		computeListSquareRootNbIntensePeaks();
		return squareRootnbIntensePeaks;
	}

	/**
	 * @return the m_firstScan
	 */
	public final Integer getM_firstScan() {
		return m_firstScan;
	}

	/**
	 * @return the m_firstTime
	 */
	public final Float getM_firstTime() {
		return m_firstTime;
	}

	/**
	 * @return the m_id
	 */
	public final long getM_id() {
		return m_id;
	}

	/**
	 * @return the m_intensityList
	 */
	public final byte[] getM_intensityList() {
		return m_intensityList;
	}

	/**
	 * @return the m_lastScan
	 */
	public final Integer getM_lastScan() {
		return m_lastScan;
	}

	/**
	 * @return the m_lastTime
	 */
	public final Float getM_lastTime() {
		return m_lastTime;
	}

	/**
	 * @return the m_massIntensitiesValues
	 */
	public final double[][] getM_massIntensitiesValues() {
		return m_massIntensitiesValues;
	}

	/**
	 * @return the m_mozList
	 */
	public final byte[] getM_mozList() {
		return m_mozList;
	}

	/**
	 * @return the m_precursorCharge
	 */
	public final Integer getM_precursorCharge() {
		return m_precursorCharge;
	}

	/**
	 * @return the m_precursorIntensity
	 */
	public final Float getM_precursorIntensity() {
		return m_precursorIntensity;
	}

	/**
	 * @return the m_precursorMoz
	 */
	public final Double getM_precursorMoz() {
		return m_precursorMoz;
	}

	/**
	 * @return the m_title
	 */
	public final String getM_title() {
		return m_title;
	}

	public double[] getMasses() {
		return getMassIntensityValues()[MASSES_INDEX];
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

	/**
	 * 
	 * @return the fragment number
	 */
	public Integer getNbFragments() {
		computeFragmentValues();
		return nbFragments;
	}

	/**
	 * @return the number of the intense peaks
	 */
	public Fragment[] getNbIntensePeaks() {
		computeNbIntensePeaks();
		return nbIntensePeaks;
	}

	/**
	 * @return the nbMatch
	 */
	public final Integer getNbMatch() {
		return nbMatch;
	}

	/**
	 * @return the nbPeaksIdenticalWithReferenceSpectrum
	 */
	public final int getNbPeaksIdenticalWithReferenceSpectrum() {
		return nbPeaksIdenticalWithReferenceSpectrum;
	}

	public Float getRetentionTime() {
		return retentionTime;
	}

	/**
	 * @return A sorted list of fragment. The sort is based on fragment intensities.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Fragment> getSortedFragments() {
		if (sortedFragments.isEmpty()) {
			sortedFragments = (ArrayList<Fragment>) fragments.clone();
			sortedFragments.sort(new Comparator<Fragment>() {
				@Override
				public int compare(Fragment f1, Fragment f2) {
					if (f1.getIntensity() > f2.getIntensity())
						return 1;
					if (f1.getIntensity() < f2.getIntensity())
						return -1;
					return 0;
				}
			});
		}
		return sortedFragments;
	}

	/**
	 * @return the titleReferenceSpectrum
	 */
	public final String getTitleReferenceSpectrum() {
		return titleReferenceSpectrum;
	}

	public void setCosTheta(double cosTheta) {
		this.cosTheta = cosTheta;
	}

	/**
	 * @param deltaMozWithReferenceSpectrum the deltaMozWithReferenceSpectrum to set
	 */
	public final void setDeltaMozWithReferenceSpectrum(double deltaMozWithReferenceSpectrum) {
		this.deltaMozWithReferenceSpectrum = deltaMozWithReferenceSpectrum;
	}

	/**
	 * @param deltaRetentionTimeWithReferenceSpectrum the
	 *                                                deltaRetentionTimeWithReferenceSpectrum
	 *                                                to set
	 */
	public final void setDeltaRetentionTimeWithReferenceSpectrum(int deltaRetentionTimeWithReferenceSpectrum) {
		this.deltaRetentionTimeWithReferenceSpectrum = deltaRetentionTimeWithReferenceSpectrum;
	}

	/**
	 * @param fragments the fragments to set
	 */
	public final void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}

	/**
	 * @param identified the identified to set
	 */
	public final void setIdentified(Boolean identified) {
		this.identified = identified;
	}

	/**
	 * @param lineStart the lineStart to set
	 */
	public final void setLineStart(Integer lineStart) {
		this.lineStart = lineStart;
	}

	/**
	 * @param lineStop the lineStop to set
	 */
	public final void setLineStop(Integer lineStop) {
		this.lineStop = lineStop;
	}

	/**
	 * @param m_firstScan the m_firstScan to set
	 */
	public final void setM_firstScan(Integer m_firstScan) {
		this.m_firstScan = m_firstScan;
	}

	/**
	 * @param m_firstTime the m_firstTime to set
	 */
	public final void setM_firstTime(Float m_firstTime) {
		this.m_firstTime = m_firstTime;
	}

	/**
	 * @param m_id the m_id to set
	 */
	public final void setM_id(long m_id) {
		this.m_id = m_id;
	}

	/**
	 * @param m_intensityList the m_intensityList to set
	 */
	public final void setM_intensityList(byte[] m_intensityList) {
		this.m_intensityList = m_intensityList;
	}

	/**
	 * @param m_lastScan the m_lastScan to set
	 */
	public final void setM_lastScan(Integer m_lastScan) {
		this.m_lastScan = m_lastScan;
	}

	/**
	 * @param m_lastTime the m_lastTime to set
	 */
	public final void setM_lastTime(Float m_lastTime) {
		this.m_lastTime = m_lastTime;
	}

	/**
	 * @param m_massIntensitiesValues the m_massIntensitiesValues to set
	 */
	public final void setM_massIntensitiesValues(double[][] m_massIntensitiesValues) {
		this.m_massIntensitiesValues = m_massIntensitiesValues;
	}

	/**
	 * @param m_mozList the m_mozList to set
	 */
	public final void setM_mozList(byte[] m_mozList) {
		this.m_mozList = m_mozList;
	}

	/**
	 * @param m_precursorCharge the m_precursorCharge to set
	 */
	public final void setM_precursorCharge(Integer m_precursorCharge) {
		this.m_precursorCharge = m_precursorCharge;
	}

	/**
	 * @param m_precursorIntensity the m_precursorIntensity to set
	 */
	public final void setM_precursorIntensity(Float m_precursorIntensity) {
		this.m_precursorIntensity = m_precursorIntensity;
	}

	/**
	 * @param m_precursorMoz the m_precursorMoz to set
	 */
	public final void setM_precursorMoz(Double m_precursorMoz) {
		this.m_precursorMoz = m_precursorMoz;
	}

	/**
	 * @param m_title the m_title to set
	 */
	public final void setM_title(String m_title) {
		this.m_title = m_title;
	}

	/**
	 * @param nbMatch the nbMatch to set
	 */
	public final void setNbMatch(Integer nbMatch) {
		this.nbMatch = nbMatch;
	}
	/**
	 * @param nbPeaksIdenticalWithReferenceSpectrum the
	 *                                              nbPeaksIdenticalWithReferenceSpectrum
	 *                                              to set
	 */
	public final void setNbPeaksIdenticalWithReferenceSpectrum(int nbPeaksIdenticalWithReferenceSpectrum) {
		this.nbPeaksIdenticalWithReferenceSpectrum = nbPeaksIdenticalWithReferenceSpectrum;
	}
	/**
	 * @param retentionTime the retentionTime to set
	 */
	public final void setRetentionTime(float retentionTime) {
		this.retentionTime = retentionTime;
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
	 * @param regex the used regex to retrieve the retention time from title
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
	 * @param titleReferenceSpectrum the titleReferenceSpectrum to set
	 */
	public final void setTitleReferenceSpectrum(String titleReferenceSpectrum) {
		this.titleReferenceSpectrum = titleReferenceSpectrum;
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
