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
		//test();
	}

	private void test() {
		WritableRaster out = image.getRaster();
		for (int band=0; band<out.getNumBands(); band++){
        	//out.setSample(296, 296, band, 100);
			System.out.println(out.getSample(296, 296, band));
		}
		//System.out.println(out.getSample(296, 296, 1));
		//System.out.println(out.getDataBuffer().getElem(88209));
		
	}

	private void saltNPepperDeNoise() {
		WritableRaster out = image.getRaster();
		double widthImage=image.getWidth();
		double heightImage=image.getHeight();
		System.out.println("jo");
		int edgex=1;
		int edgey=1;
		
		for(int y=edgey; y<heightImage-edgey;y++){
			for(int x=edgex; x<widthImage-edgex;x++){
				int i=0;
				for(int fy=0; fy<3;fy++){
					for(int fx=0; fx<3;fx++){
						//int numPix=0;
						//if(fy==0&&y==1){
						//	numPix=(int) x-1+fx;
						//}
						 if(fy==0){
							//numPix=(int) (((y-1)*widthImage)+x-1+fx);
							window[i]=out.getSample(x-1+fx, y-1, 1);
						}
						else if(fy==1){
							//numPix=(int) (((y)*widthImage)+x-1+fx);
							window[i]=out.getSample(x-1+fx, y, 1);
						}
						else if(fy==2){
							//numPix=(int) (((y+1)*widthImage)+x-1+fx);
							window[i]=out.getSample(x-1+fx, y+1, 1);
						}
						//System.out.println(numPix);
						//window[i]=out.getDataBuffer().getElem(numPix);
						//System.out.println(out.getDataBuffer().getElem(numPix));
						i=i+1;
					}
				}
				//for(int index=0;index<window.length;index++){
				//Arrays.sort(window);
				//int average=window[4];
				int middlex=x;
				int middley=y;
				for (int band=0; band<out.getNumBands(); band++){
                	out.setSample(1, 1, band, 100);
				}
					if(window[4]==255){
						//System.out.println(window[4]+"start");
						average(window);
						//System.out.println(window[4]);
						for (int band=0; band<out.getNumBands(); band++){
	                    	out.setSample(middlex, middley, band, window[4]);
	                    	//System.out.println("twofivefive"+window[4]);
	                    	//System.out.println(out.getSample(middlex, middley, band));
	                    }
						
					}
					if(window[4]==0){
						//System.out.println(window[4]+"start");
						average(window);
						for (int band=0; band<out.getNumBands(); band++){
	                    	out.setSample(middlex, middley, band, window[4]);
	                    	//System.out.println("null"+window[4]);
	                    }
					}
				//}
				
				window=new int[9];
				
				
				
			}
		}
		//for(int i=0;i<widthImage*heightImage;i++){
		System.out.println(out.getSample(1, 1, 1));
			System.out.println(out.getDataBuffer().getElem(297));
		//}
	}
	
	private void average(int[] window){
		//int average=0;
		Arrays.sort(window);
		//for(int j=0;j<window.length;j++){
		//	average=average+window[j];
		//	System.out.println(window[j]);
		//	}
		//average=average/9;
		//average=window[4];
		//System.out.println("average"+average);
		//return average;
	}
	
	/**
	 * Getter fuer das bearbeitete Bild
	 * @return Das bearbeitete Bild
	 */
	public BufferedImage getImage(){
		return image;
	}
}
