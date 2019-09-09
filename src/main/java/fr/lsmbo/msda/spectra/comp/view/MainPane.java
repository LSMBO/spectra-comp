package fr.lsmbo.msda.spectra.comp.view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.google.jhsheets.filtered.FilteredTableView;
import org.google.jhsheets.filtered.tablecolumn.FilterableBooleanTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableDoubleTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableFloatTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableIntegerTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableLongTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.model.ViewModel;
import fr.lsmbo.msda.spectra.comp.utils.TaskRunner;
import fr.lsmbo.msda.spectra.comp.view.dialog.ConfirmDialog;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class MainPane.
 */
public class MainPane extends StackPane {

	// Main view components
	// Reference table view
	/** The ref filtered table. */
	// Filtered table
	private FilteredTableView<Spectrum> referenceFilteredTable;

	/** The ref id column. */
	// Filtered columns
	private FilterableLongTableColumn<Spectrum, Long> refIdColumn;

	/** The ref title column. */
	private FilterableStringTableColumn<Spectrum, String> refTitleColumn;

	/** The ref moz column. */
	private FilterableDoubleTableColumn<Spectrum, Double> refMozColumn;

	/** The ref intensity column. */
	private FilterableFloatTableColumn<Spectrum, Float> refIntensityColumn;

	/** The ref charge column. */
	private FilterableIntegerTableColumn<Spectrum, Integer> refChargeColumn;

	/** The ref rt column. */
	private FilterableFloatTableColumn<Spectrum, Float> refRtColumn;

	/** The ref nbr fragments column. */
	private FilterableIntegerTableColumn<Spectrum, Integer> refNbrFragmentsColumn;

	/** The ref matched column. */
	private FilterableIntegerTableColumn<Spectrum, Integer> refMatchedColumn;

	/** The test filtered table. */
	// Test table view
	private FilteredTableView<Spectrum> testFilteredTable;

	/** The test id column. */
	// Filtered columns
	private FilterableLongTableColumn<Spectrum, Long> testIdColumn;

	/** The test title column. */
	private FilterableStringTableColumn<Spectrum, String> testTitleColumn;

	/** The test moz column. */
	private FilterableDoubleTableColumn<Spectrum, Float> testMozColumn;

	/** The test intensity column. */
	private FilterableFloatTableColumn<Spectrum, Float> testIntensityColumn;

	/** The test charge column. */
	private FilterableIntegerTableColumn<Spectrum, Integer> testChargeColumn;

	/** The test rt column. */
	private FilterableFloatTableColumn<Spectrum, Float> testRtColumn;

	/** The test nbr fragments column. */
	private FilterableIntegerTableColumn<Spectrum, Integer> testNbrFragmentsColumn;

	private FilterableDoubleTableColumn<Spectrum, Double> testCosColumn;
	/** The test matched column. */
	private FilterableBooleanTableColumn<Spectrum, Boolean> testMatchedColumn;

	/** The ref selected spectrum. */
	private Spectrum refSelectedSpectrum = null;

	/** The test selected spectrum. */
	private Spectrum testSelectedSpectrum = null;

	/** The spectrum property. */
	private SpectrumProperty spectrumProperty = new SpectrumProperty();

	/** The spectrum pane. */
	private SpectrumPane spectrumPane;

	/** The swing node for chart. */
	private final SwingNode swingNodeForChart = new SwingNode();
	
	/** The main view pane */
	private final BorderPane mainView = new BorderPane();
	
	/** The stage. */
	public static Stage stage;

	private TextField deltaPrecMozTF, deltaPeaksMozTF, deltaRTTF, minPeaksNbrTF, thetaMinTF, peaksNbrTF;

	/**
	 * @return the refFilteredTable
	 */
	public final FilteredTableView<Spectrum> getRefFilteredTable() {
		return referenceFilteredTable;
	}

	/**
	 * @param refFilteredTable the refFilteredTable to set
	 */
	public final void setRefFilteredTable(FilteredTableView<Spectrum> refFilteredTable) {
		this.referenceFilteredTable = refFilteredTable;
	}

	/**
	 * @return the testFilteredTable
	 */
	public final FilteredTableView<Spectrum> getTestFilteredTable() {
		return testFilteredTable;
	}

