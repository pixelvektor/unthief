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
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
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
			if(init==1){
				image=(BufferedImage) arg;
				TextureLoader loader= new TextureLoader(image);
				ImageComponent2D texImage = loader.getImage();
				Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,texImage.getWidth(), texImage.getHeight());
				texture.setImage(0, texImage);
				texture.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
			    texture.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
			    texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
				textureUnitState[0].setTexture(texture);
				
				if(!buttons.getChild(buttonClicked.get(buttonClicked.size()-1)).getName().equals("0")){
					Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-1));
					button.setAppearance(clicked);
					setActive(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-1)).getName()), false);
					setActive(0,false);
				}
			}
			if(init==0){
				image=(BufferedImage) arg;
				init();
				init++;
			}
		}else if(arg instanceof Boolean){
			Boolean check=(Boolean) arg;
			if(check==false){
				if(!buttons.getChild(buttonClicked.get(buttonClicked.size()-1)).getName().equals("0")){
					Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-2));
					button.setAppearance(wrong);
					setActive(0,true);
					for(int ind=2;ind==4;ind++){
						setActive(ind,false);
					}
				}
			}else{
				if(!buttons.getChild(buttonClicked.get(buttonClicked.size()-1)).getName().equals("0")){
					Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-2));
					button.setAppearance(right);
					setActive(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-2)).getName()), false);
					setActive(0,false);
					buttonGreen.add(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-2)).getName()));
				}
			}
		}else if(arg instanceof String){
			String message=(String) arg;
			if(message.equals("back")){
				Shape3D button=(Shape3D) buttons.getChild(buttonClicked.get(buttonClicked.size()-3));
				button.setAppearance(unClicked);
				setActive(Integer.parseInt(buttons.getChild(buttonClicked.get(buttonClicked.size()-3)).getName()),true);
				for(int ind=2;ind<=4;ind++){
						if(!buttonGreen.contains(ind)){
						setActive(ind,true);
					}
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
	
	private void help() {
		BufferedImage helpImage = null;
		try {
			helpImage = ImageIO.read(new FileInputStream("res/action_images/GameOver_2.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JFrame helpFrame = new JFrame();
		helpFrame.getContentPane().setLayout(new FlowLayout());
		helpFrame.getContentPane().add(new JLabel(new ImageIcon(helpImage)));
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
		BufferedImage win = null;
		try {
			win = ImageIO.read(new FileInputStream("res/action_images/GameOver_1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JFrame winFrame = new JFrame();
		winFrame.getContentPane().setLayout(new FlowLayout());
		winFrame.getContentPane().add(new JLabel(new ImageIcon(win)));
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
		JFrame frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		myCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		
		SimpleUniverse universe = new SimpleUniverse(myCanvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		
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
	    
	    Color3f colClicked = new Color3f(1.0f, 1.0f, 1.0f);
		ColoringAttributes colAtClicked = new ColoringAttributes(colClicked, ColoringAttributes.NICEST);
		clicked.setColoringAttributes(colAtClicked);
	    clicked.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_READ );
	    clicked.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE );
	    
	    Color3f colUnClicked = new Color3f(0.2f, 0.2f, 0.2f);
		ColoringAttributes colAtUnClicked = new ColoringAttributes(colUnClicked, ColoringAttributes.NICEST);
		unClicked.setColoringAttributes(colAtUnClicked);
		unClicked.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_READ );
		unClicked.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE );
	    
	    Color3f colRight = new Color3f(0.0f, 1.0f, 0.0f);
		ColoringAttributes colAtRight = new ColoringAttributes(colRight, ColoringAttributes.NICEST);
		right.setColoringAttributes(colAtRight);
	    right.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_READ );
	    right.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE );
	    
	    Color3f colWrong = new Color3f(1.0f, 0.0f, 0.0f);
		ColoringAttributes colAtWrong = new ColoringAttributes(colWrong, ColoringAttributes.NICEST);
		wrong.setColoringAttributes(colAtWrong);
	    wrong.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_READ );
	    wrong.setCapability( Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE );
	    
		createSceneGraph(universe);
		createSceneGraphDisplay(universe);
		buttons=createSceneGraphButton(universe);
		
	    pickCanvas= new PickCanvas(myCanvas, buttons);
		pickCanvas.setMode(PickCanvas.BOUNDS);
		myCanvas.addMouseListener((MouseListener) this);
	    
		createLights(universe);
		
		OrbitBehavior ob = new OrbitBehavior(myCanvas);
		ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE));
		universe.getViewingPlatform().setViewPlatformBehavior(ob);
		
		// Kamera bewegen
		Transform3D tfCamera = new Transform3D();
		tfCamera.setTranslation(new Vector3f(0.0f, 0.7f, 10.0f));
		
		tgCamera = universe.getViewingPlatform().getViewPlatformTransform();
		tgCamera.setTransform(tfCamera);
		
		frame.setTitle("Unthief");
		frame.setSize(1280, 720);
		frame.getContentPane().add("Center", myCanvas);
		
		frame.setVisible(true);
	}

	/**
	 * Erstellt die Lichter der Szene.
	 * @param universe Das SimpleUniverse.
	 */
	private void createLights(SimpleUniverse universe){
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
		Color3f lightColor = new Color3f(0.7f, 0.7f, 0.7f);
		Point3f lightDirection = new Point3f(0.0f, 0.9f, 1.5f);
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
	private void createSceneGraph(SimpleUniverse universe){
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		// Szene aus Datei einlesen
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
		
		// Objekt aus geladener Datei auslesen
		BranchGroup theScene = loadedScene.getSceneGroup();
		theScene.addChild(bg);
		universe.addBranchGraph(theScene);
	}
	
	/**
	 * Erstellt das Display.
	 * @param universe Das SimpleUniverse.
	 */
	private void createSceneGraphDisplay(SimpleUniverse universe){
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		// Szene aus Datei einlesen
		try
		{
			loadedScene = obj.load("res/obj/DisplayTest.obj");
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}
		// Objekt aus geladener Datei auslesen
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
	private BranchGroup createSceneGraphButton(SimpleUniverse universe){
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		// Szene aus Datei einlesen
		try
		{
			loadedScene = obj.load("res/obj/Button.obj");
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}
		// Objekt aus geladener Datei auslesen
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
		       System.out.println(name+getActive(Integer.parseInt(name))+"!!!!!!!!!!!!!!");
		       if(getActive(Integer.parseInt(name))){
		    	   if(!name.equals("1")||!name.equals("5")){
		    		   buttonClicked.add(Integer.parseInt(name));
		    	   }
			       if(s !=null){
				    	subject.play(name);
				    }else{
				    	System.out.println("ups!");
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
			active3=lever;
		}
		if(id==5){
			active3=lever;
		}
	}
	
	/**
	 * Getter für den Status des Buttons.
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
}
