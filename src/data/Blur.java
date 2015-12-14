package data;


import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;

public class Blur extends Interference {
	/** Name der Stoerung*/
	private static final String NAME = "Unsharpen";
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	/** Blur Matrix*/
	private final static float[] BLUR_MATRIX = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	private int id=2;
	
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
	
	public int getID(){
		return id;
	}
	
	/** Wendet die Unschaerfe auf das Bild an. 
	 */
	private void blur(String bla) {
		BufferedImageOp bio = new ConvolveOp(new Kernel(10, 1, BLUR_MATRIX));
		image = bio.filter(image, null);
	}
	
	private void blur() {
		WritableRaster unBlurred = image.getRaster();
		WritableRaster blurred = image.getRaster();
		int bands = unBlurred.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		int filterLength = BLUR_MATRIX.length;
		int halfFilterLength = filterLength/2;
		
		for (int i = 0; i < height; i++) {
			for (int j = halfFilterLength; j < (width - halfFilterLength); j++) {
				float newSample = 0f;
				for (int k = 0; k < filterLength; k++) {
					int jOffset = - halfFilterLength + k;
					float sample = unBlurred.getSampleFloat(j + jOffset, i, 0) * BLUR_MATRIX[k];
					newSample += sample;
				}
				for (int l = 0; l < bands; l++) {
					blurred.setSample(j, i, l, newSample);
				}
			}
		}
		image.setData(blurred);
	}
}
