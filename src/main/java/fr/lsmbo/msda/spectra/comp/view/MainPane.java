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
	private FilteredTableView<Spectrum> refFilteredTable;

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

	/** The stage. */
	public static Stage stage;

	/**
	 * Instantiates a new main pane.
	 *
	 * @param model
	 *            the model
	 */
	public MainPane(ViewModel model) {
		// Create the main view
		BorderPane mainView = new BorderPane();
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
		});
		MenuItem exportSpectra = new MenuItem(" Excel export ");
		exportSpectra.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		exportSpectra.setOnAction(e -> {
		});
		// Settings menu items
		Menu settingsMenu = new Menu(" Settings ");
		MenuItem parsingRules = new MenuItem(" Parsing Rules ");
		parsingRules.setGraphic(new ImageView(IconResource.getImage(ICON.EDIT)));
		parsingRules.setOnAction(e -> {
			model.onEditParsingRules();
		});
		MenuItem dbParameters = new MenuItem(" Database parameters ");
		dbParameters.setGraphic(new ImageView(IconResource.getImage(ICON.DATABASE)));
		dbParameters.setOnAction(e -> {
			model.onEditDbParameters();
		});
		MenuItem compParameters = new MenuItem(" Comparaison parameters ");
		compParameters.setGraphic(new ImageView(IconResource.getImage(ICON.SETTINGS)));
		compParameters.setOnAction(e -> {
			model.onEditCompParameters();
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
		fileMenu.getItems().addAll(loadSpectra, exitFile);
		settingsMenu.getItems().addAll(parsingRules, compParameters);
		helpMenu.getItems().addAll(userGuide, aboutSpectraComp);
		menuBar.getMenus().addAll(fileMenu, settingsMenu, helpMenu);
		mainView.setTop(menuBar);
		// Create ref table view
		/***********************
		 * Filtered table view *
		 ***********************/
		refFilteredTable = new FilteredTableView<>(model.getRefItems());
		refFilteredTable.setId("filtered-ref-table");
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

		refFilteredTable.getColumns().setAll(refIdColumn, refTitleColumn, refMozColumn, refIntensityColumn,
				refChargeColumn, refRtColumn, refNbrFragmentsColumn, refMatchedColumn);

		refFilteredTable.autosize();
		refFilteredTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		refFilteredTable.setPadding(new Insets(5, 5, 5, 5));

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

		testMatchedColumn = new FilterableBooleanTableColumn<>("Matched");
		testMatchedColumn.setCellValueFactory(cellData -> cellData.getValue().getMatched());
		testMatchedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(testMatchedColumn));

		testFilteredTable.getColumns().setAll(testIdColumn, testTitleColumn, testMozColumn, testIntensityColumn,
				testChargeColumn, testRtColumn, testNbrFragmentsColumn, testMatchedColumn);

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
		referenxcePklFileTab.setContent(refFilteredTable);
		referenxcePklFileTab.setClosable(false);
		referenceTabPane.getTabs().addAll(referenxcePklFileTab);

		// Create second tabpane
		TabPane testedTabPane = new TabPane();
		Tab testedPklFileTab = new Tab(" Tested spectra ");
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
						refFilteredTable.refresh();
						testFilteredTable.refresh();
						return true;
					}, stage);
		});

		BorderPane graphicsPane = new BorderPane();
		graphicsPane.setMinHeight(150);
		Label deltaMozLabel = new Label("Delta moz:");
		TextField deltaMozTF = new TextField();
		deltaMozTF.setTooltip(new Tooltip("Enter the delta Moz value!"));
		deltaMozTF.setPrefWidth(100);
		deltaMozTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaMoz()));
		deltaMozTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setDeltaMoz(Float.valueOf(n));
		});

		Label deltaRTLabel = new Label("Delta rt:");
		TextField deltaRTTF = new TextField();
		deltaRTTF.setPrefWidth(100);
		deltaRTTF.setTooltip(new Tooltip("Enter the delta retention time value!"));
		deltaRTTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaRT()));
		deltaRTTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setDeltaRT(Integer.valueOf(n));
		});

		Label minPeaksNbrLabel = new Label("Min peaks number:");
		TextField minPeaksNbrTF = new TextField();
		minPeaksNbrTF.setPrefWidth(100);
		minPeaksNbrTF.setTooltip(new Tooltip("Enter the minimum peaks number value!"));
		minPeaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaksMin()));
		minPeaksNbrTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setNbPeaksMin(Integer.valueOf(n));
		});

		Label thetaMinLabel = new Label("Theta min:");
		TextField thetaMinTF = new TextField();
		thetaMinTF.setPrefWidth(100);
		thetaMinTF.setTooltip(new Tooltip("Enter the Theta min value!"));
		thetaMinTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getThetaMin()));
		thetaMinTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setThetaMin(Integer.valueOf(n));
		});

		Label peaksNbrLabel = new Label("peaks number:");
		TextField peaksNbrTF = new TextField();
		peaksNbrTF.setPrefWidth(100);
		peaksNbrTF.setTooltip(new Tooltip("Enter the peaks number value!"));
		peaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaks()));
		peaksNbrTF.textProperty().addListener((o, v, n) -> {
			if (n != null)
				Session.USER_PARAMS.getComparison().setNbPeaks(Integer.valueOf(n));
		});
		addFloatValidation(deltaMozTF);
		addIntegerValidation(deltaRTTF);
		addIntegerValidation(minPeaksNbrTF);
		addIntegerValidation(thetaMinTF);
		addIntegerValidation(peaksNbrTF);
		compareButton.disableProperty()
				.bind(deltaMozTF.textProperty().isEmpty()
						.or(deltaRTTF.textProperty().isEmpty().or(minPeaksNbrTF.textProperty().isEmpty()
								.or(thetaMinTF.textProperty().isEmpty().or(peaksNbrTF.textProperty().isEmpty())))));

		GridPane settingsPane = new GridPane();
		settingsPane.setPadding(new Insets(2));
		settingsPane.setHgap(10);
		settingsPane.setAlignment(Pos.BASELINE_CENTER);
		settingsPane.add(deltaMozLabel, 0, 0, 1, 1);
		settingsPane.add(deltaMozTF, 1, 0, 1, 1);
		settingsPane.add(deltaRTLabel, 2, 0, 1, 1);
		settingsPane.add(deltaRTTF, 3, 0, 1, 1);
		settingsPane.add(minPeaksNbrLabel, 4, 0, 1, 1);
		settingsPane.add(minPeaksNbrTF, 5, 0, 1, 1);
		settingsPane.add(thetaMinLabel, 6, 0, 1, 1);
		settingsPane.add(thetaMinTF, 7, 0, 1, 1);
		settingsPane.add(peaksNbrLabel, 8, 0, 1, 1);
		settingsPane.add(peaksNbrTF, 9, 0, 1, 1);
		settingsPane.add(compareButton, 11, 0, 1, 1);
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
			Platform.runLater(() -> {
				if (newValue != null) {
					refSelectedSpectrum = newValue;
					// Update the matched spectra of test table view
					model.getTestItems().forEach(spec -> {
						if (refSelectedSpectrum.getM_matchedSpectra().contains(spec)) {
							spec.getMatched().setValue(true);
							System.out.println(spec.getM_title());
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
				refFilteredTable.refresh();
				testFilteredTable.refresh();
			});
		});
		// Test spectrum
		spectrumProperty.getTestSpectrumProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
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
				refFilteredTable.refresh();
				testFilteredTable.refresh();
			});
		});
		refFilteredTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				refSelectedSpectrum = newValue;
				spectrumProperty.getRefSpectrumProperty().setValue(refSelectedSpectrum);
				refFilteredTable.refresh();
			}
		});

		// Update view on spectrum test selection
		testFilteredTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				testSelectedSpectrum = newValue;
				spectrumProperty.getTestSpectrumProperty().setValue(testSelectedSpectrum);
				refFilteredTable.refresh();
			}
		});

		// Create the spectrum chart node
		this.getChildren().addAll(mainView, glassPane);
	}

	/**
	 * Adds the float validation.
	 *
	 * @param field
	 *            the field
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
	 * @param field
	 *            the field
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

}
