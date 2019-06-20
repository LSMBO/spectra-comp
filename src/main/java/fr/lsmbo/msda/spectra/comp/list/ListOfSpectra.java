/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.list;

// TODO: Auto-generated Javadoc
/**
 * Keep the information for the two list of spectrum and method to access either
 * the first or the second spectra .
 *
 * @author Aromdhani
 */
public class ListOfSpectra {

	/** The first spectra. */
	private static Spectra firstSpectra = new Spectra();
	
	/** The second spectra. */
	private static Spectra secondSpectra = new Spectra();


	/** The array spectra. */
	private static Spectra[] arraySpectra = { firstSpectra, secondSpectra };

	
	/**
	 * Adds the first spectra.
	 *
	 * @param spectra            the spectra to add
	 */
	public static void addFirstSpectra(Spectra spectra) {
		arraySpectra[0] = spectra;
	}

	/**
	 * Adds the second spectra.
	 *
	 * @param spectra            the spectra to add
	 */
	public static void addSecondSpectra(Spectra spectra) {
		arraySpectra[1] = spectra;
	}

	
	/**
	 * Gets the first spectra.
	 *
	 * @return an array of spectra of the first.
	 */
	public static Spectra getFirstSpectra() {
		return arraySpectra[0];
	}

	/**
	 * Gets the second spectra.
	 *
	 * @return an array of spectra of the second.
	 */
	public static Spectra getSecondSpectra() {
		return arraySpectra[1];
	}
}
