package fr.lsmbo.msda.spectra.comp.model;

import java.io.File;
import java.net.URI;
import java.util.Set;

import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.io.PeakListProvider;
import fr.lsmbo.msda.spectra.comp.io.PeaklistReader;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.utils.FileUtils;
import fr.lsmbo.msda.spectra.comp.utils.TaskRunner;
import fr.lsmbo.msda.spectra.comp.view.dialog.AboutDialog;
import fr.lsmbo.msda.spectra.comp.view.dialog.ConfirmDialog;
import fr.lsmbo.msda.spectra.comp.view.dialog.ParametersDialog;
import fr.lsmbo.msda.spectra.comp.view.dialog.ParsingRulesDialog;
import fr.lsmbo.msda.spectra.comp.view.dialog.ShowPopupDialog;
import fr.lsmbo.msda.spectra.comp.view.dialog.SpectraLoaderDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewModel.
 */
public class ViewModel {

	/** The stage. */
	public static Stage stage;

	/** The ref items. */
	private ObservableList<Spectrum> refItems = FXCollections.observableArrayList();

	/** The test items. */
	private ObservableList<Spectrum> testItems = FXCollections.observableArrayList();

	/** The task runner. */
	private TaskRunner task = new TaskRunner();

	/**
	 * Gets the ref items.
	 *
	 * @return the refItems
	 */
	public final ObservableList<Spectrum> getRefItems() {
		return refItems;
	}

	/**
	 * Sets the ref items.
	 *
	 * @param refItems
	 *            the refItems to set
	 */
	public final void setRefItems(ObservableList<Spectrum> refItems) {
		this.refItems = refItems;
	}

	/**
	 * Gets the test items.
	 *
	 * @return the testItems
	 */
	public final ObservableList<Spectrum> getTestItems() {
		return testItems;
	}

	/**
	 * Sets the test items.
	 *
	 * @param testItems
	 *            the testItems to set
	 */
	public final void setTestItems(ObservableList<Spectrum> testItems) {
		this.testItems = testItems;
	}

	/**
	 * * Compare spectra.
	 */
	public void compare() {
		PeakListProvider.compareSpectra();
	}

	/**
	 * Create and displays spectra loader dialog.
	 */
	public void onLoadSpectra() {
		SpectraLoaderDialog spectraLoaderDialog = new SpectraLoaderDialog();
		spectraLoaderDialog.showAndWait().ifPresent(params -> {
			System.out.println("INFO | " + params.toString());
			TaskRunner.doAsyncWork("Loading spectra", () -> {
				// Step 1
				if (!params.getRefPklByDataSourceMap().isEmpty()) {
					params.getRefPklByDataSourceMap().forEach((k, v) -> {
						// Reference spectra loaded from a file
						if (k.equals(DataSource.FILE)) {
							String refFilePath = (String) v;
							loadRefPklFile(refFilePath);
							refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
						} else {
							// Reference spectra loaded from a Proline project
							String msiDbname = params.getRefDbName();
							Set rsmIds = (Set) v;
							loadRefSpectraProline(msiDbname, rsmIds);
							refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
						}
					});
				}
				// Step 2
				if (!params.getTestedPklByDataSourceMap().isEmpty()) {
					params.getTestedPklByDataSourceMap().forEach((k, v) -> {
						// Test spectra loaded from a file
						if (k.equals(DataSource.FILE)) {
							String testFilePath = (String) v;
							loadTestedPklFile(testFilePath);
							testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
						} else {
							// Test spectra loaded from a Proline project
							String msiDbname = params.getRefDbName();
							Set rsmIds = (Set) v;
							loadTestedSpectraProline(msiDbname, rsmIds);
							testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
						}
					});
				}
				return true;
			}, (isSuccess) -> {
				System.out.println("INFO | Task has finished sucessfully!");
				if (!PeaklistReader.isRetentionTimesMissing()) {
					onEditParsingRules();
				}
			}, (failure) -> {
				System.err.println("INFO | Task has failed! " + failure);
			}, false, stage);
		});
	}

	/**
	 * Compare two peaklists task.
	 */
	public void onCompareSpectra() {
		if (isValidSpectra()) {
			TaskRunner.doAsyncWork("Comparing spectra", () -> {
				PeakListProvider.compareSpectra();
				return true;
			}, (isSuccess) -> {
				refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
				testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
				System.out.println("INFO | Task has finished sucessfully!");
			}, (failure) -> {
				System.err.println("INFO | Task has failed! " + failure);
			}, false, stage);
		} else {
			new ShowPopupDialog("Empty Spectra", "The peaklists to compare must not be empty!", stage);
		}
	}

