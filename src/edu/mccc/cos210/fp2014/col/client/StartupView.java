package edu.mccc.cos210.fp2014.col.client;
import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
public class StartupView extends JWindow {
    private static final long serialVersionUID = 1L;
    public StartupView(
        String filename,
        Frame f, 
        int waitTime){
        super(f);
        JLabel lblLogo = new JLabel(
            new ImageIcon(
                filename
            )
        );
        getContentPane().add(
            lblLogo, 
            BorderLayout.CENTER
        );
        pack();
        Dimension screenSize =
          Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = lblLogo.getPreferredSize();
        this.setLocationRelativeTo(null);
        final int pause = waitTime;
        final Runnable closerRunner = new Runnable(){
            public void run(){
                setVisible(false);
                dispose();
                Collabolicious collabolicious = 
                    new Collabolicious();
            }
        };
        Runnable waitRunner = new Runnable() {
            public void run(){
                try {
                    Thread.sleep(pause);
                    SwingUtilities.invokeAndWait(
                        closerRunner
                    );
                } catch(Exception e) {}
            }
        };
        setVisible(true);
        Thread splashThread = new Thread(
            waitRunner, 
            "SplashThread"
        );
        splashThread.start();
    }
}