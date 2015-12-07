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
    static List<float[]> sourcePos= new ArrayList<float[]>();
    static float[] sourcePosT = { 0.0f, 0.0f, 0.0f };
    //static float[] sourcePosTT = { 0.0f, 0.0f, 0.0f };
    static float[] sourceVel = { 0.0f, 0.0f, 0.0f };
    static float[] listenerPos = { 0.0f, 0.0f, 0.0f };
    static float[] listenerVel = { 0.0f, 0.0f, 0.0f };
    static float[] listenerOri = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };
    boolean playing=true;

    
    static int loadALData() {

        //variables to load into

        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];
        
        // load wav data into buffers

        al.alGenBuffers(fileList.size(), buffer, 0);
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
	        }
        
        for(int index = 0; index <= fileList.size()-1; index++){
        ALut.alutLoadWAVFile(
	            PATH + fileList.get(index),
	            format,
	            data,
	            size,
	            freq,
	            loop);
        al.alBufferData(
            buffer[index],
            format[0],
            data[0],
            size[0],
            freq[0]);
        }
        
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
        }

        return AL.AL_TRUE;
    }
    
    static void addSource(int type) {

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
    
    public Audio3D(){
    	fileList = new FileLister(PATH, "wav").getFiles();
    	System.out.println(fileList.size());
    	buffer=new int[fileList.size()];
    	source=new int[fileList.size()];
    	//System.out.println(sources[2]);
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
        for(int index = 0; index <= fileList.size()-1; index++){
        	System.out.println(index);
        	addSource(index);
        }

    	   play();
    	   long startTime = System.currentTimeMillis();
	        long totalElapsed = 0;
	      
	        //while (totalElapsed < 10000) {
	        //	totalElapsed = System.currentTimeMillis() - startTime;
	        //}
       //killAllData(); 
    }
        
	private float[] randomSource() {
		float min=0.0f;
		float max=5.0f;
		Random rand=new Random();
		float[] sourcePosTT= {rand.nextFloat()*(max-min)+min, rand.nextFloat()*(max-min)+min, rand.nextFloat()*(max-min)+min};
		return sourcePosTT;
	}

	private void play() {
		// TODO Auto-generated method stub
		//al.alSourcePlay(SOUND1);
		System.out.println("play");
        al.alSourcePlay(source[0]);
        System.out.println(source[0]);
        al.alSourcePlay(source[1]);
        System.out.println(source[1]);
        //playing=false;
	}

	@Override
	public void update(Observable o, Object arg1) {
		// TODO Auto-generated method stub
		if(subject==null)
			subject = (MainControl) o;
	}

}
