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
	 * Load reference spectra from peaklist file
	 * 
	 * @param firstPklList
	 *            the reference peaklist file path
	 */
	public void loadFirstPkl(String firstPklList) {
		try {
			PeaklistReader.isSecondPeakList = false;
			PeakListProvider.loadFirstSpectraFromFile(firstPklList);
			System.out.println("INFO | " + ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size()
					+ "  spectrum was found from reference spectra.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Load tested spectra from peaklist file
	 * 
	 * @param firstPklList
	 *            the tested peaklist file path
	 */
	public void loadSecondPkl(String secondPklList) {
		try {
			PeaklistReader.isSecondPeakList = true;
			PeakListProvider.loadSecondSpectraFromFile(secondPklList);
			System.out.println("INFO | " + ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size()
					+ " spectrum was found from tested spectra.");
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Load tested spectra from a Proline project
	 * 
	 * @param firstPklList
	 *            the reference peaklist file path
	 */
	public void loadRefSpectraProline(String projectName, Set<Long> rsmIds) {
		try {
			PeakListProvider.loadFirstSpectra(projectName, rsmIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Load tested spectra from a Proline project
	 * 
	 * @param firstPklList
	 *            the tested peaklist file path
	 */
	public void loadTestedSpectraProline(String projectName, Set<Long> rsmIds) {
		try {
			PeakListProvider.loadSecondSpectra(projectName, rsmIds);
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Exit software
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
	 */
	public void onOpenUserGuide() {
	}
}