	/**
	 * @param testFilteredTable the testFilteredTable to set
	 */
	public final void setTestFilteredTable(FilteredTableView<Spectrum> testFilteredTable) {
		this.testFilteredTable = testFilteredTable;
	}

	/**
	 * @return the spectrumProperty
	 */
	public final SpectrumProperty getSpectrumProperty() {
		return spectrumProperty;
	}

	/**
	 * @param spectrumProperty the spectrumProperty to set
	 */
	public final void setSpectrumProperty(SpectrumProperty spectrumProperty) {
		this.spectrumProperty = spectrumProperty;
	}

	/**
	 * @return the deltaMozTF
	 */
	public final TextField getPrecDeltaMozTF() {
		return deltaPrecMozTF;
	}

	/**
	 * @param deltaMozTF the deltaMozTF to set
	 */
	public final void setPrecDeltaMozTF(TextField deltaMozTF) {
		this.deltaPrecMozTF = deltaMozTF;
	}

	/**
	 * @return the deltaRTTF
	 */
	public final TextField getDeltaRTTF() {
		return deltaRTTF;
	}

	/**
	 * @param deltaRTTF the deltaRTTF to set
	 */
	public final void setDeltaRTTF(TextField deltaRTTF) {
		this.deltaRTTF = deltaRTTF;
	}

	/**
	 * @return the minPeaksNbrTF
	 */
	public final TextField getMinPeaksNbrTF() {
		return minPeaksNbrTF;
	}

	/**
	 * @param minPeaksNbrTF the minPeaksNbrTF to set
	 */
	public final void setMinPeaksNbrTF(TextField minPeaksNbrTF) {
		this.minPeaksNbrTF = minPeaksNbrTF;
	}

	/**
	 * @return the thetaMinTF
	 */
	public final TextField getThetaMinTF() {
		return thetaMinTF;
	}

	/**
	 * @param thetaMinTF the thetaMinTF to set
	 */
	public final void setThetaMinTF(TextField thetaMinTF) {
		this.thetaMinTF = thetaMinTF;
	}

	/**
	 * @return the peaksNbrTF
	 */
	public final TextField getPeaksNbrTF() {
		return peaksNbrTF;
	}

	/**
	 * @param peaksNbrTF the peaksNbrTF to set
	 */
	public final void setPeaksNbrTF(TextField peaksNbrTF) {
		this.peaksNbrTF = peaksNbrTF;
	}

