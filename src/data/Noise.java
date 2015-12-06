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
		gaussianNoise();
	}
	
	/**
	 * Methode fuer Bildrauschen
	 */
	private void gaussianNoise() {
        //Raster source = image.getRaster();
        WritableRaster out = image.getRaster();
         int currentValue;                    // the current value
        double newValue;                  // the new "noisy" value
        double gaussianNumber;                // gaussian number
        int bandsImage  = out.getNumBands(); // number of bands
        int widthImage  = image.getWidth();  // width of the image
        int heightImage = image.getHeight(); // height of the image
        standardDeviation(widthImage,heightImage,bandsImage,out);
        System.out.println(standardDeviation);
        java.util.Random rand = new java.util.Random();
       
  
        for (int height=0; height<heightImage; height++) {
            for (int widght=0; widght<widthImage; widght++) {
                gaussianNumber = rand.nextGaussian();
                  
                for (int bands=0; bands<bandsImage; bands++) {
					newValue = standardDeviation * gaussianNumber;
                    currentValue = out.getSample(widght, height, bands);
                    newValue = newValue + currentValue;
                    if (newValue < 0){   
                    	newValue = 0.0;
                    }
                    if (newValue > 255){ 
                    	newValue = 255.0;
                    }
                    out.setSample(widght, height, bands, (int)(newValue));
                    
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
	private void standardDeviation(double widthImage, double heightImage, double BandsImage, Raster source) {
		double exp;
		standardDeviation=Math.sqrt((1/(widthImage*heightImage-1))*(analyseRaster(widthImage, heightImage, BandsImage, source, exp=2)-(1/(widthImage*heightImage))*Math.pow(analyseRaster(widthImage, heightImage, BandsImage, source, exp=1),2)));
		System.out.println(standardDeviation);
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
		System.out.println(grey);
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
