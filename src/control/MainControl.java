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
import data.DeBlur;
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
	private int order[];
	private ArrayList<Boolean> right= new ArrayList<>();
	private int green=0;
	private boolean active1=true;
	private boolean active2=true;
	private boolean active3=true;

	
	public MainControl(Observer... observers) {
		for (Observer o : observers) {
			this.addObserver(o);
		}
		image = new Image();
		System.out.println("image erstellt");
		code = new Code();
		order=code.getOrder();
		gameInit();
	}
	
	public void gameInit(){
		this.user = new User("Alice");
		right.add(true);
		codeAnalyse();
		setChanged();
		notifyObservers(image.getImage().get(image.getImage().size()-1));
	}
	
	public void testbild(BufferedImage images){
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(images)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void gameEnd(){
		isRunning=false;
	}
	
	public void codeAnalyse(){
		for(int index=0;index<order.length;index++){
			System.out.println(order[index]);
			if(order[index]==5){
				Interference reducedContrast= new ReducedContrast(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(reducedContrast.getImage());
			}
			if(order[index]==7){
				Interference noise=new Noise(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(noise.getImage());
			}
			if(order[index]==2){
				//Interference blur = new Blur(copyImage(image.getImage().get(image.getImage().size()-1)));
				//image.getImage().add(blur.getImage());
			}
		}
	}
	
	static BufferedImage copyImage(BufferedImage image){
		 ColorModel colorModel = image.getColorModel();
		 boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		 WritableRaster raster = image.copyData(null);
		 return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
		}
	
	public void play(String button){
		if(button.equals("2")){
			if(active2==true){
				Filter denoise=new DeNoise(copyImage(image.getImage().get(image.getImage().size()-1)));			
				image.getImage().add(denoise.getImage());
				right.add(checkFilter(denoise.getID()));
				System.out.println(right.size()-2);
				if(right.size()>2){
					setChanged();
					notifyObservers(right.get(right.size()-2));
				}
				if(right.get(right.size()-2)==true){
					setChanged();
					notifyObservers(image.getImage().get(image.getImage().size()-1));
				}
			}
		}
		
		if(button.equals("1")){
			if(active1==true){
				Filter increaseContrast= new IncreaseContrast(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(increaseContrast.getImage());
				right.add(checkFilter(increaseContrast.getID()));
				if(right.size()>2){
					setChanged();
					notifyObservers(right.get(right.size()-2));
				}
				if(right.get(right.size()-2)==true){
					setChanged();
					notifyObservers(image.getImage().get(image.getImage().size()-1));
				}
			}
		}
		
		if(button.equals("3")){
			if(active3==true){
				Filter deBlur= new DeBlur(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(deBlur.getImage());
				right.add(checkFilter(deBlur.getID()));
				if(right.size()>2){
					setChanged();
					notifyObservers(right.get(right.size()-2));
				}
				if(right.get(right.size()-2)==true){
					setChanged();
					notifyObservers(image.getImage().get(image.getImage().size()-1));
				}
			}
		}
		
		if(button.equals("0")){
			back();
			setChanged();
			notifyObservers(image.getImage().get(image.getImage().size()-1));
		}
		if(green==3){
			System.out.println("gewonnen");
			setChanged();
			notifyObservers("win");
		}
	}
	
	public boolean checkFilter(int id){
		System.out.println(image.getImage().size());
		for(int index=0; index<3;index++){
			if(image.getImage().size()-4==index){
				System.out.println(order[2-index]+"bla");
				System.out.println(id);
				if(order[2-index]==id){
					System.out.println("testBestanden");
					green=green+1;
					setActive(id,false);
					return true;
				}else{
					System.out.println("testNichtBestanden");
					return false;
				}
			}
		}	
		return false;
	}

	private void setActive(int id,boolean lever) {
		if(id==5){
			active1=lever;
		}
		
		if(id==7){
			active2=lever;
		}
		
		if(id==2){
			active3=lever;
		}
	}

	public void win(){
		
	}
	
	public void back(){
		for(int i=0;i<2;i++){
			if(image.getImage().size()>3){
				image.getImage().remove(image.getImage().size()-1);	
			}		
		}
		right.clear();
		right.add(true);
		setChanged();
		notifyObservers("back");
	}
}
