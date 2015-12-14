package data;


import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Blur extends Interference {
	/** Name der Stoerung*/
	private static final String NAME = "Unsharpen";
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	/** Blur Matrix*/
	private final static float[] BLUR_MATRIX = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	private final static int ID = 2;
	
	/** Ctor fuer ein bild mit Blur.
	 * @param image Das zu bearbeitende Bild
	 */
	public Blur(BufferedImage image){
		this.image=image;
		blur();
	}
	
	/** Getter fuer den Namen der Stoerung
	 * @return Name der Stoerung
	 */
	public String getName() {
		return NAME;
	}
	
	/** Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
	
	public static float[] getBlurMatrix() {
		return BLUR_MATRIX;
	}
	
	public int getID(){
		return ID;
	}
	
	/** Wendet die Unschaerfe auf das Bild an. 
	 */
	private void blur() {
		WritableRaster unBlurred = image.getRaster();
		WritableRaster blurred = image.getRaster();
		int bands = unBlurred.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		int filterLength = BLUR_MATRIX.length;
		int halfFilterLength = filterLength/2;
		
		for (int y = 0; y < height; y++) {
			for (int x = halfFilterLength; x < (width - halfFilterLength); x++) {
				float newSample = 0f;
				for (int k = 0; k < filterLength; k++) {
					int xOffset = - halfFilterLength + k;
					float sample = unBlurred.getSampleFloat(x + xOffset, y, 0) * BLUR_MATRIX[k];
					newSample += sample;
				}
				for (int l = 0; l < bands; l++) {
					blurred.setSample(x, y, l, newSample);
				}
			}
		}
		image.setData(blurred);
	}
}
