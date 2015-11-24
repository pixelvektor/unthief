package control;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import data.DeNoise;
import data.Filter;
import data.Images;
import data.Interference;
import data.Noise;
import data.User;

public class MainControl extends Observable{
	private User user;
	private ArrayList<Interference> interference = new ArrayList<>();
	private ArrayList<Filter> filter = new ArrayList<>();
	private Images images;
	
	
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
	}
	
	public void gameStart(){
		
	}
	
	public void gameEnd(){
		
	}
	
	public void codeAnalyse(){
		
	}
	
	public void useFilter(){
		
	}
	
	public void useInterference(){
		
	}
	
	public void play(){
		
	}
	
	public void checkFilter(){
		
	}

	public void win(){
		
	}
	
	public void back(){
		
	}
	
	public void randomImage(){
		
	}
	
	public void randomCode(){
		
	}
}
