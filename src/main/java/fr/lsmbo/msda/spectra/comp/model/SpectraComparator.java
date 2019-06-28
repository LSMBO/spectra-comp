package fr.lsmbo.msda.spectra.comp.model;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.list.Spectra;

// TODO: Auto-generated Javadoc
/**
 * Provides a comparison of spectra.
 * 
 * @author Aromdhani
 * @author ABU
 *
 * 
 */
public class SpectraComparator {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SpectraComparator.class);

	/** The second spectra. */
	// Spectra to make comparison
	private static Spectra secondSpectra;

	/** The sub list second spectra. */
	// A sub list of spectra
	private static Spectra subListSecondSpectra = new Spectra();

	/** The valid spectra. */
	// Spectra which succeed all the step
	private static Spectra validSpectra = new Spectra();

	/** The reference spectrum. */
	// Spectrum reference
	private static Spectrum referenceSpectrum;
	// Number of peaks equals between reference spectrum (RS) and tested
	// spectrum(TS) (same MOZ and same RT)
	/** The nb peaks equals. */
	private static int nbPeaksEquals;

	/** The nb peaks. */
	private static Integer nbPeaks;

	/** The cos theta. */
	private static Double cosTheta;

	// Arrays which contain at the same index the same peaks (same moz +/-//
	// deltaRT)
	/** The list peaks reference spectrum. */
	private static float[] listPeaksReferenceSpectrum;

	/** The list peaks tested spectrum. */
	private static float[] listPeaksTestedSpectrum;

	/** The list square rootpeaks tested spectrum. */
	private static Double[] listSquareRootpeaksTestedSpectrum;

	/** The list square rootpeaks reference spectrum. */
	private static Double[] listSquareRootpeaksReferenceSpectrum;

	/** The delta moz. */
	// Constant
	private static Double deltaMoz;

	/** The delta RT. */
	private static Integer deltaRT;

	/** The nb peaks min. */
	private static Integer nbPeaksMin;

	/** The theta min. */
	private static Integer thetaMin;

	/** The cos theta min. */
	private static double cosThetaMin;

	/**
	 * Add a new value in the array.
	 *
	 * @param intensityFragmentReferenceSpectrum
	 *            the intensity fragment reference spectrum
	 * @param index
	 *            the index
	 */
	private static void addPeakReferenceSpectrum(float intensityFragmentReferenceSpectrum, int index) {
		listPeaksReferenceSpectrum[index] = intensityFragmentReferenceSpectrum;
	}

	/**
	 * Add a new value in the array of TS, if a value is present for the same
	 * peak of RS, keep the most intense value of intensity.
	 *
	 * @param intensityFragmentSubListSpectrum
	 *            the intensity fragment sub list spectrum
	 * @param index
	 *            the index
	 */
	private static void addPeakTestedSpectrum(float intensityFragmentSubListSpectrum, int index) {
		if (listPeaksTestedSpectrum[index] == 0) {
			listPeaksTestedSpectrum[index] = intensityFragmentSubListSpectrum;
		} else {
			if (listPeaksTestedSpectrum[index] < intensityFragmentSubListSpectrum) {
				listPeaksTestedSpectrum[index] = intensityFragmentSubListSpectrum;
			}
		}
	}

	/**
	 * Compute value of cos theta.
	 * 
	 * <pre>
	 * RS.peak = intensity of peaks RS
	 * TS.peak = intensity of peaks TS Cos theta = ∑NB_PEAKS(√RS.peak *
	 * √TS.peak)/(√(∑NB_PEAKS(RS.peak))*√(∑NB_PEAKS(TS.peak)))
	 * </pre>
	 */
	private static void computeCosTheta() {
		cosTheta = 0D;
		Double numeratorCosTheta = 0D;

		Double leftDenominator = 0D;
		Double rightDenominator = 0D;

		Double sumIntensityReferenceSpectrum = 0D;
		Double sumIntensityTestedSpectrum = 0D;

		Double[] arraySquareRootReferenceSpectrum = getListSquareRootpeaksReferenceSpectrum();
		Double[] arraySquareRootTestedSpectrum = getListSquareRootpeaksTestedSpectrum();
		// Compute the numerator of the equation (find the corresponding square
		// root, multiply them and sum)
		for (int i = 0; i < nbPeaksEquals; i++) {
			Double squareRootReferenceSpectrum = arraySquareRootReferenceSpectrum[i];
			Double squareRootTestedSpectrum = arraySquareRootTestedSpectrum[i];
			numeratorCosTheta += (squareRootReferenceSpectrum * squareRootTestedSpectrum);
		}

		// Compute the square root of the sum of intensity reference Spectrum
		for (int i = 0; i < listPeaksReferenceSpectrum.length; i++) {
			if (listPeaksReferenceSpectrum[i] != 0) {
				sumIntensityReferenceSpectrum += listPeaksReferenceSpectrum[i];
			}
		}
		leftDenominator = Math.sqrt(sumIntensityReferenceSpectrum);

		// Compute the square root of the sum of intensity tested Spectrum
		for (int i = 0; i < listPeaksTestedSpectrum.length; i++) {
			if (listPeaksTestedSpectrum[i] != 0) {
				sumIntensityTestedSpectrum += listPeaksTestedSpectrum[i];
			}
		}
		rightDenominator = Math.sqrt(sumIntensityTestedSpectrum);
		cosTheta = numeratorCosTheta / (leftDenominator * rightDenominator);
	}

	/**
	 * Find the non 0 values in the array of reference spectrum, get back the
	 * square root of this value and return a new array (size equals to number
	 * of peaks identical between TS and RS).
	 */
	private static void computeListSquareRootpeaksReferenceSpectrum() {
		listSquareRootpeaksReferenceSpectrum = new Double[nbPeaksEquals];
		int j = 0;
		for (int i = 0; i < listPeaksReferenceSpectrum.length; i++) {
			if (listPeaksReferenceSpectrum[i] != 0) {
				//
				listSquareRootpeaksReferenceSpectrum[j] = referenceSpectrum.getListSquareRootNbIntensePeaks()[i];
				j++;
			}
		}
	}

	/**
	 * Find the non 0 values in the array of tested spectrum, compute the square
	 * root of this value and return a new array (size equals to number of peaks
	 * identical between TS and RS).
	 */
	private static void computeListSquareRootpeaksTestedSpectrum() {
		listSquareRootpeaksTestedSpectrum = new Double[nbPeaksEquals];
		int j = 0;
		for (int i = 0; i < listPeaksTestedSpectrum.length; i++) {
			if (listPeaksTestedSpectrum[i] != 0) {
				listSquareRootpeaksTestedSpectrum[j] = Math.sqrt(listPeaksTestedSpectrum[i]);
				j++;
			}
		}
	}

	/**
	 * Compute subList of spectra with spectra near to reference spectrum (same
	 * moz +/- deltamoz and same RT +/- deltaRT).
	 */
	private static void computeSubListSecondSpectra() {
		double referenceSpectrumMoz = referenceSpectrum.getM_precursorMoz();
		// RT of reference spectrum in sec.
		// TODO think if RT was already in seconds
		int referenceSpectrumRTSec = (int) (referenceSpectrum.getRetentionTime() * 60);
		int nbSpectra = secondSpectra.getSpectraAsObservable().size();
		for (int i = 0; i < nbSpectra; i++) {
			Spectrum testedSpectrum = secondSpectra.getSpectraAsObservable().get(i);
			// check if the moz precursor of the tested spectrum is equals to
			// moz precursor of reference spectrum (+/- deltamoz)
			if (testedSpectrum.getM_precursorMoz() >= (referenceSpectrumMoz - deltaMoz)
					&& testedSpectrum.getM_precursorMoz() <= (referenceSpectrumMoz + deltaMoz)) {
				// check if the RT of the tested spectrum is equals to RT of
				// reference spectrum (+/- deltaRT)
				if ((testedSpectrum.getRetentionTime() * 60) >= (referenceSpectrumRTSec - deltaRT)
						&& (testedSpectrum.getRetentionTime() * 60) <= (referenceSpectrumRTSec + deltaRT)) {
					// when this two condition was successful, the tested
					// spectrum is added to the sublist;
					subListSecondSpectra.addSpectrum(testedSpectrum);
				}
			}
		}
	}

	/**
	 * Count the number of peak which matched between RS and TS.
	 */
	private static void countNbPeak() {
		nbPeaksEquals = 0;
		for (float f : listPeaksTestedSpectrum) {
			if (f != 0) {
				nbPeaksEquals++;
			}
		}
	}

	/**
	 * Determines whether the number of peaks of the reference spectrum can have
	 * their equivalent in a spectrum of sublist (same moz +/- deltaMoz).
	 *
	 * @param spectrumOfSecSpectraSubList
	 *            the spectrum of sec spectra sub list
	 */
	private static void findFragment(Spectrum spectrumOfSecSpectraSubList) {
		// Recover the nbpeaks most intense of the reference spectrum
		Fragment[] nbIntensePeaks = referenceSpectrum.getNbIntensePeaks();
		if (nbIntensePeaks != null) {
			logger.info("The number of intense peaks is: {}", nbIntensePeaks.length);
		} else {
			logger.info("The number of spectrum peaks is under than {}.",
					Session.USER_PARAMS.getComparison().getNbPeaks());
		}
		ArrayList<Fragment> fragmentEquals = spectrumOfSecSpectraSubList.getFragmentEqualsToChart();
		fragmentEquals.clear();
		// Reset value of arrays
		resetPeaks();
		//
		int nbFragmentSpectrumSubList = spectrumOfSecSpectraSubList.getNbFragments();
		// get fragment of the reference spectrum
		for (int i = 0; nbIntensePeaks != null && i < nbIntensePeaks.length; i++) {
			Fragment fragmentReferenceSpectrum = nbIntensePeaks[i];
			// set the range of moz
			double minMozFragmentReferenceSpectrum = fragmentReferenceSpectrum.getMz() - deltaMoz;
			double maxMozFragmentReferenceSpectrum = fragmentReferenceSpectrum.getMz() + deltaMoz;
			// get fragment of the tested spectrum
			for (int j = 0; j < nbFragmentSpectrumSubList; j++) {
				Fragment fragmentSubListSpectrum = spectrumOfSecSpectraSubList.getFragments().get(j);
				double mozFragmentSubListSpectrum = fragmentSubListSpectrum.getMz();
				// check if fragments have the same moz (+/- deltaMoz)
				if (mozFragmentSubListSpectrum >= minMozFragmentReferenceSpectrum
						&& mozFragmentSubListSpectrum <= maxMozFragmentReferenceSpectrum) {
					// if the condition was respected, save the value of the
					// intensity of the fragment in the array (for RS and
					// TS) at
					// the same index
					addPeakReferenceSpectrum(fragmentReferenceSpectrum.getIntensity(), i);
					addPeakTestedSpectrum(fragmentSubListSpectrum.getIntensity(), i);
					// add in the ArrayList of fragmentEquals the most
					// intense
					// fragment (between RS and TS)
					if (fragmentReferenceSpectrum.getIntensity() > fragmentSubListSpectrum.getIntensity()) {
						fragmentEquals.add(fragmentReferenceSpectrum);
					} else {
						fragmentEquals.add(fragmentSubListSpectrum);
					}
				}
			}
		}

	}

	/**
	 * Gets the list square rootpeaks reference spectrum.
	 *
	 * @return the list of square root peaks of the reference spectrum
	 */
	private static Double[] getListSquareRootpeaksReferenceSpectrum() {
		computeListSquareRootpeaksReferenceSpectrum();
		return listSquareRootpeaksReferenceSpectrum;
	}

	/**
	 * Gets the list square rootpeaks tested spectrum.
	 *
	 * @return the list of square root peaks of the tested spectrum
	 */
	private static Double[] getListSquareRootpeaksTestedSpectrum() {
		computeListSquareRootpeaksTestedSpectrum();
		return listSquareRootpeaksTestedSpectrum;
	}

	/**
	 * Gets the reference spectrum.
	 *
	 * @return the reference spectrum
	 */
	public static Spectrum getReferenceSpectrum() {
		return referenceSpectrum;
	}

	/**
	 * Gets the valid spectra.
	 *
	 * @return spectra within the validated spectrum
	 */
	public static Spectra getValidSpectra() {
		return validSpectra;
	}

	/**
	 * Reset the value of sublist and the valid spectra every time the algorithm
	 * was used (for different reference spectrum) and initialize second peak
	 * list.
	 */
	private static void initialize() {
		secondSpectra = ListOfSpectra.getSecondSpectra();
		subListSecondSpectra.initialize();
		validSpectra.initialize();
		// validSpectra.initialize();
		deltaMoz = Session.USER_PARAMS.getComparison().getDeltaMoz();
		deltaRT = Session.USER_PARAMS.getComparison().getDeltaRT();
		nbPeaksMin = Session.USER_PARAMS.getComparison().getNbPeaksMin();
		thetaMin = Session.USER_PARAMS.getComparison().getThetaMin();
		cosThetaMin = Math.cos(Math.toRadians(thetaMin));
		nbPeaks = Session.USER_PARAMS.getComparison().getNbPeaks();
	}

	/**
	 * Run spectra comparison.
	 *
	 * @param spectrumRef
	 *            the spectrum to set as reference.
	 */
	public static void run(Spectrum spectrumRef) {
		referenceSpectrum = spectrumRef;
		setReferenceSpectrum(referenceSpectrum);
		initialize();
		computeSubListSecondSpectra();
		if (subListSecondSpectra.getSpectraAsObservable().size() != 0) {
			for (int i = 0; i < subListSecondSpectra.getSpectraAsObservable().size(); i++) {
				Spectrum testedSpectrum = subListSecondSpectra.getSpectraAsObservable().get(i);
				findFragment(testedSpectrum);
				countNbPeak();
				testedSpectrum.setNbPeaksIdenticalWithReferenceSpectrum(nbPeaksEquals);
				if (nbPeaksEquals >= nbPeaksMin) {
					computeCosTheta();
					if (cosTheta >= cosThetaMin) {
						testedSpectrum.setCosTheta(cosTheta);
						testedSpectrum.setTitleReferenceSpectrum(referenceSpectrum.getM_title());
						testedSpectrum.setDeltaMozWithReferenceSpectrum(
								testedSpectrum.getM_precursorMoz() - referenceSpectrum.getM_precursorMoz());
						testedSpectrum.setDeltaRetentionTimeWithReferenceSpectrum(
								(int) ((testedSpectrum.getRetentionTime() * 60)
										- (referenceSpectrum.getRetentionTime() * 60)));
						validSpectra.addSpectrum(testedSpectrum);
						spectrumRef.getM_matchedSpectra().setAll(validSpectra.getSpectraAsObservable());
					}
				}
			}
		}
	}

	/**
	 * Reset the arrays.
	 */
	private static void resetPeaks() {
		listPeaksReferenceSpectrum = new float[nbPeaks];
		listPeaksTestedSpectrum = new float[nbPeaks];
	}

	/**
	 * Set as reference spectrum.
	 *
	 * @param _referenceSpectrum
	 *            the reference spectrum to set.
	 */
	public static void setReferenceSpectrum(Spectrum _referenceSpectrum) {
		referenceSpectrum = _referenceSpectrum;
	}

}
