package control;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import data.DeNoise;
import data.Filter;
import data.Image;
import data.Interference;
import data.Noise;
import data.User;

public class MainControl extends Observable{
	private User user;
	private ArrayList<Interference> interference = new ArrayList<>();
	private ArrayList<Filter> filter = new ArrayList<>();
	
	private final Image image;
	/** true solange das Spiel laeuft. */
	private boolean isRunning = true;

	
	public MainControl(Observer... observers) {
		for (Observer o : observers) {
			this.addObserver(o);
		}
		image = new Image();
		gameInit();
	}
	
	public void gameInit(){
		this.user = new User("Alice");
		//interference.add(new Noise());
		//filter.add(new DeNoise());
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
			isRunning = false;
		}
	}
	
	public boolean checkFilter(){
		return true;
	}

	public void win(){
		
	}
	
	public void back(){
		if(image.getImage().size()>=2){
			image.getImage().remove(image.getImage().size()-1);
		}
	}
}
