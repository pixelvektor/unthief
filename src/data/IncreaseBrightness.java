package data;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class IncreaseBrightness extends Filter{
	private BufferedImage image;
	private final static float INC_FACTOR = 1.3333f;
    private final static int ID = 5;
	
	public IncreaseBrightness(BufferedImage image){
		this.image=image;
		increase();
	}
	
	/** Getter fuer das bearbeitete Bild.
	* @return Das bearbeitete Bild.
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
	
	public void increase() {
		WritableRaster raster = image.getRaster();
		int bands = raster.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float tmpSample = raster.getSampleFloat(x, y, 0);
				tmpSample = tmpSample * INC_FACTOR;
				
				for (int l = 0; l < bands; l++) {
					raster.setSample(x, y, l, tmpSample);
				}
			}
		}
		
		image.setData(raster);
	}
}
