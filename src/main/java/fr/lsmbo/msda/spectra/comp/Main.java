package fr.lsmbo.msda.spectra.comp;

import java.sql.SQLException;

import fr.lsmbo.msda.spectra.comp.db.DBAccess;
import fr.lsmbo.msda.spectra.comp.io.PeakListProvider;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		Config.getInstance();
		// TODO to remove this test
				try {
					PeakListProvider.loadFirstSpectra(Session.USER_PARAMS.getProjectName(),
							Session.USER_PARAMS.getFirstPklList());
					PeakListProvider.loadSecondSpectra(Session.USER_PARAMS.getProjectName(),
							Session.USER_PARAMS.getSecondPklList());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		launch(args);
		
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

	@Override
	public void start(Stage primaryStage) {
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
