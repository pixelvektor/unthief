package control;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import data.Code;
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
	private final Code code;
	/** true solange das Spiel laeuft. */
	private boolean isRunning = true;

	
	public MainControl(Observer... observers) {
		for (Observer o : observers) {
			this.addObserver(o);
		}
		image = new Image();
		System.out.println("image erstellt");
		code = new Code();
		gameInit();
	}
	
	public void gameInit(){
		//testbild(image.getImage().get(image.getImage().size()-1));
		
		this.user = new User("Alice");
		
		Interference noise=new Noise(image.getImage().get(0));
		
		//System.out.println(noise.getImage().hashCode());
		
		image.getImage().add(noise.getImage());
		
		testbild(image.getImage().get(0));
		
		//System.out.println(image.getImage().get(0).hashCode()+"wat?");
		
		//System.out.println(image.getImage().get(1).hashCode()+"wut?");
		
		Filter denoise=new DeNoise(noise.getImage());
		
		//image.getImage().add(denoise.getImage());
		
		testbild(denoise.getImage());
		
		codeAnalyse();
		useInterference();
		gameStart();
	}
	
	public void testbild(BufferedImage images){
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(images)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
