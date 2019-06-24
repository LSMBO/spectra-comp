/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.utils;

import java.util.function.Supplier;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays pop up dialog.
 *
 * @author Aromdhani
 * @param <T> the generic type
 */
public class ConfirmDialog<T> extends Stage {
	
	/** The popup title. */
	private String popupTitle;
	
	/** The message. */
	private String message;
	
	/** The action. */
	private Supplier<T> action;
	
	/** The parent stage. */
	private Stage parentStage;

	/**
	 * Instantiates a new confirm dialog.
	 *
	 * @param icon the icon
	 * @param popupTitle the popup title
	 * @param message the message
	 * @param action the action
	 * @param parentStage the parent stage
	 */
	public ConfirmDialog(ICON icon, String popupTitle, String message, Supplier<T> action, Stage parentStage) {
		Stage popup = this;
		popup.initOwner(parentStage);
		popup.getIcons().add(IconResource.getImage(icon));
		// Close this dialog on cancel button pressed
		Button buttonCancel = new Button(" Cancel ");
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));
		buttonCancel.setOnAction((e) -> {
			popup.close();
		});
		// Set action on ok button pressed
		Button buttonOk = new Button(" OK ");
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));
		buttonOk.setOnAction((e) -> {
			popup.close();
			action.get();
			
		});
		// Create dialog content
		VBox root = new VBox(30);
		root.setPadding(new Insets(20, 10, 10, 10));
		// Create buttons panel
		HBox buttonsPanel = new HBox(20, buttonOk, buttonCancel);
		buttonsPanel.setAlignment(Pos.CENTER);
		// Create message Label
		Label messageLabel = new Label(message);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(messageLabel, buttonsPanel);
		// Create scene
		Scene scene = new Scene(new VBox(5, root));
		popup.setTitle(popupTitle);
		popup.setScene(scene);
		// Size
		popup.setWidth(480);
		popup.setHeight(160);
		popup.setResizable(false);
		popup.show();
	}

	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public Supplier<T> getAction() {
		return action;
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
	 * Sets the action.
	 *
	 * @param action the new action
	 */
	public void setAction(Supplier<T> action) {
		this.action = action;
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