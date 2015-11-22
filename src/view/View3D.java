package view;

import java.util.Observable;
import java.util.Observer;

import control.MainControl;

public class View3D implements Observer{
	private MainControl subject;
	private String state;
	
//	public View3D() {
//		
//	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public void update(final Observable o, final Object arg) {
		if(subject==null)
			subject = (MainControl) o;
		setState(subject.getState());
		System.out.println("V3D: " + getState());
	}

}
