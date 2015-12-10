package data;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class DeNoise extends Filter {
	
	private BufferedImage image;
	int[] window=new int[9];
	
	public DeNoise(BufferedImage image){
		this.image=image;
		saltNPepperDeNoise();
	}

	private void saltNPepperDeNoise() {
		WritableRaster out = image.getRaster();
		double widthImage=image.getWidth();
		double heightImage=image.getHeight();
		int edgex=1;
		int edgey=1;
		
		for(int y=edgey; y<heightImage-edgey;y++){
			for(int x=edgex; x<widthImage-edgex;x++){
				int i=0;
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
				for (int band=0; band<out.getNumBands(); band++){
                	out.setSample(1, 1, band, 100);
				}
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
}
