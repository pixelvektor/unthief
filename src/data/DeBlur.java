package data;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class DeBlur extends Filter {
	private final BufferedImage image;
	private static final Complex[] BLUR_MATRIX =  Blur.getBlurMatrix();
	private static final int ID = 2;
	
	public DeBlur(final BufferedImage image){
		this.image = image;
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
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		
		int bands = blurred.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		int n = width * height;
		int fillSpace = Blur.fillSpace(n);
		
		Complex[] f = new Complex[n + fillSpace];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				f[y * width + x] = new Complex(blurred.getSampleDouble(x, y, 0));
			}
		}
		
		for (int l = n; l < (n  + fillSpace); l++) {
			f[l] = new Complex(0);
		}
		
		fft.transform(f, TransformType.FORWARD);
		
		
		//TODO DeBlur
		
		/*for (int l = 0; l < f.length; l++) {
			System.out.println("^f[" + l + "] = " + f[l]);
		}*/
		
		fft.transform(f, TransformType.INVERSE);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int l = 0; l < bands; l++) {
					blurred.setSample(x, y, l, f[y * width + x].getReal());
				}
			}
		}
	}
	
	/*
	@Deprecated
	private void deBlurOld() {
		WritableRaster blurred = image.getRaster();
		WritableRaster unBlurred = image.copyData(null);
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
					float newSample = oldSample * BLUR_MATRIX[k];
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
	*/
}
