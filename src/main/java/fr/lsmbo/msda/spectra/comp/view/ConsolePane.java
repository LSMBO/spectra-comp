/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.view;

import java.io.PrintStream;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

// TODO: Auto-generated Javadoc
/**
 * 
 * Creates and displays console view . Set the PrintStream to the console .
 * 
 * @author aromdhani
 *
 */

public class ConsolePane {

	/**
	 * Gets the single instance of ConsolePane.
	 *
	 * @return single instance of ConsolePane
	 */
	public static final TabPane getInstance() {
		// Console
		WebView webview = new WebView();
		PrintStream psOut = new PrintStream(new ConsoleStream(webview));
		System.setOut(psOut);
		System.setErr(psOut);
		webview.autosize();

		StackPane stckPane = new StackPane();
		stckPane.setPadding(new Insets(5, 5, 5, 5));
		stckPane.autosize();
		stckPane.getChildren().addAll(webview);
		// Progress
		TableView table = new TableView();
		TableColumn<Task, String> taskNameCol //
				= new TableColumn<Task, String>("Task");
		TableColumn<Task, String> taskStatusCol //
				= new TableColumn<Task, String>("Status");
		table.getColumns().addAll(taskNameCol, taskStatusCol);
		table.autosize();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		StackPane progressStckPane = new StackPane();
		progressStckPane.setPadding(new Insets(5, 5, 5, 5));
		progressStckPane.autosize();
		progressStckPane.getChildren().addAll(table);

		TabPane tabPane = new TabPane();
		Tab tab = new Tab();
		tab.setText("Console");
		tab.setClosable(false);
		tab.setGraphic(new ImageView(IconResource.getImage(ICON.CONSOLE)));
		tab.setContent(stckPane);
		// Progress
		Tab progressTab = new Tab();
		progressTab.setText("Progress");
		progressTab.setClosable(false);
		progressTab.setGraphic(new ImageView(IconResource.getImage(ICON.CONSOLE)));
		progressTab.setContent(progressStckPane);
		tabPane.getTabs().addAll(tab);
		return tabPane;

	}
}