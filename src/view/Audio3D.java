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
    static List sources = new ArrayList();
    static float[] sourcePos = { 0.0f, 0.0f, 0.0f };
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
        int[] source = new int[1];

        al.alGenSources(1, source, 0);

        if (al.alGetError() != AL.AL_NO_ERROR) {
            System.err.println("Error generating audio source.");
            System.exit(1);
        }

        al.alSourcei (source[0], AL.AL_BUFFER,   buffers[type]);
        al.alSourcef (source[0], AL.AL_PITCH,    1.0f         );
        al.alSourcef (source[0], AL.AL_GAIN,     1.0f         );
        al.alSourcefv(source[0], AL.AL_POSITION, sourcePos    , 0);
        al.alSourcefv(source[0], AL.AL_VELOCITY, sourceVel    , 0);
        al.alSourcei (source[0], AL.AL_LOOPING,  AL.AL_TRUE      );

        al.alSourcePlay(source[0]);

        sources.add(new Integer(source[0]));
    }
    
    static void setListenerValues() {
        al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
    }

    static void killAllData() {
    	Iterator iter = sources.iterator();
        while(iter.hasNext()) {
            al.alDeleteSources(1, new int[] { ((Integer)iter.next()).intValue() }, 0);
        }
        sources.clear();
        al.alDeleteBuffers(fileList.size()-1, buffers, 0);
        ALut.alutExit();
    }
    
    public Audio3D(){
    	fileList = new FileLister(PATH, "wav").getFiles();
    	System.out.println(fileList.size()-1);
    	System.out.println("blub1");
    	buffers=new int[fileList.size()];
    	al = ALFactory.getAL();
        ALut.alutInit();
        al.alGetError();
        System.out.println("blub2");
        if(loadALData() == AL.AL_FALSE) {
        	System.out.println("blub5");
            System.exit(1);    
        }
        System.out.println("blub3");
        setListenerValues();
        System.out.println("blub4");
        for(int index = 0; index <= fileList.size()-1; index++){
        	addSource(index);
        }
        
        //play();
        System.out.println("blub");
        killAllData();
        
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
