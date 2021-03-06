
package fr.lsmbo.msda.spectra.comp.list;

import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// TODO: Auto-generated Javadoc
/**
 * Regroup all spectrum as an ObservableList and make specific treatment: Add
 * spectrum in the observable list, update retention time for all spectrum,
 * check the number of spectrum recovered and find a spectrum with its title.
 * 
 * @author Aromdhani
 */
public class Spectra {

	/** The spectra. */
	private ObservableList<Spectrum> spectra = initialiseList();
	
	/** The nb spectra. */
	private Integer nbSpectra = 0;
	
	/** The nb identified. */
	private Integer nbIdentified = 0;
	
	/** The nb matched. */
	private Integer nbMatched = 0;
	
	/** The percentage identified. */
	private Float percentageIdentified = 0F;

	/**
	 * Default constructor.
	 */
	public Spectra() {
		super();
	}

	/**
	 * Add a spectrum.
	 *
	 * @param spectrum            the spectrum to add
	 */
	public void addSpectrum(Spectrum spectrum) {

		spectra.add(spectrum);
		nbSpectra++;
	}

	/**
	 * Compute the percentage of identified spectrum.
	 */
	private void computePercentageIdentified() {
		if (nbSpectra != 0) {
			percentageIdentified = (((float) nbIdentified / (float) nbSpectra) * 100);
		}

	}

	/**
	 * Count the number of matched spectrum.
	 */
	private void countNbMatched() {
		nbMatched = 0;
		for (Spectrum sp : getSpectraAsObservable()) {
			if (sp.getNbMatch() != 0) {
				nbMatched++;
			}
		}
	}

	/**
	 * Scan all the spectrum and increment the number of recover every time the
	 * value of recover for this spectrum will be true.
	 */
	public void countRecoveredAndIdentifiedSpectra() {

		nbIdentified = 0;
		Integer nb = getSpectraAsObservable().size();
		for (int i = 0; i < nb; i++) {
			Spectrum spectrum = getSpectraAsObservable().get(i);
			if (spectrum.getMatched().get())
				nbIdentified++;
		}
	}

	/**
	 * Return the number of identified spectra.
	 * 
	 * @return nbIdentified
	 */
	public Integer getNbIdentified() {
		countRecoveredAndIdentifiedSpectra();
		return nbIdentified;
	}

	/**
	 * Return the number of matched spectrum.
	 *
	 * @return the nb matched
	 */
	public Integer getNbMatched() {
		countNbMatched();
		return nbMatched;
	}

	/**
	 * Return the number of spectra.
	 * 
	 * @return nbSpectra
	 */
	public Integer getNbSpectra() {
		return nbSpectra;
	}

	/**
	 * Return the percentage of identified spectrum.
	 *
	 * @return the percentage identified
	 */
	public Float getPercentageIdentified() {
		computePercentageIdentified();
		return percentageIdentified;
	}

	/**
	 * Return an observable list of spectra.
	 * 
	 * @return spectra
	 */
	public ObservableList<Spectrum> getSpectraAsObservable() {
		return spectra;
	}

	/**
	 * Gets the spectrum with title.
	 *
	 * @param title            title of spectrum that we need to find
	 * @return specificSpectrum
	 */
	public Spectrum getSpectrumWithTitle(String title) {
		Integer nb = getSpectraAsObservable().size();
		Spectrum specificSpectrum = null;
		for (int i = 0; i < nb; i++) {
			Spectrum spectrum = getSpectraAsObservable().get(i);
			if (spectrum.getM_title().equalsIgnoreCase(title)) {
				specificSpectrum = spectrum;
			}
		}
		return specificSpectrum;
	}

	/**
	 * Initialize the list.
	 * 
	 * @return an empty observableArrayList
	 */
	private ObservableList<Spectrum> initialiseList() {
		ObservableList<Spectrum> list = FXCollections.observableArrayList();
		return list;
	}

	/**
	 * Initialize the spectra.
	 */
	public void initialize() {
		if (!spectra.isEmpty())
			spectra.clear();
		nbSpectra = 0;
		nbIdentified = 0;
		percentageIdentified = 0F;
		nbMatched = 0;
	}

	/**
	 * Remove the first spectra.
	 */
	public void removeOne() {
		if (spectra.size() > 0)
			spectra.remove(0);
	}

	/**
	 * Reset the Cos theta.
	 */
	public synchronized void resetCosTheta() {
		for (Spectrum sp : getSpectraAsObservable()) {
			sp.setCosTheta(0);
		}
	}

	/**
	 * Update the retention time from the titles.
	 */
	public synchronized void updateRetentionTimeFromTitle() {
		for (Spectrum spectrum : spectra) {
			spectrum.setRetentionTimeFromTitle();
		}
	}
}
