package edu.mccc.cos210.fp2014.col.client;
import javax.swing.JFrame;
public class Collabolicious {

	public static void main(String[] args) {
		StartupView startupView = new StartupView(
            "bin/edu/mccc/cos210/fp2014/"+
            "col/client/assets/"+
            "Collabolicous_Splash_Screen.png",
            new JFrame(),
            2000
        );
	}

	public Collabolicious(){
		CollaboliciousFrame window = 
			new CollaboliciousFrame();
	}

}