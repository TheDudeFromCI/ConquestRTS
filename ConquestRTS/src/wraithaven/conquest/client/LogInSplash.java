package wraithaven.conquest.client;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import wraithaven.conquest.client.GameWorld.WindowUtil.SplashScreenProtocol;
import wraithaven.conquest.client.GameWorld.WindowUtil.TypeListener;

public class LogInSplash implements SplashScreenProtocol{
	private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
	private static final int FADE_DELAY = 20;
	private static final int FADE_TICKS = 40;
	public static final int TEXT_BOX_1_Y = 64;
	public static final int TEXT_BOX_2_Y = 111;
	public static final int TEXT_BOX_HEIGHT = 34;
	public static final int TEXT_BOX_WIDTH = 176;
	public static final int TEXT_BOX_X = 411;
	@SuppressWarnings("unused") private static boolean authinticate(String username, String password){
		// TODO Ping account server for stuff.
		return true;
	}
	private Image icon;
	private KeyAdapter keyListener;
	private MouseAdapter mouseListener;
	private TypeListener password;
	private Runnable run;
	private int selectedBox;
	private SplashScreen splash;
	private String title;
	private TypeListener username;
	public void addCompletionListener(Runnable run){
		this.run = run;
	}
	public void setIcon(Image icon){
		this.icon = icon;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void showSplash(){
		splash = new SplashScreen(ClientLauncher.game.getFolder().getImage("Splash.png"), new SplashScreenListener(){
			public void onFadedIn(){
				username = new TypeListener(LogInSplash.ALLOWED_CHARACTERS, null);
				password = new TypeListener(LogInSplash.ALLOWED_CHARACTERS, null);
				username.setCharacterCap(15);
				password.setCharacterCap(15);
				splash.addKeyListener(keyListener = new KeyAdapter(){
					@Override public void keyPressed(KeyEvent e){
						if(selectedBox==1){
							username.keyPressed(e);
							splash.setUsername(username.toString(), username.getCarrentPosition(), username.isInsert());
						}
						if(selectedBox==2){
							password.keyPressed(e);
							splash.setPassword(password.toString(), password.getCarrentPosition(), password.isInsert());
						}
						splash.repaint();
					}
					@Override public void keyTyped(KeyEvent e){
						if(selectedBox==1){
							username.keyTyped(e);
							splash.setUsername(username.toString(), username.getCarrentPosition(), username.isInsert());
						}
						if(selectedBox==2){
							password.keyTyped(e);
							splash.setPassword(password.toString(), password.getCarrentPosition(), password.isInsert());
						}
						splash.repaint();
					}
				});
				splash.addMouseListener(mouseListener = new MouseAdapter(){
					private boolean playButton,
							signUpButton;
					@Override public void mousePressed(MouseEvent e){
						Point p = e.getPoint();
						if(p.x>=LogInSplash.TEXT_BOX_X&&p.x<LogInSplash.TEXT_BOX_X+LogInSplash.TEXT_BOX_WIDTH){
							if(p.y>=LogInSplash.TEXT_BOX_1_Y&&p.y<LogInSplash.TEXT_BOX_1_Y+LogInSplash.TEXT_BOX_HEIGHT){
								selectedBox = 1;
								username.setInsert(false);
								username.setCarretPosition(username.getLength());
							}else if(p.y>=LogInSplash.TEXT_BOX_2_Y&&p.y<LogInSplash.TEXT_BOX_2_Y+LogInSplash.TEXT_BOX_HEIGHT){
								selectedBox = 2;
								password.setInsert(false);
								password.setCarretPosition(password.getLength());
							}else selectedBox = 0;
						}else selectedBox = 0;
						splash.setShowCarret(selectedBox);
						splash.setUsername(username.toString(), username.getCarrentPosition(), username.isInsert());
						splash.setPassword(password.toString(), password.getCarrentPosition(), password.isInsert());
						playButton = p.x>=SplashScreen.PLAY_BUTTON_X&&p.x<SplashScreen.PLAY_BUTTON_X+SplashScreen.BUTTON_WIDTH&&p.y>=SplashScreen.BUTTON_Y&&p.y<SplashScreen.BUTTON_Y+SplashScreen.BUTTON_HEIGHT;
						signUpButton = p.x>=SplashScreen.SIGN_UP_BUTTON_X&&p.x<SplashScreen.SIGN_UP_BUTTON_X+SplashScreen.BUTTON_WIDTH&&p.y>=SplashScreen.BUTTON_Y&&p.y<SplashScreen.BUTTON_Y+SplashScreen.BUTTON_HEIGHT;
						splash.setButtonStates(playButton, signUpButton);
						splash.repaint();
					}
					@Override public void mouseReleased(MouseEvent e){
						Point p = e.getPoint();
						playButton = playButton&&p.x>=SplashScreen.PLAY_BUTTON_X&&p.x<SplashScreen.PLAY_BUTTON_X+SplashScreen.BUTTON_WIDTH&&p.y>=SplashScreen.BUTTON_Y&&p.y<SplashScreen.BUTTON_Y+SplashScreen.BUTTON_HEIGHT;
						signUpButton = signUpButton&&p.x>=SplashScreen.SIGN_UP_BUTTON_X&&p.x<SplashScreen.SIGN_UP_BUTTON_X+SplashScreen.BUTTON_WIDTH&&p.y>=SplashScreen.BUTTON_Y&&p.y<SplashScreen.BUTTON_Y+SplashScreen.BUTTON_HEIGHT;
						if(playButton){
							String username = LogInSplash.this.username.toString();
							String password = LogInSplash.this.password.toString();
							if(LogInSplash.authinticate(username, password)){
								splash.removeKeyListener(keyListener);
								splash.removeMouseListener(mouseListener);
								splash.fadeOut(LogInSplash.FADE_TICKS, LogInSplash.FADE_DELAY);
							}else{
								// TODO Yell at player.
							}
						}
						if(signUpButton){
							try{
								Desktop.getDesktop().browse(new URI("https://google.com"));
							}catch(Exception exception){
								// TODO Tell player
								// "could not connect to account web site".
							}
						}
						splash.setButtonStates(false, false);
						splash.repaint();
					}
				});
			}
			public void onFadedOut(){
				splash.dispose();
				run.run();
			}
		});
		splash.setTitle(title);
		splash.setIconImage(icon);
		splash.fadeIn(LogInSplash.FADE_TICKS, LogInSplash.FADE_DELAY);
	}
}