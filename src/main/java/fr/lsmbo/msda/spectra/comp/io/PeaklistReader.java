/*
 * 
 */

package fr.lsmbo.msda.spectra.comp.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.list.Spectra;
import fr.lsmbo.msda.spectra.comp.model.Fragment;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;

/**
 * Read a file(mgf or pkl) and stock the information concerning spectrum and
 * spectra.
 * 
 * @see Spectrum, Spectra, ListOfSpectra
 * @author Aromdhani
 *
 */
public class PeaklistReader {

	private static boolean isRetentionTimeMissing = true;
	public static boolean isSecondPeakList = false;

	/**
	 * Determines whether the retention is defined.
	 * 
	 * @return <code> true </code> if the retention time is defined
	 */
	public static Boolean isRetentionTimesDefined() {
		return isRetentionTimeMissing;
	}

	/**
	 * Load peakList file
	 * 
	 * @param file
	 *            the file to load.
	 */
	public static void load(File file) {

		String filePath = file.getAbsolutePath();
		Session.HIGHEST_FRAGMENT_MZ = 0D;
		Session.HIGHEST_FRAGMENT_INTENSITY = 0F;
		// Start to read file
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			if (file.getName().endsWith(".mgf")) {
				readMgfFile(reader);
			} else if (file.getName().endsWith(".pkl")) {
				readPklFile(reader);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read mgf file
	 * 
	 * @param reader
	 *            the bufferedReader
	 */
	private static void readMgfFile(BufferedReader reader) throws IOException {
		String line;
		Integer lineNumber = 1;
		Spectrum spectrum = null;
		String textBeforeFirstSpectrum = "";
		Integer spectrumId = 0;
		Integer fragmentId = 0;
		Spectra spectra = new Spectra();
		while ((line = reader.readLine()) != null) { // iterate through the
														// lines of the file
			if (line.startsWith("BEGIN IONS")) {
				spectrumId++;
				spectrum = new Spectrum();
				spectrum.setLineStart(lineNumber);
				spectrum.setM_id(spectrumId);

			} else if (line.startsWith("TITLE")) {
				String title = line.replaceFirst("TITLE.\\s+", "");
				if (!title.contains("TITLE=")) {
					spectrum.setM_title(line.replaceFirst("TITLE.\\s+", ""));
				} else if (title.contains("TITLE=")) {
					spectrum.setM_title(line.replaceFirst("TITLE=", ""));
				}
				if (isRetentionTimeMissing && spectrum.getRetentionTime() > 0)
					isRetentionTimeMissing = false;

			} else if (line.startsWith("RTINSECONDS")) {
				// Convert seconds to minutes
				spectrum.setRetentionTime(new Float(line.replaceFirst("RTINSECONDS=", "")) / 60);
				isRetentionTimeMissing = false;

			} else if (line.startsWith("PEPMASS")) {
				String[] items = line.replaceFirst("PEPMASS=", "").split("[\\t\\s]");
				spectrum.setM_precursorMoz(new Double(items[0]));
				if (items.length > 1) {
					spectrum.setM_precursorIntensity(new Float(items[1]));
				} else {
					spectrum.setM_precursorIntensity(0F);
				}

			} else if (line.startsWith("CHARGE") && spectrum != null) {
				spectrum.setM_precursorCharge(new Integer(line.replaceFirst("CHARGE=", "").replaceAll("\\+", "")));

			} else if (line.startsWith("END IONS")) {
				spectrum.setLineStop(lineNumber);
				spectra.addSpectrum(spectrum);

			} else if (line.matches("^[\\d\\s\\t\\.\\+]+$")) {
				Fragment fragment = new Fragment();
				try { // just in case casting fails
					String[] items = line.split("[\\s\\t]");
					fragmentId++;
					fragment.setId(fragmentId);
					fragment.setMz(new Double(items[0]));
					fragment.setIntensity(new Float(items[1]));
					if (items.length == 3)
						fragment.setCharge(new Integer(items[2].replaceAll("\\+", "")));
					if (fragment.getMz() > Session.HIGHEST_FRAGMENT_MZ)
						Session.HIGHEST_FRAGMENT_MZ = fragment.getMz();
					if (fragment.getIntensity() > Session.HIGHEST_FRAGMENT_INTENSITY)
						Session.HIGHEST_FRAGMENT_INTENSITY = fragment.getIntensity();
				} catch (Exception e) {
				}
				spectrum.addFragment(fragment);

			} else if (spectrum == null) {
				textBeforeFirstSpectrum += line;
			}

			lineNumber++;
		}
		// Add spectra as a first spectra
		if (!isSecondPeakList) {
			System.out.println("INFO - Add first peaklist");
			ListOfSpectra.addFirstSpectra(spectra);
		}
		// Add spectra as a second spectra
		if (isSecondPeakList) {
			System.out.println("INFO - Add second peaklist");
			ListOfSpectra.addSecondSpectra(spectra);
		}
		isRetentionTimeMissing = false;
	}

	/**
	 * Read peak list file
	 * 
	 * @param reader
	 *            the buffered reader
	 * @throws IOException
	 */
	private static void readPklFile(BufferedReader reader) throws IOException {
		String line;
		Integer lineNumber = 1;
		Spectrum spectrum = null;
		Integer spectrumId = 0;
		Integer fragmentId = 0;
		Spectra spectra = new Spectra();
		while ((line = reader.readLine()) != null) { // iterate through the
														// lines of the file
			if (line.isEmpty()) {
				// spectra are separated by a blank line
				if (spectrum != null) {
					// store the previous spectrum if any
					spectrum.setLineStop(lineNumber);
					spectra.addSpectrum(spectrum);
				}
				// reset spectrum
				spectrum = null;
			} else {
				String[] items = line.split("[\\t\\s]");
				if (items.length == 3) {
					spectrum = new Spectrum();
					spectrumId++;
					spectrum.setM_id(spectrumId);
					spectrum.setLineStart(lineNumber);
					try { // just in case casting fails
						spectrum.setM_precursorMoz(new Double(items[0]));
						spectrum.setM_precursorIntensity(new Float(items[1]));
						spectrum.setM_precursorCharge(new Integer(items[2]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (items.length == 2) {
					fragmentId++;
					Fragment fragment = new Fragment();
					fragment.setId(fragmentId);
					try { // just in case casting fails
						fragment.setMz(new Double(items[0]));
						fragment.setIntensity(new Float(items[1]));
						if (fragment.getMz() > Session.HIGHEST_FRAGMENT_MZ)
							fragment.setMz(Session.HIGHEST_FRAGMENT_MZ);
						if (fragment.getIntensity() > Session.HIGHEST_FRAGMENT_INTENSITY)
							fragment.setIntensity(Session.HIGHEST_FRAGMENT_INTENSITY);
					} catch (Exception e) {
					}
					spectrum.addFragment(fragment);
				} // other cases should never happen !
			}
			lineNumber++;
		}
		// Add spectra as a first spectra
		if (!isSecondPeakList) {
			System.out.println("INFO - Add first peaklist");
			ListOfSpectra.addFirstSpectra(spectra);
		}
		// Add spectra as a second spectra
		if (isSecondPeakList) {
			System.out.println("INFO - Add second peaklist");
			ListOfSpectra.addSecondSpectra(spectra);
		}
		isRetentionTimeMissing = false;
	}
}
