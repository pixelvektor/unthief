package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Code extends Image{
	/** Pfad zu den Codes.*/
	private static final String PATH = "res/codes/";
	/** Array fuer die ausgelesenen Filter (4 Stueck) in ihrer Reihenfolge. */
	private int[] order = new int[4];
	
	/** Ctor fuer einen Code.
	 * Erstellt einen Code und liest die Reihenfolge der Filter aus.
	 */
	public Code() {
		super(PATH);
		System.out.println("Code erstellen ...");
		readOrder();
		System.out.println("done");
	}
	
	/** Liest die Reihenfolge der Filter aus dem Code aus.
	 */
	private void readOrder() {
		// Laden des Bildes
		BufferedImage image = this.getImage().get(0);
		Raster rasterImg = image.getRaster();
		int width = image.getWidth();
		
		// Auslesen der Bildwerte
		int black = 0;
		int j = 0;
		for (int i = 0; i < width; i++) {
			// 0 ist Schwarz
			if (rasterImg.getDataBuffer().getElem(i) == 0) {
				black++;
			} else if(i != 0 && rasterImg.getDataBuffer().getElem(i-1) == 0) {
				order[j] = black;
				black = 0;
				j++;
			}
		}
		for (int i = 0; i < order.length; i++) {
			System.out.println(order[i]);
		}
	}

	/** Getter fuer die Reihenfolge der Filter.
	 * @return Gibt die Reihenfolge der Filter zurueck.
	 */
	public int[] getOrder() {
		return order;
	}
}
