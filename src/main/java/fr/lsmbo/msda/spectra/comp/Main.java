package fr.lsmbo.msda.spectra.comp;

import fr.lsmbo.msda.spectra.comp.db.DBAccess;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		Config.initialize();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#init()
	 */
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		DBAccess.closeAll();
		super.stop();
	}

}
