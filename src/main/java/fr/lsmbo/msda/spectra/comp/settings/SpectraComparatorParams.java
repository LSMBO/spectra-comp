/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.settings;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * Holds default values for spectra comparison.
 * 
 * @author Aromdhani
 *
 */
public class SpectraComparatorParams {

	/** The delta moz. */
	private double deltaPrecMoz = 0.007F;
	/** The delta moz. */

	private double deltaPeaksMoz = 0.007F;
	/** The delta RT. */
	private Integer deltaRT = 90;

	/** The nb peaks min. */
	private Integer nbPeaksMin = 4;

	/** The theta min. */
	private Integer thetaMin = 11;

	/** The nb peaks. */
	private Integer nbPeaks = 8;

	/**
	 * Instantiates a new spectra comparator params.
	 */
	public SpectraComparatorParams() {
		this.initialize();
	}

	/**
	 * Instantiates a new spectra comparator params.
	 *
	 * @param deltaMoz   the delta moz
	 * @param deltaRT    the delta RT
	 * @param nbPeaksMin the nb peaks min
	 * @param thetaMin   the theta min
	 * @param nbPeaks    the nb peaks
	 */
	public SpectraComparatorParams(double deltaPrecMoz, double deltaPeaksMoz, Integer deltaRT, Integer nbPeaksMin,
			Integer thetaMin, Integer nbPeaks) {
		this.deltaPrecMoz = deltaPrecMoz;
		this.deltaPeaksMoz = deltaPeaksMoz;
		this.deltaRT = deltaRT;
		this.nbPeaksMin = nbPeaksMin;
		this.thetaMin = thetaMin;
		this.nbPeaks = nbPeaks;
		this.initialize();
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public Map<String, Number> getValues() {
		Map<String, Number> map = new HashMap<>();
		map.put("deltaPrecMoz", deltaPrecMoz);
		map.put("deltaPeaksMoz", deltaPeaksMoz);
		map.put("deltaRT", deltaRT);
		map.put("nbPeaksMin", nbPeaksMin);
		map.put("thetaMin", thetaMin);
		map.put("nbPeaks", nbPeaks);
		return map;
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
	}

	/**
	 * @return the deltaPrecMoz
	 */
	public final double getDeltaPrecMoz() {
		return deltaPrecMoz;
	}

	/**
	 * @param deltaPrecMoz the deltaPrecMoz to set
	 */
	public final void setDeltaPrecMoz(double deltaPrecMoz) {
		this.deltaPrecMoz = deltaPrecMoz;
	}

	/**
	 * @return the deltaPeaksMoz
	 */
	public final double getDeltaPeaksMoz() {
		return deltaPeaksMoz;
	}

	/**
	 * @param deltaPeaksMoz the deltaPeaksMoz to set
	 */
	public final void setDeltaPeaksMoz(double deltaPeaksMoz) {
		this.deltaPeaksMoz = deltaPeaksMoz;
	}

	/**
	 * Gets the delta RT.
	 *
	 * @return the deltaRT
	 */
	public final Integer getDeltaRT() {
		return deltaRT;
	}

	/**
	 * Sets the delta RT.
	 *
	 * @param deltaRT the deltaRT to set
	 */
	public final void setDeltaRT(Integer deltaRT) {
		this.deltaRT = deltaRT;
	}

	/**
	 * Gets the nb peaks min.
	 *
	 * @return the nbPeaksMin
	 */
	public final Integer getNbPeaksMin() {
		return nbPeaksMin;
	}

	/**
	 * Sets the nb peaks min.
	 *
	 * @param nbPeaksMin the nbPeaksMin to set
	 */
	public final void setNbPeaksMin(Integer nbPeaksMin) {
		this.nbPeaksMin = nbPeaksMin;
	}

	/**
	 * Gets the theta min.
	 *
	 * @return the thetaMin
	 */
	public final Integer getThetaMin() {
		return thetaMin;
	}

	/**
	 * Sets the theta min.
	 *
	 * @param thetaMin the thetaMin to set
	 */
	public final void setThetaMin(Integer thetaMin) {
		this.thetaMin = thetaMin;
	}

	/**
	 * Gets the nb peaks.
	 *
	 * @return the nbPeaks
	 */
	public final Integer getNbPeaks() {
		return nbPeaks;
	}

	/**
	 * Sets the nb peaks.
	 *
	 * @param nbPeaks the nbPeaks to set
	 */
	public final void setNbPeaks(Integer nbPeaks) {
		this.nbPeaks = nbPeaks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "precursor delta moz: " + deltaPrecMoz + "; delta peaks moz: " + deltaPeaksMoz + "; delta RT: " + deltaRT
				+ "; min peaks nbr: " + nbPeaksMin + "; theta min: " + thetaMin + "; peaks nbr: " + nbPeaks;

	}
}
