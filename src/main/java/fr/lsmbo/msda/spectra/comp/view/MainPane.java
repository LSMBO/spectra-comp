package fr.lsmbo.msda.spectra.comp.view;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.utils.FileUtils;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import fr.lsmbo.msda.spectra.comp.view.dialog.LoginDialog;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPane extends StackPane {
	private TextField userNameTF;
	private PasswordField passwordTF;
	private TextField hostNameTF;
	private ComboBox<String> userProjectsCBX;
	private ComboBox<String> secondUserProjectsCBX;
	private Label connectionLabel;
	private Label secondConnectionLabel;
	public static Stage stage;

	public MainPane() {
		// Create the main view
		BorderPane mainView = new BorderPane();
		mainView.setPrefSize(1400, 800);
		// Create main Splite pane
		SplitPane mainSplitPane = new SplitPane();
		mainSplitPane.setPadding(new Insets(10));
		mainSplitPane.setOrientation(Orientation.VERTICAL);
		mainSplitPane.setPrefHeight(320);
		mainSplitPane.setDividerPositions(0.8f, 0.2f);
		// Create the peaklists pane
		SplitPane peaklistSplitPane = new SplitPane();
		peaklistSplitPane.setOrientation(Orientation.HORIZONTAL);
		peaklistSplitPane.setPrefHeight(320);
		peaklistSplitPane.setDividerPositions(0.5f, 0.5f);

		ToggleGroup group = new ToggleGroup();
		RadioButton pklListRefFileRB = new RadioButton("Select the first peaklist file to set as a reference");
		pklListRefFileRB.setToggleGroup(group);
		pklListRefFileRB.setSelected(true);
		RadioButton pklListRefDBRB = new RadioButton(
				"Select the first peaklist to set as a reference from your Proline projects");
		pklListRefDBRB.setToggleGroup(group);
		// Create the 1 peak list pane
		SplitPane peaklist1SplitPane = new SplitPane();
		peaklist1SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist1SplitPane.setPrefHeight(320);
		peaklist1SplitPane.setDividerPositions(0.2f, 0.8f);

		VBox warningPane = new VBox(2);
		Label emptyFirstPklListLabel = new Label(
				"Choose the first peaklist file as reference. Make sure that you have selected a valid file!");
		emptyFirstPklListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyFirstPklListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningPane.getChildren().addAll(emptyFirstPklListLabel);
		Label refPklListLabel = new Label("Reference peaklist:");
		TextField refPklListTF = new TextField();
		refPklListTF.setText(Session.USER_PARAMS.getFirstPklList());
		refPklListTF.setTooltip(new Tooltip("Select a reference peaklist."));
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
		// from database
		Button ButtonConnection = new Button("Connect ...");
		ButtonConnection.setGraphic(new ImageView(IconResource.getImage(ICON.ADMIN)));
		ButtonConnection.setOnAction(e -> {
			LoginDialog loginDialog = new LoginDialog();
			loginDialog.showAndWait().ifPresent(userProject -> {
				connectionLabel.setVisible(false);
				userProjectsCBX.setItems(userProject);
			});
		});
		VBox warningDbPane = new VBox(2);
		connectionLabel = new Label("Please connect to your Proline account!");
		connectionLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		connectionLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningDbPane.getChildren().addAll(connectionLabel);
		// Create dialog components
		Label fileLocationLabel = new Label("User peaklists:");
		fileLocationLabel.setPrefWidth(580);
		StackPane root = new StackPane();
		root.setPadding(new Insets(5));
		// Set components control
		TreeItem rootItem = new TreeItem("Peaklists");
		rootItem.setExpanded(true);
		rootItem.getChildren().addAll();
		TreeView treeView = new TreeView(rootItem);
		root.getChildren().add(treeView);
		// Layout
		GridPane projectsPane = new GridPane();
		projectsPane.setAlignment(Pos.TOP_LEFT);
		projectsPane.setPadding(new Insets(10));
		projectsPane.setHgap(15);
		projectsPane.setVgap(15);
		projectsPane.addRow(0, fileLocationLabel);
		projectsPane.add(root, 0, 1, 1, 6);

		Label userProjectLabel = new Label("User projects:");
		userProjectsCBX = new ComboBox<String>();

		GridPane refPklListDBPane = new GridPane();
		refPklListDBPane.setAlignment(Pos.TOP_LEFT);
		refPklListDBPane.setPadding(new Insets(15));
		refPklListDBPane.setHgap(15);
		refPklListDBPane.setVgap(15);

		refPklListDBPane.add(warningDbPane, 0, 0, 3, 1);
		refPklListDBPane.add(pklListRefDBRB, 0, 1, 3, 1);
		refPklListDBPane.add(ButtonConnection, 3, 1, 1, 1);
		refPklListDBPane.add(userProjectLabel, 0, 2, 2, 1);
		refPklListDBPane.add(userProjectsCBX, 3, 2, 1, 1);
		refPklListDBPane.add(root, 0, 3, 4, 1);
		refPklListDBPane.setHgrow(root, Priority.ALWAYS);
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

		// Second peak list pane
		SplitPane peaklist2SplitPane = new SplitPane();
		peaklist2SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist2SplitPane.setPrefHeight(320);
		peaklist2SplitPane.setDividerPositions(0.2f, 0.8f);

		ToggleGroup group2 = new ToggleGroup();
		RadioButton secondPklListRefFileRB = new RadioButton("Select the second peaklist file to test");
		secondPklListRefFileRB.setToggleGroup(group2);
		secondPklListRefFileRB.setSelected(true);
		RadioButton secondPklListDBRB = new RadioButton(
				"Select the second peaklist file to test from your Proline projects");
		secondPklListDBRB.setToggleGroup(group2);

		VBox warning2Pane = new VBox(2);
		Label emptySecondPklListLabel = new Label(
				"Choose the second peaklist file to test. Make sure that you have selected a valid file!");
		emptySecondPklListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptySecondPklListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warning2Pane.getChildren().addAll(emptySecondPklListLabel);
		Label secondPklListLabel = new Label("Second peaklist:");
		TextField secondPklListTF = new TextField();
		secondPklListTF.setText(Session.USER_PARAMS.getSecondPklList());
		secondPklListTF.setTooltip(new Tooltip("Select the second peaklist to test."));
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
				secondUserProjectsCBX.setItems(userProject);
			});
		});
		VBox warning2DbPane = new VBox(2);
		secondConnectionLabel = new Label("Please connect to your Proline account!");
		secondConnectionLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		secondConnectionLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warning2DbPane.getChildren().addAll(secondConnectionLabel);
		// Create dialog components
		Label secondFileLocationLabel = new Label("User peaklists:");
		secondConnectionLabel.setPrefWidth(580);
		StackPane secondRoot = new StackPane();
		secondRoot.setPadding(new Insets(5));
		// Set components control
		TreeItem secondRootItem = new TreeItem("Peaklists");
		secondRootItem.setExpanded(true);
		secondRootItem.getChildren().addAll();
		TreeView secondTreeView = new TreeView(secondRootItem);
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
		secondUserProjectsCBX = new ComboBox<String>();

		GridPane secondPklListDBPane = new GridPane();
		secondPklListDBPane.setAlignment(Pos.TOP_LEFT);
		secondPklListDBPane.setPadding(new Insets(15));
		secondPklListDBPane.setHgap(15);
		secondPklListDBPane.setVgap(15);

		secondPklListDBPane.add(warning2DbPane, 0, 0, 3, 1);
		secondPklListDBPane.add(secondPklListDBRB, 0, 1, 3, 1);
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
		peaklistSplitPane.getItems().addAll(peaklist1SplitPane, peaklist2SplitPane);
		peaklistSplitPane.setPadding(new Insets(10));
		mainSplitPane.getItems().addAll(peaklistSplitPane, ConsoleView.getInstance());
		this.getChildren().addAll(mainSplitPane);
	}

	/**
	 * Return the list of user projects
	 */
	private ObservableList<String> getUserProjects(String login) throws Exception {
		return DBSpectraHandler.findProjects(login);
	}

	/**
	 * Test connection to database
	 * 
	 * @throws Exception
	 */
	private void login(String login) throws Exception {
		DBSpectraHandler.findUser(login);
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
}
