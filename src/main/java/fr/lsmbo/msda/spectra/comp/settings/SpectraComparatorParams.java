/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds default values for spectra comparison.
 * 
 * @author Aromdhani
 *
 */
public class SpectraComparatorParams {

	private float deltaMoz = 0.007F;
	private Integer deltaRT = 90;
	private Integer nbPeaksMin = 4;
	private Integer thetaMin = 11;
	private Integer nbPeaks = 8;

	public SpectraComparatorParams() {
		this.initialize();
	}

	public SpectraComparatorParams(float deltaMoz, Integer deltaRT, Integer nbPeaksMin, Integer thetaMin,
			Integer nbPeaks) {
		deltaMoz = this.deltaMoz;
		deltaRT = this.deltaRT;
		nbPeaksMin = this.nbPeaksMin;
		thetaMin = this.thetaMin;
		nbPeaks = this.nbPeaks;
		this.initialize();
	}

	public Map<String, Number> getValues() {
		Map<String, Number> map = new HashMap<>();
		map.put("deltaMoz", deltaMoz);
		map.put("deltaRT", deltaRT);
		map.put("nbPeaksMin", nbPeaksMin);
		map.put("thetaMin", thetaMin);
		map.put("nbPeaks", nbPeaks);
		return map;
	}

	private void initialize() {
	}

	/**
	 * @return the deltaMoz
	 */
	public final float getDeltaMoz() {
		return deltaMoz;
	}

	/**
	 * @param deltaMoz
	 *            the deltaMoz to set
	 */
	public final void setDeltaMoz(float deltaMoz) {
		this.deltaMoz = deltaMoz;
	}

	/**
	 * @return the deltaRT
	 */
	public final Integer getDeltaRT() {
		return deltaRT;
	}

	/**
	 * @param deltaRT
	 *            the deltaRT to set
	 */
	public final void setDeltaRT(Integer deltaRT) {
		this.deltaRT = deltaRT;
	}

	/**
	 * @return the nbPeaksMin
	 */
	public final Integer getNbPeaksMin() {
		return nbPeaksMin;
	}

	/**
	 * @param nbPeaksMin
	 *            the nbPeaksMin to set
	 */
	public final void setNbPeaksMin(Integer nbPeaksMin) {
		this.nbPeaksMin = nbPeaksMin;
	}

	/**
	 * @return the thetaMin
	 */
	public final Integer getThetaMin() {
		return thetaMin;
	}

	/**
	 * @param thetaMin
	 *            the thetaMin to set
	 */
	public final void setThetaMin(Integer thetaMin) {
		this.thetaMin = thetaMin;
	}

	/**
	 * @return the nbPeaks
	 */
	public final Integer getNbPeaks() {
		return nbPeaks;
	}

	/**
	 * @param nbPeaks
	 *            the nbPeaks to set
	 */
	public final void setNbPeaks(Integer nbPeaks) {
		this.nbPeaks = nbPeaks;
	}

	@Override
	public String toString() {
		return "Spectra comparator parameters deltaMoz: " + deltaMoz + "; deltaRT: " + deltaRT + " ; nbPeaksMin: "
				+ nbPeaksMin + " ;thetaMin: " + thetaMin + "; nbPeaks:" + nbPeaks;

	}

}
