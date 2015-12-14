package data;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class DeBlur extends Filter {
	private BufferedImage image;
	private static final float[] BLUR_MATRIX =  Blur.getBlurMatrix();
	private static final int ID = 2;
	
	public DeBlur(BufferedImage image){
		this.image=image;
		deBlur();
	}
	
	/** Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
	
	public int getID(){
		return ID;
	}
	
	private void deBlur() {
		WritableRaster blurred = image.getRaster();
		WritableRaster unBlurred = image.getRaster();
		int bands = blurred.getNumBands();
		int width = image.getWidth();
		int heigth = image.getHeight();
		int filterLength = BLUR_MATRIX.length;
		int halfFilterLength = filterLength/2;
		
		float zero = 0f;
		for (int y = 0; y < heigth; y++) {
			for (int x = 0; x < width; x++) {
				for (int l = 0; l < bands; l++) {
					unBlurred.setSample(x, y, l, zero);
				}
			}
		}
		
		for (int y = 0; y < heigth; y++) {
			for (int x = halfFilterLength; x < (width - halfFilterLength); x++) {
				float oldSample = blurred.getSampleFloat(x, y, 0);
				for (int k = 0; k < filterLength; k++) {
					float newSample = oldSample / BLUR_MATRIX[k];
					int xOffset = - halfFilterLength + k;
					for (int l = 0; l < bands; l++) {
						float tmpSample = unBlurred.getSampleFloat(x + xOffset, y, l);
						unBlurred.setSample(x + xOffset, y, l, newSample + tmpSample);
					}
				}
			}
		}
		image.setData(unBlurred);
	}
}
