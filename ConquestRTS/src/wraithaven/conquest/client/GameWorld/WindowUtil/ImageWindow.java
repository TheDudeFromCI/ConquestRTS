package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wraithaven.conquest.client.GameWorld.FadeListener;
import wraithaven.conquest.client.GameWorld.FadeTimer;

@SuppressWarnings("serial") public class ImageWindow extends JFrame{
	protected float fade;
	private boolean fadeTimer;
	protected BufferedImage img;
	protected JPanel panel;
	public ImageWindow(BufferedImage image){
		img = image;
		init();
		setVisible(true);
	}
	public void addFadeTimer(int fadeIn, int fadeStay, int fadeOut, int pingDelay){
		if(fadeTimer) return;
		fadeTimer = true;
		final FadeTimer fadeTimer = new FadeTimer(fadeIn, fadeStay, fadeOut, pingDelay);
		fadeTimer.addListener(new FadeListener(){
			public void onComplete(){
				dispose();
			}
			public void onFadeInComplete(){
				updateFadeLevel(fadeTimer.getFadeLevel());
			}
			public void onFadeInTick(){
				updateFadeLevel(fadeTimer.getFadeLevel());
			}
			public void onFadeOutComplete(){}
			public void onFadeOutTick(){
				updateFadeLevel(fadeTimer.getFadeLevel());
			}
			public void onFadeStayComplete(){}
			public void onFadeStayTick(){}
		});
		fadeTimer.start();
	}
	protected JPanel createPanel(){
		return new JPanel(){
			@Override public void paintComponent(Graphics g){
				g.setColor(getBackground());
				g.clearRect(0, 0, getWidth(), getHeight());
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade));
				g.drawImage(img, 0, 0, this);
				g.dispose();
			}
		};
	}
	private void init(){
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(img.getWidth(), img.getHeight());
		setLocationRelativeTo(null);
		setBackground(new Color(0, 0, 0, 0));
		setAlwaysOnTop(true);
		add(panel = createPanel());
	}
	public void updateFadeLevel(float fade){
		this.fade = fade;
		repaint();
	}
}