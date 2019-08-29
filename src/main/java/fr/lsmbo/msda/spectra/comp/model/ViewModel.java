package fr.lsmbo.msda.spectra.comp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBAccess;
import fr.lsmbo.msda.spectra.comp.db.SpectraSource;
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

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ViewModel.class);
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
	 * @param refItems the refItems to set
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
	 * @param testItems the testItems to set
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
			logger.info(params.toString());
			TaskRunner.doAsyncWork("Loading spectra", () -> {
				// Step 1 : load reference spectra
				if (!params.getRefPklByDataSourceMap().isEmpty()) {
					params.getRefPklByDataSourceMap().forEach((k, v) -> {
						if (k.equals(SpectraSource.FILE)) {
							// Reference spectra loaded from a file
							String refFilePath = (String) v;
							loadRefPklFile(refFilePath);
							refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
						} else {
							Long projectId = params.getRefProjectId();
							logger.info("Reference spectra loaded from a Proline project with id=#{}", projectId);
							Set rsmIds = (Set) v;
							loadRefSpectraProline(projectId, rsmIds);
							refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
							Session.USER_PARAMS.setFirstPklList(projectId + " Rsm id= " + rsmIds);
						}
					});
				}
				// Step 2: load test spectra
				if (!params.getTestedPklByDataSourceMap().isEmpty()) {
					params.getTestedPklByDataSourceMap().forEach((k, v) -> {
						// Test spectra loaded from a file
						if (k.equals(SpectraSource.FILE)) {
							String testFilePath = (String) v;
							loadTestedPklFile(testFilePath);
							testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
						} else {
							// Test spectra loaded from a Proline project
							Long testProjectId = params.getRefProjectId();
							logger.info("Test spectra will be loaded from a Proline project with id=#{}",
									testProjectId);
							Set rsmIds = (Set) v;
							loadTestedSpectraProline(testProjectId, rsmIds);
							testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
							Session.USER_PARAMS.setSecondPklList(testProjectId + " Rsm id= " + rsmIds);
						}
					});
				}
				return true;
			}, (isSuccess) -> {
				logger.info("Task has finished sucessfully!");
				System.out.println("INFO | Task has finished sucessfully!");
				if (!PeaklistReader.isRetentionTimesMissing()) {
					onEditParsingRules();
				}
			}, (failure) -> {
				logger.error("Task has failed {}", failure);
				System.err.println("INFO | Task has failed: " + failure);
			}, false, stage);
		});
	}

	/**
	 * Export comparison
	 * 
	 */
	public void onExportComparsion() {
		if (isValidSpectra()) {
			try {
				createPdfFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			new ShowPopupDialog("Empty Spectra", "The peaklists to compare must not be empty!", stage);
		}
	}

	/**
	 * Compare two peak lists task.
	 */
	public void onCompareSpectra() {
		if (isValidSpectra()) {
			TaskRunner.doAsyncWork("Comparing spectra", () -> {
				PeakListProvider.compareSpectra();
				return true;
			}, (isSuccess) -> {
				refItems.setAll(ListOfSpectra.getFirstSpectra().getSpectraAsObservable());
				testItems.setAll(ListOfSpectra.getSecondSpectra().getSpectraAsObservable());
				logger.info("Task has finished sucessfully!");
				System.out.println("INFO | Task has finished sucessfully!");
			}, (failure) -> {
				logger.error("Task has failed {}", failure);
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
					logger.info("Task has finished sucessfully!");
					System.out.println("INFO | Task has finished sucessfully!");
				}, (failure) -> {
					logger.error("Task has failed {}", failure);
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
	 * Create and displays comparison spectra editor dialog. It will update session
	 * comparison parameters.
	 * 
	 */
	public void onEditCompParameters() {
		ParametersDialog paramsDialog = new ParametersDialog();
		paramsDialog.showAndWait().ifPresent(parametersMap -> {
			if (!parametersMap.isEmpty()) {
				Session.USER_PARAMS.getComparison().setDeltaPrecMoz((float) parametersMap.get("delta_prec_moz"));
				Session.USER_PARAMS.getComparison().setDeltaPeaksMoz((float) parametersMap.get("delta_peaks_moz"));
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
	 * @param refPklFilePath the peaklist file path from where the spectra will be
	 *                       loaded.
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
	 * @param testPklFilePath the peaklist file path from where the spectra will be
	 *                        loaded.
	 */
	private void loadTestedPklFile(String testPklFilePath) {
		try {
			PeaklistReader.isSecondPeakList = true;
			PeakListProvider.loadTestedSpectraFromFile(testPklFilePath);
			logger.info("{} spectrum was found from the tested spectra",
					ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size());
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
	 * @param dbName The database name to connect to. Usually it's msi_db_project_ID
	 * @param rsmIds the result_summary ids from where to compute the spectra.
	 */
	private void loadRefSpectraProline(final long projectId, final Set<Long> rsmIds) {
		try {
			PeakListProvider.loadTest(projectId, (long) 50);
			PeakListProvider.loadRefSpectraFrmProline(projectId, rsmIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Load tested spectra from a Proline project.
	 * 
	 * @param dbName the database name to connect to. Usually it's msi_db_project_ID
	 * @param rsmIds the result_summary ids from where to compute the spectra.
	 */
	private void loadTestedSpectraProline(final Long projectId, final Set<Long> rsmIds) {
		try {
			PeakListProvider.loadTestedSpectraFrmProline(projectId, rsmIds);
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
		logger.warn("Exit spectra-comp");
		new ConfirmDialog<Object>(ICON.EXIT, "Exit spectra-comp", "Are you sure you want to exit spectra-comp ?",
				() -> {
					// Close all active connections
					DBAccess.closeAllDb();
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
			logger.info("About spectra-comp software: {}", spectraComp);
		});
	}

	/**
	 * Open the user guide file(RecoverFx_user_guide.pdf).
	 * 
	 */
	public void onOpenUserGuide() {
		try {
			logger.info("Open user guide file: spectra_comp_user_guide.pdf");
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

	/***
	 * Create Pdf file
	 * 
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	private void createPdfFile() throws FileNotFoundException, DocumentException {
		FileUtils.openPdfFile(file -> {
			TaskRunner.doAsyncWork("Export Comparison file", () -> {
				Document document = new Document();
				try {
					PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
					document.open();
					// Chunk title = new Chunk("Sepctra-Comp " +
					// Session.SPECTRACOMP_RELEASE_VERSION,
					// FontFactory.getFont(FontFactory.COURIER, 15, Font.ITALIC,
					// new BaseColor(0, 0, 0)));
					// document.add(title);
					// document.add(Chunk.NEWLINE);
					// document.add(Chunk.NEWLINE);
					// Add comparison parameters
					Paragraph p1 = new Paragraph("The reference peaklist: " + Session.USER_PARAMS.getFirstPklList());
					document.add(p1);
					document.add(Chunk.NEWLINE);
					Paragraph p12 = new Paragraph("The tested peaklist: " + Session.USER_PARAMS.getSecondPklList());
					document.add(p12);
					document.add(Chunk.NEWLINE);
					Paragraph p2 = new Paragraph("The comparison parameters:");
					document.add(p2);
					document.add(Chunk.NEWLINE);
					PdfPTable paramsTable = new PdfPTable(6);
					addTableHeaderContent(paramsTable);
					addParamsRow(paramsTable);
					document.add(paramsTable);
					document.add(Chunk.NEWLINE);
					// Add comparison result
					Paragraph p3 = new Paragraph("The comparison result:");
					document.add(p3);
					document.add(Chunk.NEWLINE);
					PdfPTable table = new PdfPTable(3);
					addTableHeader(table);
					addRows(table);
					// addCustomRows(table);
					document.add(table);
					document.add(Chunk.NEWLINE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} finally {
					if (document != null && document.isOpen())
						document.close();
				}
				return true;
			}, (isSuccess) -> {
				System.out.println("INFO | Task has finished sucessfully!");
			}, (failure) -> {
				logger.error("Task has failed {}", failure);
				System.err.println("INFO | Task has failed! " + failure);
			}, false, stage);
		}, stage);

	}

	/**
	 * Create pdf table
	 * 
	 * @param table the table to add
	 */
	private void addTableHeader(PdfPTable table) {
		Stream.of(" ", "Spectra number", "Matched spectra number").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(1);
			header.setPhrase(new Phrase(columnTitle));
			table.addCell(header);
		});
	}

	/**
	 * add rows to the pdf table
	 * 
	 * @param table the table to add rows in.
	 */
	private void addRows(PdfPTable table) {
		Long refItemsSize = refItems.stream().filter(spec -> !spec.getM_matchedSpectra().isEmpty()).count();
		table.addCell("Reference spectra");
		table.addCell(refItems.size() + "");
		table.addCell(refItemsSize + "");
		// tested spectra
		table.addCell("Tested spectra");
		table.addCell(testItems.size() + "");
		table.addCell("n");
	}

	/**
	 * Create pdf table
	 * 
	 * @param table the table to add
	 */
	private void addTableHeaderContent(PdfPTable table) {
		Stream.of("", "Delta Moz(da) ", "Delta retention time(s)", "Min peaks number", "Min theta", "Peaks number")
				.forEach(columnTitle -> {
					PdfPCell header = new PdfPCell();
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setBorderWidth(1);
					header.setPhrase(new Phrase(columnTitle));
					table.addCell(header);
				});
	}

	/**
	 * Add rows to the pdf table
	 * 
	 * @param table the table to add rows in.
	 */
	private void addParamsRow(PdfPTable table) {
		table.addCell("Comparison parameters");
		table.addCell(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaPrecMoz()));
		table.addCell(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaRT()));
		table.addCell(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaksMin()));
		table.addCell(String.valueOf(Session.USER_PARAMS.getComparison().getThetaMin()));
		table.addCell(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaks()));
	}
}
