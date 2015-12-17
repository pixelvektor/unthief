package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

/** Ernfernt Bildrauschen mittels Medianfilter.
 */
public class DeNoise extends Filter {
	/** Das zu bearbeitende Bild. */
	private BufferedImage image;
	/** 3x3 Matrix. */
	int[] window=new int[9];
	/** ID des Filters */
	private int id=7;
	
	public DeNoise(BufferedImage image){
		this.image=image;
		saltNPepperDeNoise();
	}
	
	/**
	 * Erstellt einen Medianfilter.
	 */
	private void saltNPepperDeNoise() {
		WritableRaster out = image.getRaster();
		double widthImage=image.getWidth();
		double heightImage=image.getHeight();
		int edgex=1;
		int edgey=1;
		
		for(int y=edgey; y<heightImage-edgey;y++){
			for(int x=edgex; x<widthImage-edgex;x++){
				int i=0;
				//3x3-Matrix
				for(int fy=0; fy<3;fy++){
					for(int fx=0; fx<3;fx++){
						 if(fy==0){
							window[i]=out.getSample(x-1+fx, y-1, 1);
						}
						else if(fy==1){
							window[i]=out.getSample(x-1+fx, y, 1);
						}
						else if(fy==2){
							window[i]=out.getSample(x-1+fx, y+1, 1);
						}
						i=i+1;
					}
				}
				int middlex=x;
				int middley=y;
				//Bildstoerung
				if(window[4]==255){
					Arrays.sort(window);
					for (int band=0; band<out.getNumBands(); band++){
                    	out.setSample(middlex, middley, band, window[4]);
                    }
					
				}
				if(window[4]==0){
					Arrays.sort(window);
					for (int band=0; band<out.getNumBands(); band++){
                    	out.setSample(middlex, middley, band, window[4]);
                    }
				}
				
				window=new int[9];
				
				
				
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
	 * Getter fuer die ID
	 * @return die ID
	 */
	public int getID(){
		return id;
	}
	
	
}
