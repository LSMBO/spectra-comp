package fr.lsmbo.msda.spectra.comp.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// TODO: Auto-generated Javadoc
/**
 * The Class Spectrum.
 */
public class Spectrum {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Spectrum.class);

	/** The Constant MASSES_INDEX. */
	public static final int MASSES_INDEX = 0;

	/** The Constant INTENSITIES_INDEX. */
	public static final int INTENSITIES_INDEX = 1;

	/** The m id. */
	private long m_id;

	/** The ref ids. */
	private Set<Long> m_refIdSet = new HashSet<>();
	/** The test ids. */
	private Set<Long> m_testIdSet = new HashSet<>();
	private ObservableList<Spectrum> m_matchedSpectra = FXCollections.observableArrayList();
	private int m_matchedSize = 0;
	/** The m first scan. */
	private Integer m_firstScan;

	/** The m last scan. */
	private Integer m_lastScan;

	/** The m first time. */
	private Float m_firstTime;

	/** The m last time. */
	private Float m_lastTime;

	/** The m intensity list. */
	private byte[] m_intensityList = null;

	/** The m moz list. */
	private byte[] m_mozList = null;

	/** The m mass intensities values. */
	private double[][] m_massIntensitiesValues = null;

	/** The m precursor charge. */
	private int m_precursorCharge = -1;

	/** The m precursor intensity. */
	private Float m_precursorIntensity;

	/** The m precursor moz. */
	private Double m_precursorMoz;

	/** The m title. */
	private String m_title = null;

	/** The nb match. */
	private Integer nbMatch;

	/** The matched. */
	private BooleanProperty matched = new SimpleBooleanProperty(false);

	/** The retention time. */
	private float retentionTime;

	/** The line start. */
	// The index of the line start in file of the spectrum
	private Integer lineStart = 0;

	/** The line stop. */
	// The index of the line stop in file of the spectrum
	private Integer lineStop = 0;

	/** The fragments. */
	// May be empty if file is too big
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	/** The sorted fragments. */
	// May be empty if file is too big
	private ArrayList<Fragment> sortedFragments = new ArrayList<Fragment>();

	/** The fragment equals to chart. */
	private ArrayList<Fragment> fragmentEqualsToChart = new ArrayList<Fragment>();

	/** The nb intense peaks. */
	private Fragment[] nbIntensePeaks;

	/** The square rootnb intense peaks. */
	private Double[] squareRootnbIntensePeaks;

	/** The nb fragments. */
	private Integer nbFragments = 0;

	/** The index of most intense fragment. */
	// The index of the most intense fragment.
	private Integer indexOfMostIntenseFragment = 0;

	/** The fragment max moz. */
	// The maximum of Moz
	private double fragmentMaxMoz = 0;

	/** The fragment max intensity. */
	// The fragment maximum intensity
	private float fragmentMaxIntensity = 0;

	/** The median fragments intensities. */
	// Median of fragment intensity
	private float medianFragmentsIntensities = 0;

	/** The average fragments intensities. */
	// The average of fragment intensity
	private float averageFragmentsIntensities = 0;

	/** The nb peaks. */
	// The number of peaks
	private int nbPeaks;

	/** The cos theta. */
	// TODO More about the reference spectrum
	private double cosTheta = 0D;

	/** The delta moz with reference spectrum. */
	private double deltaMozWithReferenceSpectrum = 0F;

	/** The delta retention time with reference spectrum. */
	private int deltaRetentionTimeWithReferenceSpectrum = 0;

	/** The nb peaks identical with reference spectrum. */
	private int nbPeaksIdenticalWithReferenceSpectrum = 0;

	/** The title reference spectrum. */
	private String titleReferenceSpectrum;

	/**
	 * Instantiates a new spectrum.
	 */
	public Spectrum() {
	}

	/**
	 * Instantiates a new spectrum.
	 *
	 * @param id
	 *            the id
	 * @param firstScan
	 *            the first scan
	 * @param firstTime
	 *            the first time
	 * @param lastTime
	 *            the last time
	 * @param intensityList
	 *            the intensity list
	 * @param mozeList
	 *            the moze list
	 * @param precursorCharge
	 *            the precursor charge
	 * @param precursorIntensity
	 *            the precursor intensity
	 * @param precursorMoz
	 *            the precursor moz
	 * @param title
	 *            the title
	 */
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
	 * Adds the fragment.
	 *
	 * @param fragment
	 *            the fragment to set
	 */
	public void addFragment(Fragment fragment) {
		this.fragments.add(fragment);
	}

	/**
	 * Adds the fragments.
	 *
	 * @param fragments
	 *            the list of fragment to set
	 */
	public void addFragments(ArrayList<Fragment> fragments) {
		this.fragments.addAll(fragments);
	}

	/**
	 * Compute fragment values . It computes the average and the median of
	 * fragment intensities.
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
	 * computeNbIntensePeaks().
	 */
	private void computeListSquareRootNbIntensePeaks() {
		nbPeaks = Session.USER_PARAMS.getComparison().getNbPeaks();
		squareRootnbIntensePeaks = new Double[nbPeaks];

		for (int i = 0; i < nbPeaks; i++) {
			Fragment fragment = nbIntensePeaks[i];
			float intensity = fragment.getIntensity();
			squareRootnbIntensePeaks[i] = Math.sqrt(intensity);
		}
	}

	/**
	 * Compute the number of intense peaks. It get the nbPeak last fragment of
	 * the spectrum (sorted by intensity) and put them in an array. they are the
	 * most intense peaks of the spectrum
	 * 
	 * @return <code> true</code> if the number of fragment is above the session
	 *         number of peaks otherwise <code>false</code>
	 */
	private boolean computeNbIntensePeaks() {
		nbPeaks = Session.USER_PARAMS.getComparison().getNbPeaks();
		nbIntensePeaks = new Fragment[nbPeaks];
		if (getNbFragments() >= nbPeaks) {
			int firstValue = getNbFragments() - 1;
			int lastValue = firstValue - nbPeaks;
			for (int i = firstValue; i > lastValue; i--) {
				Fragment fragment = getSortedFragments().get(i);
				nbIntensePeaks[firstValue - i] = fragment;
			}
			return true;
		} else {
			return false;
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
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Spectrum)) {
			return false;
		}
		Spectrum spec = (Spectrum) obj;
		return (this.getM_title().equals(spec.getM_title()));
	}

	/**
	 * Gets the cos theta.
	 *
	 * @return the cos theta
	 */
	public double getCosTheta() {
		return cosTheta;
	}

	/**
	 * Gets the delta moz with reference spectrum.
	 *
	 * @return the deltaMozWithReferenceSpectrum
	 */
	public final double getDeltaMozWithReferenceSpectrum() {
		return deltaMozWithReferenceSpectrum;
	}

	/**
	 * Gets the delta retention time with reference spectrum.
	 *
	 * @return the deltaRetentionTimeWithReferenceSpectrum
	 */
	public final int getDeltaRetentionTimeWithReferenceSpectrum() {
		return deltaRetentionTimeWithReferenceSpectrum;
	}

	/**
	 * Gets the fragment equals to chart.
	 *
	 * @return the list of fragment that equals to chart
	 */
	public ArrayList<Fragment> getFragmentEqualsToChart() {
		return fragmentEqualsToChart;
	}

	/**
	 * Gets the fragments.
	 *
	 * @return the fragments
	 */
	public final ArrayList<Fragment> getFragments() {
		return fragments;
	}

	/**
	 * Gets the matched.
	 *
	 * @return the matched
	 */
	public BooleanProperty getMatched() {
		return matched;
	}

	/**
	 * Gets the intensities.
	 *
	 * @return the intensities
	 */
	public double[] getIntensities() {
		return getMassIntensityValues()[INTENSITIES_INDEX];
	}

	/**
	 * Gets the line start.
	 *
	 * @return the lineStart
	 */
	public final Integer getLineStart() {
		return lineStart;
	}

	/**
	 * Gets the line stop.
	 *
	 * @return the lineStop
	 */
	public final Integer getLineStop() {
		return lineStop;
	}

	/**
	 * Gets the list square root nb intense peaks.
	 *
	 * @return the list of the square root of the intense peaks.
	 */
	public Double[] getListSquareRootNbIntensePeaks() {
		computeListSquareRootNbIntensePeaks();
		return squareRootnbIntensePeaks;
	}

	/**
	 * Gets the m first scan.
	 *
	 * @return the m_firstScan
	 */
	public final Integer getM_firstScan() {
		return m_firstScan;
	}

	/**
	 * Gets the m first time.
	 *
	 * @return the m_firstTime
	 */
	public final Float getM_firstTime() {
		return m_firstTime;
	}

	/**
	 * Gets the m id.
	 *
	 * @return the m_id
	 */
	public final long getM_id() {
		return m_id;
	}

	/**
	 * Gets the m intensity list.
	 *
	 * @return the m_intensityList
	 */
	public final byte[] getM_intensityList() {
		return m_intensityList;
	}

	/**
	 * Gets the m last scan.
	 *
	 * @return the m_lastScan
	 */
	public final Integer getM_lastScan() {
		return m_lastScan;
	}

	/**
	 * Gets the m last time.
	 *
	 * @return the m_lastTime
	 */
	public final Float getM_lastTime() {
		return m_lastTime;
	}

	/**
	 * Gets the m mass intensities values.
	 *
	 * @return the m_massIntensitiesValues
	 */
	public final double[][] getM_massIntensitiesValues() {
		return m_massIntensitiesValues;
	}

	/**
	 * Gets the m moz list.
	 *
	 * @return the m_mozList
	 */
	public final byte[] getM_mozList() {
		return m_mozList;
	}

	/**
	 * Gets the m precursor charge.
	 *
	 * @return the m_precursorCharge
	 */
	public final Integer getM_precursorCharge() {
		return m_precursorCharge;
	}

	/**
	 * Gets the m precursor intensity.
	 *
	 * @return the m_precursorIntensity
	 */
	public final Float getM_precursorIntensity() {
		return m_precursorIntensity;
	}

	/**
	 * Gets the m precursor moz.
	 *
	 * @return the m_precursorMoz
	 */
	public final Double getM_precursorMoz() {
		return m_precursorMoz;
	}

	/**
	 * Gets the m title.
	 *
	 * @return the m_title
	 */
	public final String getM_title() {
		return m_title;
	}

	/**
	 * Gets the masses.
	 *
	 * @return the masses
	 */
	public double[] getMasses() {
		return getMassIntensityValues()[MASSES_INDEX];
	}

	/**
	 * Gets the mass intensity values.
	 *
	 * @return the mass intensity values
	 */
	public double[][] getMassIntensityValues() {
		if (m_massIntensitiesValues == null) {
			if ((m_intensityList == null) || (m_mozList == null)) {
				return null;
			}
			ByteBuffer intensityByteBuffer = ByteBuffer.wrap(m_intensityList).order(ByteOrder.LITTLE_ENDIAN);
			FloatBuffer intensityFloatBuffer = intensityByteBuffer.asFloatBuffer();
			double[] intensityDoubleArray = new double[intensityFloatBuffer.remaining()];
			for (int i = 0; i < intensityDoubleArray.length; i++) {
				intensityDoubleArray[i] = intensityFloatBuffer.get();
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
	 * Gets the nb fragments.
	 *
	 * @return the fragment number
	 */
	public Integer getNbFragments() {
		computeFragmentValues();
		return nbFragments;
	}

	/**
	 * Gets the nb intense peaks.
	 *
	 * @return the number of the intense peaks
	 */
	public Fragment[] getNbIntensePeaks() {
		if (computeNbIntensePeaks()) {
			return nbIntensePeaks;
		} else {
			return null;
		}
	}

	/**
	 * Gets the nb match.
	 *
	 * @return the nbMatch
	 */
	public final Integer getNbMatch() {
		return nbMatch;
	}

	/**
	 * Gets the nb peaks identical with reference spectrum.
	 *
	 * @return the nbPeaksIdenticalWithReferenceSpectrum
	 */
	public final int getNbPeaksIdenticalWithReferenceSpectrum() {
		return nbPeaksIdenticalWithReferenceSpectrum;
	}

	/**
	 * Gets the retention time.
	 *
	 * @return the retention time
	 */
	public Float getRetentionTime() {
		return retentionTime;
	}

	/**
	 * Gets the sorted fragments.
	 *
	 * @return A sorted list of fragment. The sort is based on fragment
	 *         intensities.
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
	 * Gets the title reference spectrum.
	 *
	 * @return the titleReferenceSpectrum
	 */
	public final String getTitleReferenceSpectrum() {
		return titleReferenceSpectrum;
	}

	/**
	 * Sets the cos theta.
	 *
	 * @param cosTheta
	 *            the new cos theta
	 */
	public void setCosTheta(double cosTheta) {
		this.cosTheta = cosTheta;
	}

	/**
	 * Sets the delta moz with reference spectrum.
	 *
	 * @param deltaMozWithReferenceSpectrum
	 *            the deltaMozWithReferenceSpectrum to set
	 */
	public final void setDeltaMozWithReferenceSpectrum(double deltaMozWithReferenceSpectrum) {
		this.deltaMozWithReferenceSpectrum = deltaMozWithReferenceSpectrum;
	}

	/**
	 * Sets the delta retention time with reference spectrum.
	 *
	 * @param deltaRetentionTimeWithReferenceSpectrum
	 *            the deltaRetentionTimeWithReferenceSpectrum to set
	 */
	public final void setDeltaRetentionTimeWithReferenceSpectrum(int deltaRetentionTimeWithReferenceSpectrum) {
		this.deltaRetentionTimeWithReferenceSpectrum = deltaRetentionTimeWithReferenceSpectrum;
	}

	/**
	 * Sets the fragments.
	 *
	 * @param fragments
	 *            the fragments to set
	 */
	public final void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}

	/**
	 * Sets the matched.
	 *
	 * @param matched
	 *            the matched to set
	 */
	public final void setMatched(BooleanProperty matched) {
		this.matched = matched;
	}

	/**
	 * Sets the line start.
	 *
	 * @param lineStart
	 *            the lineStart to set
	 */
	public final void setLineStart(Integer lineStart) {
		this.lineStart = lineStart;
	}

	/**
	 * Sets the line stop.
	 *
	 * @param lineStop
	 *            the lineStop to set
	 */
	public final void setLineStop(Integer lineStop) {
		this.lineStop = lineStop;
	}

	/**
	 * Sets the m first scan.
	 *
	 * @param m_firstScan
	 *            the m_firstScan to set
	 */
	public final void setM_firstScan(Integer m_firstScan) {
		this.m_firstScan = m_firstScan;
	}

	/**
	 * Sets the m first time.
	 *
	 * @param m_firstTime
	 *            the m_firstTime to set
	 */
	public final void setM_firstTime(Float m_firstTime) {
		this.m_firstTime = m_firstTime;
	}

	/**
	 * Sets the m id.
	 *
	 * @param m_id
	 *            the m_id to set
	 */
	public final void setM_id(long m_id) {
		this.m_id = m_id;
	}

	/**
	 * Sets the m intensity list.
	 *
	 * @param m_intensityList
	 *            the m_intensityList to set
	 */
	public final void setM_intensityList(byte[] m_intensityList) {
		this.m_intensityList = m_intensityList;
	}

	/**
	 * Sets the m last scan.
	 *
	 * @param m_lastScan
	 *            the m_lastScan to set
	 */
	public final void setM_lastScan(Integer m_lastScan) {
		this.m_lastScan = m_lastScan;
	}

	/**
	 * Sets the m last time.
	 *
	 * @param m_lastTime
	 *            the m_lastTime to set
	 */
	public final void setM_lastTime(Float m_lastTime) {
		this.m_lastTime = m_lastTime;
	}

	/**
	 * Sets the m mass intensities values.
	 *
	 * @param m_massIntensitiesValues
	 *            the m_massIntensitiesValues to set
	 */
	public final void setM_massIntensitiesValues(double[][] m_massIntensitiesValues) {
		this.m_massIntensitiesValues = m_massIntensitiesValues;
	}

	/**
	 * Sets the m moz list.
	 *
	 * @param m_mozList
	 *            the m_mozList to set
	 */
	public final void setM_mozList(byte[] m_mozList) {
		this.m_mozList = m_mozList;
	}

	/**
	 * Sets the m precursor charge.
	 *
	 * @param m_precursorCharge
	 *            the m_precursorCharge to set
	 */
	public final void setM_precursorCharge(Integer m_precursorCharge) {
		this.m_precursorCharge = m_precursorCharge;
	}

	/**
	 * Sets the m precursor intensity.
	 *
	 * @param m_precursorIntensity
	 *            the m_precursorIntensity to set
	 */
	public final void setM_precursorIntensity(Float m_precursorIntensity) {
		this.m_precursorIntensity = m_precursorIntensity;
	}

	/**
	 * Sets the m precursor moz.
	 *
	 * @param m_precursorMoz
	 *            the m_precursorMoz to set
	 */
	public final void setM_precursorMoz(Double m_precursorMoz) {
		this.m_precursorMoz = m_precursorMoz;
	}

	/**
	 * Sets the m title.
	 *
	 * @param m_title
	 *            the m_title to set
	 */
	public final void setM_title(String m_title) {
		this.m_title = m_title;
	}

	/**
	 * Sets the nb match.
	 *
	 * @param nbMatch
	 *            the nbMatch to set
	 */
	public final void setNbMatch(Integer nbMatch) {
		this.nbMatch = nbMatch;
	}

	/**
	 * Sets the nb peaks identical with reference spectrum.
	 *
	 * @param nbPeaksIdenticalWithReferenceSpectrum
	 *            the nbPeaksIdenticalWithReferenceSpectrum to set
	 */
	public final void setNbPeaksIdenticalWithReferenceSpectrum(int nbPeaksIdenticalWithReferenceSpectrum) {
		this.nbPeaksIdenticalWithReferenceSpectrum = nbPeaksIdenticalWithReferenceSpectrum;
	}

	/**
	 * Sets the retention time.
	 *
	 * @param retentionTime
	 *            the retentionTime to set
	 */
	public final void setRetentionTime(float retentionTime) {
		this.retentionTime = retentionTime;
	}

	/**
	 * Sets the retention time.
	 *
	 * @param retentionTime
	 *            the new retention time
	 */
	public void setRetentionTime(Float retentionTime) {
		this.retentionTime = retentionTime;
	}

	/**
	 * Set the retention time from title.
	 */
	public void setRetentionTimeFromTitle() {
		if ((!StringsUtils.isEmpty(m_title)) && (!StringsUtils.isEmpty(Session.CURRENT_REGEX_RT))) {
			setRetentionTimeFromTitle(SoftwareType.getParsingRule(Session.CURRENT_REGEX_RT).getRegexValue());
		}
	}

	/**
	 * Set the retention from title.
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
	 * Sets the title reference spectrum.
	 *
	 * @param titleReferenceSpectrum
	 *            the titleReferenceSpectrum to set
	 */
	public final void setTitleReferenceSpectrum(String titleReferenceSpectrum) {
		this.titleReferenceSpectrum = titleReferenceSpectrum;
	}

	/**
	 * @return the ref_idSet
	 */
	public final Set<Long> getRef_idSet() {
		return m_refIdSet;
	}

	/**
	 * @param ref_idSet
	 *            the m_reference ids to set
	 */
	public final void setRef_idSet(Set<Long> ref_idSet) {
		this.m_refIdSet = ref_idSet;
	}

	/**
	 * @return the m_testIdSet
	 */
	public final Set<Long> getM_testIdSet() {
		return m_testIdSet;
	}

	/**
	 * @param m_testIdSet
	 *            the m_testIdSet to set
	 */
	public final void setM_testIdSet(Set<Long> m_testIdSet) {
		this.m_testIdSet = m_testIdSet;
	}

	/**
	 * @return the m_matchedSpectra
	 */
	public final ObservableList<Spectrum> getM_matchedSpectra() {
		return m_matchedSpectra;
	}

	/**
	 * @param m_matchedSpectra
	 *            the m_matchedSpectra to set
	 */
	public final void setM_matchedSpectra(ObservableList<Spectrum> m_matchedSpectra) {
		this.m_matchedSpectra = m_matchedSpectra;
	}

	/**
	 * @return the m_matchedSize
	 */
	public final int getM_matchedSize() {
		m_matchedSize = m_matchedSpectra.size();
		return m_matchedSize;
	}

	/**
	 * @param m_matchedSize
	 *            the m_matchedSize to set
	 */
	public final void setM_matchedSize(int m_matchedSize) {
		this.m_matchedSize = m_matchedSize;
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
