package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.*;

/** Reduziert die Helligkeit eines Bildes.
 */
public class ReducedBrightness extends Interference {
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	/** Faktor auf den die Helligkeit reduziert werden soll. */ 
	private final static float RED_FACTOR = 0.75f;
	/** Die ID der Stoerung. */
    private final static int ID = 5;
	
    /** Ctor fuer die reduzierte Helligkeit.
     * @param image Das zu bearbeitende Bild.
     */
	public ReducedBrightness(final BufferedImage image){
		this.image=image;
		reduce();
	}
	
	/**
	* Getter fuer das bearbeitete Bild
	* @return Das bearbeitete Bild
	*/
	public BufferedImage getImage(){
		return image;
	}

	/** Getter fuer die ID.
	 * @return Gibt die ID zurueck.
	 */
	public int getID(){
		return ID;
	}

	/** Reduziert die Helligkeit des Bildes.
	 */
	private void reduce() {
		WritableRaster raster = image.getRaster();
		int bands = raster.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float tmpSample = raster.getSampleFloat(x, y, 0);
				tmpSample = tmpSample * RED_FACTOR;
				
				for (int l = 0; l < bands; l++) {
					raster.setSample(x, y, l, tmpSample);
				}
			}
		}
		
		image.setData(raster);
	}
}
