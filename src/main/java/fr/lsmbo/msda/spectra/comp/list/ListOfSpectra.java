/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.list;

/**
 * Keep the information for the two list of spectrum and method to access either
 * the first or the second spectra 
 * 
 * @author Aromdhani
 *
 */
public class ListOfSpectra {

	private static Spectra firstSpectra = new Spectra();
	private static Spectra secondSpectra = new Spectra();


	private static Spectra[] arraySpectra = { firstSpectra, secondSpectra };

	
	/**
	 * 
	 * @param spectra
	 *            the spectra to add
	 */
	public static void addFirstSpectra(Spectra spectra) {
		arraySpectra[0] = spectra;
	}

	/**
	 * 
	 * @param spectra
	 *            the spectra to add
	 */
	public static void addSecondSpectra(Spectra spectra) {
		arraySpectra[1] = spectra;
	}

	
	/**
	 * 
	 * @return an array of spectra of the first.
	 */
	public static Spectra getFirstSpectra() {
		return arraySpectra[0];
	}

	/**
	 * 
	 * @return an array of spectra of the second.
	 */
	public static Spectra getSecondSpectra() {
		return arraySpectra[1];
	}
}
