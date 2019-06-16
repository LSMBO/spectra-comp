package fr.lsmbo.msda.spectra.comp.model;

import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.io.PeakListProvider;
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
	 * Load first spectra
	 * 
	 * @param projectName   the project name
	 * @param secondPklList the second peakist
	 */
	public void loadFirstPkl(String projectName, String firstPklList) {
		try {
			Session.USER_PARAMS.setDataSource("file");
			PeakListProvider.loadFirstSpectra(projectName, firstPklList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * Load second spectra
	 * 
	 * @param projectName   the project name
	 * @param secondPklList the second peakist
	 */
	public void loadSecondPkl(String projectName, String secondPklList) {
		try {
			Session.USER_PARAMS.setDataSource("file");
			PeakListProvider.loadSecondSpectra(projectName, secondPklList);
		} catch (SQLException e) {
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
