package data;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Images{
	private ArrayList<String> link = new ArrayList<String>();
	private ArrayList<BufferedImage> images = new ArrayList<>();
	private String link1 = "res/pix/testbild.jpg";
	
	public Images(){
		link.add(link1);
	}
	
	public void randomImage(){
		try {
			loadImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadImage() throws IOException{
		BufferedImage image = ImageIO.read(new FileInputStream("res/pix/testbild.jpg"));
		images.add(image);
	}
	
	
	public ArrayList<BufferedImage> getImage(){
		return images;
	}
	
	
}
