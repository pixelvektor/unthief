package data;

import java.awt.image.BufferedImage;

/** Superklasse der Filter.
 */
public class Filter {
	/** Speichert die ID des Filters. */
	private int id;
	
	/** Getter fuer ein gefiltertes Bild.
	 * @return Gibt das gefilterte Bild zurueck.
	 */
	public BufferedImage getImage() {
		return null;
	}
	
	/** Getter fuer die ID des Filters.
	 * @return Gibt die ID des Filters zurueck.
	 */
	public int getID(){
		return id;
	}
}
