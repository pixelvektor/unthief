package view;

import java.util.Observable;
import java.util.Observer;

import control.MainControl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PointLight;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
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
	BufferedImage image;
	Shape3D shape;
	private PickCanvas pickCanvas;
	

	@Override
	public void update(final Observable o, final Object arg) {
		if(subject==null)
			subject = (MainControl) o;
		image=(BufferedImage) arg;
		init();
	}

	
	public View3D(){
		
		
	}
	
	public void init(){
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
	    
	    Appearance ap = new Appearance();
	    ap.setTexture(texture);
	    ap.setTextureAttributes(texAttr);
	    
		createSceneGraph(universe, ap);
		createSceneGraphD(universe, ap);
		
	    
	    pickCanvas= new PickCanvas(myCanvas, createSceneGraphButton(universe, ap,"Button.obj"));
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
		
		frame.setTitle("Object Loader Demo");
		frame.setSize(1280, 720);
		frame.getContentPane().add("Center", myCanvas);
		
		frame.setVisible(true);
	}
	
	private void createLights(SimpleUniverse universe)
	{
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
		Color3f lightColor = new Color3f(0.7f, 0.7f, 0.7f);
		Point3f lightDirection = new Point3f(0.0f, 0.9f, 1.5f);
		PointLight dLight = new PointLight(true, lightColor, lightDirection, lightDirection);
		dLight.setInfluencingBounds(bounds);
		
		BranchGroup lights = new BranchGroup();
		lights.addChild(dLight);
		
		universe.addBranchGraph(lights);
	}

	private void createSceneGraph(SimpleUniverse universe, Appearance ap)
	{
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
	
	private void createSceneGraphD(SimpleUniverse universe, Appearance ap)
	{
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
		shape.setAppearance(ap);
		universe.addBranchGraph(theScene);
		
		
	}
	
	private BranchGroup createSceneGraphButton(SimpleUniverse universe, Appearance ap,String name)
	{
		ObjectFile obj = new ObjectFile();
		Scene loadedScene = null;
		
		// Szene aus Datei einlesen
		try
		{
			loadedScene = obj.load("res/obj/"+name);
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e)
		{
			e.printStackTrace();
		}
		// Objekt aus geladener Datei auslesen
		BranchGroup theScene = loadedScene.getSceneGroup();
		//shape = (Shape3D) theScene.getChild(0);
		universe.addBranchGraph(theScene);
		return theScene;
		
	}
	
	
	public void mouseClicked(MouseEvent e)

	{

	    pickCanvas.setShapeLocation(e);

	    PickResult result = pickCanvas.pickClosest();

	    if (result == null) {

	       System.out.println("Nothing picked");

	    } else {

	       Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);
	       if(s !=null){
		    	System.out.println("juhu!");
		    	System.out.println(s.hashCode());
		    }else{
		    	System.out.println("ups!");
		    }
	    }
	    

	}
}
