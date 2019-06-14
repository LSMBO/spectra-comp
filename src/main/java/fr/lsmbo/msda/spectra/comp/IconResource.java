package fr.lsmbo.msda.spectra.comp;

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
		SPECTRA_COMP, TICK, CONSOLE, HELP, INFORMATION, WARNING, RESET, EXIT, LOAD, LOGIN, CROSS;
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
			case SPECTRA_COMP:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/spectra-comp.png")));
				break;
			case LOGIN:
				images.put(icon, new Image(IconResource.class.getResourceAsStream("/images/login.gif")));
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
			default:
				break;
			}
		}
		return images.get(icon);
	}
}