	/**
	 * Instantiates a new main pane.
	 *
	 * @param model the model
	 */
	public MainPane(ViewModel model) {
		// Create the main view
		mainView.setPrefSize(1400, 800);
		// Create the glassePane
		VBox glassPane = new VBox();
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		progressIndicator.setVisible(true);
		glassPane.getChildren().add(progressIndicator);
		glassPane.setAlignment(Pos.CENTER);
		glassPane.setVisible(false);
		// Create menu and menu items
		MenuBar menuBar = new MenuBar();
		// File menu items
		// Load spectra
		Menu fileMenu = new Menu(" File ");
		MenuItem loadSpectra = new MenuItem(" Load spectra ");
		loadSpectra.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		loadSpectra.setOnAction(e -> {
			model.onLoadSpectra();
			refreshTables();
		});
		MenuItem exportComparison = new MenuItem(" Export comparison ");
		exportComparison.setGraphic(new ImageView(IconResource.getImage(ICON.EXPORT)));
		exportComparison.setOnAction(e -> {
			model.onExportComparsion();
			refreshTables();
		});
		// Settings menu items
		Menu settingsMenu = new Menu(" Settings ");
		MenuItem parsingRules = new MenuItem(" Parsing Rules ");
		parsingRules.setGraphic(new ImageView(IconResource.getImage(ICON.EDIT)));
		parsingRules.setOnAction(e -> {
			model.onEditParsingRules();
			refreshTables();
		});
		MenuItem dbParameters = new MenuItem(" Database parameters ");
		dbParameters.setGraphic(new ImageView(IconResource.getImage(ICON.DATABASE)));
		dbParameters.setOnAction(e -> {
			model.onEditDbParameters();
			refreshTables();
		});
		MenuItem compParameters = new MenuItem(" Comparaison parameters ");
		compParameters.setGraphic(new ImageView(IconResource.getImage(ICON.SETTINGS)));
		compParameters.setOnAction(e -> {
			model.onEditCompParameters();
			deltaPrecMozTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaPrecMoz()));
			deltaPeaksMozTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaPeaksMoz()));
			peaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaks()));
			thetaMinTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getThetaMin()));
			minPeaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaksMin()));
			deltaRTTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaRT()));
		});
		// Exit
		MenuItem exitFile = new MenuItem(" Exit ");
		exitFile.setGraphic(new ImageView(IconResource.getImage(ICON.EXIT)));
		exitFile.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		exitFile.setOnAction((e) -> {
			model.onExit();
		});
		// Help menu items
		// User guide menu item
		Menu helpMenu = new Menu(" Help ");
		MenuItem userGuide = new MenuItem(" User guide ");
		userGuide.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
		userGuide.setGraphic(new ImageView(IconResource.getImage(ICON.HELP)));
		userGuide.setOnAction((ActionEvent t) -> {
			model.onOpenUserGuide();
		});
		// About Recover menu item
		MenuItem aboutSpectraComp = new MenuItem(" About ");
		aboutSpectraComp.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
		aboutSpectraComp.setGraphic(new ImageView(IconResource.getImage(ICON.INFORMATION)));
		aboutSpectraComp.setOnAction((ActionEvent t) -> {
			model.onAboutSpectraComp();
		});
		fileMenu.getItems().addAll(loadSpectra, exportComparison, exitFile);
		settingsMenu.getItems().addAll(parsingRules, compParameters);
		helpMenu.getItems().addAll(userGuide, aboutSpectraComp);
		menuBar.getMenus().addAll(fileMenu, settingsMenu, helpMenu);
		mainView.setTop(menuBar);
		// Create ref table view
		/***********************
		 * Filtered table view *
		 ***********************/
		referenceFilteredTable = new FilteredTableView<>(model.getReferenceItems());
		referenceFilteredTable.setId("filtered-ref-table");
		// Id column
		refIdColumn = new FilterableLongTableColumn<>("Id");
		refIdColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Long>("m_id"));

		// Title Column
		refTitleColumn = new FilterableStringTableColumn<>("Title");
		refTitleColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("m_title"));

		// Mz Column
		refMozColumn = new FilterableDoubleTableColumn<>("Mz");
		refMozColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Double>("m_precursorMoz"));

		// Intensity Column
		refIntensityColumn = new FilterableFloatTableColumn<>("Intensity");
		refIntensityColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("m_precursorIntensity"));

		// Charge Column
		refChargeColumn = new FilterableIntegerTableColumn<>("Charge");
		refChargeColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("m_precursorCharge"));

		// RT Column
		refRtColumn = new FilterableFloatTableColumn<>("Retention time");
		refRtColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("retentionTime"));

		// Fragment number
		refNbrFragmentsColumn = new FilterableIntegerTableColumn<>("Fragment number");
		refNbrFragmentsColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("nbFragments"));

		// Identified Column
		refMatchedColumn = new FilterableIntegerTableColumn<>("Matched");
		refMatchedColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("m_matchedSize"));

		referenceFilteredTable.getColumns().setAll(refIdColumn, refTitleColumn, refMozColumn, refIntensityColumn,
				refChargeColumn, refRtColumn, refNbrFragmentsColumn, refMatchedColumn);

		referenceFilteredTable.autosize();
		referenceFilteredTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		referenceFilteredTable.setPadding(new Insets(5, 5, 5, 5));

		/***********************
		 * Filtered table view *
		 ***********************/
		testFilteredTable = new FilteredTableView<>(model.getTestItems());
		testFilteredTable.setId("filtered-test-table");
		// Id column
		testIdColumn = new FilterableLongTableColumn<>("Id");
		testIdColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Long>("m_id"));

		// Title Column
		testTitleColumn = new FilterableStringTableColumn<>("Title");
		testTitleColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("m_title"));

		// Mz Column
		testMozColumn = new FilterableDoubleTableColumn<>("Mz");
		testMozColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Double>("m_precursorMoz"));

		// Intensity Column
		testIntensityColumn = new FilterableFloatTableColumn<>("Intensity");
		testIntensityColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("m_precursorIntensity"));

		// Charge Column
		testChargeColumn = new FilterableIntegerTableColumn<>("Charge");
		testChargeColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("m_precursorCharge"));

		// RT Column
		testRtColumn = new FilterableFloatTableColumn<>("Retention time");
		testRtColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("retentionTime"));

		// Fragment number
		testNbrFragmentsColumn = new FilterableIntegerTableColumn<>("Fragment number");
		testNbrFragmentsColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("nbFragments"));

		testCosColumn = new FilterableDoubleTableColumn<>("Cos θ");
		testCosColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Double>("cosTheta"));

		testMatchedColumn = new FilterableBooleanTableColumn<>("Matched");
		testMatchedColumn.setCellValueFactory(cellData -> cellData.getValue().getMatched());
		testMatchedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(testMatchedColumn));

		testFilteredTable.getColumns().setAll(testIdColumn, testTitleColumn, testMozColumn, testIntensityColumn,
				testChargeColumn, testRtColumn, testNbrFragmentsColumn, testCosColumn, testMatchedColumn);

		testFilteredTable.autosize();
		testFilteredTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		testFilteredTable.setPadding(new Insets(5, 5, 5, 5));
		// Create main Splite pane
		SplitPane mainSplitPane = new SplitPane();
		mainSplitPane.setPadding(new Insets(10));
		mainSplitPane.setOrientation(Orientation.VERTICAL);
		mainSplitPane.setPrefHeight(320);
		mainSplitPane.setDividerPositions(0.6f, 0.2f, 0.1f);
		// Create the peaklists pane
		SplitPane peaklistSplitPane = new SplitPane();
		peaklistSplitPane.setOrientation(Orientation.HORIZONTAL);
		peaklistSplitPane.setPrefHeight(320);
		peaklistSplitPane.setDividerPositions(0.5f, 0.5f);

		// Create the 2 peak list pane
		// Create first tabpane
		TabPane referenceTabPane = new TabPane();
		Tab referenxcePklFileTab = new Tab(" Reference spectra ");
		referenxcePklFileTab.setContent(referenceFilteredTable);
		referenxcePklFileTab.setClosable(false);
		referenceTabPane.getTabs().addAll(referenxcePklFileTab);

		// Create second tabpane
		TabPane testedTabPane = new TabPane();
		Tab testedPklFileTab = new Tab(" Test spectra ");
		testedPklFileTab.setContent(testFilteredTable);
		testedPklFileTab.setClosable(false);
		testedTabPane.getTabs().addAll(testedPklFileTab);
		// Main pane
		peaklistSplitPane.getItems().addAll(referenceTabPane, testedTabPane);
		peaklistSplitPane.setPadding(new Insets(10));
		peaklistSplitPane.setMinHeight(350);
		/* Settings spectra pane */
		Button compareButton = new Button("Compare");
		compareButton.setGraphic(new ImageView(IconResource.getImage(ICON.EXECUTE)));
		compareButton.setOnAction(e -> {
			new ConfirmDialog<Boolean>(ICON.WARNING, "Compare spectra",
					"Are you sure that you want to compare spectra ? This action could take a while!", () -> {
						model.onCompareSpectra();
						referenceFilteredTable.refresh();
						testFilteredTable.refresh();
						return true;
					}, stage);
		});

		BorderPane graphicsPane = new BorderPane();
		graphicsPane.setMinHeight(150);
		Label deltaMozLabel = new Label("Prec delta moz (Da):");
		deltaPrecMozTF = new TextField();
		deltaPrecMozTF.setTooltip(new Tooltip("Enter the delta moz value (Da)!"));
		deltaPrecMozTF.setPrefWidth(80);
		deltaPrecMozTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaPrecMoz()));
		deltaPrecMozTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setDeltaPrecMoz(Float.valueOf(n));
		});

		Label peaksDeltaMozLabel = new Label("Fragment delta moz (Da):");
		deltaPeaksMozTF = new TextField();
		deltaPeaksMozTF.setTooltip(new Tooltip("Enter the fragment delta moz value (Da)!"));
		deltaPeaksMozTF.setPrefWidth(80);
		deltaPeaksMozTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaPeaksMoz()));
		deltaPeaksMozTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setDeltaPeaksMoz(Float.valueOf(n));
		});

		Label deltaRTLabel = new Label("Delta rt (sec):");
		deltaRTTF = new TextField();
		deltaRTTF.setPrefWidth(80);
		deltaRTTF.setTooltip(new Tooltip("Enter the delta retention time value in secondes!"));
		deltaRTTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaRT()));
		deltaRTTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setDeltaRT(Integer.valueOf(n));
		});

		Label minPeaksNbrLabel = new Label("Min peaks number:");
		minPeaksNbrTF = new TextField();
		minPeaksNbrTF.setPrefWidth(80);
		minPeaksNbrTF.setTooltip(new Tooltip("Enter the minimum peaks number value!"));
		minPeaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaksMin()));
		minPeaksNbrTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setNbPeaksMin(Integer.valueOf(n));
		});

		Label thetaMinLabel = new Label("Theta min:");
		thetaMinTF = new TextField();
		thetaMinTF.setPrefWidth(80);
		thetaMinTF.setTooltip(new Tooltip("Enter the Theta min value!"));
		thetaMinTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getThetaMin()));
		thetaMinTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setThetaMin(Integer.valueOf(n));
		});

		Label peaksNbrLabel = new Label("Peaks number:");
		peaksNbrTF = new TextField();
		peaksNbrTF.setPrefWidth(80);
		peaksNbrTF.setTooltip(new Tooltip("Enter the peaks number value!"));
		peaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaks()));
		peaksNbrTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setNbPeaks(Integer.valueOf(n));
		});
		addFloatValidation(deltaPrecMozTF);
		addFloatValidation(deltaPeaksMozTF);
		addIntegerValidation(deltaRTTF);
		addIntegerValidation(minPeaksNbrTF);
		addIntegerValidation(thetaMinTF);
		addIntegerValidation(peaksNbrTF);
		compareButton.disableProperty().bind(deltaPrecMozTF.textProperty().isEmpty().or(deltaRTTF.textProperty()
				.isEmpty().or(minPeaksNbrTF.textProperty().isEmpty().or(thetaMinTF.textProperty().isEmpty()
						.or(peaksNbrTF.textProperty().isEmpty().or(deltaPeaksMozTF.textProperty().isEmpty()))))));

		GridPane settingsPane = new GridPane();
		settingsPane.setPadding(new Insets(2));
		settingsPane.setHgap(10);
		settingsPane.setAlignment(Pos.BASELINE_CENTER);
		settingsPane.add(deltaMozLabel, 0, 0, 1, 1);
		settingsPane.add(deltaPrecMozTF, 1, 0, 1, 1);
		settingsPane.add(peaksDeltaMozLabel, 2, 0, 1, 1);
		settingsPane.add(deltaPeaksMozTF, 3, 0, 1, 1);
		settingsPane.add(deltaRTLabel, 4, 0, 1, 1);
		settingsPane.add(deltaRTTF, 5, 0, 1, 1);
		settingsPane.add(minPeaksNbrLabel, 6, 0, 1, 1);
		settingsPane.add(minPeaksNbrTF, 7, 0, 1, 1);
		settingsPane.add(thetaMinLabel, 8, 0, 1, 1);
		settingsPane.add(thetaMinTF, 9, 0, 1, 1);
		settingsPane.add(peaksNbrLabel, 10, 0, 1, 1);
		settingsPane.add(peaksNbrTF, 11, 0, 1, 1);
		settingsPane.add(compareButton, 12, 0, 1, 1);
		graphicsPane.setTop(settingsPane);

		graphicsPane.setCenter(swingNodeForChart);
		mainSplitPane.getItems().addAll(peaklistSplitPane, graphicsPane, ConsolePane.getInstance());
		mainView.setTop(menuBar);
		mainView.setCenter(mainSplitPane);

		TaskRunner.mainView = mainSplitPane;
		TaskRunner.glassPane = glassPane;
		TaskRunner.statusLabel = new Label("");
		// Reference spectrum
		// Update view on reference spectrum selection
		spectrumProperty.getRefSpectrumProperty().addListener((observable, oldValue, newValue) -> {
			updateOnJFx(() -> {
				if (newValue != null) {
					refSelectedSpectrum = newValue;
					// Update the matched spectra of test table view
					model.getTestItems().forEach(spec -> {
						if (refSelectedSpectrum.getM_matchedSpectra().contains(spec)) {
							spec.getMatched().setValue(true);
						} else {
							spec.getMatched().setValue(false);
						}
					});
					// Create the spectrum chart node
					spectrumPane = new SpectrumPane(refSelectedSpectrum, true);
					// Update the spectrum view
					SwingUtilities.invokeLater(() -> {
						swingNodeForChart.setContent(spectrumPane.getPanel());
					});
				} else {
					refSelectedSpectrum = null;
					// Update the spectrum view
					SwingUtilities.invokeLater(() -> {
						swingNodeForChart.setContent(new JPanel());
					});
				}
				referenceFilteredTable.refresh();
				testFilteredTable.refresh();
			});
		});
		// Test spectrum
		spectrumProperty.getTestSpectrumProperty().addListener((observable, oldValue, newValue) -> {
			updateOnJFx(() -> {
				if (newValue != null && spectrumPane != null) {
					testSelectedSpectrum = newValue;
					// Update the spectrum view
					spectrumPane.addMirroredSpectrum(testSelectedSpectrum);
					SwingUtilities.invokeLater(() -> {
						swingNodeForChart.setContent(spectrumPane.getPanel());
					});
				} else {
					testSelectedSpectrum = null;
					// Update the spectrum view
					SwingUtilities.invokeLater(() -> {
						swingNodeForChart.setContent(new JPanel());
					});
				}
				referenceFilteredTable.refresh();
				testFilteredTable.refresh();
			});
		});
		referenceFilteredTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				refSelectedSpectrum = newValue;
				spectrumProperty.getRefSpectrumProperty().setValue(refSelectedSpectrum);
				referenceFilteredTable.refresh();
			}
		});

		// Update view on spectrum test selection
		testFilteredTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				testSelectedSpectrum = newValue;
				spectrumProperty.getTestSpectrumProperty().setValue(testSelectedSpectrum);
				referenceFilteredTable.refresh();
			}
		});

		// Create the spectrum chart node
		this.getChildren().addAll(mainView, glassPane);
	}

	/**
	 * Adds the float validation.
	 *
	 * @param field the field
	 */
	private void addFloatValidation(TextField field) {
		field.getProperties().put("type", "float");
		field.setTextFormatter(new TextFormatter<>(c -> {
			if (c.isContentChange()) {
				if (c.getControlNewText().length() == 0) {
					return c;
				}
				try {
					Float.parseFloat(c.getControlNewText());
					return c;
				} catch (NumberFormatException e) {
				}
				return null;
			}
			return c;
		}));
	}

	/**
	 * Adds the integer validation.
	 *
	 * @param field the field
	 */
	private void addIntegerValidation(TextField field) {
		field.getProperties().put("type", "integer");
		field.setTextFormatter(new TextFormatter<>(c -> {
			if (c.isContentChange()) {
				if (c.getControlNewText().length() == 0) {
					return c;
				}
				try {
					Integer.parseInt(c.getControlNewText());
					return c;
				} catch (NumberFormatException e) {
				}
				return null;
			}
			return c;
		}));
	}

	/**
	 * Update the view on Java Fx thread
	 * 
	 * @param r Runnable to submit
	 */
	private void updateOnJFx(Runnable r) {
		try {
			stage.getScene().setCursor(Cursor.WAIT);
			mainView.setDisable(true);
			referenceFilteredTable.setDisable(true);
			Platform.runLater(r);
		} finally {
			stage.getScene().setCursor(Cursor.DEFAULT);
			mainView.setDisable(false);
		}
	}

	/**
	 * Refresh tables
	 * 
	 */
	private void refreshTables() {
		updateOnJFx(() -> {
			referenceFilteredTable.refresh();
			testFilteredTable.refresh();
		});
	}
}
