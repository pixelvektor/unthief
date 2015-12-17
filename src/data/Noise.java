package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/** Fuegt zu einem Bild Rauschen hinzu.
 */
public class Noise extends Interference {
	/** Das zu bearbeitende Bild */
	private BufferedImage image;
	/** ID der Stoerung. */
    private int id=7;
	
	/** Ctor fuer das verrauschte Bild.
	 * @param image Das zu bearbeitende Bild
	 */
	public Noise(BufferedImage image){
		this.image=image;
		saltNPepperNoise();
	}
	
	/**
	 * Methode fuer Bildrauschen.
	 */
	private void saltNPepperNoise() {
        WritableRaster out = image.getRaster();
        int bandsImage  = out.getNumBands();
        int widthImage  = image.getWidth();
        int heightImage = image.getHeight();
      
        double rand;
        java.util.Random randGen = new java.util.Random();
          
        for (int height=0; height<heightImage; height++) {
            for (int width=0; width<widthImage; width++) {
                rand = randGen.nextDouble();
                //zufaelliges SaltNPepper
                if (rand > 0.9 && rand <=0.95) {
                    for (int band=0; band<bandsImage; band++){
                    	out.setSample(width, height, band, 0);
                    }
                } else if (rand > 0.95) {
                    for (int band=0; band<bandsImage; band++){
                    	out.setSample(width, height, band, 255);
                    }
                }
            }
        }
	}
		
	/**
	 * Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}

	/**
	 * Getter fuer die ID.
	 * @return die ID.
	 */
	public int getID(){
		return id;
	}
}
