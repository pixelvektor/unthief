package control;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import view.View3D;

public class MainControl extends Observable{
	private String state;
	
	public MainControl(Observer... observers) {
		for (Observer o : observers) {
			this.addObserver(o);
		}
		for (int i = 0; i < 5; i++) {
			setState("" + i);
		}
	}

	public String getState() {
		return state;
	}

	public void setState(final String state) {
		this.state = state;
		setChanged();
		notifyObservers();
	}
	
	public void gameInit(){
		
	}
	
	public void gameStart(){
		
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
	
	public void gameEnd(){
		
	}
}
