package data;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class IncreaseContrast extends Filter{
	private RescaleOp rescale;
	private BufferedImage image;
	float scaleFactor = 2.0f;
    float offset = 0;
    private int id=5;
	
	public IncreaseContrast(BufferedImage image){
		this.image=image;
		increase();
	}
	
	 public void increase() {
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
	
	public int getID(){
		return id;
	}
}
