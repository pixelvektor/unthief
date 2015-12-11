package view;

import java.util.Observable;
import java.util.Observer;

import control.MainControl;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
	import javax.media.j3d.BoundingSphere;
	import javax.media.j3d.BranchGroup;
	import javax.media.j3d.Canvas3D;
	import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
	import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
	import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
	import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.sun.j3d.loaders.IncorrectFormatException;
	import com.sun.j3d.loaders.ParsingErrorException;
	import com.sun.j3d.loaders.Scene;
	import com.sun.j3d.loaders.objectfile.ObjectFile;
	import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

	public class View3D extends JFrame implements Observer{
		private MainControl subject;
		private Canvas3D myCanvas;
		private TransformGroup tgCamera;
		BufferedImage image;
		Shape3D shape;
		
//		public View3D() {
//			
//		}

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
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			myCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
			
			SimpleUniverse universe = new SimpleUniverse(myCanvas);
			universe.getViewingPlatform().setNominalViewingTransform();
			int up= TextureLoader.Y_UP;
			System.out.println(up);
			TextureLoader loader= new TextureLoader(image);
			//Texture texture = loader.getTexture();
			int format = ImageComponent.FORMAT_RGB;
			//ImageComponent2D twoD= new ImageComponent2D(format,image.getWidth(),image.getHeight());
			//twoD.set(image);
			ImageComponent2D image = loader.getImage();
			Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB,image.getWidth(), image.getHeight());
			texture.setImage(0, image);
			texture.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
		    texture.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
		    texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		    
		    System.out.println(image.getWidth());
		    System.out.println(texture.getWidth());
		    System.out.println(texture.getHeight());
		    Transform3D transform = new Transform3D();
		    transform.rotX(180);
		    TextureAttributes texAttr = new TextureAttributes();
		    //TextureAttributes.MODULATE,transform, new Color4f(0.0f,0.0f,0.0f,0.0f), TextureAttributes.ALLOW_TRANSFORM_WRITE
		    texAttr.setTextureMode(TextureAttributes.MODULATE);
		    //texAttr.setTextureTransform(transform);
		    
		    Appearance ap = new Appearance();
		    ap.setTexture(texture);
		    ap.setTextureAttributes(texAttr);
		  
		    //int object = TexCoordGeneration.EYE_LINEAR;
		    //int twoD = TexCoordGeneration.TEXTURE_COORDINATE_2;
		    //float[] coordT= new float[]{-0.440730f,-1.009965f, 1.299243f, 0.0f};
		    //float[] coord= new float[]{0.384715f,0.572089f, 1.452694f, 0.0f};
		    //Vector4f planeS = new Vector4f(coord);
		    //Vector4f planeT = new Vector4f(coordT);
		    //new TexCoordGeneration(object,twoD,planeS,planeT);
		    //ap.setTexCoordGeneration(new TexCoordGeneration(object,twoD,planeS,planeT));
		    
			createSceneGraph(universe, ap);
			createSceneGraphD(universe, ap);
			
			createLights(universe);
			
			OrbitBehavior ob = new OrbitBehavior(myCanvas);
			ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE));
			universe.getViewingPlatform().setViewPlatformBehavior(ob);
			
			// Kamera bewegen
			Transform3D tfCamera = new Transform3D();
			tfCamera.setTranslation(new Vector3f(0.0f, 0.7f, 10.0f));
			
			tgCamera = universe.getViewingPlatform().getViewPlatformTransform();
			tgCamera.setTransform(tfCamera);
			
			setTitle("Object Loader Demo");
			setSize(1280, 720);
			
			getContentPane().add("Center", myCanvas);
			
			setVisible(true);
		}
		
		private void createLights(SimpleUniverse universe)
		{
			BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
			Color3f lightColor = new Color3f(0.7f, 0.7f, 0.7f);
			Vector3f lightDirection = new Vector3f(0.0f, -0.2f, -0.3f);
			DirectionalLight dLight = new DirectionalLight(true, lightColor, lightDirection);
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
			
			Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
			BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE);
			bg.setApplicationBounds(bounds);
			
			// Objekt aus geladener Datei auslesen
			BranchGroup theScene = loadedScene.getSceneGroup();
			//shape = (Shape3D) theScene.getChild(0);
			//shape.setAppearance(ap);
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
			
			Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
			BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE);
			bg.setApplicationBounds(bounds);
			
			// Objekt aus geladener Datei auslesen
			BranchGroup theScene = loadedScene.getSceneGroup();
			shape = (Shape3D) theScene.getChild(0);
			shape.setAppearance(ap);
			theScene.addChild(bg);
			
			
			universe.addBranchGraph(theScene);
		}


}
