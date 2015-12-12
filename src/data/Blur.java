package data;


import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Blur extends Interference {
	/** Name der Stoerung*/
	private static final String NAME = "Unsharpen";
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	/** Blur Matrix*/
	private final static float[] BLUR_MATRIX = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	
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
	
	/** Wendet die Unschaerfe auf das Bild an. 
	 */
	private void blur() {
		BufferedImageOp bio = new ConvolveOp(new Kernel(10, 1, BLUR_MATRIX));
		image = bio.filter(image, null);
	}
}
