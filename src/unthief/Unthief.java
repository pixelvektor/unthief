package unthief;

import java.util.Observer;

import control.MainControl;
import view.Audio3D;
import view.View3D;

public class Unthief{
	
	public static void main(String[] args) {
		new Unthief();
	}
	
	public Unthief() {
		Observer v3d = new View3D();
		//Observer a3d = new Audio3D();
		
		new MainControl(v3d);
	}

}
