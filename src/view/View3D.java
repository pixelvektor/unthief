package view;

import java.util.Observable;
import java.util.Observer;

import control.MainControl;

public class View3D implements Observer{
	private MainControl subject;
	
//	public View3D() {
//		
//	}

	@Override
	public void update(final Observable o, final Object arg) {
		if(subject==null)
			subject = (MainControl) o;
	}

}
