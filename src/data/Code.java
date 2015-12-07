package data;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Code extends Image{
	private static final String PATH = "res/codes/";
	private int[] order;
	
	public Code() {
		super(PATH);
		readOrder();
	}
	
	private void readOrder() {
		BufferedImage image = this.getImage().get(0);
		Raster rasterImg = image.getRaster();
		int width = image.getWidth();
		System.out.println("huhu");
		for (int i = 0; i < width; i++) {
			if (rasterImg.getDataBuffer().getElem(i) == 255)
				System.out.println(i);
		}
	}
	
	public int[] getOrder() {
		return order;
	}
}
