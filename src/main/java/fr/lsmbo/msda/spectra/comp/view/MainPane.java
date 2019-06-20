package fr.lsmbo.msda.spectra.comp.view;

import org.google.jhsheets.filtered.FilteredTableView;
import org.google.jhsheets.filtered.tablecolumn.FilterableBooleanTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableDoubleTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableFloatTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableIntegerTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableLongTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.model.ViewModel;
import fr.lsmbo.msda.spectra.comp.utils.TaskRunner;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	private FilterableBooleanTableColumn<Spectrum, Boolean> refMatchedColumn;

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

	/** The test ref id column. */
	private FilterableLongTableColumn<Spectrum, Long> testRefIdColumn;

	/** The spectrum pane. */
	// Spectrum pane
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
		// Settings menu items
		Menu settingsMenu = new Menu(" Settings ");
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
		settingsMenu.getItems().addAll(dbParameters, compParameters);
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
		refMatchedColumn = new FilterableBooleanTableColumn<>("Matched");
		refMatchedColumn.setCellValueFactory(cellData -> cellData.getValue().getMatched());
		refMatchedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(refMatchedColumn));

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

		// Identified Column
		testRefIdColumn = new FilterableLongTableColumn<>("Ref Id");
		testRefIdColumn.setCellValueFactory(new PropertyValueFactory<Spectrum, Long>("ref_id"));
		testFilteredTable.getColumns().setAll(testIdColumn, testTitleColumn, testMozColumn, testIntensityColumn,
				testChargeColumn, testRtColumn, testNbrFragmentsColumn, testRefIdColumn);

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
		//
		peaklistSplitPane.getItems().addAll(referenceTabPane, testedTabPane);
		peaklistSplitPane.setPadding(new Insets(10));
		peaklistSplitPane.setMinHeight(350);
		Button compareButton = new Button("Compare");
		compareButton.setOnAction(e -> {
		});
		BorderPane graphicsPane = new BorderPane();
		graphicsPane.setMinHeight(150);
		// Create and Set SwingContent(swingNode);
		HBox compareButtonPane = new HBox();
		compareButtonPane.getChildren().addAll(compareButton);
		compareButtonPane.setPadding(new Insets(5));
		compareButtonPane.setAlignment(Pos.BASELINE_CENTER);
		graphicsPane.setTop(compareButtonPane);
		graphicsPane.setCenter(swingNodeForChart);
		compareButton.setGraphic(new ImageView(IconResource.getImage(ICON.EXECUTE)));
		mainSplitPane.getItems().addAll(peaklistSplitPane, graphicsPane, ConsolePane.getInstance());
		mainView.setTop(menuBar);
		mainView.setCenter(mainSplitPane);

		TaskRunner.mainView = mainSplitPane;
		TaskRunner.glassPane = glassPane;
		TaskRunner.statusLabel = new Label("");

		this.getChildren().addAll(mainView, glassPane);
	}
}
