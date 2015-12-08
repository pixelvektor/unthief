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
		// TODO Auto-generated method stub
		WritableRaster out = image.getRaster();
		double widthImage=image.getWidth();
		double heightImage=image.getHeight();
		System.out.println("jo");
		int edgex=Math.round(3/2);
		int edgey=Math.round(3/2);
		
		for(int y=edgey; y<=heightImage-edgey;y++){
			for(int x=edgex; x<=widthImage-edgex;x++){
				int i=0;
				for(double fy=0; fy<3;fy++){
					for(double fx=0; fx<3;fx++){
						int numPix;
						if(y==1){
							numPix=(int) x;
						}
						else{
							numPix=(int) (((y-1)*widthImage)+x);
						}
						//System.out.println(numPix);
						window[i]=out.getDataBuffer().getElem(numPix);
						System.out.println(out.getDataBuffer().getElem(numPix));
						i=i+1;
					}
				}
				//for(int index=0;index<window.length;index++){
					if(window[4]==255){
						for (int band=0; band<out.getNumBands(); band++){
	                    	out.setSample(x, y, band, average());
	                    }
						
					}
					if(window[4]==0){						
						for (int band=0; band<out.getNumBands(); band++){
	                    	out.setSample(x, y, band, average());
	                    }
					}
				//}
				
				window=new int[9];
				
				
			}
		}
	}
	
	private int average(){
		int average=0;
		for(int j=0;j<window.length;j++){
			average=average+window[j];
			}
		average=average/9;
		//System.out.println(average);
		return average;
	}
	
	/**
	 * Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
}