	/**
	 * Edit parsing rules.
	 */
	public void onEditParsingRules() {
		if (isValidSpectra()) {
			ParsingRulesDialog parsingRulesDialog = new ParsingRulesDialog();
			parsingRulesDialog.showAndWait().ifPresent(parsingRule -> {
				TaskRunner.doAsyncWork("Update retention time", () -> {
					ListOfSpectra.getFirstSpectra().getSpectraAsObservable()
							.forEach(spectrum -> spectrum.setRetentionTimeFromTitle(parsingRule.getRegex()));
					ListOfSpectra.getSecondSpectra().getSpectraAsObservable()
							.forEach(spectrum -> spectrum.setRetentionTimeFromTitle(parsingRule.getRegex()));
					return true;
				}, (isSuccess) -> {
					refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
					testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
					System.out.println("INFO | Task has finished sucessfully!");
				}, (failure) -> {
					System.err.println("INFO | Task has failed! " + failure);
				}, false, stage);
			});
		} else {
			new ShowPopupDialog("Empty Spectra", "The peaklists to compare must not be empty!", stage);
		}
	}

	/**
	 * Create and displays database parameters editor dialog.
	 */
	public void onEditDbParameters() {

	}

	/**
	 * Create and displays comparison spectra editor dialog. It will update
	 * session comparison parameters.
	 * 
	 */
	public void onEditCompParameters() {
		ParametersDialog paramsDialog = new ParametersDialog();
		paramsDialog.showAndWait().ifPresent(parametersMap -> {
			if (!parametersMap.isEmpty()) {
				Session.USER_PARAMS.getComparison().setDeltaMoz((float) parametersMap.get("delta_moz"));
				Session.USER_PARAMS.getComparison().setDeltaRT((int) parametersMap.get("delta_rt"));
				Session.USER_PARAMS.getComparison().setNbPeaksMin((int) parametersMap.get("min_peaks_number"));
				Session.USER_PARAMS.getComparison().setNbPeaks((int) parametersMap.get("peaks_number"));
				Session.USER_PARAMS.getComparison().setThetaMin((int) parametersMap.get("theta_min"));
				System.out.println("INFO | Session comparison parameters have been changed successfully!");
			}
		});
	}

	/**
	 * * Load the reference spectra from a peaklist file.
	 *
	 * @param refPklFilePath
	 *            the peaklist file path from where the spectra will be loaded.
	 */
	private void loadRefPklFile(String refPklFilePath) {
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

	/**
	 * * Load the spectra to test from a peaklist file.
	 *
	 * @param testPklFilePath
	 *            the peaklist file path from where the spectra will be loaded.
	 */
	private void loadTestedPklFile(String testPklFilePath) {
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
	private void loadRefSpectraProline(String dbName, Set<Long> rsmIds) {
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
	private void loadTestedSpectraProline(String dbName, Set<Long> rsmIds) {
		try {
			PeakListProvider.loadTestedSpectraFrmProline(dbName, rsmIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * * Exit Spectra-comp software.
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

	/**
	 * Determines whether the peaklist to compare are not empty.
	 * 
	 * @return <code>true</> if the two peaklists to compare are not empty.
	 */

	private Boolean isValidSpectra() {
		return (!ListOfSpectra.getFirstSpectra().getSpectraAsObservable().isEmpty()
				&& !ListOfSpectra.getSecondSpectra().getSpectraAsObservable().isEmpty());
	}

	/**
	 * * About spectra.
	 */
	public void onAboutSpectraComp() {
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.showAndWait().ifPresent(spectraComp -> {
			System.out.println("INFO | About spectra-comp software: " + spectraComp);
		});
	}

	/**
	 * Open the user guide file(RecoverFx_user_guide.pdf).
	 * 
	 */
	public void onOpenUserGuide() {
		try {
			System.out.println("INFO | Open user guide file: spectra_comp_user_guide.pdf.");
			URI srcPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
			String path = new File(srcPath).getParent().replaceAll("\\\\", "/") + File.separator + "config"
					+ File.separator + "documentation" + File.separator + "spectra_comp_user_guide.pdf";
			FileUtils.showFile(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
