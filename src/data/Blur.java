package data;


import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class Blur extends Interference {
	/** Name der Stoerung*/
	private static final String NAME = "Unsharpen";
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	/** Blur Matrix*/
	private final static float[] BLUR_TARGET_F = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	private final static double[] BLUR_TARGET = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
	private static Complex[] blurMatrix;
	private final static int ID = 2;
	
	/** Ctor fuer ein bild mit Blur.
	 * @param image Das zu bearbeitende Bild
	 */
	public Blur(BufferedImage image){
		this.image=image;
		
		int n = image.getWidth() * image.getHeight();
		int fillSpace = fillSpace(n);
		
		blurMatrix = new Complex[n + fillSpace];
		createBlurMatrix();
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
	
	public static Complex[] getBlurMatrix() {
		return blurMatrix;
	}
	
	public int getID(){
		return ID;
	}
	
	public static int fillSpace(final int n) {
		int fillSpace = 0;
		
		for (int mask = 1; mask < n; mask <<= 1) {
			fillSpace = mask;
		}
		fillSpace = fillSpace * 2 - n;
		return fillSpace;
	}

	private void createBlurMatrix() {
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		int size = blurMatrix.length;
		int width = image.getWidth();
		int height = image.getHeight();
		int halfStop = (height / 2) * width - (width / 2 - BLUR_TARGET.length / 2);
		
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (halfStop < i && i <= (halfStop + BLUR_TARGET.length)) {
				blurMatrix[i] = new Complex(BLUR_TARGET[j]);
				j++;
			} else {
				blurMatrix[i] = new Complex(0);
			}
		}
		fft.transform(blurMatrix, TransformType.FORWARD);
		
	}
	
	private void blurFFT() {
		WritableRaster unblurred = image.getRaster();
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		
		int bands = unblurred.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		int n = width * height;
		int fillSpace = fillSpace(n);
		
		Complex[] f = new Complex[n + fillSpace];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				f[y * width + x] = new Complex(unblurred.getSampleDouble(x, y, 0));
			}
		}
		
		for (int l = n; l < (n  + fillSpace); l++) {
			f[l] = new Complex(0);
		}
		
		fft.transform(f, TransformType.FORWARD);
		
		// Matrix Multiplikation
		
		Complex[][] fMulti = new Complex[1024][512];
		Complex[][] blurMatrixMulti = new Complex[1024][512];
		
		int ubRow = fMulti.length;
		int ubCol = fMulti[0].length;
		int blurRow = blurMatrixMulti.length;
		int blurCol = blurMatrixMulti[0].length;
		
		convertToMultiArray(f, fMulti);
		convertToMultiArray(blurMatrix, blurMatrixMulti);
		
		Complex[][] result = new Complex[ubRow][blurCol];
		int blaRow = result.length;
		int blaColumn = result[0].length;
		
		for (int i = 0; i < blaRow; i++) {
			for (int j = 0; j < blaColumn; j++) {
				result[i][j] = new Complex(0);
			}
		}
		
		for (int i = 0; i < ubRow; i++) {
			for (int j = 0; j < blurCol; j++) {
				for (int k = 0; k < ubCol; k++) {
					Complex tmp = fMulti[i][k].multiply(blurMatrixMulti[k][j]);
					result[i][j].add(tmp);
				}
			}
		}
		
		int k = 0;
		for (int i = 0; i < blaRow; i++) {
			for (int j = 0; j < blaColumn; j++) {
				f[k] = result[i][j];
				k++;
			}
			
		}
		
		fft.transform(f, TransformType.INVERSE);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int l = 0; l < bands; l++) {
					unblurred.setSample(x, y, l, f[y * width + x].getReal());
				}
			}
		}
	}

	private void convertToMultiArray(final Complex[] from, final Complex[][] to) {
		int k = 0;
		for (int i = 0; i < to.length; i++) {
			for (int j = 0; j < to[i].length; j++) {
				to[i][j] = from[k];
				k++;
			}
		}
	}
	
	/** Wendet die Unschaerfe auf das Bild an. 
	 */
	private void blur() {
		WritableRaster unBlurred = image.getRaster();
		WritableRaster blurred = image.copyData(null);
		int bands = unBlurred.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		int filterLength = BLUR_TARGET_F.length;
		int halfFilterLength = filterLength/2;
		
		for (int y = 0; y < height; y++) {
			for (int x = halfFilterLength; x < (width - halfFilterLength); x++) {
				float newSample = 0f;
				for (int k = 0; k < filterLength; k++) {
					int xOffset = - halfFilterLength + k;
					float sample = unBlurred.getSampleFloat(x + xOffset, y, 0) * BLUR_TARGET_F[k];
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
