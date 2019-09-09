/*
 * 
 */
package fr.lsmbo.msda.spectra.comp;

import java.util.HashMap;

import javafx.scene.image.Image;

// TODO: Auto-generated Javadoc
/**
 * Add an icon resource.
 *
 * @author Aromdhani
 */
public class IconResource {
	/**
	 * Enum type that indicates the name of the icon.
	 */
	public enum ICON {

		/** The admin. */
		ADMIN,
		/** The spectra comp. */
		SPECTRA_COMP,
		/** The tick. */
		TICK,
		/** The console. */
		CONSOLE,
		/** The help. */
		HELP,
		/** The information. */
		INFORMATION,
		/** The warning. */
		WARNING,
		/** The reset. */
		RESET,
		/** The exit. */
		EXIT,
		/** The load. */
		LOAD,
		/** The execute. */
		EXECUTE,
		/** The cross. */
		CROSS,
		/** The database. */
		DATABASE,
		/** The edit. */
		EDIT,
		/** The dataset rsm. */
		DATASET_RSM,
		/** The dataset rsm merged a. */
		DATASET_RSM_MERGED_A,
		/** The settings. */
		SETTINGS,
		/** The progress. */
		PROGRESS,
		/** The export */
		EXPORT,
		/** The plus */
		EYE;
	}

	/** The images. */
	private static HashMap<ICON, Image> images = new HashMap<ICON, Image>();

	/**
	 * Return an image.
	 *
	 * @param icon the icon to load.
	 * @return Image
	 */
	public static Image getImage(ICON icon) {
		if (!images.containsKey(icon)) {
			switch (icon) {
			case EXPORT:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/export.png")));
				break;
			case EDIT:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/edit-parsing-rules.png")));
				break;
			case SETTINGS:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/settings.png")));
				break;
			case PROGRESS:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/progress.png")));
				break;
			case ADMIN:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/admin.png")));
				break;
			case DATASET_RSM:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/dataset-rsm.png")));
				break;
			case DATASET_RSM_MERGED_A:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/dataset-rsm-mergedA.png")));
				break;
			case DATABASE:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/database.png")));
				break;
			case EXECUTE:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/execute.png")));
				break;

			case SPECTRA_COMP:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/spectra-comp.png")));
				break;
			case LOAD:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/load.png")));
				break;
			case TICK:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/tick.png")));
				break;
			case HELP:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/help.png")));
				break;
			case INFORMATION:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/information.png")));
				break;
			case WARNING:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/warning.png")));
				break;
			case RESET:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/arrowcircle.png")));
				break;
			case EXIT:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/exit.png")));
				break;
			case CONSOLE:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/console.png")));
				break;
			case CROSS:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/cross.png")));
				break;
			case EYE:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/eye.png")));
				break;
			default:
				break;
			}
		}
		return images.get(icon);
	}
}