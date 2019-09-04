/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.view.dialog;

import java.util.HashMap;
import java.util.Map;

import fr.lsmbo.msda.spectra.comp.IconResource;
import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.list.ListOfSpectra;
import fr.lsmbo.msda.spectra.comp.list.ParsingRules;
import fr.lsmbo.msda.spectra.comp.model.ParsingRule;
import fr.lsmbo.msda.spectra.comp.model.ParsingRuleType;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import fr.lsmbo.msda.spectra.comp.utils.JavaFxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * Creates and displays parsing rules dialog to edit parsing rules.
 * 
 * @author aromdhani
 *
 */
public class ParsingRulesDialog extends Dialog<Map<ParsingRuleType, ParsingRule>> {

	/** The titles. */
	private ObservableList<Spectrum> referenceTitles = FXCollections.observableArrayList();

	/** The titles. */
	private ObservableList<Spectrum> testTitles = FXCollections.observableArrayList();

	/** The selected parsing rule. */
	private ParsingRule refSelectedParsingRule;

	/** The selected parsing rule. */
	private ParsingRule testSelectedParsingRule;

	/** The table. */
	private TableView<Spectrum> refTable;

	/** The table. */
	private TableView<Spectrum> testTable;

	private Map<ParsingRuleType, ParsingRule> parsingRuleByType = new HashMap<>();

