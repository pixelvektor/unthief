package data;


import java.awt.image.*;

public class Noise extends Interference {
	/** Name der Stoerung*/
	private static final String NAME = "Noise";
	/** Das zu bearbeitende Bild*/
	private BufferedImage image;
	
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
        WritableRaster out = image.getRaster();
        int bandsImage  = out.getNumBands();
        int widthImage  = image.getWidth();
        int heightImage = image.getHeight();
      
        double rand;
        java.util.Random randGen = new java.util.Random();
          
        for (int height=0; height<heightImage; height++) {
            for (int width=0; width<widthImage; width++) {
                rand = randGen.nextDouble();
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
}
