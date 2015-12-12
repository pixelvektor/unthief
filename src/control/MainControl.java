package control;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import data.Blur;
import data.Code;
import data.DeNoise;
import data.Filter;
import data.Image;
import data.IncreaseContrast;
import data.Interference;
import data.Noise;
import data.ReducedContrast;
import data.User;

public class MainControl extends Observable{
	private User user;
	private ArrayList<Interference> interference = new ArrayList<>();
	private ArrayList<Filter> filter = new ArrayList<>();
	private final Image image;
	private final Code code;
	/** true solange das Spiel laeuft. */
	private boolean isRunning = true;
	private boolean buttonClicked=false;

	
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
		this.user = new User("Alice");
		codeAnalyse();
		useInterference();
		setChanged();
		notifyObservers(image.getImage().get(image.getImage().size()-1));
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
		
	}
	
	public void gameEnd(){
		isRunning=false;
	}
	
	public void codeAnalyse(){
		
	}
	
	public void useFilter(){
		
	}
	
	public void useInterference(){
		Interference reducedContrast= new ReducedContrast(copyImage(image.getImage().get(image.getImage().size()-1)));
		image.getImage().add(reducedContrast.getImage());
		
		Interference noise=new Noise(copyImage(image.getImage().get(image.getImage().size()-1)));
		image.getImage().add(noise.getImage());
		
		//Interference blur = new Blur(copyImage(image.getImage().get(image.getImage().size()-1)));
		//image.getImage().add(blur.getImage());
	}
	
	static BufferedImage copyImage(BufferedImage image) {
		 ColorModel colorModel = image.getColorModel();
		 boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		 WritableRaster raster = image.copyData(null);
		 return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
		}
	
	public void play(String button){
		System.out.println("clicked");
		if(button.equals("Button2")){
		Filter denoise=new DeNoise(copyImage(image.getImage().get(image.getImage().size()-1)));			
		image.getImage().add(denoise.getImage());
		}
		
		if(button.equals("Button1")){
			Filter increaseContrast= new IncreaseContrast(copyImage(image.getImage().get(image.getImage().size()-1)));
			image.getImage().add(increaseContrast.getImage());
		}
		if(button.equals("Button0")){
			back();
		}
		setChanged();
		notifyObservers(image.getImage().get(image.getImage().size()-1));
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
	
	public BufferedImage getImage(){
		return image.getImage().get(0);
		
	}
}
