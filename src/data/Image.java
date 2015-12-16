package data;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/** Laedt ein Bild aus den Ressourcen.
 */
public class Image{
	/** Name der Datei */
	private final String file;
	/** ArrayList von Bildern */
	private ArrayList<BufferedImage> image = new ArrayList<>();
	/** Pfad zu den Bildern */
	private String path = "res/images/";
	
	/** Erstellt ein Bild fuer das Spiel.
	 */
	public Image(){
		file = new FileLister(path, "jpg").getRandomFile();
		loadImage();
	}
	
	/** Erstellt ein Bild aus einem vorgegebenen Verzeichnis.
	 * @param path Der Verzeichnispfad zum Bild.
	 */
	public Image(final String path) {
		this.path = path;
		file = new FileLister(path, "jpg").getRandomFile();
		loadImage();
	}
	
	/**
	 * Getter fuer das Bild
	 * @return das Bild
	 */
	public ArrayList<BufferedImage> getImage(){
		return image;
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
}
