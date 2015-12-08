package data;


import java.awt.image.*;

public class Noise extends Interference {
	/** Name der Stoerung*/
	private static final String NAME = "Noise";
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	//private BufferedImage output;
	/** Standardabweichung*/
	double standardDeviation;
	
	/**
	 * Getter fuer den Namen der Stoerung
	 * @return Name der Stoerung
	 */
	public String getName() {
		return NAME;
	}
	
	/**
	 * 
	 * @param image Das zu bearbeitende Bild
	 */
	public Noise(BufferedImage image){
		this.image=image;
		//output=image;
		saltNPepperNoise();
	}
	
	/**
	 * Methode fuer Bildrauschen
	 */
	private void saltNPepperNoise() {
        //Raster source = image.getRaster();
        WritableRaster out = image.getRaster();
        int bandsImage  = out.getNumBands(); // number of bands
        int widthImage  = image.getWidth();  // width of the image
        int heightImage = image.getHeight(); // height of the image
        double impulseRatio=standardDeviation(widthImage,heightImage,bandsImage,out)/200.0d;
        System.out.println(impulseRatio);
        //java.util.Random rand = new java.util.Random();
      
        double rand;
        double halfImpulseRatio = impulseRatio / 2.0;
        java.util.Random randGen = new java.util.Random();
          
        for (int height=0; height<heightImage; height++) {
            for (int width=0; width<widthImage; width++) {
                rand = randGen.nextDouble();
                if (rand > 0.8 && rand <=0.9) {
                    for (int band=0; band<bandsImage; band++){
                    	out.setSample(width, height, band, 0);
                    }
                } else if (rand > 0.9) {
                    for (int band=0; band<bandsImage; band++){
                    	out.setSample(width, height, band, 255);
                    }
                }
            }
        }

        
    }
	
	/**
	 * Methode zur Berechnung der Standardabweichung
	 * @param widthImage Breite des Bildes
	 * @param heightImage Höhe des Bildes
	 * @param BandsImage Frequenz?
	 * @param source Das Bild
	 */
	private double standardDeviation(double widthImage, double heightImage, double BandsImage, Raster source) {
		double exp;
		standardDeviation=Math.sqrt((1/(widthImage*heightImage-1))*(analyseRaster(widthImage, heightImage, BandsImage, source, exp=2)-(1/(widthImage*heightImage))*Math.pow(analyseRaster(widthImage, heightImage, BandsImage, source, exp=1),2)));
		return standardDeviation;
	}
	
	/**
	 * Berechnet die Grauwerte der Pixel
	 * @param widthImage Breite des Bildes
	 * @param heightImage Höhe des Bildes
	 * @param bandsImage Frequenz?
	 * @param source Das Bild
	 * @param exp Exponent
	 * @return Grauwert der Pixel
	 */
	private double analyseRaster(double widthImage, double heightImage, double bandsImage, Raster source, double exp){
		double pixel=widthImage*heightImage;
		double grey=0;
		for(int i=1;i<=pixel;i++){
			grey=(double) (grey+Math.pow(source.getDataBuffer().getElem(i),exp));
			
		}
		//System.out.println(grey);
		return grey;
		
	}            	
	
	
	/**
	 * Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
}
