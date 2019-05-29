package fr.lsmbo.msda.spectra.comparator;

import java.util.HashMap;

import javafx.scene.image.Image;

/**
 * Add an icon resource
 * 
 * @author Aromdhani
 *
 */
public class IconResource {
	/**
	 * Enum type that indicates the name of the icon.
	 */
	public enum ICON {
		CONSOLE, HELP, INFORMATION, WARNING, RESET, EXIT;
	}

	private static HashMap<ICON, Image> images = new HashMap<ICON, Image>();

	/**
	 * Return an image
	 * 
	 * @param icon
	 *            the icon to load.
	 * @return Image
	 */
	public static Image getImage(ICON icon) {
		if (!images.containsKey(icon)) {
			switch (icon) {

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
			default:
				break;
			}
		}
		return images.get(icon);
	}
}