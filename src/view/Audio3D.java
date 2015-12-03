package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
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
    static int[] buffers;
    static int[] sources;
    static List<float[]> sourcePos= new ArrayList<float[]>();
    static float[] sourcePosT = { 0.0f, 0.0f, 0.0f };
    static float[] sourceVel = { 0.0f, 0.0f, 0.0f };
    static float[] listenerPos = { 0.0f, 0.0f, 0.0f };
    static float[] listenerVel = { 0.0f, 0.0f, 0.0f };
    static float[] listenerOri = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };

    
    static int loadALData() {

        //variables to load into

        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];
        
        // load wav data into buffers

        al.alGenBuffers(fileList.size(), buffers, 0);
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
            buffers[index],
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

        al.alGenSources(1, sources, 0);
        System.out.println("did");

        if (al.alGetError() != AL.AL_NO_ERROR) {
            System.err.println("Error generating audio source.");
            System.exit(1);
        }

        al.alSourcei (type, AL.AL_BUFFER,   buffers[type]);
        al.alSourcef (type, AL.AL_PITCH,    1.0f         );
        al.alSourcef (type, AL.AL_GAIN,     1.0f         );
        al.alSourcefv(type, AL.AL_POSITION, sourcePos.get(type)    , 0);
        al.alSourcefv(type, AL.AL_VELOCITY, sourceVel    , 0);
        al.alSourcei (type, AL.AL_LOOPING,  AL.AL_TRUE      );
        System.out.println(type+"test");

        
    }
    
    static void setListenerValues() {
        al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
    }

    static void killAllData() {
    	for(int index = 0; index <= fileList.size()-1; index++){
            al.alDeleteSources(1,sources, 0);
        }
        al.alDeleteBuffers(fileList.size()-1, buffers, 0);
        ALut.alutExit();
    }
    
    public Audio3D(){
    	fileList = new FileLister(PATH, "wav").getFiles();
    	System.out.println(fileList.size()-1);
    	buffers=new int[fileList.size()];
    	sources=new int[fileList.size()];
    	//System.out.println(sources[2]);
    	randomSource();
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
        al.alSourcePlay(sources[0]);
        //play();
        System.out.println("blub");
        killAllData();
        
    }
        
	private void randomSource() {
		for(int index = 0; index <= fileList.size()-1; index++){
        	sourcePos.add(sourcePosT);
        	System.out.println(sourcePos.get(index).toString());
        }		
	}

	private void play() {
		// TODO Auto-generated method stub
		//al.alSourcePlay(SOUND1);
	}

	@Override
	public void update(Observable o, Object arg1) {
		// TODO Auto-generated method stub
		if(subject==null)
			subject = (MainControl) o;
	}

}
