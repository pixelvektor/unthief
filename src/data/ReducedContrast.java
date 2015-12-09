package data;

import java.awt.image.*;

public class ReducedContrast extends Interference {
	private RescaleOp rescale;
	private BufferedImage image;
	float scaleFactor = 0.5f;
    float offset = 0;
	
	public ReducedContrast(BufferedImage image){
		this.image=image;
		decrease();
	}
	
	 public void decrease() {
	        rescale = new RescaleOp(scaleFactor, offset, null);
	        rescale.filter(image, image);
	    }
	 
	 /**
		 * Getter fuer das bearbeitete Bild
		 * @return Das bearbeitete Bild
		 */
		public BufferedImage getImage(){
			return image;
		}

}
