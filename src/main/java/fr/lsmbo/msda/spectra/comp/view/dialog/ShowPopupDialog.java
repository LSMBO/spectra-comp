/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.view.dialog;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays dialog window.
 *
 * @author Aromdhani
 */
public class ShowPopupDialog extends Stage {
	
	/** The popup title. */
	private String popupTitle;
	
	/** The message. */
	private String message;
	
	/** The parent stage. */
	private Stage parentStage;

	/**
	 * Instantiates a new show popup dialog.
	 *
	 * @param popupTitle the popup title
	 * @param message the message
	 * @param parentStage the parent stage
	 */
	public ShowPopupDialog(String popupTitle, String message, Stage parentStage) {
		Stage popup = this;
		popup.initOwner(parentStage);
		popup.getIcons().add(IconResource.getImage(ICON.SPECTRA_COMP));
		Button buttonOk = new Button(" OK ");
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));
		buttonOk.setOnAction((e) -> {
			popup.close();
		});
		VBox root = new VBox(40);
		root.setPadding(new Insets(20, 10, 10, 10));
		Label messageLabel = new Label(message);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(messageLabel, buttonOk);
		// Create scene
		Scene scene = new Scene(new VBox(5, root));
		popup.setTitle(popupTitle);
		popup.setScene(scene);
		popup.setWidth(450);
		popup.setHeight(180);
		popup.setResizable(false);
		popup.show();
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the parent stage.
	 *
	 * @return the parent stage
	 */
	public Stage getParentStage() {
		return parentStage;
	}

	/**
	 * Gets the popup title.
	 *
	 * @return the popup title
	 */
	public String getPopupTitle() {
		return popupTitle;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the parent stage.
	 *
	 * @param parentStage the new parent stage
	 */
	public void setParentStage(Stage parentStage) {
		this.parentStage = parentStage;
	}

	/**
	 * Sets the popup title.
	 *
	 * @param popupTitle the new popup title
	 */
	public void setPopupTitle(String popupTitle) {
		this.popupTitle = popupTitle;
	}
}