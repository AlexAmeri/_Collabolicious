package edu.mccc.cos210.fp2014.col.client.user;
import edu.mccc.cos210.fp2014.col.client.CollaboliciousFrame;
import edu.mccc.cos210.fp2014.col.client.network.NetworkManager;
import edu.mccc.cos210.fp2014.col.client.whiteboard.WhiteboardModel;
import edu.mccc.cos210.fp2014.col.client.UI.CustomBasicButtonUI;
import edu.mccc.cos210.fp2014.col.client.UI.CustomTextfieldUI;
import edu.mccc.cos210.fp2014.col.client.UI.CustomPasswordFieldUI;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import java.io.IOException;
/*
* Authenticates the user with the server
*/
public class LoginView extends JPanel{
    private static final long serialVersionUID = 1L;
	private CollaboliciousFrame window;
	private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JButton btnLogin;
    private JButton btnRegister;
	public LoginView(CollaboliciousFrame mainWindow) {
		window = mainWindow;
		show();
	}
	@SuppressWarnings("deprecation")
	public void show() {
		JPanel panel = new JPanel();
        try {
            String filename = 
                "../assets/Collabolicious_Name_Logo.png";
            BufferedImage logo = ImageIO.read(
                getClass().getResource(filename)
            );
            JLabel lblLogo = new JLabel(
                new ImageIcon(
                    resizeImage(logo)
                )
            );
            panel.add(lblLogo);
        } catch(IOException e){}
        window.getContentPane().add(
            panel, 
            BorderLayout.CENTER
        );
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.WHITE);
 
        JPanel txtInfo = new JPanel();
        txtInfo.setBackground(
            Color.WHITE
        );
        txtInfo.setLayout(
            new BoxLayout(txtInfo, BoxLayout.PAGE_AXIS)
        );
        txtInfo.add(
            Box.createRigidArea(
                new Dimension(0, 30)
            )
        );
        txtUsername = new JTextField(20);
        txtUsername.setUI(new CustomTextfieldUI());
        txtPassword = new JPasswordField(20);
        txtPassword.setUI(new CustomPasswordFieldUI());
        btnLogin = new JButton("Sign in");
        btnLogin.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doLoginAction(
                    txtUsername.getText(), 
                    txtPassword.getText()
                );
            }
        });
        txtInfo.add(txtUsername);
        txtInfo.add(
            Box.createRigidArea(
                new Dimension(0, 20)
            )
        );
        txtInfo.add(txtPassword);
        txtInfo.add(
            Box.createRigidArea(
                new Dimension(0, 20)
            )
        );
        btnLogin.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );
        txtInfo.add(btnLogin);
        txtInfo.add(
            Box.createRigidArea(
                new Dimension(0, 20)
            )
        );
        panel.add(txtInfo);

        JPanel bottomBar = new JPanel();
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setLayout(new BorderLayout());
        
        JPanel pRegister = new JPanel();
        pRegister.setBackground(
            Color.WHITE
        );
        pRegister.setBorder(
            new EmptyBorder(
                new Insets(0,20,0,0)
            )
        );
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doRegisterAction(
                    txtUsername.getText(), 
                    txtPassword.getText()
                );
            }
        });
        pRegister.setPreferredSize(
            new Dimension(105, 45)
        );
        pRegister.add(btnRegister);
        bottomBar.add(pRegister, BorderLayout.LINE_START);
        
        JPanel pContinue = new JPanel();
        pContinue.setBackground(Color.WHITE);
        pContinue.setBorder(
            new EmptyBorder(
                new Insets(0,0,0,20)
            )
        );
        JButton btnContinueWithoutLogin = new JButton("Continue Offline");
        btnContinueWithoutLogin.addActionListener(
        new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                window.showWhiteboardView(
                    new WhiteboardModel("New Whiteboard")
                );
            }
        });
        pContinue.setPreferredSize(
            new Dimension(145, 45)
        );
        pContinue.add(btnContinueWithoutLogin);
        bottomBar.add(pContinue, BorderLayout.LINE_END);

        window.getContentPane().add(
            bottomBar, 
            BorderLayout.PAGE_END
        );
        btnLogin.setUI(new CustomBasicButtonUI());
        btnRegister.setUI(new CustomBasicButtonUI());
        btnContinueWithoutLogin.setUI(new CustomBasicButtonUI());
		window.setVisible(true);
        window.setResizable(false);
        window.pack(); 
	}
	private void doRegisterAction(
        String userName, 
        String password) {
		try {
            if(!userName.equals("") && !password.equals("")) {
                boolean registerSuccess = 
                    this.window.getNetworkManager().
                        sendRegisterMessage(
                            userName, 
                            password
                        );
				if (registerSuccess) {
					this.window.getOnlineWhiteboardModels().clear();
					JFrame success = new JFrame();
					success.setLocationRelativeTo(null);
					success.add(new JLabel
								("Successful Register! Please login to start Collaborating!"));
					success.pack();
					success.setVisible(true);
				} else {
					JFrame error = new JFrame();
					error.setLocationRelativeTo(null);
					error.add(new JLabel
								("That Username / Password combo is invalid"));
					error.pack();
					error.setVisible(true);
				}
            } else {
                JFrame error = new JFrame();
                error.setLocationRelativeTo(null);
                error.add(new JLabel("No empty fields allowed"));
                error.pack();
                error.setVisible(true);
            }
		} catch (Exception e) {}
	}
	private void doLoginAction(
        String userName, 
        String password) {
		this.window.getOnlineWhiteboardModels().clear();
        if(!userName.equals("") && !password.equals("")) {
    		try {
    			NetworkManager n = this.window.getNetworkManager();
    			n.setCredentials(userName, password);
    			String loginSuccess = n.sendLoginMessage();
    			if (!loginSuccess.equals("")) {
					if (!loginSuccess.equals("0")) {
						String[] whiteboardNames = loginSuccess.split("[|]");
						for (int i = 0; i < whiteboardNames.length; i++) {
							String name = whiteboardNames[i];
							WhiteboardModel w = new WhiteboardModel(name);
							w.networked = true;
							this.window.getOnlineWhiteboardModels().add(w);
						}
					} else {
						JFrame error = new JFrame();
						error.setLocationRelativeTo(null);
						error.add(new JLabel("Login Failed, please try again"));
						error.pack();
						error.setVisible(true);
					}
    			}
    			this.window.showOnlineWhiteboardSelectionView();
    		} catch (Exception e) {}
        } else {
            JFrame error = new JFrame();
            error.setLocationRelativeTo(null);
            error.add(new JLabel("No empty fields allowed"));
            error.pack();
            error.setVisible(true);
        }
	}

    private BufferedImage resizeImage(
        BufferedImage image) {
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint (
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.dispose();
        return image;
    }
}