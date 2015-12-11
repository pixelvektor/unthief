package data;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class DeBlur extends Filter {
	private BufferedImage image;
	int[] window=new int[9];
	
	public DeBlur(BufferedImage image){
		this.image=image;
		pointSpread();
	}
	
	/** Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
	
	private void pointSpread() {
		
	}
}