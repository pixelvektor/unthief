package control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import data.DeNoise;
import data.Filter;
import data.Images;
import data.Interference;
import data.Noise;
import data.User;
import javafx.scene.image.Image;

public class MainControl extends Observable{
	private User user;
	private ArrayList<Interference> interference = new ArrayList<>();
	private ArrayList<Filter> filter = new ArrayList<>();
	private ArrayList<BufferedImage> images = new ArrayList<>();
	//private Images images;
	/** true solange das Spiel laeuft. */
	private boolean isRunning = true;

	
	public MainControl(Observer... observers) {
		for (Observer o : observers) {
			this.addObserver(o);
		}
		gameInit();
	}
	
	public void gameInit(){
		this.user = new User("Alice");
		interference.add(new Noise());
		filter.add(new DeNoise());
		randomImage();
		randomCode();
		codeAnalyse();
		useInterference();
		gameStart();
	}
	
	public void gameStart(){
		play();
	}
	
	public void gameEnd(){
		isRunning=false;
	}
	
	public void codeAnalyse(){
		
	}
	
	public void useFilter(){
		
	}
	
	public void useInterference(){
		
	}
	
	public void play(){
		while(isRunning){
			
		}
	}
	
	public boolean checkFilter(){
		return true;
	}

	public void win(){
		
	}
	
	public void back(){
		if(images.size()>=2){
			images.remove(images.size()-1);
		}
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
	
	public void randomCode(){
		
	}
}
