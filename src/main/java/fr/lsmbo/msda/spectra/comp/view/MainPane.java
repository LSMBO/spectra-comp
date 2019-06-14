package fr.lsmbo.msda.spectra.comp.view;

import java.sql.SQLException;
import java.util.List;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBAccess;
import fr.lsmbo.msda.spectra.comp.db.DBConfig;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainPane extends StackPane {
	TextField userNameTF;
	PasswordField passwordTF;
	TextField hostNameTF;

	public MainPane() {
		// Create the main view
		BorderPane mainView = new BorderPane();
		mainView.setPrefSize(1400, 800);
		// Create main Splite pane
		SplitPane mainSplitPane = new SplitPane();
		mainSplitPane.setOrientation(Orientation.VERTICAL);
		mainSplitPane.setPrefHeight(320);
		mainSplitPane.setDividerPositions(0.8f, 0.2f);
		// Create the peaklists pane
		SplitPane peaklistSplitPane = new SplitPane();
		peaklistSplitPane.setOrientation(Orientation.HORIZONTAL);
		peaklistSplitPane.setPrefHeight(320);
		peaklistSplitPane.setDividerPositions(0.5f, 0.5f);
		// Create the 1 peak list pane
		ToggleGroup group = new ToggleGroup();
		RadioButton pklListRefFileRB = new RadioButton("Select a peaklist file as a reference");
		pklListRefFileRB.setToggleGroup(group);
		pklListRefFileRB.setSelected(true);
		RadioButton pklListRefDBRB = new RadioButton("Select a peaklist as a reference from Proline databases");
		pklListRefDBRB.setToggleGroup(group);

		SplitPane peaklist1SplitPane = new SplitPane();
		peaklist1SplitPane.setOrientation(Orientation.VERTICAL);
		peaklist1SplitPane.setPrefHeight(320);
		peaklist1SplitPane.setDividerPositions(0.3f, 0.7f);

		VBox warningPane = new VBox(2);
		Label emptyFirstPklListLabel = new Label(
				"Choose a first peaklist file as reference. Make sure that you have selected a valid file!");
		emptyFirstPklListLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyFirstPklListLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningPane.getChildren().addAll(emptyFirstPklListLabel);
		Label refPklListLabel = new Label("Reference peaklist:");
		TextField refPklListTF = new TextField();
		refPklListTF.setText(Session.USER_PARAMS.getFirstPklList());
		refPklListTF.setTooltip(new Tooltip("Select a reference peak list"));
		Button loadRefPklListButton = new Button("Load");
		loadRefPklListButton.setGraphic(new ImageView(IconResource.getImage(ICON.LOAD)));
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
		VBox warningDbPane = new VBox(2);
		Label emptyFirstPklListDbLabel = new Label("Choose properties to connect to Proline databases!");
		emptyFirstPklListDbLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyFirstPklListDbLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningDbPane.getChildren().addAll(emptyFirstPklListDbLabel);
		//
		Label hostNameLabel = new Label("Server host: ");
		hostNameTF = new TextField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getHost()))
			hostNameTF.setText(DBConfig.getInstance().getHost());
		else
			hostNameTF.setText("proline");
		hostNameTF.setTooltip(new Tooltip("Enter a hsot name"));

		Label userNameLabel = new Label("User: ");
		userNameTF = new TextField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getUser()))
			userNameTF.setText(DBConfig.getInstance().getUser());
		else
			userNameTF.setText("proline");
		userNameTF.setTooltip(new Tooltip("Enter a user name"));

		Label passwordLabel = new Label("Password: ");
		passwordTF = new PasswordField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getPassword()))
			passwordTF.setText(DBConfig.getInstance().getPassword());
		else
			passwordTF.setText("proline");
		passwordLabel.setTooltip(new Tooltip("Enter a user password"));

		Button connectButton = new Button("Connect");
		connectButton.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));
		connectButton.setOnAction(e -> {
			try {
				System.out.println(userNameTF.getText());
				login(userNameTF.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Button defaultButton = new Button("Default");
		defaultButton.setGraphic(new ImageView(IconResource.getImage(ICON.RESET)));
		defaultButton.setOnAction(e -> {
			setDefaultValues();
		});

		HBox connectionPane = new HBox(10);
		connectionPane.getChildren().addAll(defaultButton, connectButton);

		// Create dialog components
		Label fileLocationLabel = new Label("User peak lists:");
		fileLocationLabel.setPrefWidth(580);
		StackPane root = new StackPane();
		root.setPadding(new Insets(5));
		// Set components control
		TreeItem rootItem = new TreeItem("peak lists");
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
		ComboBox<String> userProjectsCBX = new ComboBox<String>();

		GridPane refPklListDBPane = new GridPane();
		refPklListDBPane.setAlignment(Pos.TOP_LEFT);
		refPklListDBPane.setPadding(new Insets(15));
		refPklListDBPane.setHgap(15);
		refPklListDBPane.setVgap(15);

		refPklListDBPane.add(pklListRefDBRB, 0, 0, 3, 1);
		refPklListDBPane.add(warningDbPane, 0, 1, 3, 1);
		refPklListDBPane.add(hostNameLabel, 0, 2, 1, 1);
		refPklListDBPane.add(hostNameTF, 1, 2, 1, 1);
		refPklListDBPane.add(userNameLabel, 0, 3, 1, 1);
		refPklListDBPane.add(userNameTF, 1, 3, 1, 1);
		refPklListDBPane.add(passwordLabel, 0, 4, 1, 1);
		refPklListDBPane.add(passwordTF, 1, 4, 1, 1);
		refPklListDBPane.add(connectionPane, 1, 5, 2, 1);
		refPklListDBPane.add(userProjectLabel, 0, 6, 2, 1);
		refPklListDBPane.add(userProjectsCBX, 1, 6, 2, 1);
		refPklListDBPane.add(root, 0, 7, 3, 1);
		refPklListDBPane.setHgrow(hostNameTF, Priority.ALWAYS);

		peaklist1SplitPane.getItems().addAll(refPklListPane, refPklListDBPane);
		// Control

		// Create the 2 peak list pane
		peaklistSplitPane.getItems().addAll(peaklist1SplitPane);
		mainSplitPane.getItems().addAll(peaklistSplitPane, ConsoleView.getInstance());
		this.getChildren().addAll(mainSplitPane);
	}

	/**
	 * Set database connection default values
	 */
	private void setDefaultValues() {
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getHost()))
			hostNameTF.setText(DBConfig.getInstance().getHost());
		else
			hostNameTF.setText("proline");
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getUser()))
			userNameTF.setText(DBConfig.getInstance().getUser());
		else
			userNameTF.setText("proline");
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getPassword()))
			passwordTF.setText(DBConfig.getInstance().getPassword());
		else
			passwordTF.setText("proline");
	}

	/**
	 * Return the list of user projects
	 */
	private List<String> getUserProjects(String login)throws Exception {
		return DBSpectraHandler.findProjects(login);

	}

	/**
	 * Test connection to database
	 * @throws SQLException 
	 */
	private void login(String login) throws Exception {
			DBSpectraHandler.findUser(login);
	}
}
