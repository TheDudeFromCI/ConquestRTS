package wraithaven.conquest.client;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wraithaven.conquest.client.GameWorld.FadeListener;
import wraithaven.conquest.client.GameWorld.FadeTimer;
import wraithaven.conquest.client.GameWorld.WindowUtil.ImageWindow;

public class SplashScreen extends ImageWindow{
	public static final int BUTTON_HEIGHT = 40;
	public static final int BUTTON_WIDTH = 83;
	public static final int BUTTON_Y = 151;
	public static final int PLAY_BUTTON_X = 504;
	public static final int SIGN_UP_BUTTON_X = 411;
	private static final int TEXT_X_POSITION = LogInSplash.TEXT_BOX_X+3;
	private static int centerTextVertically(FontMetrics fm){
		return fm.getAscent()+(LogInSplash.TEXT_BOX_HEIGHT-(fm.getAscent()+fm.getDescent()))/2;
	}
	private boolean carretTick;
	private Font font;
	private boolean isPlayButtonDown;
	private boolean isSignUpButtonDown;
	private SplashScreenListener listener;
	private String password;
	private String passwordCarret;
	private String passwordCarretCharacter;
	private BufferedImage playButtonUp,
			playButtonDown,
			signUpButtonUp,
			signUpButtonDown;
	private int showCarret;
	private String username;
	private String usernameCarret;
	private String usernameCarretCharacter;
	public SplashScreen(BufferedImage image, SplashScreenListener listener){
		super(image);
		playButtonUp = ClientLauncher.game.getFolder().getImage("Play Button Up.png");
		playButtonDown = ClientLauncher.game.getFolder().getImage("Play Button Down.png");
		signUpButtonUp = ClientLauncher.game.getFolder().getImage("Sign Up Button Up.png");
		signUpButtonDown = ClientLauncher.game.getFolder().getImage("Sign Up Button Down.png");
		this.listener = listener;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		font = new Font("Aerial", Font.PLAIN, 15);
		new Timer().schedule(new TimerTask(){
			@Override public void run(){
				if(!isVisible()) cancel();
				else{
					carretTick = !carretTick;
					repaint();
				}
			}
		}, 400, 400);
	}
	@Override protected JPanel createPanel(){
		return new JPanel(){
			@Override public void paintComponent(Graphics g1){
				Graphics2D g = (Graphics2D)g1;
				g.setFont(font);
				g.setColor(getBackground());
				g.clearRect(0, 0, getWidth(), getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade));
				g.drawImage(img, 0, 0, this);
				g.drawImage(isPlayButtonDown?playButtonDown:playButtonUp, SplashScreen.PLAY_BUTTON_X, SplashScreen.BUTTON_Y, null);
				g.drawImage(isSignUpButtonDown?signUpButtonDown:signUpButtonUp, SplashScreen.SIGN_UP_BUTTON_X, SplashScreen.BUTTON_Y, null);
				g.setColor(Color.black);
				FontMetrics fm = g.getFontMetrics();
				int ver = SplashScreen.centerTextVertically(fm);
				if(username!=null){
					g.drawString(username, SplashScreen.TEXT_X_POSITION, LogInSplash.TEXT_BOX_1_Y+ver);
					if(showCarret==1&&carretTick) g.drawString(usernameCarretCharacter, SplashScreen.TEXT_X_POSITION+fm.stringWidth(usernameCarret), LogInSplash.TEXT_BOX_1_Y+ver);
				}
				if(password!=null){
					g.drawString(password, SplashScreen.TEXT_X_POSITION, LogInSplash.TEXT_BOX_2_Y+ver);
					if(showCarret==2&&carretTick) g.drawString(passwordCarretCharacter, SplashScreen.TEXT_X_POSITION+fm.stringWidth(passwordCarret), LogInSplash.TEXT_BOX_2_Y+ver);
				}
				g.dispose();
			}
		};
	}
	public void fadeIn(int fadeTicks, int tickDelay){
		final FadeTimer fadeTimer = new FadeTimer(fadeTicks, 0, 0, tickDelay);
		fadeTimer.addListener(new FadeListener(){
			public void onComplete(){
				listener.onFadedIn();
			}
			public void onFadeInComplete(){}
			public void onFadeInTick(){
				updateFadeLevel(fadeTimer.getFadeLevel());
			}
			public void onFadeOutComplete(){}
			public void onFadeOutTick(){}
			public void onFadeStayComplete(){}
			public void onFadeStayTick(){}
		});
		fadeTimer.start();
	}
	public void fadeOut(int fadeTicks, int tickDelay){
		final FadeTimer fadeTimer = new FadeTimer(0, 0, fadeTicks, tickDelay);
		fadeTimer.addListener(new FadeListener(){
			public void onComplete(){
				listener.onFadedOut();
			}
			public void onFadeInComplete(){}
			public void onFadeInTick(){}
			public void onFadeOutComplete(){}
			public void onFadeOutTick(){
				updateFadeLevel(fadeTimer.getFadeLevel());
			}
			public void onFadeStayComplete(){}
			public void onFadeStayTick(){}
		});
		fadeTimer.start();
	}
	public JPanel getPanel(){
		return panel;
	}
	public void setButtonStates(boolean play, boolean signUp){
		isPlayButtonDown = play;
		isSignUpButtonDown = signUp;
	}
	public void setPassword(String password, int carretPosition, boolean insert){
		this.password = password;
		passwordCarret = password.substring(0, carretPosition);
		passwordCarretCharacter = insert?"_":"|";
	}
	public void setShowCarret(int box){
		showCarret = box;
		repaint();
	}
	public void setUsername(String username, int carretPosition, boolean insert){
		this.username = username;
		usernameCarret = username.substring(0, carretPosition);
		usernameCarretCharacter = insert?"_":"|";
	}
}