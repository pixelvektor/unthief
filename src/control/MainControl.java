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
import data.IncreaseBrightness;
import data.Interference;
import data.Noise;
import data.ReducedBrightness;
import data.User;

public class MainControl extends Observable{
	private User user;
	private ArrayList<Interference> interference = new ArrayList<>();
	private ArrayList<Filter> filter = new ArrayList<>();
	/** Bild */
	private final Image image;
	/** true solange das Spiel laeuft. */
	private boolean isRunning = true;
	/** Array der Reihenfolge der Stoerungen. */
	private int order[];
	/** ArrayList mit Ergebnissen von checkFilter(). */
	private ArrayList<Boolean> right= new ArrayList<>();
	/** Richtige Filter. */
	private int green=0;
	
	/** 
	 * C-tor 
	 * @param observers Beobachter
	 */
	public MainControl(Observer... observers) {
		for (Observer o : observers) {
			this.addObserver(o);
		}
		image = new Image();
		order= new Code().getOrder();
		gameInit();
	}
	
	/**
	 * Initialisiert das Spiel.
	 */
	private void gameInit(){
		this.user = new User("Alice");
		right.add(true);
		codeAnalyse();
		setChanged();
		notifyObservers(image.getImage().get(image.getImage().size()-1));
		setChanged();
		notifyObservers("init");
	}
	
	private void testbild(BufferedImage images){
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(images)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void gameEnd(){
		isRunning=false;
	}
	
	/**
	 * Analysiert den Code und legt die Reihenfolge fest.
	 */
	private void codeAnalyse(){
		for(int index=0;index<order.length;index++){
			System.out.println(order[index]);
			if(order[index]==5){
				Interference reducedContrast= new ReducedBrightness(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(reducedContrast.getImage());
			}
			if(order[index]==7){
				Interference noise=new Noise(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(noise.getImage());
			}
			if(order[index]==2){
				Interference blur = new Blur(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(blur.getImage());
			}
		}
	}
	
	/**
	 * Kopiert Bilder.
	 * @param image das zu kopierende Bild.
	 * @return das kopierte Bild.
	 */
	static BufferedImage copyImage(BufferedImage image){
		 ColorModel colorModel = image.getColorModel();
		 boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		 WritableRaster raster = image.copyData(null);
		 return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
		}
	
	/**
	 * Definiert die Reaktion auf einen gedrueckten Button.
	 * @param button der gedrueckte Button.
	 */
	public void play(String button){
		if(button.equals("0")){
			setChanged();
			notifyObservers("buttonClicked");
			back();
			setChanged();
			notifyObservers(image.getImage().get(image.getImage().size()-1));
			setChanged();
			notifyObservers("play");
		}
		
		if(button.equals("1")){
			setChanged();
			notifyObservers("buttonClicked");
			setChanged();
			notifyObservers("help");
			setChanged();
			notifyObservers("play");
		}
		
		if(button.equals("2")){
			setChanged();
			notifyObservers("buttonClicked");
			Filter increaseContrast= new IncreaseBrightness(copyImage(image.getImage().get(image.getImage().size()-1)));
			image.getImage().add(increaseContrast.getImage());
			right.add(checkFilter(increaseContrast.getID()));
			System.out.println(right.get(right.size()-2)+"!!!2");
			if(right.size()>2){
				setChanged();
				notifyObservers(right.get(right.size()-2));
			}
			if(right.get(right.size()-2)==true){
				System.out.println("xx2");
				setChanged();
				notifyObservers(image.getImage().get(image.getImage().size()-1));
			}
			setChanged();
			notifyObservers("play");
		}
		
		if(button.equals("3")){
			System.out.println("3");
			setChanged();
			notifyObservers("buttonClicked");
			Filter deBlur= new DeBlur(copyImage(image.getImage().get(image.getImage().size()-1)));
			image.getImage().add(deBlur.getImage());
			System.out.println(deBlur.getID()+"id");
			right.add(checkFilter(deBlur.getID()));
			System.out.println(right.get(right.size()-2)+"!!!3");
			if(right.size()>2){
				setChanged();
				notifyObservers(right.get(right.size()-2));
			}
			if(right.get(right.size()-2)==true){
				System.out.println("xx3");
				setChanged();
				notifyObservers(image.getImage().get(image.getImage().size()-1));
			}
			setChanged();
			notifyObservers("play");
		}
		
		if(button.equals("4")){
			setChanged();
			notifyObservers("buttonClicked");
			Filter denoise=new DeNoise(copyImage(image.getImage().get(image.getImage().size()-1)));			
			image.getImage().add(denoise.getImage());
			right.add(checkFilter(denoise.getID()));
			System.out.println(right.get(right.size()-2)+"!!!!4");
			if(right.size()>2){
				setChanged();
				notifyObservers(right.get(right.size()-2));
			}
			if(right.get(right.size()-2)==true){
				System.out.println("xx4");
				setChanged();
				notifyObservers(image.getImage().get(image.getImage().size()-1));
			}
			setChanged();
			notifyObservers("play");
			
		}
		
		if(button.equals("5")){
			setChanged();
			notifyObservers("buttonClicked");
			setChanged();
			notifyObservers("end");
			System.exit(0);
		}
		if(green==2){
			System.out.println("gewonnen");
			setChanged();
			notifyObservers("win");
		}
		
	}
	
	/**
	 * Ueberprueft ob die Filter in der richtigen Reihenfolge angewandt worden sind. 
	 * @param id ID des Filters.
	 * @return true, wenn in der richtigen Reihenfolge.
	 * @return false, sonst.
	 */
	private boolean checkFilter(int id){
		for(int index=0; index<3;index++){
			if(image.getImage().size()-5==index){
				if(order[2-index]==id){
					System.out.println("testBestanden");
					return true;
				}else{
					System.out.println("testNichtBestanden");
					return false;
				}
			}
		}	
		return false;
	}

	/**
	 * Einen Schritt zurueck, bei falscher Eingabe.
	 */
	private void back(){
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

	public void setGreen() {
		green=green+1;
		
	}
}
