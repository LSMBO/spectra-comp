package fr.lsmbo.msda.spectra.comp.view.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.SpectraSource;
import fr.lsmbo.msda.spectra.comp.model.Dataset;
import fr.lsmbo.msda.spectra.comp.model.Dataset.DatasetType;
import fr.lsmbo.msda.spectra.comp.model.Parameters;
import fr.lsmbo.msda.spectra.comp.model.Project;
import fr.lsmbo.msda.spectra.comp.utils.FileUtils;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays spectra loader dialog.
 * 
 * @author Aromdhani
 *
 */
public class SpectraLoaderDialog extends Dialog<Parameters> {

	/** The connection label. */
	// Components
	private Label connectionLabel;

	/** The second connection label. */
	private Label testConnectionLabel;

	/** The user projects CBX. */
	private ComboBox<Project> userProjectsCBX = new ComboBox<Project>();

	/** The second user projects CBX. */
	private ComboBox<Project> testUserProjectsCBX = new ComboBox<Project>();

	/** The ref user projects. */
	private ObservableList<Project> refUserProjects = FXCollections.observableArrayList();

	/** The test user projects. */
	private ObservableList<Project> testUserProjects = FXCollections.observableArrayList();

	/** The params. */
	private Parameters params;

	/** The first root. */
	private StackPane refRoot;

	/** The root item. */
	private TreeItem refRootItem;

	/** The tree view. */
	private TreeView<Dataset> referenceTreeView;

	/** The second root. */
	private StackPane testRoot;

	/** The second root item. */
	private TreeItem testRootItem;

	/** The second tree view. */
	private TreeView<Dataset> testTreeView;

	/** The stage. */
	public static Stage stage;

	/** The reference project id. */
	//
	private Long referenceProjectId;

	/** The test project id. */
	private Long testProjectId;

	/** The ref pkl by data source map. */
	private Map<SpectraSource, Object> refPeakListByDataSourceMap = new HashMap<>();

	/** The test pkl by data source map. */
	private Map<SpectraSource, Object> testPeakListByDataSourceMap = new HashMap<>();

	/** The reference resultset id set. */
	private HashSet<Long> referenceResultSetIdSet = new HashSet<>();

