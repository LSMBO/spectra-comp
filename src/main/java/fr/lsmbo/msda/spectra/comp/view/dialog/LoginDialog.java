package fr.lsmbo.msda.spectra.comp.view.dialog;

import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.db.DBConfig;
import fr.lsmbo.msda.spectra.comp.db.DBSpectraHandler;
import fr.lsmbo.msda.spectra.comp.model.Project;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates and displays login dialog.
 * 
 * @author Aromdhani
 *
 */
public class LoginDialog extends Dialog<ObservableList<Project>> {
	TextField userNameTF;
	PasswordField passwordTF;
	TextField hostNameTF;

	/**
	 * Default constructor
	 * 
	 */
	@SuppressWarnings("unchecked")
	public LoginDialog() {
		VBox warningDbPane = new VBox(2);
		Label emptyUserNameLabel = new Label("User name must not be empty!");
		emptyUserNameLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyUserNameLabel.setStyle(JavaFxUtils.RED_ITALIC);
		Label emptyPasswordLabel = new Label("Password must not be empty!");
		emptyPasswordLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyPasswordLabel.setStyle(JavaFxUtils.RED_ITALIC);
		Label emptyHostLabel = new Label("Host name must not be empty!");
		emptyHostLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyHostLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningDbPane.getChildren().addAll(emptyUserNameLabel, emptyPasswordLabel);
		Label hostNameLabel = new Label("Server host: ");
		hostNameTF = new TextField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getHost()))
			hostNameTF.setText(DBConfig.getInstance().getHost());
		else
			hostNameTF.setText("proline");
		hostNameTF.setTooltip(new Tooltip("Enter a host name"));
		hostNameTF.setDisable(true);
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

		// Layout
		GridPane loginPane = new GridPane();
		loginPane.setAlignment(Pos.TOP_LEFT);
		loginPane.setPadding(new Insets(10));
		loginPane.setHgap(15);
		loginPane.setVgap(15);

		loginPane.add(warningDbPane, 0, 1, 3, 1);
		loginPane.add(hostNameLabel, 0, 2, 1, 1);
		loginPane.add(hostNameTF, 1, 2, 1, 1);
		loginPane.add(userNameLabel, 0, 3, 1, 1);
		loginPane.add(userNameTF, 1, 3, 1, 1);
		loginPane.add(passwordLabel, 0, 4, 1, 1);
		loginPane.add(passwordTF, 1, 4, 1, 1);
		loginPane.setHgrow(hostNameTF, Priority.ALWAYS);

		/********************
		 * Main dialog pane *
		 ********************/

		// Create and display the main dialog pane
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(loginPane);
		dialogPane.setHeaderText("Server connection");
		dialogPane.setGraphic(new ImageView(IconResource.getImage(ICON.ADMIN)));
		dialogPane.setPrefSize(350, 300);
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new ImageView(IconResource.getImage(ICON.ADMIN)).getImage());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		Button buttonCancel = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));
		Button buttonOk = (Button) dialogPane.lookupButton(ButtonType.OK);
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));
      
		this.setTitle("Server connection");
		this.setDialogPane(dialogPane);
		// Control
		emptyUserNameLabel.visibleProperty().bind(userNameTF.textProperty().isEmpty());
		emptyPasswordLabel.visibleProperty().bind(passwordTF.textProperty().isEmpty());
		emptyHostLabel.visibleProperty().bind(hostNameTF.textProperty().isEmpty());
		// On apply button
		this.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK && login(userNameTF.getText())) {
				try {
					return getUserProjects(userNameTF.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return null;
				}
			} else {
				return null;
			}
		});
	}

	/**
	 * Return the list of user projects
	 * 
	 * @param login
	 *            the login to set
	 * @return list of projects
	 */
	private ObservableList<Project> getUserProjects(String login) throws Exception {
		return DBSpectraHandler.findProjects(login);
	}

	/**
	 * Test connection to database
	 * 
	 * @throws SQLException
	 */
	private boolean login(String login) {
		try {
			DBSpectraHandler.findUser(login);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}