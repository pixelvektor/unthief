package unthief;

/** Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing I (Unthief)
 * (C) 2015 Kevin Otte, Adrian Schmidt, Fabian Schneider
 */

import java.util.Observer;

import control.MainControl;
import view.Audio3D;
import view.View3D;

/** Das Programm Unthief.
 */
public class Unthief{
	
	/** Startet das Programm.
	 * @param args Parameter werden ignoriert.
	 */
	public static void main(String[] args) {
		new Unthief();
	}
	
	/** Ctor fuer Unthief.
	 * Erstellt die Observer und das Observable.
	 */
	public Unthief() {
		Observer v3d = new View3D();
		Observer a3d = new Audio3D();
		
		new MainControl(v3d,a3d);
	}

}