	/**
	 * Instantiates a new parsing rules dialog.
	 */
	public ParsingRulesDialog() {

		// Create notifications pane
		VBox refParsingRuleWarningPane = new VBox(2);
		Label refParsingRuleWarningLabel = new Label(
				"Parsing rule for reference spectra must not be empty. Select a parsing rule.");
		refParsingRuleWarningLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		refParsingRuleWarningLabel.setStyle(JavaFxUtils.RED_ITALIC);
		refParsingRuleWarningPane.getChildren().addAll(refParsingRuleWarningLabel);

		// Create Parsing rules dialog components
		Label refParsingRuleLabel = new Label("Select a parsing rule: ");
		Label refSelectedParsingRuleLabel = new Label("Selected parsing rule: ");
		TextField refSelectedParsingRuleTF = new TextField();
		refSelectedParsingRuleTF.setTooltip(new Tooltip("Enter a parsing rule"));
		// Set parsing rules values
		ComboBox<String> refSelectedParsingCmBox = new ComboBox<String>();
		for (ParsingRule parsingRule : ParsingRules.get()) {
			refSelectedParsingCmBox.getItems().add(parsingRule.getName());
		}

		// Set table view values
		referenceTitles.clear();
		refTable = new TableView<Spectrum>(referenceTitles);
		TableColumn<Spectrum, String> refTitleCol = new TableColumn<Spectrum, String>("Title");
		refTitleCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("m_title"));
		TableColumn<Spectrum, Float> refNewRTCol = new TableColumn<Spectrum, Float>("Retention time");
		refNewRTCol.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("retentionTime"));
		refTable.getColumns().addAll(refTitleCol, refNewRTCol);
		refTable.autosize();
		refTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		refSelectedParsingCmBox.setOnAction((e) -> {
			refSelectedParsingRule = ParsingRules.get(refSelectedParsingCmBox.getValue());
			refSelectedParsingRuleTF.setText(refSelectedParsingRule.getRegex());
			tryRegex(ParsingRuleType.REFERENCE);
		});
		int refSpectraNb = ListOfSpectra.getFirstSpectra().getSpectraAsObservable().size();
		if (refSpectraNb > 5)
			refSpectraNb = 5;
		for (int i = 0; i < refSpectraNb; i++) {
			referenceTitles.add(ListOfSpectra.getFirstSpectra().getSpectraAsObservable().get(i));
		}
		// Set default value
		// default values
		ParsingRule refPR = ParsingRules.getCurrentParsingRule();
		if (refPR != null) {
			refSelectedParsingCmBox.setValue(refPR.getName());
			refSelectedParsingRuleTF.setText(refPR.getRegex());
		}
		// Show notification
		refParsingRuleWarningLabel.visibleProperty().bind(refSelectedParsingRuleTF.textProperty().isEmpty()
				.or(refSelectedParsingCmBox.getSelectionModel().selectedItemProperty().isNull()));

		// Layout
		GridPane refParsingRulesPane = new GridPane();
		refParsingRulesPane.setAlignment(Pos.CENTER);
		refParsingRulesPane.setPadding(new Insets(10));
		refParsingRulesPane.setHgap(25);
		refParsingRulesPane.setVgap(25);
		refParsingRulesPane.add(refParsingRuleWarningPane, 0, 0, 5, 1);
		refParsingRulesPane.addRow(1, refParsingRuleLabel, refSelectedParsingCmBox, refSelectedParsingRuleLabel,
				refSelectedParsingRuleTF);
		refParsingRulesPane.add(refTable, 0, 2, 5, 3);

		// Create test tab Pane

		// Create notifications pane
		VBox testParsingRuleWarningPane = new VBox(2);
		Label testParsingRuleWarningLabel = new Label(
				"Parsing rule for test spectra must not be empty. Select a parsing rule.");
		testParsingRuleWarningLabel.setGraphic(new ImageView(IconResource.getImage(ICON.WARNING)));
		testParsingRuleWarningLabel.setStyle(JavaFxUtils.RED_ITALIC);
		testParsingRuleWarningPane.getChildren().addAll(testParsingRuleWarningLabel);

		// Create Parsing rules dialog components
		Label testParsingRuleLabel = new Label("Select a parsing rule: ");
		Label testSelectedParsingRuleLabel = new Label("Selected parsing rule: ");
		TextField testSelectedParsingRuleTF = new TextField();
		testSelectedParsingRuleTF.setTooltip(new Tooltip("Enter a parsing rule"));
		// Set parsing rules values
		ComboBox<String> testSelectedParsingCmBox = new ComboBox<String>();
		for (ParsingRule parsingRule : ParsingRules.get()) {
			testSelectedParsingCmBox.getItems().add(parsingRule.getName());
		}
		// Set table view values
		testTitles.clear();
		testTable = new TableView<Spectrum>(testTitles);
		TableColumn<Spectrum, String> testTitlesCol = new TableColumn<Spectrum, String>("Title");
		testTitlesCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("m_title"));
		TableColumn<Spectrum, Float> testNewRTCol = new TableColumn<Spectrum, Float>("Retention time");
		testNewRTCol.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("retentionTime"));
		testTable.getColumns().addAll(testTitlesCol, testNewRTCol);
		testTable.autosize();
		testTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		testSelectedParsingCmBox.setOnAction((e) -> {
			testSelectedParsingRule = ParsingRules.get(testSelectedParsingCmBox.getValue());
			testSelectedParsingRuleTF.setText(testSelectedParsingRule.getRegex());
			tryRegex(ParsingRuleType.TEST);
		});
		int testSpectraNb = ListOfSpectra.getSecondSpectra().getSpectraAsObservable().size();
		if (testSpectraNb > 5)
			testSpectraNb = 5;
		for (int i = 0; i < testSpectraNb; i++) {
			testTitles.add(ListOfSpectra.getSecondSpectra().getSpectraAsObservable().get(i));
		}
		// Set default value
		// default values
		ParsingRule testPR = ParsingRules.getCurrentParsingRule();
		if (testPR != null) {
			testSelectedParsingCmBox.setValue(testPR.getName());
			testSelectedParsingRuleTF.setText(testPR.getRegex());
		}
		// Show notification
		testParsingRuleWarningLabel.visibleProperty().bind(testSelectedParsingRuleTF.textProperty().isEmpty()
				.or(testSelectedParsingCmBox.getSelectionModel().selectedItemProperty().isNull()));

		// Layout
		GridPane testParsingRulesPane = new GridPane();
		testParsingRulesPane.setAlignment(Pos.CENTER);
		testParsingRulesPane.setPadding(new Insets(10));
		testParsingRulesPane.setHgap(25);
		testParsingRulesPane.setVgap(25);
		testParsingRulesPane.add(testParsingRuleWarningPane, 0, 0, 5, 1);
		testParsingRulesPane.addRow(1, testParsingRuleLabel, testSelectedParsingCmBox, testSelectedParsingRuleLabel,
				testSelectedParsingRuleTF);
		testParsingRulesPane.add(testTable, 0, 2, 5, 3);

		// Create first tabpane
		TabPane referenceTabPane = new TabPane();
		Tab referenParsingRuleTab = new Tab(" Reference spectra ");
		referenParsingRuleTab.setContent(refParsingRulesPane);
		referenParsingRuleTab.setClosable(false);
		Tab testParsingRuleTab = new Tab(" Test spectra ");
		testParsingRuleTab.setContent(testParsingRulesPane);
		testParsingRuleTab.setClosable(false);
		referenceTabPane.getTabs().addAll(referenParsingRuleTab, testParsingRuleTab);

		/********************
		 * Main dialog pane *
		 ********************/

		// Create and display the main dialog pane
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(referenceTabPane);
		dialogPane.setHeaderText("Edit parsing rules");
		dialogPane.setGraphic(new ImageView(IconResource.getImage(ICON.EDIT)));
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new ImageView(IconResource.getImage(ICON.EDIT)).getImage());
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setPrefSize(800, 500);

		Button buttonOk = (Button) dialogPane.lookupButton(ButtonType.OK);
		buttonOk.setGraphic(new ImageView(IconResource.getImage(ICON.TICK)));
		Button buttonCancel = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
		buttonCancel.setGraphic(new ImageView(IconResource.getImage(ICON.CROSS)));

		this.setTitle("Parsing Rules");
		this.setDialogPane(dialogPane);

		// Enable Ok button when input fields are valid.
		buttonOk.disableProperty().bind(refParsingRuleWarningLabel.visibleProperty());
		// On apply button
		this.setResultConverter(buttonType -> {
			if ((buttonType == ButtonType.OK) && (refSelectedParsingRule != null)
					&& (testSelectedParsingRule != null)) {
				return this.parsingRuleByType;
			} else {
				return null;
			}
		});
	}

	/**
	 * Try Regex; set the selected parsing rule from GUI as to retrieve retention
	 * time from title.
	 * 
	 * @param type the parsing rule type.
	 */
	private void tryRegex(ParsingRuleType type) {
		switch (type) {
		case REFERENCE: {
			for (Spectrum s : referenceTitles) {
				s.setRetentionTimeFromTitle(refSelectedParsingRule.getRegex());
			}
			parsingRuleByType.put(ParsingRuleType.REFERENCE, refSelectedParsingRule);
			refTable.refresh();
			break;
		}
		case TEST: {
			for (Spectrum s : testTitles) {
				s.setRetentionTimeFromTitle(testSelectedParsingRule.getRegex());
			}
			parsingRuleByType.put(ParsingRuleType.TEST, testSelectedParsingRule);
			testTable.refresh();
			break;
		}
		default:
			break;
		}
	}

}