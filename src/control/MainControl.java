package control;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

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

/** Observable, welches die Hauptsteuerung uebernimmt.
 */
public class MainControl extends Observable{
	/** Bild */
	private final Image image;
	/** Bild vor dem Blur. */
	private BufferedImage preBlur;
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
	 * Counter fuer die richtigen Buttons.
	 */
	public void setGreen() {
		green=green+1;
		
	}

	/**
	 * Definiert die Reaktion auf einen gedrueckten Button.
	 * @param button der gedrueckte Button.
	 */
	public void play(String button){
		if(button.equals("0")){
			setChanged();
			notifyObservers("buttonClickedRight");
			back();
			setChanged();
			notifyObservers(image.getImage().get(image.getImage().size()-1));
			setChanged();
			notifyObservers("play");
		}
		
		if(button.equals("1")){
			setChanged();
			notifyObservers("buttonClickedLeft");
			setChanged();
			notifyObservers("help");
			setChanged();
			notifyObservers("play");
		}
		
		if(button.equals("2")){
			setChanged();
			notifyObservers("buttonClickedRight");
			Filter increaseContrast= new IncreaseBrightness(copyImage(image.getImage().get(image.getImage().size()-1)));
			image.getImage().add(increaseContrast.getImage());
			right.add(checkFilter(increaseContrast.getID()));
			checkNotify();
		}
		
		if(button.equals("3")){
			setChanged();
			notifyObservers("buttonClickedRight");
			Filter deBlur= new DeBlur(copyImage(image.getImage().get(image.getImage().size()-1)), preBlur);
			image.getImage().add(deBlur.getImage());
			right.add(checkFilter(deBlur.getID()));
			checkNotify();
		}
		
		if(button.equals("4")){
			setChanged();
			notifyObservers("buttonClickedRight");
			Filter denoise=new DeNoise(copyImage(image.getImage().get(image.getImage().size()-1)));			
			image.getImage().add(denoise.getImage());
			right.add(checkFilter(denoise.getID()));
			checkNotify();
			
		}
		
		if(button.equals("5")){
			setChanged();
			notifyObservers("buttonClickedLeft");
			setChanged();
			notifyObservers("end");
			System.exit(0);
		}
		if(green==2){
			setChanged();
			notifyObservers("win");
		}
		
	}

	/**
	 * Kopiert Bilder.
	 * @param image das zu kopierende Bild.
	 * @return das kopierte Bild.
	 */
	private static BufferedImage copyImage(BufferedImage image){
		 ColorModel colorModel = image.getColorModel();
		 boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		 WritableRaster raster = image.copyData(null);
		 return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	}

	/** Benachrichtigt die Observer.
	 * - ueber die Richtigkeit des angewanten Filters
	 * - das veraenderte Bild
	 */
	private void checkNotify() {
		if(right.size()>2){
			setChanged();
			notifyObservers(right.get(right.size()-2));
		}
		if(right.get(right.size()-2)==true){
			setChanged();
			notifyObservers(image.getImage().get(image.getImage().size()-1));
		}
		setChanged();
		notifyObservers("play");
	}
	
	/**
	 * Initialisiert das Spiel.
	 */
	private void gameInit(){
		right.add(true);
		codeAnalyse();
		setChanged();
		notifyObservers(image.getImage().get(image.getImage().size()-1));
		setChanged();
		notifyObservers("init");
	}
		
	/**
	 * Analysiert den Code und legt die Reihenfolge fest.
	 */
	private void codeAnalyse(){
		for(int index=0;index<order.length;index++){
			if(order[index]==5){
				Interference reducedContrast= new ReducedBrightness(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(reducedContrast.getImage());
			}
			if(order[index]==7){
				Interference noise=new Noise(copyImage(image.getImage().get(image.getImage().size()-1)));
				image.getImage().add(noise.getImage());
			}
			if(order[index]==2){
				preBlur = copyImage(image.getImage().get(image.getImage().size()-1));
				Interference blur = new Blur(copyImage(preBlur));
				image.getImage().add(blur.getImage());
			}
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
					return true;
				}else{
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
}
