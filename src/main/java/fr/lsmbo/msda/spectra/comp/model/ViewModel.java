package fr.lsmbo.msda.spectra.comp.model;

import java.util.Set;

import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.io.PeakListProvider;
import fr.lsmbo.msda.spectra.comp.io.PeaklistReader;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.utils.ConfirmDialog;
import fr.lsmbo.msda.spectra.comp.view.dialog.SpectraLoaderDialog;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ViewModel {
	public static Stage stage;
	private ObservableList<Spectrum> refItems;
	private ObservableList<Spectrum> testItems;
	
	/**
	 * @return the refItems
	 */
	public final ObservableList<Spectrum> getRefItems() {
		return refItems;
	}

	/**
	 * @param refItems the refItems to set
	 */
	public final void setRefItems(ObservableList<Spectrum> refItems) {
		this.refItems = refItems;
	}

	/**
	 * @return the testItems
	 */
	public final ObservableList<Spectrum> getTestItems() {
		return testItems;
	}

	/**
	 * @param testItems the testItems to set
	 */
	public final void setTestItems(ObservableList<Spectrum> testItems) {
		this.testItems = testItems;
	}

	/***
	 * Compare spectra
	 */
	public void compare() {
		PeakListProvider.compareSpectra();
	}

	/**
	 * Create and displays spectra loader dialog
	 * 
	 */
	public void onLoadSpectra() {
		SpectraLoaderDialog spectraLoaderDialog = new SpectraLoaderDialog();
		spectraLoaderDialog.showAndWait().ifPresent(params -> {
			System.out.println("INFO | "+params.toString());
		});
	}

	/**
	 * Create and displays database parameters editor dialog
	 * 
	 */
	public void onEditDbParameters() {

	}

	/**
	 * Create and displays comparison spectra editor dialog
	 * 
	 */
	public void onEditCompParameters() {

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
