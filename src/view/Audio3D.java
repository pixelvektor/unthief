package view;

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

public class Audio3D implements Observer {
	private static ArrayList<String> fileList = new ArrayList<String>();
	private static final String PATH = "res/audio/";
	private MainControl subject;
	static AL al;
    static int[] buffer;
    static int[] source;
    int[] sourceButton=new int[1];
    static List<float[]> sourcePos= new ArrayList<float[]>();
    static float[] sourcePosT = { 0.0f, 0.0f, 0.0f };
    static float[] sourceVel = { 0.0f, 0.0f, 0.0f };
    static float[] listenerPos = { 0.0f, 0.0f, 0.0f };
    static float[] listenerVel = { 0.0f, 0.0f, 0.0f };
    static float[] listenerOri = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };

    
    static int loadALData() {

        //variables to load into
    	System.out.println("load");
        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];
        
        // load wav data into buffers

        al.alGenBuffers(buffer.length, buffer, 0);
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
	        }
        System.out.println(buffer.length+"wat");
        for(int index = 0; index <= fileList.size()-1; index++){
        	System.out.println(index);
        	
        	System.out.println(buffer[index]);
        	String file = PATH + fileList.get(index);
        	System.out.println(file);
        	ALut.alutLoadWAVFile(file,format,data,size,freq,loop);
        	al.alBufferData(buffer[index],format[0],data[0],size[0],freq[0]);
        	System.out.println(PATH+fileList.get(index));
        	System.out.println(buffer[index]);
        	
        }
        
        if (al.alGetError() != AL.AL_NO_ERROR) {
        	System.out.println("wtf");
            return AL.AL_FALSE;
        }
        System.out.println("end");
        return AL.AL_TRUE;
    }
    
    static void addSource(int type) {
    	System.out.println("add");
        al.alGenSources(fileList.size(), source, 0);
        System.out.println("did");

        if (al.alGetError() != AL.AL_NO_ERROR) {
            System.err.println("Error generating audio source.");
            System.exit(1);
        }

        al.alSourcei (source[type], AL.AL_BUFFER,   buffer[type]);
        al.alSourcef (source[type], AL.AL_PITCH,    1.0f         );
        al.alSourcef (source[type], AL.AL_GAIN,     1.0f         );
        al.alSourcefv(source[type], AL.AL_POSITION, sourcePos.get(type)    , 0);
        al.alSourcefv(source[type], AL.AL_VELOCITY, sourceVel    , 0);
        al.alSourcei (source[type], AL.AL_LOOPING,  AL.AL_FALSE      );
        System.out.println(type+"test");

        
    }
    
    static void setListenerValues() {
        al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
    }

    static void killAllData() {
    	for(int index = 0; index <= fileList.size()-1; index++){
            al.alDeleteSources(1,source, 0);
        }
        al.alDeleteBuffers(fileList.size()-1, buffer, 0);
        ALut.alutExit();
    }
    
    private void init(){
    	fileList = new FileLister(PATH, "wav").getFiles();
    	System.out.println("huhu");
    	buffer=new int[fileList.size()];
    	source=new int[fileList.size()];
    	//System.out.println(sources[2]);
    	for(int index = 0; index <= fileList.size()-1; index++){
        	sourcePos.add(randomSource());
        	System.out.println(sourcePos.get(index));
    	}
    	al = ALFactory.getAL();
        ALut.alutInit();
        al.alGetError();
        System.out.println("bla1");
        if(loadALData() == AL.AL_FALSE) {
        	System.out.println("bla2");
            System.exit(1);    
        }
        System.out.println("bla3");
        setListenerValues();
        for(int index = 0; index <= fileList.size()-1; index++){
        	System.out.println("ha");
        	addSource(index);
        }
    }
        
	private float[] randomSource() {
		float min=0.0f;
		float max=5.0f;
		Random rand=new Random();
		float[] sourcePosTT= {rand.nextFloat()*(max-min)+min, rand.nextFloat()*(max-min)+min, rand.nextFloat()*(max-min)+min};
		return sourcePosTT;
	}

	private void play() {
		System.out.println("play");
        al.alSourcePlay(source[0]);
        System.out.println(source[0]);
	}

	@Override
	public void update(Observable o, Object arg1) {
		if(subject==null)
			subject = (MainControl) o;
		if(arg1 instanceof String){
			String message=(String)arg1;
			if(message.equals("init")){
				System.out.println(message);
				init();
				initButton();
			}
			if(message.equals("buttonClicked")){
				System.out.println(message);
				playButton();
			}
			if(message.equals("end")){
				killAllData();
			}		
		}
	}

	private void initButton() {
		int[] buffer=new int[1];
		float[] sourcePos = { 0.0f, 0.0f, 0.0f };
	    float[] sourceVel = { 0.0f, 0.0f, 0.0f };
	    int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];
	    al.alGenBuffers(1, buffer, 0);
	    ALut.alutLoadWAVFile("res/audio_bt/277651__coral-island-studios__button-4.wav", format, data, size, freq, loop);
        al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
	    al.alGenSources(1, sourceButton, 0);
        System.out.println("button");

        if (al.alGetError() != AL.AL_NO_ERROR) {
            System.err.println("Error generating audio source.");
            System.exit(1);
        }

        al.alSourcei (sourceButton[0], AL.AL_BUFFER,   buffer[0]);
        al.alSourcef (sourceButton[0], AL.AL_PITCH,    1.0f         );
        al.alSourcef (sourceButton[0], AL.AL_GAIN,     1.0f         );
        al.alSourcefv(sourceButton[0], AL.AL_POSITION, sourcePos    , 0);
        al.alSourcefv(sourceButton[0], AL.AL_VELOCITY, sourceVel    , 0);
        al.alSourcei (sourceButton[0], AL.AL_LOOPING,  AL.AL_FALSE      );
        
	}

	private void playButton() {
		al.alSourcePlay(sourceButton[0]);
	}

}
