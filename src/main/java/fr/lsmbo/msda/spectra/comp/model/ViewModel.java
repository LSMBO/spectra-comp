package fr.lsmbo.msda.spectra.comp.model;

import java.sql.SQLException;
import java.util.Set;

import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.io.PeakListProvider;
import fr.lsmbo.msda.spectra.comp.io.PeaklistReader;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.utils.ConfirmDialog;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ViewModel {
	public static Stage stage;

	/***
	 * Compare spectra
	 */
	public void compare() {
		PeakListProvider.compareSpectra();
	}

	/***
	 * Load the reference spectra from a peaklist file
	 * 
	 * @param refPklFilePath
	 *            the peaklist file path from where the spectra will be loaded.
	 */
	public void loadRefPklFile(String refPklFilePath) {
		try {
			PeaklistReader.isSecondPeakList = false;
			PeakListProvider.loadRefSpectraFromFile(refPklFilePath);
			System.out.println("INFO | " + ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size()
					+ "  spectrum was found from the reference spectra.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Load the spectra to test from a peaklist file
	 * 
	 * @param testPklFilePath
	 *            the peaklist file path from where the spectra will be loaded.
	 */
	public void loadTestedPklFile(String testPklFilePath) {
		try {
			PeaklistReader.isSecondPeakList = true;
			PeakListProvider.loadTestedSpectraFromFile(testPklFilePath);
			System.out.println("INFO | " + ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size()
					+ " spectrum was found from the tested spectra.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load reference spectra from a Proline project.
	 * 
	 * @param dbName
	 *            The database name to connect to. Usually it's
	 *            msi_db_project_ID
	 * @param rsmIds
	 *            the result_summary ids from where to compute the spectra.
	 */
	void loadRefSpectraProline(String dbName, Set<Long> rsmIds) {
		try {
			PeakListProvider.loadRefSpectraFrmProline(dbName, rsmIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load tested spectra from a Proline project.
	 * 
	 * @param dbName
	 *            the database name to connect to. Usually it's
	 *            msi_db_project_ID
	 * @param rsmIds
	 *            the result_summary ids from where to compute the spectra.
	 */
	public void loadTestedSpectraProline(String dbName, Set<Long> rsmIds) {
		try {
			PeakListProvider.loadTestedSpectraFrmProline(dbName, rsmIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Exit Spectra-comp software
	 */
	public void onExit() {
		System.out.println("WARN | Exit spectra-comp");
		new ConfirmDialog<Object>(ICON.EXIT, "Exit spectra-comp", "Are you sure you want to exit spectra-comp ?",
				() -> {
					Platform.exit();
					System.exit(0);
					return null;
				}, stage);
	}

	/***
	 * About spectra
	 */
	public void onAboutSpectraComp() {
	}

	/***
	 * Open user guide
	 * 
	 */
	public void onOpenUserGuide() {
	}
}
