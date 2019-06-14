package fr.lsmbo.msda.spectra.comp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lsmbo.msda.spectra.comp.IconResource.ICON;
import fr.lsmbo.msda.spectra.comp.db.DBAccess;
import fr.lsmbo.msda.spectra.comp.view.MainPane;
import fr.lsmbo.msda.spectra.comp.view.dialog.ShowPopupDialog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SpectraCompFx extends Application {
	private static final Logger logger = LogManager.getLogger(SpectraCompFx.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// TODO Auto-generated method stub
		Thread.currentThread().setUncaughtExceptionHandler((thread, ex) -> {
			logger.error("Exception was thrown : {}", ex);
			ex.printStackTrace();
			logger.error(ex);
			new ShowPopupDialog("Exception", "Exception was thrown on Java-FX thread: " + ex.getMessage(),
					primaryStage);
		});
		MainPane mainPane = new MainPane();
		primaryStage.getIcons().add(new ImageView(IconResource.getImage(ICON.SPECTRA_COMP)).getImage());
		Scene scene = new Scene(mainPane, 1224, 800);
		// Add style sheets
		scene.getStylesheets().add("/css/style.css");
		// Set software name and release version
		primaryStage.setTitle(Session.SPECTRACOMP_RELEASE_NAME + " " + Session.SPECTRACOMP_RELEASE_VERSION);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void run() {
		launch();
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
		Config.getInstance();
		
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
