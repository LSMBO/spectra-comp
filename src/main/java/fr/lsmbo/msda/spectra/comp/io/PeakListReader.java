package fr.lsmbo.msda.spectra.comp.io;

import java.io.File;

import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;

public class PeakListReader {
	
	static File file = null;

	/**
	 * Load first spectra
	 */
	public static void loadFirstSpectra() {
		if (Session.userParams.getDataSource() == DataSource.DATABASE) {
			ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			loadFile(file);
		}
	}

	/**
	 * Load second spectra
	 */
	public static void loadSecondSpectra() {
		if (Session.userParams.getDataSource() == DataSource.DATABASE) {
			ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
					.setAll(DBSpectraHandler.getSpectra().getSpectraAsObservable());
		} else {
			loadFile(file);
		}
	}

	/**
	 * Load spectra from file
	 * 
	 * @param file
	 */

	private static void loadFile(File file) {

	}
}
