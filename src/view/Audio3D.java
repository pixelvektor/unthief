package view;

import java.util.Observable;
import java.util.Observer;
import java.nio.ByteBuffer;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

import control.MainControl;

public class Audio3D implements Observer {
	private static final String PATH = "res/audio/";
	private MainControl subject;
	static AL al;
	static final int NUM_BUFFERS = 1;
	static final int NUM_SOURCES = 1;
    static final int SOUND1 = 0;
    static int[] buffers = new int[NUM_BUFFERS];
    static int[] sources = new int[NUM_SOURCES];
    static float[] sourcePos1 = { 2.0f, 0.0f, -0.1f };
    static float[] sourceVel1 = { -1.0f, 0.0f, 0.0f };
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

        al.alGenBuffers(NUM_BUFFERS, buffers, 0);
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
	        }
        ALut.alutLoadWAVFile(
	            PATH + "file01.wav",
	            format,
	            data,
	            size,
	            freq,
	            loop);
        al.alBufferData(
            buffers[SOUND1],
            format[0],
            data[0],
            size[0],
            freq[0]);
       
        al.alGenSources(NUM_SOURCES, sources, 0);
    
        al.alSourcei(sources[SOUND1], AL.AL_BUFFER, buffers[SOUND1]);
        al.alSourcef(sources[SOUND1], AL.AL_PITCH, 1.0f);
        al.alSourcef(sources[SOUND1], AL.AL_GAIN, 1.0f);
        al.alSourcefv(sources[SOUND1], AL.AL_POSITION, sourcePos1, 0);
        al.alSourcefv(sources[SOUND1], AL.AL_POSITION, sourceVel1, 0);
        al.alSourcei(sources[SOUND1], AL.AL_LOOPING, AL.AL_FALSE);
        
        if (al.alGetError() != AL.AL_NO_ERROR) {
            return AL.AL_FALSE;
        }

        return AL.AL_TRUE;
    }
    
    static void setListenerValues() {
        al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
    }

    static void killAllData() {
        al.alDeleteBuffers(NUM_BUFFERS, buffers, 0);
        al.alDeleteSources(NUM_SOURCES, sources, 0);
        ALut.alutExit();
    }
    
    public Audio3D(){
    	al = ALFactory.getAL();
        ALut.alutInit();
        al.alGetError();
        
        if(loadALData() == AL.AL_FALSE) {
            System.exit(1);    
        }
        setListenerValues();
        
        play();
        
        killAllData();
        
    }
        
	private void play() {
		// TODO Auto-generated method stub
		al.alSourcePlay(SOUND1);
	}

	@Override
	public void update(Observable o, Object arg1) {
		// TODO Auto-generated method stub
		if(subject==null)
			subject = (MainControl) o;
	}

}
