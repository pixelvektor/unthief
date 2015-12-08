package data;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Image{
	/** Name der Datei */
	private final String file;
	/** ArrayList von Bildern */
	private ArrayList<BufferedImage> image = new ArrayList<>();
	/** Pfad zu den Bildern */
	private String path = "res/images/";
	
	public Image(){
		file = new FileLister(path, "jpg").getRandomFile();
		loadImage();
	}
	
	public Image(final String path) {
		this.path = path;
		file = new FileLister(path, "jpg").getRandomFile();
		loadImage();
	}
	
	/**
	 * Laed das Bild
	 */
	private void loadImage(){
		BufferedImage newImage = null;
		try {
			newImage = ImageIO.read(new FileInputStream(path + file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.add(newImage);
	}
	
	/**
	 * Getter fuer das Bild
	 * @return das Bild
	 */
	public ArrayList<BufferedImage> getImage(){
		return image;
	}
}
