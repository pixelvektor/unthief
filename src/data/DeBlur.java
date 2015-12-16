package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/** Entfernt das MotionBlur, welches Blur erstellt von einem Bild.
 */
public class DeBlur extends Filter {
	/** Das zu bearbeitende Bild*/
	private final BufferedImage image;
	/** Die Blur Matrix (Punktantwort). */
	private static final float[] BLUR_MATRIX_F = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	/** Die Blur Matrix (Punktantwort). */
	private static final Complex[] BLUR_MATRIX =  Blur.getBlurMatrix();
	/** Die ID des Filters. */
	private static final int ID = 2;
	
	/** Ctor fuer den Filter DeBlur.
	 * @param image Das zu filternde Bild.
	 */
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
	
	/** Getter fuer die ID des Filters.
	 * @return Gibt die ID des Filters zurueck.
	 */
	public int getID(){
		return ID;
	}
	
	/** Ein gescheiterter Versuch das Bild von dem MotionBlur zu befreien.
	 */
	private void deBlur() {
		WritableRaster blurred = image.getRaster();
		WritableRaster unBlurred = image.copyData(null);
		int bands = blurred.getNumBands();
		int width = image.getWidth();
		int heigth = image.getHeight();
		int filterLength = BLUR_MATRIX_F.length;
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
					float newSample = oldSample * BLUR_MATRIX_F[k];
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

	/** Befreit das Bild von dem MotionBlur mittels der Fouriertransformation.
	 * Nicht vollstaendig implementiert, aufgrund der Probleme in Blur.
	 */
	private void deBlurFFT() {
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
		
		
		// DeBlur mit der Fouriertransformation wurde aufgrund der Probleme in Blur an dieser Stelle nicht implementiert.
		
		fft.transform(f, TransformType.INVERSE);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int l = 0; l < bands; l++) {
					blurred.setSample(x, y, l, f[y * width + x].getReal());
				}
			}
		}
	}
}
