package fr.lsmbo.msda.spectra.comp.view.dialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.db.DataSource;
import fr.lsmbo.msda.spectra.comp.model.Dataset;
import fr.lsmbo.msda.spectra.comp.model.Project;
import fr.lsmbo.msda.spectra.comp.model.SpectraParams;
import fr.lsmbo.msda.spectra.comp.utils.FileUtils;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
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

/**
 * Creates and displays spectra loader dialog.
 * 
 * @author Aromdhani
 *
 */
public class SpectraLoaderDialog extends Dialog<Object> {
	// Components
	private Label connectionLabel;
	private Label secondConnectionLabel;
	private ComboBox<Project> userProjectsCBX = new ComboBox<Project>();
	private ComboBox<Project> secondUserProjectsCBX = new ComboBox<Project>();
	private ObservableList<Project> refUserProjects = FXCollections.observableArrayList();
	private ObservableList<Project> testUserProjects = FXCollections.observableArrayList();
	private SpectraParams params;

	private StackPane firstRoot;
	private TreeItem rootItem;
	private TreeView<Dataset> treeView;

	private StackPane secondRoot;
	private TreeItem secondRootItem;
	private TreeView<Dataset> secondTreeView;
	public static Stage stage;
	//
	private String refDbName;
	private String testDbName;
	private Map<DataSource, Object> refPklByDataSourceMap = new HashMap<>();
	private Map<DataSource, Object> testPklByDataSourceMap = new HashMap<>();
	private HashSet<Long> refRsmIds = new HashSet<>();
	private HashSet<Long> testRsmIds = new HashSet<>();

