package wraithaven.conquest.client;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import wraith.library.MiscUtil.TypeListener;
import wraith.library.WindowUtil.SplashScreenProtocol;

public class LogInSplash implements SplashScreenProtocol{
	private Image icon;
	private String title;
	private Runnable run;
	private SplashScreen splash;
	private int selectedBox;
	private TypeListener username;
	private TypeListener password;
	private static final int FADE_TICKS = 40;
	private static final int FADE_DELAY = 20;
	private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
	public static final int TEXT_BOX_WIDTH = 176;
	public static final int TEXT_BOX_HEIGHT = 34;
	public static final int TEXT_BOX_X = 411;
	public static final int TEXT_BOX_1_Y = 64;
	public static final int TEXT_BOX_2_Y = 111;
	public void showSplash(){
		splash=new SplashScreen(ClientLauncher.game.getFolder().getImage("Splash.png"), new SplashScreenListener(){
			public void onFadedOut(){
				splash.dispose();
				run.run();
			}
			public void onFadedIn(){
				username=new TypeListener(ALLOWED_CHARACTERS, null);
				password=new TypeListener(ALLOWED_CHARACTERS, null);
				username.setCharacterCap(15);
				password.setCharacterCap(15);
				splash.addKeyListener(new KeyAdapter(){
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
				splash.addMouseListener(new MouseAdapter(){
					@Override public void mousePressed(MouseEvent e){
						Point p = e.getPoint();
						if(p.x>=TEXT_BOX_X&&p.x<TEXT_BOX_X+TEXT_BOX_WIDTH){
							if(p.y>=TEXT_BOX_1_Y&&p.y<TEXT_BOX_1_Y+TEXT_BOX_HEIGHT){
								selectedBox=1;
								username.setInsert(false);
								username.setCarretPosition(username.getLength());
							}else if(p.y>=TEXT_BOX_2_Y&&p.y<TEXT_BOX_2_Y+TEXT_BOX_HEIGHT){
								selectedBox=2;
								password.setInsert(false);
								password.setCarretPosition(password.getLength());
							}else selectedBox=0;
						}else selectedBox=0;
						splash.setShowCarret(selectedBox);
						splash.setUsername(username.toString(), username.getCarrentPosition(), username.isInsert());
						splash.setPassword(password.toString(), password.getCarrentPosition(), password.isInsert());
						splash.repaint();
					}
				});
			}
		});
		splash.setTitle(title);
		splash.setIconImage(icon);
		splash.fadeIn(FADE_TICKS, FADE_DELAY);
		//splash.fadeOut(FADE_TICKS, FADE_DELAY);
	}
	public void addCompletionListener(Runnable run){ this.run=run; }
	public void setTitle(String title){ this.title=title; }
	public void setIcon(Image icon){ this.icon=icon; }
}