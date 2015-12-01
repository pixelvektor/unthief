package data;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Image{
	private ArrayList<String> fileList = new ArrayList<String>();
	private ArrayList<BufferedImage> image = new ArrayList<>();
	private static final String PATH = "res/images/";
	
	public Image(){
		fileList = new FileLister(PATH).getFiles();
	}
	
	private String randomImage(){
		Collections.shuffle(fileList);
		return fileList.get(0);
	}
	
	private void loadImage(){
		BufferedImage newImage = null;
		try {
			newImage = ImageIO.read(new FileInputStream(PATH + randomImage()));
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
