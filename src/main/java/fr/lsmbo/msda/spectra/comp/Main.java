package fr.lsmbo.msda.spectra.comp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			logger.info("Start Spectra-comp");
			SpectraCompFx.run();
		} else {
			logger.info("Add CLI");
		}
	}
}
