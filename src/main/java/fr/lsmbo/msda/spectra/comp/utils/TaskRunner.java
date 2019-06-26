/*
 * 
 */

package fr.lsmbo.msda.spectra.comp.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.view.dialog.ShowPopupDialog;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class TaskRunner.
 * 
 * @author Aromdhani
 * 
 */
public class TaskRunner {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TaskRunner.class);
	
	/** The main view. */
	/* The main view. */
	public static Node mainView;

	/** The glass pane. */
	/* The glass pane. */
	public static Node glassPane;

	/** The status label. */
	/* The status label. */
	public static Label statusLabel;

	/**
	 *  Disable/enable the main view.
	 *
	 * @param progressEnabled the progress enabled
	 */
	private static void showProgress(boolean progressEnabled) {
		// mainView.setDisable(progressEnabled);
		mainView.setDisable(false);
		glassPane.setVisible(progressEnabled);
	}

	/**
	 * Perform an async call.
	 *
	 * @param <T> the generic type
	 * @param caption the caption
	 * @param action the action
	 * @param success the success
	 * @param error the error
	 * @param showDialog the show dialog
	 * @param stage the stage
	 */
	public static <T extends Object> void doAsyncWork(String caption, Supplier<T> action, Consumer<T> success,
			Consumer<Throwable> error, boolean showDialog, Stage stage) {
		Task<T> task = new Task<T>() {
			@Override
			protected synchronized T call() throws Exception {
				return action.get();
			}

			@Override
			protected void failed() {
				logger.error(getException());
				System.err.println("ERROR | " + caption + " - has failed: " + getException().getMessage());
				mainView.getScene().setCursor(Cursor.DEFAULT);
				error.accept(getException());
				showProgress(false);
				// Show popup
				if (showDialog) {
					new ShowPopupDialog("Failure", caption + " - has failed: " + getException().getMessage(), stage);
				}
			}

			@Override
			protected void running() {
				logger.info(caption + " - is running...");
				System.out.println("INFO | " + caption + " - is running...");
				mainView.getScene().setCursor(Cursor.WAIT);
				showProgress(true);
			}

			@Override
			protected void succeeded() {
				logger.info(caption + " - has finished successfully!");
				System.out.println("INFO | " + caption + " - has finished successfully!");
				mainView.getScene().setCursor(Cursor.DEFAULT);
				success.accept(getValue());
				showProgress(false);
				// Show popup
				if (showDialog) {
					new ShowPopupDialog("Success", caption + " - has finished successfully!", stage);
				}
			}
		};
		// Set the task as daemon
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
}
