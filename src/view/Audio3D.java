package view;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.nio.ByteBuffer;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

import control.MainControl;
import data.FileLister;

/** Steuert die Audiosignale und gibt diese aus.
 */
public class Audio3D implements Observer {
	/** ArrayList mit den Audiodateien. */
	private static ArrayList<String> fileList = new ArrayList<String>();
	/** Pfad zu den Dateien. */
	private static final String PATH = "res/audio/";
	/** Das Observable. */
	private MainControl subject;
	/** Die AudioLibrary. */
	private static AL al;
	/** Buffer fuer die Audiodaten. */
    private static int[] buffer;
    /** Die Audioquellen. */
    private static int[] source;
    /** Die Audiopositionen. */
    private static List<float[]> sourcePos= new ArrayList<float[]>();
    /** Position der Buttons an der linken Seite. */
    private static float[] sourcePosBleft = {0.5f, 0.7f, 0.0f};
    /** Position der Buttons an der rechten Seite. */
    private static float[] sourcePosBright = {-0.5f, 0.7f, 0.0f};
    /** Geschwindigkeit der Audioquellen. */
    private static float[] sourceVel = { 0.0f, 0.0f, 0.0f };
    /** Position des Listeners. */
    private static float[] listenerPos = { 0.0f, 0.7f, 3.5f };
    /** Geschwindigkeit des Listeners. */
    private static float[] listenerVel = { 0.0f, 0.0f, 0.0f };
    /** Orientierung des Listeners. */
    private static float[] listenerOri = { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f };
    
    @Override
	public void update(Observable o, Object arg1) {
		if(subject==null)
			subject = (MainControl) o;
		if(arg1 instanceof String){
			String message=(String)arg1;
			if(message.equals("init")){
				init();
			}
			if(message.equals("buttonClickedLeft")){
				System.out.println(message);
				playButton(fileList.size());
			}
			if(message.equals("buttonClickedRight")){
				System.out.println(message);
				playButton(fileList.size()+1);
			}
			if(message.equals("play")){
				try {
				    Thread.sleep(1000);                 
				} catch(InterruptedException ex) {
					System.out.println("Fatal Error playing Sound!");
				    Thread.currentThread().interrupt();
				}
				play();
			}
			if(message.equals("end")){
				killAllData();
			}		
		}
	}

	/**
	 * Initialisert die Buttonsounds.
	 */
	private void initButtons() {
		al.alSourcei (source[fileList.size()], AL.AL_BUFFER,   buffer[fileList.size()]);
	    al.alSourcef (source[fileList.size()], AL.AL_PITCH,    1.0f         );
	    al.alSourcef (source[fileList.size()], AL.AL_GAIN,     1.0f         );
	    al.alSourcefv(source[fileList.size()], AL.AL_POSITION, sourcePosBleft    , 0);
	    al.alSourcefv(source[fileList.size()], AL.AL_VELOCITY, sourceVel    , 0);
	    al.alSourcei (source[fileList.size()], AL.AL_LOOPING,  AL.AL_FALSE      );
	    
	    al.alSourcei (source[fileList.size()+1], AL.AL_BUFFER,   buffer[fileList.size()+1]);
	    al.alSourcef (source[fileList.size()+1], AL.AL_PITCH,    1.0f         );
	    al.alSourcef (source[fileList.size()+1], AL.AL_GAIN,     1.0f         );
	    al.alSourcefv(source[fileList.size()+1], AL.AL_POSITION, sourcePosBright    , 0);
	    al.alSourcefv(source[fileList.size()+1], AL.AL_VELOCITY, sourceVel    , 0);
	    al.alSourcei (source[fileList.size()+1], AL.AL_LOOPING,  AL.AL_FALSE      );
	}

	/**
	 * initialisiert das Audio.
	 */
	private void init(){
		fileList = new FileLister(PATH, "wav").getFiles();
		buffer=new int[fileList.size()+2];
		source=new int[fileList.size()+2];
		for(int index = 0; index <= fileList.size()-1; index++){
	    	sourcePos.add(randomSource());
		}
		al = ALFactory.getAL();
	    ALut.alutInit();
	    al.alGetError();
	    if(loadALData() == AL.AL_FALSE) {
	        System.exit(1);    
	    }
	    setListenerValues();
	    al.alGenSources(source.length, source, 0);
	    
	    if (al.alGetError() != AL.AL_NO_ERROR) {
	        System.err.println("Error generating audio source.");
	        System.exit(1);
	    }
	    for(int index = 0; index <= fileList.size()-1; index++){
	    	addSource(index);
	    }
	    initButtons();
	}

	/**
     * laed die Audiodateien. 
     * @return ob erfolgreich.
     */
    private static int loadALData() {
        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];

        al.alGenBuffers(buffer.length, buffer, 0);
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
	        }
        for(int index = 0; index <= fileList.size()-1; index++){     
        	String file = PATH + fileList.get(index);
        	ALut.alutLoadWAVFile(file,format,data,size,freq,loop);
        	al.alBufferData(buffer[index],format[0],data[0],size[0],freq[0]);
        	
        }
        ALut.alutLoadWAVFile("res/audio_bt/277651__coral-island-studios__button-4.wav", format, data, size, freq, loop);
        al.alBufferData(buffer[fileList.size()], format[0], data[0], size[0], freq[0]);
        
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
        }
        return AL.AL_TRUE;
    }
    
    /**
	 * erstellt den Listener.
	 */
	private static void setListenerValues() {
	    al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
	    al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
	    al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
	}

	/**
	 * erstellt eine zufaellige Position der Quelle
	 * @return die zufaellige Position
	 */
	private float[] randomSource() {
		float minx=-5.0f;
		float maxx=5.0f;
		float miny=0.3f;
		float maxy=1.0f;
		float minz=3.5f;
		float maxz=10.0f;
		Random rand=new Random();
		float[] sourcePosTT= {rand.nextFloat()*(maxx-minx)+minx, rand.nextFloat()*(maxy-miny)+miny, rand.nextFloat()*(maxz-minz)+minz};
		return sourcePosTT;
	}

	/**
	 * fuegt die Quellen hinzu.
	 * @param index der Quelle.
	 */
	private static void addSource(int index) {
	    al.alSourcei (source[index], AL.AL_BUFFER,   buffer[index]);
	    al.alSourcef (source[index], AL.AL_PITCH,    1.0f         );
	    al.alSourcef (source[index], AL.AL_GAIN,     1.0f         );
	    al.alSourcefv(source[index], AL.AL_POSITION, sourcePos.get(index)    , 0);
	    al.alSourcefv(source[index], AL.AL_VELOCITY, sourceVel    , 0);
	    al.alSourcei (source[index], AL.AL_LOOPING,  AL.AL_FALSE      );   
	}

	/**
	 * zerstoert alle Quellen und Buffer.
	 */
	private static void killAllData() {
		for(int index = 0; index <= fileList.size(); index++){
	        al.alDeleteSources(1,source, 0);
	    }
	    al.alDeleteBuffers(fileList.size(), buffer, 0);
	    ALut.alutExit();
	}

	/**
	 * spielt eine zufaellige Quelle 
	 */
	private void play() {
		Random rand=new Random();
		int index=rand.nextInt(fileList.size()-1);
        al.alSourcePlay(source[index]);
	}

	/**
	 * Spielt den Buttonsound
	 * @param index der Quelle
	 */
	private void playButton(int index) {
		al.alSourcePlay(source[index]);
	}

}
