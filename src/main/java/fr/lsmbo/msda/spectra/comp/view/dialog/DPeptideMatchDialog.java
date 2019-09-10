/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.view.dialog;

import java.util.List;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.model.DPeptide;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays peptide match dialog.
 * 
 * @author aromdhani
 *
 */
public class DPeptideMatchDialog extends Dialog<Integer> {

	/** The petide table. */
	private TableView<DPeptide> peptideTableView;

	/** The peptide list */
	private List<DPeptide> peptideList;

	/**
	 * @return the peptideTableView
	 */
	public final TableView<DPeptide> getPeptideTableView() {
		return peptideTableView;
	}

	/**
	 * @param peptideTableView the peptideTableView to set
	 */
	public final void setPeptideTableView(TableView<DPeptide> peptideTableView) {
		this.peptideTableView = peptideTableView;
	}

	/**
	 * @return the peptideList
	 */
	public final List<DPeptide> getPeptideList() {
		return peptideList;
	}

	/**
	 * @param peptideList the peptideList to set
	 */
	public final void setPeptideList(List<DPeptide> peptideList) {
		this.peptideList = peptideList;
	}

	/**
	 * Instantiates peptide dialog.
	 */
	public DPeptideMatchDialog(ObservableList<DPeptide> peptideList) {

		this.peptideList = peptideList;
		// Create notifications pane
		VBox peptideWarningPane = new VBox(2);
		Label peptideWarningLabel = new Label("No peptide were found for this ms query");
		peptideWarningLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		peptideWarningLabel.setStyle(JavaFxUtils.ORANGE_ITALIC);
		peptideWarningPane.getChildren().addAll(peptideWarningLabel);
		peptideWarningPane.setPrefWidth(500);
		// Create peptide match dialog components
		Label peptideLabel = new Label("The peptide(s):");
		peptideTableView = new TableView<DPeptide>(peptideList);
		TableColumn<DPeptide, String> sequenceCol = new TableColumn<DPeptide, String>("Sequence");
		sequenceCol.setCellValueFactory(new PropertyValueFactory<DPeptide, String>("m_sequence"));
		TableColumn<DPeptide, Double> caluclatedMassCol = new TableColumn<DPeptide, Double>("Calculated mass");
		caluclatedMassCol.setCellValueFactory(new PropertyValueFactory<DPeptide, Double>("m_calculatedMass"));
		peptideTableView.getColumns().addAll(sequenceCol, caluclatedMassCol);
		peptideTableView.autosize();
		peptideTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		peptideWarningPane.setVisible(peptideList.isEmpty());
		// Layout
		GridPane peptidePane = new GridPane();
		peptidePane.setAlignment(Pos.CENTER);
		peptidePane.setPadding(new Insets(10));
		peptidePane.setHgap(25);
		peptidePane.setVgap(25);
		peptidePane.addRow(0, peptideWarningPane);
		peptidePane.addRow(1, peptideLabel);
		peptidePane.addRow(2, peptideTableView);

		/********************
		 * Main dialog pane *
		 ********************/

		// Create and display the main dialog pane
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(peptidePane);
		dialogPane.setHeaderText("Peptide matches");
		dialogPane.setGraphic(new ImageView(IconResource.getImage(ICON.EDIT)));
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new ImageView(IconResource.getImage(ICON.EDIT)).getImage());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setPrefSize(600, 500);
		Button buttonOk = (Button) dialogPane.lookupButton(ButtonType.OK);
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));
		Button buttonCancel = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));
		this.setTitle("Peptide matches");
		this.setDialogPane(dialogPane);
		// On apply button
		this.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				return peptideList.size();
			} else {
				return null;
			}
		});
	}
}