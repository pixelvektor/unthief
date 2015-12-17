package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/** Erstellt einen Motion Blur auf dem Bild.
 */
public class Blur extends Interference {
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	/** Die Blur Matrix (Punktantwort). */
	private final static float[] BLUR_TARGET_F = {0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	/** Die Blur Matrix (Punktantwort). */
	private final static double[] BLUR_TARGET = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
	/** Die Blur Matrix (Punktantwort). */
	private static Complex[] blurMatrix;
	/** Die ID der Stoerung. */
	private final static int ID = 2;
	
	/** Ctor fuer ein Bild mit Blur.
	 * @param image Das zu bearbeitende Bild
	 */
	public Blur(BufferedImage image){
		this.image=image;
		
		/* Wird fuer die FFT Version benoetigt.
		// Pixel eines Bildes.
		int n = image.getWidth() * image.getHeight();
		int fillSpace = fillSpace(n);
		
		blurMatrix = new Complex[n + fillSpace];
		createBlurMatrix();
		*/
		
		blur(image);
	}
	
	/** Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
	
	/** Getter fuer die Blur Matrix in komplexen Zahlen.
	 * @return gibt die Blur Matrix in komplexen Zahlen zurueck.
	 */
	public static Complex[] getBlurMatrix() {
		return blurMatrix;
	}
	
	/** Getter fuer die ID der Stoerung.
	 * @return Gibt die ID der Stoerung zurueck.
	 */
	public int getID(){
		return ID;
	}
	
	/** Berechnet den Platz bis zur naechst hoeheren 2er-Potenz.
	 * @param n Der Wert zu dem der Platz berechnet werden soll.
	 * @return Gibt den Platz als int zurueck.
	 */
	public static int fillSpace(final int n) {
		int fillSpace = 0;
		
		// Schiebt die Maske binaer um den Faktor 2 (2er Potenz)
		for (int mask = 1; mask < n; mask <<= 1) {
			fillSpace = mask;
		}
		fillSpace = fillSpace * 2 - n;
		return fillSpace;
	}

	/** Wendet den MotionBlur auf das Bild an.
	 * @param img Das Bild, welches geblurrt werden soll.
	 */
	public static void blur(final BufferedImage img) {
		WritableRaster unBlurred = img.getRaster();
		WritableRaster blurred = img.copyData(null);
		int bands = unBlurred.getNumBands();
		int width = img.getWidth();
		int height = img.getHeight();
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
		img.setData(blurred);
	}

	/** Erstellt die BlurMatrix passend zur Eingangsbildgroesse.
	 * Die BlurMatrix liegt anschliessend in der Fouriertransformierten vor.
	 */
	private void createBlurMatrix() {
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		int size = blurMatrix.length;
		int width = image.getWidth();
		int height = image.getHeight();
		// Wert ab dem die PSF in die Matrix eingebaut werden soll
		int halfStop = (height / 2) * width - (width / 2 - BLUR_TARGET.length / 2); 
		
		// Aufbauen der BlurMatrix
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
	
	/** Erstellt den Blur basierend auf der Fouriertransformation.
	 * Funktion ist fehlerhaft:
	 * Berechnung dauert zu lange, Bild wird schwarz.
	 */
	private void blurFFT() {
		WritableRaster unblurred = image.getRaster();
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		
		int bands = unblurred.getNumBands();
		int width = image.getWidth();
		int height = image.getHeight();
		int n = width * height;
		int fillSpace = fillSpace(n);
		
		Complex[] f = new Complex[n + fillSpace];
		
		// Aufbau des Bildes als Complex
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				f[y * width + x] = new Complex(unblurred.getSampleDouble(x, y, 0));
			}
		}
		
		// Auffuellen des Complex Bild Arrays mit 0 bis zur naechsten 2er-Potenz
		for (int l = n; l < (n  + fillSpace); l++) {
			f[l] = new Complex(0);
		}
		
		fft.transform(f, TransformType.FORWARD);
		
		// Matrix Multiplikation
		
		Complex[][] fMulti = new Complex[1024][512];
		Complex[][] blurMatrixMulti = new Complex[1024][512];
		
		int ubRow = fMulti.length;
		int ubCol = fMulti[0].length;
		int blurCol = blurMatrixMulti[0].length;
		
		convertToMultiArray(f, fMulti);
		convertToMultiArray(blurMatrix, blurMatrixMulti);
		
		Complex[][] result = new Complex[ubRow][blurCol];
		int resultRow = result.length;
		int resultColumn = result[0].length;
		
		// Umwandeln in ein zweidimensionales Array fuer die Matrixmultiplikation
		for (int i = 0; i < resultRow; i++) {
			for (int j = 0; j < resultColumn; j++) {
				result[i][j] = new Complex(0);
			}
		}
		
		/* Durchfuehren der Matrix Multiplikation mit dem fouriertransformierten
		 * Filter blurMatrixMulti und Bild fMulti.
		 * Das Ergebnis ist fehlerhaft, da das Resultat nach der Ruecktransformation
		 * schwarz ist.
		 */
		for (int i = 0; i < ubRow; i++) {
			for (int j = 0; j < blurCol; j++) {
				for (int k = 0; k < ubCol; k++) {
					Complex tmp = fMulti[i][k].multiply(blurMatrixMulti[k][j]);
					result[i][j].add(tmp);
				}
			}
		}
		
		// Umwandeln in ein eindimensionales Array
		int k = 0;
		for (int i = 0; i < resultRow; i++) {
			for (int j = 0; j < resultColumn; j++) {
				f[k] = result[i][j];
				k++;
			}
			
		}
		
		// Ruecktransformation
		fft.transform(f, TransformType.INVERSE);
		
		// Setzen des geblurrten Bildes
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int l = 0; l < bands; l++) {
					unblurred.setSample(x, y, l, f[y * width + x].getReal());
				}
			}
		}
	}

	/** Konvertiert ein eindimensionales Array in ein zweidimensionales.
	 * @param from Das zu konvertierende Array.
	 * @param to Das Array in das konvertiert werden soll.
	 */
	private void convertToMultiArray(final Complex[] from, final Complex[][] to) {
		int k = 0;
		for (int i = 0; i < to.length; i++) {
			for (int j = 0; j < to[i].length; j++) {
				to[i][j] = from[k];
				k++;
			}
		}
	}
}
