package fr.lsmbo.msda.spectra.comp.view.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.Session;
import fr.lsmbo.msda.spectra.comp.db.DBConfig;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import fr.lsmbo.msda.spectra.comp.utils.StringsUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays login dialog.
 * 
 * @author Aromdhani
 *
 */
public class ParametersDialog extends Dialog<Map<String, Number>> {

	/** The delta moz label. */
	private Label deltaMozLabel;

	/** The delta moz TF. */
	private TextField deltaMozTF;

	/** The delta RT label. */
	private Label deltaRTLabel;

	/** The delta RTTF. */
	private TextField deltaRTTF;

	/** The min nbr peaks label. */
	private Label minNbrPeaksLabel;

	/** The min nbr peaks TF. */
	private TextField minNbrPeaksTF;

	/** The peaks nbr label. */
	private Label peaksNbrLabel;

	/** The peaks nbr TF. */
	private TextField peaksNbrTF;

	/** The thetha min label. */
	private Label thethaMinLabel;

	/** The theta min TF. */
	private TextField thetaMinTF;

	/** The parameters map. */
	private Map<String, Number> parametersMap = new HashMap<>();

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unchecked")
	public ParametersDialog() {
		VBox warningDbPane = new VBox(2);
		Label emptyParemetersLabel = new Label("Parameters of comparsion must not be empty!");
		emptyParemetersLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		emptyParemetersLabel.setStyle(JavaFxUtils.RED_ITALIC);
		warningDbPane.getChildren().addAll(emptyParemetersLabel);
		deltaMozLabel = new Label("Delta moz: ");
		deltaMozTF = new TextField();
		if (!StringsUtils.isEmpty(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaMoz())))
			deltaMozTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaMoz()));
		deltaMozTF.setTooltip(new Tooltip("Enter the delta moz value"));

		deltaRTLabel = new Label("Delta retention time: ");
		deltaRTTF = new TextField();
		if (!StringsUtils.isEmpty(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaRT())))
			deltaRTTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getDeltaRT()));
		deltaRTTF.setTooltip(new Tooltip("Enter the delta retention time"));

		minNbrPeaksLabel = new Label("Minimum peaks number: ");
		minNbrPeaksTF = new TextField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getPassword()))
			minNbrPeaksTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaksMin()));

		minNbrPeaksTF.setTooltip(new Tooltip("Enter the minimum peaks number"));

		peaksNbrLabel = new Label("Peaks number: ");
		peaksNbrTF = new TextField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getPassword()))
			peaksNbrTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getNbPeaks()));

		peaksNbrTF.setTooltip(new Tooltip("Enter the peaks number"));

		thethaMinLabel = new Label("Minimum theta: ");
		thetaMinTF = new TextField();
		if (!StringsUtils.isEmpty(DBConfig.getInstance().getPassword()))
			thetaMinTF.setText(String.valueOf(Session.USER_PARAMS.getComparison().getThetaMin()));

		thetaMinTF.setTooltip(new Tooltip("Enter the minimum theta"));
		// Style
		List<TextField> textFields = new ArrayList<>();
		textFields.add(deltaMozTF);
		textFields.add(deltaRTTF);
		textFields.add(minNbrPeaksTF);
		textFields.add(peaksNbrTF);
		textFields.add(thetaMinTF);
		textFields.forEach(field -> field.prefWidth(150));
		// Layout
		GridPane loginPane = new GridPane();
		loginPane.setAlignment(Pos.TOP_LEFT);
		loginPane.setPadding(new Insets(10));
		loginPane.setHgap(15);
		loginPane.setVgap(15);

		loginPane.add(warningDbPane, 0, 1, 2, 1);
		loginPane.add(deltaMozLabel, 0, 2, 1, 1);
		loginPane.add(deltaMozTF, 1, 2, 1, 1);
		loginPane.add(deltaRTLabel, 0, 3, 1, 1);
		loginPane.add(deltaRTTF, 1, 3, 1, 1);
		loginPane.add(minNbrPeaksLabel, 0, 4, 1, 1);
		loginPane.add(minNbrPeaksTF, 1, 4, 1, 1);
		loginPane.add(peaksNbrLabel, 0, 5, 1, 1);
		loginPane.add(peaksNbrTF, 1, 5, 1, 1);
		loginPane.add(thethaMinLabel, 0, 6, 1, 1);
		loginPane.add(thetaMinTF, 1, 6, 1, 1);

		/********************
		 * Main dialog pane *
		 ********************/

		// Create and display the main dialog pane
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(loginPane);
		dialogPane.setHeaderText("Comparsion parameters");
		dialogPane.setGraphic(new ImageView(IconResource.getImage(ICON.SETTINGS)));
		dialogPane.setPrefSize(450, 400);
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new ImageView(IconResource.getImage(ICON.SETTINGS)).getImage());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		Button buttonCancel = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));
		Button buttonOk = (Button) dialogPane.lookupButton(ButtonType.OK);
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));

		this.setTitle("Comparsion parameters");
		this.setDialogPane(dialogPane);
		// Control
		emptyParemetersLabel.visibleProperty()
				.bind(deltaMozTF.textProperty().isEmpty()
						.or(deltaRTTF.textProperty().isEmpty().or(minNbrPeaksTF.textProperty().isEmpty()
								.or(peaksNbrTF.textProperty().isEmpty().or(thetaMinTF.textProperty().isEmpty())))));
		buttonOk.disableProperty().bind(emptyParemetersLabel.visibleProperty());

		// On apply button
		this.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				try {
					parametersMap.put("delta_moz", Float.valueOf(deltaMozTF.getText()));
					parametersMap.put("delta_rt", Integer.valueOf(deltaRTTF.getText()));
					parametersMap.put("min_peaks_number", Integer.valueOf(minNbrPeaksTF.getText()));
					parametersMap.put("peaks_number", Integer.valueOf(peaksNbrTF.getText()));
					parametersMap.put("theta_min", Integer.valueOf(thetaMinTF.getText()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return parametersMap;
		});
	}
}