	/** The test resultset id set. */
	private HashSet<Long> testResultSetIdSet = new HashSet<>();

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unchecked")
	public SpectraLoaderDialog() {

		SplitPane peaklistSplitPane = new SplitPane();
		peaklistSplitPane.setOrientation(Orientation.HORIZONTAL);
		peaklistSplitPane.setPrefHeight(320);
		peaklistSplitPane.setDividerPositions(0.5f, 0.5f);
		// Create radio buttons
		ToggleGroup group = new ToggleGroup();
		RadioButton refPeakListFileRB = new RadioButton("Select the reference peaklist file");
		refPeakListFileRB.setToggleGroup(group);
		RadioButton refPeakListProlineRB = new RadioButton("Select the reference peaklist from your Proline projects");
		refPeakListProlineRB.setSelected(true);
		refPeakListProlineRB.setToggleGroup(group);
		refPeakListFileRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("file");
		});
		refPeakListProlineRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("database");
		});
		// Create the 1 peak list pane
		SplitPane peaklist1SplitPane = new SplitPane();
		peaklist1SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist1SplitPane.setPrefHeight(320);
		peaklist1SplitPane.setDividerPositions(0.2f, 0.8f);

		VBox warningPane = new VBox(2);
		Label emptyrefPeakListLabel = new Label(
				"Choose the reference peaklist file. Make sure that you have selected a valid file!");
		emptyrefPeakListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyrefPeakListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningPane.getChildren().addAll(emptyrefPeakListLabel);
		Label refPeakListLabel = new Label("Reference peaklist:");
		TextField refPeakListTF = new TextField();
		refPeakListTF.setText(Session.USER_PARAMS.getFirstPklList());
		refPeakListTF.setTooltip(new Tooltip("Choose the reference peaklist file."));
		Button loadRefPeakListButton = new Button("Load");
		loadRefPeakListButton.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		loadRefPeakListButton.setOnAction(e -> {
			load(refPeakListTF);
		});
		// Layout
		GridPane refPklListPane = new GridPane();
		refPklListPane.setAlignment(Pos.TOP_LEFT);
		refPklListPane.setPadding(new Insets(15));
		refPklListPane.setHgap(15);
		refPklListPane.setVgap(15);
		refPklListPane.add(refPeakListFileRB, 0, 0, 3, 1);
		refPklListPane.add(warningPane, 0, 1, 3, 1);
		refPklListPane.add(refPeakListLabel, 0, 2, 1, 1);
		refPklListPane.add(refPeakListTF, 1, 2, 1, 1);
		refPklListPane.add(loadRefPeakListButton, 2, 2, 1, 1);
		GridPane.setHgrow(refPeakListTF, Priority.ALWAYS);
		// From Proline projects
		Button ButtonConnection = new Button("Connect ...");
		ButtonConnection.setGraphic(new ImageView(IconResource.getImage(ICON.ADMIN)));
		ButtonConnection.setOnAction(e -> {
			LoginDialog loginDialog = new LoginDialog();
			loginDialog.showAndWait().ifPresent(userProject -> {
				connectionLabel.setVisible(false);
				refUserProjects.setAll(userProject);
				userProjectsCBX.setItems(refUserProjects);
				userProjectsCBX.setConverter(new StringConverter<Project>() {
					@Override
					public String toString(Project object) {
						return object.getName();
					}

					@Override
					public Project fromString(String string) {
						return null;
					}
				});
			});
		});

		// Update the view
		userProjectsCBX.setOnAction(e -> {
			referenceProjectId = userProjectsCBX.getValue().getId();
			refRootItem.getChildren().clear();
			refRootItem.getChildren().addAll(createDatasets(userProjectsCBX.getValue().getId()));
			referenceTreeView = new TreeView(refRootItem);
			refRoot.getChildren().add(referenceTreeView);
		});
		VBox warningDbPane = new VBox(2);
		connectionLabel = new Label("Off connection. Connect to your Proline account please!");
		connectionLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		connectionLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningDbPane.getChildren().addAll(connectionLabel);

		// Create dialog components
		Label fileLocationLabel = new Label("User peaklists:");
		connectionLabel.setPrefWidth(580);
		refRoot = new StackPane();
		refRoot.setPadding(new Insets(5));

		// Set components control
		refRootItem = new TreeItem("Peaklists");
		refRootItem.setExpanded(true);
		refRootItem.getChildren().addAll();
		referenceTreeView = new TreeView(refRootItem);
		refRoot.getChildren().add(referenceTreeView);

		// Layout
		GridPane projectsPane = new GridPane();
		projectsPane.setAlignment(Pos.TOP_LEFT);
		projectsPane.setPadding(new Insets(10));
		projectsPane.setHgap(15);
		projectsPane.setVgap(15);
		projectsPane.addRow(0, fileLocationLabel);
		projectsPane.add(refRoot, 0, 1, 1, 6);

		Label userProjectLabel = new Label("User projects:");

		GridPane refPklListDBPane = new GridPane();
		refPklListDBPane.setAlignment(Pos.TOP_LEFT);
		refPklListDBPane.setPadding(new Insets(15));
		refPklListDBPane.setHgap(15);
		refPklListDBPane.setVgap(15);

		refPklListDBPane.add(refPeakListProlineRB, 0, 0, 3, 1);
		refPklListDBPane.add(warningDbPane, 0, 1, 3, 1);
		refPklListDBPane.add(ButtonConnection, 3, 1, 1, 1);
		refPklListDBPane.add(userProjectLabel, 0, 2, 2, 1);
		refPklListDBPane.add(userProjectsCBX, 3, 2, 1, 1);
		refPklListDBPane.add(refRoot, 0, 3, 4, 1);
		GridPane.setHgrow(refRoot, Priority.ALWAYS);
		peaklist1SplitPane.getItems().addAll(refPklListPane, refPklListDBPane);
		// Control
		warningPane.visibleProperty().bind(refPeakListTF.textProperty().isEmpty());
		warningPane.disableProperty().bind(refPeakListProlineRB.selectedProperty());
		refPeakListLabel.disableProperty().bind(refPeakListProlineRB.selectedProperty());
		refPeakListTF.disableProperty().bind(refPeakListProlineRB.selectedProperty());
		loadRefPeakListButton.disableProperty().bind(refPeakListProlineRB.selectedProperty());

		warningDbPane.disableProperty().bind(refPeakListFileRB.selectedProperty());
		userProjectLabel.disableProperty().bind(refPeakListFileRB.selectedProperty());
		ButtonConnection.disableProperty().bind(refPeakListFileRB.selectedProperty());
		userProjectsCBX.disableProperty().bind(refPeakListFileRB.selectedProperty());
		// Create the second pane
		SplitPane peaklist2SplitPane = new SplitPane();
		peaklist2SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist2SplitPane.setPrefHeight(320);
		peaklist2SplitPane.setDividerPositions(0.2f, 0.8f);

		ToggleGroup group2 = new ToggleGroup();
		RadioButton testPeakListFileRB = new RadioButton("Select the test peaklist file");
		testPeakListFileRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("file");
		});
		testPeakListFileRB.setToggleGroup(group2);
		RadioButton testPeakListProlineRB = new RadioButton("Select the test peaklist from your Proline projects");
		testPeakListProlineRB.setSelected(true);
		testPeakListProlineRB.setToggleGroup(group2);
		testPeakListProlineRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("database");
		});

		VBox warning2Pane = new VBox(2);
		Label emptySecondPklListLabel = new Label(
				"Choose the test peaklist file. Make sure that you have selected a valid file!");
		emptySecondPklListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptySecondPklListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warning2Pane.getChildren().addAll(emptySecondPklListLabel);
		Label testPeakListLabel = new Label("Test peaklist:");
		TextField testPeakListTF = new TextField();
		testPeakListTF.setText(Session.USER_PARAMS.getSecondPklList());
		testPeakListTF.setTooltip(new Tooltip("Choose the test peaklist file."));
		Button loadtestPeakListButton = new Button("Load");
		loadtestPeakListButton.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		loadtestPeakListButton.setOnAction(e -> {
			load(testPeakListTF);
		});
		// Layout
		GridPane testPeakListPane = new GridPane();
		testPeakListPane.setAlignment(Pos.TOP_LEFT);
		testPeakListPane.setPadding(new Insets(15));
		testPeakListPane.setHgap(15);
		testPeakListPane.setVgap(15);
		testPeakListPane.add(testPeakListFileRB, 0, 0, 3, 1);
		testPeakListPane.add(warning2Pane, 0, 1, 3, 1);
		testPeakListPane.add(testPeakListLabel, 0, 2, 1, 1);
		testPeakListPane.add(testPeakListTF, 1, 2, 1, 1);
		testPeakListPane.add(loadtestPeakListButton, 2, 2, 1, 1);
		GridPane.setHgrow(testPeakListTF, Priority.ALWAYS);
		// From Proline databases
		Button secondButtonConnection = new Button("Connect ...");
		secondButtonConnection.setGraphic(new ImageView(IconResource.getImage(ICON.ADMIN)));
		secondButtonConnection.setOnAction(e -> {
			LoginDialog loginDialog = new LoginDialog();
			loginDialog.showAndWait().ifPresent(userProject -> {
				testConnectionLabel.setVisible(false);
				testUserProjects.setAll(userProject);
				testUserProjectsCBX.setItems(testUserProjects);
				testUserProjectsCBX.setConverter(new StringConverter<Project>() {
					@Override
					public String toString(Project object) {
						return object.getName();
					}

					@Override
					public Project fromString(String string) {
						return null;
					}
				});
			});

		});
		// Update the view
		testUserProjectsCBX.setOnAction(e -> {
			testProjectId = testUserProjectsCBX.getValue().getId();
			testRootItem.getChildren().clear();
			testRootItem.getChildren().addAll(createDatasets(testUserProjectsCBX.getValue().getId()));
			testTreeView = new TreeView(testRootItem);
			testRoot.getChildren().add(testTreeView);
		});
		VBox warning2DbPane = new VBox(2);
		testConnectionLabel = new Label("Off connection. Connect to your Proline account please!");
		testConnectionLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		testConnectionLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warning2DbPane.getChildren().addAll(testConnectionLabel);
		// Create dialog components
		Label secondFileLocationLabel = new Label("User peaklists:");
		testConnectionLabel.setPrefWidth(580);
		testRoot = new StackPane();
		testRoot.setPadding(new Insets(5));
		testRootItem = new TreeItem("Peaklists");
		testRootItem.setExpanded(true);
		testRootItem.getChildren().addAll();
		testTreeView = new TreeView(testRootItem);
		testRoot.getChildren().add(testTreeView);
		// Layout
		GridPane projects2Pane = new GridPane();
		projects2Pane.setAlignment(Pos.TOP_LEFT);
		projects2Pane.setPadding(new Insets(10));
		projects2Pane.setHgap(15);
		projects2Pane.setVgap(15);
		projects2Pane.addRow(0, secondFileLocationLabel);
		projects2Pane.add(testRoot, 0, 1, 1, 6);

		Label secondUserProjectLabel = new Label("User projects:");

		GridPane secondPklListDBPane = new GridPane();
		secondPklListDBPane.setAlignment(Pos.TOP_LEFT);
		secondPklListDBPane.setPadding(new Insets(15));
		secondPklListDBPane.setHgap(15);
		secondPklListDBPane.setVgap(15);

		secondPklListDBPane.add(testPeakListProlineRB, 0, 0, 3, 1);
		secondPklListDBPane.add(warning2DbPane, 0, 1, 3, 1);
		secondPklListDBPane.add(secondButtonConnection, 3, 1, 1, 1);
		secondPklListDBPane.add(secondUserProjectLabel, 0, 2, 2, 1);
		secondPklListDBPane.add(testUserProjectsCBX, 3, 2, 1, 1);
		secondPklListDBPane.add(testRoot, 0, 3, 4, 1);
		GridPane.setHgrow(testRoot, Priority.ALWAYS);
		peaklist2SplitPane.getItems().addAll(testPeakListPane, secondPklListDBPane);

		// Control
		warning2Pane.visibleProperty().bind(refPeakListTF.textProperty().isEmpty());
		warning2Pane.disableProperty().bind(testPeakListProlineRB.selectedProperty());
		testPeakListLabel.disableProperty().bind(testPeakListProlineRB.selectedProperty());
		testPeakListTF.disableProperty().bind(testPeakListProlineRB.selectedProperty());
		loadtestPeakListButton.disableProperty().bind(testPeakListProlineRB.selectedProperty());

		warning2DbPane.disableProperty().bind(testPeakListFileRB.selectedProperty());
		secondUserProjectLabel.disableProperty().bind(testPeakListFileRB.selectedProperty());
		secondButtonConnection.disableProperty().bind(testPeakListFileRB.selectedProperty());
		testUserProjectsCBX.disableProperty().bind(testPeakListFileRB.selectedProperty());

		// Create the 2 peak list pane
		// Create first tabpane
		TabPane referenceTabPane = new TabPane();
		Tab referenxcePklFileTab = new Tab("Peaklist File");
		referenxcePklFileTab.setContent(refPklListPane);
		referenxcePklFileTab.setClosable(false);

		Tab referencePklFromDBTab = new Tab("Proline projects");
		referencePklFromDBTab.setContent(refPklListDBPane);
		referencePklFromDBTab.setClosable(false);
		referenceTabPane.getTabs().addAll(referencePklFromDBTab, referenxcePklFileTab);
		referenceTabPane.getSelectionModel().select(referencePklFromDBTab);

		// Create second tabpane
		TabPane testedTabPane = new TabPane();
		Tab testedPklFileTab = new Tab("Peaklist File");
		testedPklFileTab.setContent(testPeakListPane);
		testedPklFileTab.setClosable(false);

		Tab testedPklFromDBTab = new Tab("Proline projects");
		testedPklFromDBTab.setContent(secondPklListDBPane);
		testedPklFromDBTab.setClosable(false);

		testedTabPane.getTabs().addAll(testedPklFromDBTab, testedPklFileTab);
		testedTabPane.getSelectionModel().select(testedPklFromDBTab);
		//
		peaklistSplitPane.getItems().addAll(referenceTabPane, testedTabPane);
		peaklistSplitPane.setPadding(new Insets(10));
		peaklistSplitPane.setMinHeight(350);

		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(peaklistSplitPane);
		dialogPane.setHeaderText("Spectra Loader");
		dialogPane.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		dialogPane.setPrefSize(1080, 640);
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new ImageView(IconResource.getImage(ICON.LOAD)).getImage());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		Button buttonCancel = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));
		Button buttonOk = (Button) dialogPane.lookupButton(ButtonType.OK);
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));

