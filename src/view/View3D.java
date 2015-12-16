package view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import control.MainControl;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PointLight;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class View3D extends MouseAdapter implements Observer {
	private MainControl subject;
	private Canvas3D myCanvas;
	private TransformGroup tgCamera;
	private BufferedImage image;
	private Shape3D shape;
	private PickCanvas pickCanvas;
	private int init=0;
	private Appearance display = new Appearance();
	private Appearance unClicked = new Appearance();
	private Appearance clicked = new Appearance();
	private Appearance right = new Appearance();
	private Appearance wrong = new Appearance();
	private TextureUnitState textureUnitState[] = new TextureUnitState[1];
	private BranchGroup buttons;
	private ArrayList<Integer> buttonClicked= new ArrayList<Integer>();
	private ArrayList<Integer> buttonGreen= new ArrayList<Integer>();
	private Boolean win=false;
	private boolean active0=false;
	private boolean active1=true;
	private boolean active2=true;
	private boolean active3=true;
	private boolean active4=true;
	private boolean active5=true;

	@Override
	/**
	 * Aktualisiert die View.
	 */
	public void update(final Observable o, final Object arg) {
		if(subject==null)
			subject = (MainControl) o;
		if(arg instanceof BufferedImage){
			image=(BufferedImage) arg;
			if(init==1){
				changeDisplayTexture();
				if(!buttons.getChild(buttonClicked.get(buttonClicked.size()-1)).getName().equals("0")){
					Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-1));
					button.setAppearance(clicked);
					setActive(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-1)).getName()), false);
					setActive(0,false);
				}
			}
			if(init==0){
				init();
				init++;
			}
		}else if(arg instanceof Boolean){
			Boolean check=(Boolean) arg;
			if(check==false){
				Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-2));
				button.setAppearance(wrong);
				setActive(0,true);
				for(int ind=2;ind<=4;ind++){
					setActive(ind,false);
				}
			}else{
				Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-2));
				button.setAppearance(right);
				setActive(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-2)).getName()), false);
				setActive(0,false);
				buttonGreen.add(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-2)).getName()));
				subject.setGreen();
			}
		}else if(arg instanceof String){
			String message=(String) arg;
			if(message.equals("back")){
				Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-3));
				button.setAppearance(unClicked);
				
				if(!buttonGreen.contains(2)){
					setActive(2,true);	
				}
				if(!buttonGreen.contains(3)){
					setActive(3,true);	
				}
				if(!buttonGreen.contains(4)){
					setActive(4,true);	
				}
			}
			if(message.equals("win")){
				Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-1));
				button.setAppearance(right);
				win();
			}
			if(message.equals("help")){
				help();
			}
		}	
	}
	
	private void changeDisplayTexture() {
		TextureLoader loader= new TextureLoader(image);
		ImageComponent2D texImage = loader.getImage();
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,texImage.getWidth(), texImage.getHeight());
		texture.setImage(0, texImage);
		texture.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
	    texture.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
	    texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		textureUnitState[0].setTexture(texture);
	}

	private void help() {
		JFrame helpFrame = new JFrame();
		helpFrame.getContentPane().setLayout(new FlowLayout());
		helpFrame.getContentPane().add(new JLabel(new ImageIcon(loadImage("Hilfe.jpg"))));
		helpFrame.pack();
		helpFrame.setTitle("Help");
		helpFrame.setSize(800, 600);
		helpFrame.setVisible(true);	
	}

	/**
	 * Gibt Gewinnmitteilung aus.
	 */
	private void win() {
		win=true;
		JFrame winFrame = new JFrame();
		winFrame.getContentPane().setLayout(new FlowLayout());
		winFrame.getContentPane().add(new JLabel(new ImageIcon(loadImage("GameOver.jpg"))));
		winFrame.pack();
		winFrame.setTitle("Win");
		winFrame.setSize(800, 600);
		winFrame.setVisible(true);
		winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Initialisiert die View.
	 */
	private void init(){
		JFrame mainFrame=new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		myCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		
		SimpleUniverse universe = new SimpleUniverse(myCanvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		
		loadDisplayTexture();
		 
	    Color3f colClicked = new Color3f(1.0f, 1.0f, 1.0f);
		ColoringAttributes colAtClicked = new ColoringAttributes(colClicked, ColoringAttributes.NICEST);
		clicked.setColoringAttributes(colAtClicked);
		
	    Color3f colUnClicked = new Color3f(0.2f, 0.2f, 0.2f);
		ColoringAttributes colAtUnClicked = new ColoringAttributes(colUnClicked, ColoringAttributes.NICEST);
		unClicked.setColoringAttributes(colAtUnClicked);
	    
	    Color3f colRight = new Color3f(0.0f, 1.0f, 0.0f);
		ColoringAttributes colAtRight = new ColoringAttributes(colRight, ColoringAttributes.NICEST);
		right.setColoringAttributes(colAtRight);
	    
	    Color3f colWrong = new Color3f(1.0f, 0.0f, 0.0f);
		ColoringAttributes colAtWrong = new ColoringAttributes(colWrong, ColoringAttributes.NICEST);
		wrong.setColoringAttributes(colAtWrong);
	    
		createBody(universe);
		createDisplayCasing(universe);
		createDisplay(universe);
		buttons=createButton(universe);
		
	    pickCanvas= new PickCanvas(myCanvas, buttons);
		pickCanvas.setMode(PickCanvas.BOUNDS);
		myCanvas.addMouseListener((MouseListener) this);
	    
		createLights(universe);
		
		Transform3D tfCamera = new Transform3D();
		tfCamera.setTranslation(new Vector3f(0.0f, 0.7f, 3.5f));
		tgCamera = universe.getViewingPlatform().getViewPlatformTransform();
		tgCamera.setTransform(tfCamera);
		
		mainFrame.setTitle("Unthief");
		mainFrame.setSize(1280, 720);
		mainFrame.getContentPane().add("Center", myCanvas);
		mainFrame.setVisible(true);
	}

	private void loadDisplayTexture() {
		TextureLoader loader= new TextureLoader(image);
		ImageComponent2D image = loader.getImage();
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		texture.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
	    texture.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
	    texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f)); 
	    TextureAttributes texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.MODULATE);
	    textureUnitState[0]=new TextureUnitState(texture, texAttr, null);
	    textureUnitState[0].setCapability(TextureUnitState.ALLOW_STATE_WRITE);
	    display.setTextureUnitState(textureUnitState);
	    display.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_READ );
	    display.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE );
	}

	private void createDisplayCasing(SimpleUniverse universe) {
		Appearance displayCasing = new Appearance();
		TextureLoader loader= new TextureLoader(loadImage("Interface.jpg"));
		ImageComponent2D image = loader.getImage();
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		texture.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
	    texture.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
	    texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f)); 
	    TextureAttributes texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.MODULATE);
	    displayCasing.setTextureAttributes(texAttr);
	    displayCasing.setTexture(texture);
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		try
		{
			loadedScene = obj.load("res/obj/DisplayCasing.obj");
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}

		BranchGroup theScene = loadedScene.getSceneGroup();
		Shape3D shape = (Shape3D) theScene.getChild(0);
		shape.setAppearance(displayCasing);
		universe.addBranchGraph(theScene);	
		
	}

	/**
	 * Erstellt die Lichter der Szene.
	 * @param universe Das SimpleUniverse.
	 */
	private void createLights(SimpleUniverse universe){
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
		Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);
		Point3f lightDirection = new Point3f(0.0f, 0.9f, 2.0f);
		PointLight dLight = new PointLight(true, lightColor, lightDirection, lightDirection);
		dLight.setInfluencingBounds(bounds);
		
		BranchGroup lights = new BranchGroup();
		lights.addChild(dLight);
		universe.addBranchGraph(lights);
	}
	
	/**
	 * Erstellt den Grundkoerper.
	 * @param universe Das SimpleUniverse.
	 */
	private void createBody(SimpleUniverse universe){
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		try
		{
			loadedScene = obj.load("res/obj/Body.obj");
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}
		
		Background bg = new Background(new Color3f(0.0f,0.0f,0.0f));
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE);
		bg.setApplicationBounds(bounds);
		
		BranchGroup theScene = loadedScene.getSceneGroup();
		theScene.addChild(bg);
		universe.addBranchGraph(theScene);
	}
	
	/**
	 * Erstellt das Display.
	 * @param universe Das SimpleUniverse.
	 */
	private void createDisplay(SimpleUniverse universe){
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		try
		{
			loadedScene = obj.load("res/obj/DisplayTest.obj");
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}
		BranchGroup theScene = loadedScene.getSceneGroup();
		shape = (Shape3D) theScene.getChild(0);
		shape.setAppearance(display);
		universe.addBranchGraph(theScene);		
	}
	
	/**
	 * Erstellt die Buttons.
	 * @param universe Das SimpleUniverse.
	 * @return die Buttons.
	 */
	private BranchGroup createButton(SimpleUniverse universe){
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		try
		{
			loadedScene = obj.load("res/obj/Button.obj");
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}
		BranchGroup theScene = loadedScene.getSceneGroup();
		for(int i=0;i<6;i++){
		Shape3D shape = (Shape3D) theScene.getChild(i);
		shape.setName(Integer.toString(i));
		shape.setAppearance(unClicked);
		shape.setCapability(shape.ALLOW_APPEARANCE_WRITE);
		}
		universe.addBranchGraph(theScene);
		return theScene;		
	}
	
	/**
	 * Wird bei Mausklick ausgefuehrt.
	 */
	public void mouseClicked(MouseEvent e){
		if(win==false){
		    pickCanvas.setShapeLocation(e);
		    PickResult result = pickCanvas.pickClosest();
		    if(result == null){
		       System.out.println("Nothing picked");
		    }else{
		       Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);
		       String name= result.getNode(PickResult.SHAPE3D).getName();
		       if(getActive(Integer.parseInt(name))){
		    	   if(!name.equals("1")){
		    		   if(!name.equals("5")){
		    			   buttonClicked.add(Integer.parseInt(name));
		    		   }
		    	   }
			       if(s !=null){
				    	subject.play(name);
				    }else{
				    	System.out.println("Fatal Error!");
				    }
		       }
		    }   
		}
	}
	
	/**
	 * Aktiviert oder Deaktiviert den Button.
	 * @param id ID des Buttons.
	 * @param lever true oder false.
	 */
	private void setActive(int id,boolean lever) {
		if(id==0){
			active0=lever;
		}
		if(id==1){
			active1=lever;
		}
		if(id==2){
			active2=lever;
		}
		if(id==3){
			active3=lever;
		}
		if(id==4){
			active4=lever;
		}
		if(id==5){
			active5=lever;
		}
	}
	
	/**
	 * Getter f�r den Status des Buttons.
	 * @param id ID des Buttons.
	 * @return Der Status des Buttons.
	 */
	private boolean getActive(int id) {
		if(id==0){
			return active0;
		}
		if(id==1){
			return active1;
		}
		if(id==2){
			return active2;
		}
		if(id==3){
			return active3;
		}
		if(id==4){
			return active4;
		}
		if(id==5){
			return active5;
		}
		return true;
	}
	
	private BufferedImage loadImage(String name){
		BufferedImage newImage = null;
		try {
			newImage = ImageIO.read(new FileInputStream("res/action_images/"+name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newImage;
	}
}
