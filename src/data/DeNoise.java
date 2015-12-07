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
		
		double edgex=Math.round(3/2);
		double edgey=Math.round(3/2);
		for(double x=edgex; x<=widthImage-edgex;x++){
			for(double y=edgey; y<=heightImage-edgey;y++){
				int i=0;
				for(double fx=0; fx<=3;fx++){
					for(double fy=0; fy<=3;fy++){
						int numPix;
						if(y==1){
							numPix=(int) x;
						}
						else{
							numPix=(int) (((y-1)*widthImage)+x);
						}
						window[i]=image.getData().getDataBuffer().getElem(numPix);
						i=i+1;
					}
				}
				for(int index=0;index<window.length;index++){
					if(Arrays.asList(window).get(index).equals(255)){
						out.getDataBuffer().setElem(index, average());
					}
					if(Arrays.asList(window).get(index).equals(0)){						
						out.getDataBuffer().setElem(index, average());
					}
				}
			}
		}
	}
	
	private int average(){
		int average=0;
		for(int j=0;j<window.length;j++){
			average=average+window[j];
			}
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