//		BooleanProperty isRefPeakListPropertyOK = new SimpleBooleanProperty();
//		isRefPeakListPropertyOK.bind(referenceTreeView.getSelectionModel().selectedItemProperty().isNull()
//				.or(refPeakListTF.textProperty().isEmpty()));
//
//		BooleanProperty isTestPeakListPropertyOK = new SimpleBooleanProperty();
//		isTestPeakListPropertyOK.bind(testTreeView.getSelectionModel().selectedItemProperty().isNull()
//				.or(testPeakListTF.textProperty().isEmpty()));
//		// Disable ok button
//		buttonOk.disableProperty().bind(isRefPeakListPropertyOK.or(isTestPeakListPropertyOK));

		this.setTitle("Spectra Loader");
		this.setDialogPane(dialogPane);
		// On apply button
		this.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				if (refPeakListFileRB.isSelected()) {
					Session.USER_PARAMS.setDataSource("file");
					refPeakListByDataSourceMap.put(SpectraSource.FILE, refPeakListTF.getText());
				} else {
					Session.USER_PARAMS.setDataSource("database");
					Long refResultSetId = referenceTreeView.getSelectionModel().getSelectedItem().getValue()
							.getResultSetId();
					referenceResultSetIdSet.add(refResultSetId);
					refPeakListByDataSourceMap.put(SpectraSource.DATABASE, referenceResultSetIdSet);
				}
				if (testPeakListFileRB.isSelected()) {
					Session.USER_PARAMS.setDataSource("file");
					testPeakListByDataSourceMap.put(SpectraSource.FILE, testPeakListTF.getText());
				} else {
					Session.USER_PARAMS.setDataSource("database");
					Long testResultSetId = testTreeView.getSelectionModel().getSelectedItem().getValue()
							.getResultSetId();
					testResultSetIdSet.add(testResultSetId);
					testPeakListByDataSourceMap.put(SpectraSource.DATABASE, testResultSetIdSet);
				}
				this.params = new Parameters(refPeakListByDataSourceMap, testPeakListByDataSourceMap,
						referenceProjectId, testProjectId);
				return params;
			} else {
				return null;
			}
		});
	}

	/**
	 * Load file.
	 *
	 * @param text the text
	 */

	private void load(TextField text) {
		FileUtils.openPeakListFile(file -> {
			text.setText(file.getPath());
		}, stage);
	}

	/**
	 * Create dataset nodes.
	 *
	 * @param projectId the selected project id
	 * @return the dataset nodes of the chosen project.
	 */
	// TODO Handle by better way the datasets
	private ArrayList<TreeItem> createDatasets(Long projectId) {
		ArrayList<TreeItem> datasets = new ArrayList<>();

		try {
			DBSpectraHandler.fillDataSetByProject(projectId).forEach(ds -> {
				if (ds.getType() == DatasetType.IDENTIFICATION) {
					TreeItem dsName = new TreeItem(ds);
					dsName.setGraphic(new ImageView(IconResource.getImage(ICON.DATASET_RSM)));
					datasets.add(dsName);
				} else if (ds.getType() == DatasetType.AGGREGATE) {
					TreeItem dsName = new TreeItem(ds);
					dsName.setGraphic(new ImageView(IconResource.getImage(ICON.DATASET_RSM_MERGED_A)));
					datasets.add(dsName);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasets;
	}

}