	/**
	 * Default constructor
	 * 
	 */
	@SuppressWarnings("unchecked")
	public SpectraLoaderDialog() {

		SplitPane peaklistSplitPane = new SplitPane();
		peaklistSplitPane.setOrientation(Orientation.HORIZONTAL);
		peaklistSplitPane.setPrefHeight(320);
		peaklistSplitPane.setDividerPositions(0.5f, 0.5f);
		// Create radio buttons
		ToggleGroup group = new ToggleGroup();
		RadioButton pklListRefFileRB = new RadioButton("Select the first peaklist file to set as a reference");
		pklListRefFileRB.setToggleGroup(group);
		RadioButton pklListRefDBRB = new RadioButton(
				"Select the first peaklist to set as a reference from your Proline projects");
		pklListRefDBRB.setSelected(true);
		pklListRefDBRB.setToggleGroup(group);
		pklListRefFileRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("file");
		});
		pklListRefDBRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("database");
		});
		// Create the 1 peak list pane
		SplitPane peaklist1SplitPane = new SplitPane();
		peaklist1SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist1SplitPane.setPrefHeight(320);
		peaklist1SplitPane.setDividerPositions(0.2f, 0.8f);

		VBox warningPane = new VBox(2);
		Label emptyFirstPklListLabel = new Label(
				"Choose the first peaklist file to set as reference. Make sure that you have selected a valid file!");
		emptyFirstPklListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyFirstPklListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningPane.getChildren().addAll(emptyFirstPklListLabel);
		Label refPklListLabel = new Label("Reference peaklist:");
		TextField refPklListTF = new TextField();
		refPklListTF.setText(Session.USER_PARAMS.getFirstPklList());
		refPklListTF.setTooltip(new Tooltip("Choose the first peaklist file to set as reference."));
		Button loadRefPklListButton = new Button("Load");
		loadRefPklListButton.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		loadRefPklListButton.setOnAction(e -> {
			load(refPklListTF);
		});
		// Layout
		GridPane refPklListPane = new GridPane();
		refPklListPane.setAlignment(Pos.TOP_LEFT);
		refPklListPane.setPadding(new Insets(15));
		refPklListPane.setHgap(15);
		refPklListPane.setVgap(15);
		refPklListPane.add(pklListRefFileRB, 0, 0, 3, 1);
		refPklListPane.add(warningPane, 0, 1, 3, 1);
		refPklListPane.add(refPklListLabel, 0, 2, 1, 1);
		refPklListPane.add(refPklListTF, 1, 2, 1, 1);
		refPklListPane.add(loadRefPklListButton, 2, 2, 1, 1);
		refPklListPane.setHgrow(refPklListTF, Priority.ALWAYS);
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
		// update the view
		userProjectsCBX.setOnAction(e -> {
			refDbName = "msi_db_project_" + userProjectsCBX.getValue().getId();
			rootItem.getChildren().clear();
			rootItem.getChildren().addAll(createDatasets(userProjectsCBX.getValue().getId()));
			treeView = new TreeView(rootItem);
			firstRoot.getChildren().add(treeView);
		});
		VBox warningDbPane = new VBox(2);
		connectionLabel = new Label("Off connection. Connect to your Proline account please!");
		connectionLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		connectionLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningDbPane.getChildren().addAll(connectionLabel);

		// Create dialog components
		Label fileLocationLabel = new Label("User peaklists:");
		connectionLabel.setPrefWidth(580);
		firstRoot = new StackPane();
		firstRoot.setPadding(new Insets(5));

		// Set components control
		rootItem = new TreeItem("Peaklists");
		rootItem.setExpanded(true);
		rootItem.getChildren().addAll();
		treeView = new TreeView(rootItem);
		firstRoot.getChildren().add(treeView);

		// Layout
		GridPane projectsPane = new GridPane();
		projectsPane.setAlignment(Pos.TOP_LEFT);
		projectsPane.setPadding(new Insets(10));
		projectsPane.setHgap(15);
		projectsPane.setVgap(15);
		projectsPane.addRow(0, fileLocationLabel);
		projectsPane.add(firstRoot, 0, 1, 1, 6);

		Label userProjectLabel = new Label("User projects:");

		GridPane refPklListDBPane = new GridPane();
		refPklListDBPane.setAlignment(Pos.TOP_LEFT);
		refPklListDBPane.setPadding(new Insets(15));
		refPklListDBPane.setHgap(15);
		refPklListDBPane.setVgap(15);

		refPklListDBPane.add(pklListRefDBRB, 0, 0, 3, 1);
		refPklListDBPane.add(warningDbPane, 0, 1, 3, 1);
		refPklListDBPane.add(ButtonConnection, 3, 1, 1, 1);
		refPklListDBPane.add(userProjectLabel, 0, 2, 2, 1);
		refPklListDBPane.add(userProjectsCBX, 3, 2, 1, 1);
		refPklListDBPane.add(firstRoot, 0, 3, 4, 1);
		refPklListDBPane.setHgrow(firstRoot, Priority.ALWAYS);
		peaklist1SplitPane.getItems().addAll(refPklListPane, refPklListDBPane);
		// Control
		warningPane.visibleProperty().bind(refPklListTF.textProperty().isEmpty());
		warningPane.disableProperty().bind(pklListRefDBRB.selectedProperty());
		refPklListLabel.disableProperty().bind(pklListRefDBRB.selectedProperty());
		refPklListTF.disableProperty().bind(pklListRefDBRB.selectedProperty());
		loadRefPklListButton.disableProperty().bind(pklListRefDBRB.selectedProperty());

		warningDbPane.disableProperty().bind(pklListRefFileRB.selectedProperty());
		userProjectLabel.disableProperty().bind(pklListRefFileRB.selectedProperty());
		ButtonConnection.disableProperty().bind(pklListRefFileRB.selectedProperty());
		userProjectsCBX.disableProperty().bind(pklListRefFileRB.selectedProperty());
		// Create the second pane
		SplitPane peaklist2SplitPane = new SplitPane();
		peaklist2SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist2SplitPane.setPrefHeight(320);
		peaklist2SplitPane.setDividerPositions(0.2f, 0.8f);

		ToggleGroup group2 = new ToggleGroup();
		RadioButton secondPklListRefFileRB = new RadioButton("Select the second peaklist file to test");
		secondPklListRefFileRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("file");
		});
		secondPklListRefFileRB.setToggleGroup(group2);
		RadioButton secondPklListDBRB = new RadioButton(
				"Select the second peaklist to test from your Proline projects");
		secondPklListDBRB.setSelected(true);
		secondPklListDBRB.setToggleGroup(group2);
		secondPklListDBRB.setOnAction(e -> {
			Session.USER_PARAMS.setDataSource("database");
		});

		VBox warning2Pane = new VBox(2);
		Label emptySecondPklListLabel = new Label(
				"Choose the second peaklist file to test. Make sure that you have selected a valid file!");
		emptySecondPklListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptySecondPklListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warning2Pane.getChildren().addAll(emptySecondPklListLabel);
		Label secondPklListLabel = new Label("Second peaklist:");
		TextField secondPklListTF = new TextField();
		secondPklListTF.setText(Session.USER_PARAMS.getSecondPklList());
		secondPklListTF.setTooltip(new Tooltip("Choose the second peaklist file to test."));
		Button loadSecondPklListButton = new Button("Load");
		loadSecondPklListButton.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
		loadSecondPklListButton.setOnAction(e -> {
			load(secondPklListTF);
		});
		// Layout
		GridPane secondPklListPane = new GridPane();
		secondPklListPane.setAlignment(Pos.TOP_LEFT);
		secondPklListPane.setPadding(new Insets(15));
		secondPklListPane.setHgap(15);
		secondPklListPane.setVgap(15);
		secondPklListPane.add(secondPklListRefFileRB, 0, 0, 3, 1);
		secondPklListPane.add(warning2Pane, 0, 1, 3, 1);
		secondPklListPane.add(secondPklListLabel, 0, 2, 1, 1);
		secondPklListPane.add(secondPklListTF, 1, 2, 1, 1);
		secondPklListPane.add(loadSecondPklListButton, 2, 2, 1, 1);
		secondPklListPane.setHgrow(secondPklListTF, Priority.ALWAYS);
		// From Proline databases
		Button secondButtonConnection = new Button("Connect ...");
		secondButtonConnection.setGraphic(new ImageView(IconResource.getImage(ICON.ADMIN)));
		secondButtonConnection.setOnAction(e -> {
			LoginDialog loginDialog = new LoginDialog();
			loginDialog.showAndWait().ifPresent(userProject -> {
				secondConnectionLabel.setVisible(false);
				testUserProjects.setAll(userProject);
				secondUserProjectsCBX.setItems(testUserProjects);
				secondUserProjectsCBX.setConverter(new StringConverter<Project>() {
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
		secondUserProjectsCBX.setOnAction(e -> {
			testDbName = "msi_db_project_" + secondUserProjectsCBX.getValue().getId();
			secondRootItem.getChildren().clear();
			secondRootItem.getChildren().addAll(createDatasets(secondUserProjectsCBX.getValue().getId()));
			secondTreeView = new TreeView(secondRootItem);
			secondRoot.getChildren().add(secondTreeView);
		});
		VBox warning2DbPane = new VBox(2);
		secondConnectionLabel = new Label("Off connection. Connect to your Proline account please!");
		secondConnectionLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		secondConnectionLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warning2DbPane.getChildren().addAll(secondConnectionLabel);
		// Create dialog components
		Label secondFileLocationLabel = new Label("User peaklists:");
		secondConnectionLabel.setPrefWidth(580);
		secondRoot = new StackPane();
		secondRoot.setPadding(new Insets(5));
		secondRootItem = new TreeItem("Peaklists");
		secondRootItem.setExpanded(true);
		secondRootItem.getChildren().addAll();
		secondTreeView = new TreeView(secondRootItem);
		secondRoot.getChildren().add(secondTreeView);
		// Layout
		GridPane projects2Pane = new GridPane();
		projects2Pane.setAlignment(Pos.TOP_LEFT);
		projects2Pane.setPadding(new Insets(10));
		projects2Pane.setHgap(15);
		projects2Pane.setVgap(15);
		projects2Pane.addRow(0, secondFileLocationLabel);
		projects2Pane.add(secondRoot, 0, 1, 1, 6);

		Label secondUserProjectLabel = new Label("User projects:");

		GridPane secondPklListDBPane = new GridPane();
		secondPklListDBPane.setAlignment(Pos.TOP_LEFT);
		secondPklListDBPane.setPadding(new Insets(15));
		secondPklListDBPane.setHgap(15);
		secondPklListDBPane.setVgap(15);

		secondPklListDBPane.add(secondPklListDBRB, 0, 0, 3, 1);
		secondPklListDBPane.add(warning2DbPane, 0, 1, 3, 1);
		secondPklListDBPane.add(secondButtonConnection, 3, 1, 1, 1);
		secondPklListDBPane.add(secondUserProjectLabel, 0, 2, 2, 1);
		secondPklListDBPane.add(secondUserProjectsCBX, 3, 2, 1, 1);
		secondPklListDBPane.add(secondRoot, 0, 3, 4, 1);
		secondPklListDBPane.setHgrow(secondRoot, Priority.ALWAYS);
		peaklist2SplitPane.getItems().addAll(secondPklListPane, secondPklListDBPane);

		// Control
		warning2Pane.visibleProperty().bind(refPklListTF.textProperty().isEmpty());
		warning2Pane.disableProperty().bind(secondPklListDBRB.selectedProperty());
		secondPklListLabel.disableProperty().bind(secondPklListDBRB.selectedProperty());
		secondPklListTF.disableProperty().bind(secondPklListDBRB.selectedProperty());
		loadSecondPklListButton.disableProperty().bind(secondPklListDBRB.selectedProperty());

		warning2DbPane.disableProperty().bind(secondPklListRefFileRB.selectedProperty());
		secondUserProjectLabel.disableProperty().bind(secondPklListRefFileRB.selectedProperty());
		secondButtonConnection.disableProperty().bind(secondPklListRefFileRB.selectedProperty());
		secondUserProjectsCBX.disableProperty().bind(secondPklListRefFileRB.selectedProperty());

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
		testedPklFileTab.setContent(secondPklListPane);
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
		dialogPane.setPrefSize(800, 600);
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new ImageView(IconResource.getImage(ICON.LOAD)).getImage());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		Button buttonCancel = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));
		Button buttonOk = (Button) dialogPane.lookupButton(ButtonType.OK);
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));

		this.setTitle("Spectra Loader");
		this.setDialogPane(dialogPane);
		// On apply button
		this.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				if (pklListRefFileRB.isSelected()) {
					Session.USER_PARAMS.setDataSource("file");
					refPklByDataSourceMap.put(DataSource.FILE, refPklListTF.getText());
				} else {
					assert !StringsUtils.isEmpty(refDbName) : "reference database name must not be null nor empty";
					Session.USER_PARAMS.setDataSource("database");
					refRsmIds.add(treeView.getSelectionModel().getSelectedItem().getValue().getResultSummaryId());
					refPklByDataSourceMap.put(DataSource.DATABASE, refRsmIds);
				}
				if (secondPklListRefFileRB.isSelected()) {
					Session.USER_PARAMS.setDataSource("file");
					testPklByDataSourceMap.put(DataSource.FILE, secondPklListTF.getText());
				} else {
					assert !StringsUtils.isEmpty(testDbName) : "test database name must not be null nor empty";
					Session.USER_PARAMS.setDataSource("database");
					testRsmIds
							.add(secondTreeView.getSelectionModel().getSelectedItem().getValue().getResultSummaryId());
					testPklByDataSourceMap.put(DataSource.DATABASE, testRsmIds);
				}
				this.params = new SpectraParams(refPklByDataSourceMap, testPklByDataSourceMap, refDbName, testDbName);
				return params;
			} else {
				return null;
			}
		});
	}

	/**
	 * Load file
	 * 
	 * @param text
	 */

	private void load(TextField text) {
		FileUtils.openPeakListFile(file -> {
			text.setText(file.getPath());
		}, stage);
	}

	/**
	 * Create dataset nodes
	 * 
	 * @param projectId
	 *            the selected project id
	 * @return the dataset nodes of the chosen project.
	 */
	// TODO Handle by better way the datasets
	private ArrayList<TreeItem> createDatasets(Long projectId) {
		ArrayList<TreeItem> datasets = new ArrayList<>();
		try {
			DBSpectraHandler.fillDataSetByProject(projectId).forEach(ds -> {
				TreeItem dsName = new TreeItem(ds);
				dsName.setGraphic(new ImageView(IconResource.getImage(ICON.DATASET_RSM)));
				datasets.add(dsName);
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datasets;
	}

}