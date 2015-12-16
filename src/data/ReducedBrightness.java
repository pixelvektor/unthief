package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.*;

public class ReducedBrightness extends Interference {
	private BufferedImage image;
	private final static float RED_FACTOR = 0.75f;
    private final static int ID = 5;
	
	public ReducedBrightness(BufferedImage image){
		this.image=image;
		reduce();
	}
	
	public void reduce() {
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
}
