package wraithaven.conquest.client;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wraith.library.MiscUtil.FadeListener;
import wraith.library.MiscUtil.FadeTimer;
import wraith.library.WindowUtil.ImageWindow;

public class SplashScreen extends ImageWindow{
	private SplashScreenListener listener;
	private String username;
	private String password;
	private String usernameCarret;
	private String passwordCarret;
	private int showCarret;
	private Font font;
	private static final int TEXT_X_POSITION = LogInSplash.TEXT_BOX_X+3;
	public SplashScreen(BufferedImage image, SplashScreenListener listener){
		super(image);
		this.listener=listener;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		font=new Font("Aerial", Font.PLAIN, 15);
	}
	public void fadeIn(int fadeTicks, int tickDelay){
		final FadeTimer fadeTimer = new FadeTimer(fadeTicks, 0, 0, tickDelay);
		fadeTimer.addListener(new FadeListener(){
			public void onFadeInTick(){ updateFadeLevel(fadeTimer.getFadeLevel()); }
			public void onComplete(){ listener.onFadedIn(); }
			public void onFadeOutTick(){}
			public void onFadeOutComplete(){}
			public void onFadeInComplete(){}
			public void onFadeStayTick(){}
			public void onFadeStayComplete(){}
		});
		fadeTimer.start();
	}
	public void fadeOut(int fadeTicks, int tickDelay){
		final FadeTimer fadeTimer = new FadeTimer(0, 0, fadeTicks, tickDelay);
		fadeTimer.addListener(new FadeListener(){
			public void onFadeOutTick(){ updateFadeLevel(fadeTimer.getFadeLevel()); }
			public void onComplete(){ listener.onFadedOut(); }
			public void onFadeInTick(){}
			public void onFadeOutComplete(){}
			public void onFadeInComplete(){}
			public void onFadeStayTick(){}
			public void onFadeStayComplete(){}
		});
		fadeTimer.start();
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
				g.setColor(Color.black);
				int ver = centerTextVertically(g.getFontMetrics());
				if(username!=null){
					if(showCarret==1)g.drawString(usernameCarret, TEXT_X_POSITION, LogInSplash.TEXT_BOX_1_Y+ver);
					else g.drawString(username, TEXT_X_POSITION, LogInSplash.TEXT_BOX_1_Y+ver);
				}
				if(password!=null){
					if(showCarret==2)g.drawString(passwordCarret, TEXT_X_POSITION, LogInSplash.TEXT_BOX_2_Y+ver);
					else g.drawString(password, TEXT_X_POSITION, LogInSplash.TEXT_BOX_2_Y+ver);
				}
				g.dispose();
			}
		};
	}
	public void setUsername(String username, int carretPosition, boolean insert){
		this.username=username;
		StringBuilder sb = new StringBuilder(username);
		sb.insert(carretPosition, insert?'_':'|');
		usernameCarret=sb.toString();
	}
	public void setPassword(String password, int carretPosition, boolean insert){
		this.password=password;
		StringBuilder sb = new StringBuilder(password);
		sb.insert(carretPosition, insert?'_':'|');
		passwordCarret=sb.toString();
	}
	public void setShowCarret(int box){
		showCarret=box;
		repaint();
	}
	public JPanel getPanel(){ return panel; }
	private static int centerTextVertically(FontMetrics fm){ return fm.getAscent()+(LogInSplash.TEXT_BOX_HEIGHT-(fm.getAscent()+fm.getDescent()))/2; }
}