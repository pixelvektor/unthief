package data;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Image{
	private final String file;
	private ArrayList<BufferedImage> image = new ArrayList<>();
	private String path = "res/images/";
	
	public Image(){
		file = new FileLister(path, "jpg").getRandomFile();
	}
	
	public Image(final String path) {
		this.path = path;
		file = new FileLister(path, "jpg").getRandomFile();
	}
	
	private void loadImage(){
		BufferedImage newImage = null;
		try {
			newImage = ImageIO.read(new FileInputStream(path + file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.add(newImage);
	}
	
	
	public ArrayList<BufferedImage> getImage(){
		loadImage();
		return image;
	}
}
