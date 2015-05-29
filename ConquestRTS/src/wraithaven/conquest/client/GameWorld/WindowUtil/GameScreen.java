package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameScreen{
	private UserInputListener adapter;
	private Color backgroundColor = Color.black;
	private JFrame frame;
	private GameRenderer gameRenderer;
	private final Dimension renderSize;
	private final Dimension screenSize;
	private int x,
			y,
			nx,
			ny;
	public GameScreen(String name, Image icon, GameRenderer renderer, UserInputListener adapter){
		gameRenderer = renderer;
		this.adapter = adapter;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		renderSize = new Dimension(screenSize);
		frame = new JFrame();
		JPanel panel = new JPanel(){
			@Override public void paintComponent(Graphics g){
				g.setColor(backgroundColor);
				g.fillRect(0, 0, x, screenSize.height);
				g.fillRect(x, 0, renderSize.width, y);
				g.fillRect(x, ny, renderSize.width, y);
				g.fillRect(nx, 0, x, screenSize.height);
				if(gameRenderer!=null) gameRenderer.render((Graphics2D)g, x, y, renderSize.width, renderSize.height);
				g.dispose();
			}
		};
		frame.add(panel);
		frame.setIconImage(icon);
		frame.setTitle(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(screenSize);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		if(adapter!=null){
			frame.addMouseListener(adapter);
			frame.addMouseMotionListener(adapter);
			frame.addMouseWheelListener(adapter);
			frame.addKeyListener(adapter);
		}
		frame.setVisible(true);
		new Timer().scheduleAtFixedRate(new TimerTask(){
			@Override public void run(){
				if(!frame.isVisible()) cancel();
				else frame.repaint();
			}
		}, 20, 20);
	}
	public void addUserInputListener(UserInputListener listener){
		frame.addMouseListener(listener);
		frame.addMouseMotionListener(listener);
		frame.addMouseWheelListener(listener);
		frame.addKeyListener(listener);
	}
	public void dispose(){
		frame.dispose();
	}
	public GameRenderer getGameRenderer(){
		return gameRenderer;
	}
	public Dimension getRenderSize(){
		return renderSize;
	}
	public int getRenderX(){
		return x;
	}
	public int getRenderY(){
		return y;
	}
	public Dimension getScreenSize(){
		return screenSize;
	}
	public void removeUserInputListener(UserInputListener listener){
		frame.removeMouseListener(listener);
		frame.removeMouseMotionListener(listener);
		frame.removeMouseWheelListener(listener);
		frame.removeKeyListener(listener);
	}
	public void setBackgroundColor(Color color){
		backgroundColor = color;
	}
	public void setGameRenderer(GameRenderer gameRenderer){
		this.gameRenderer = gameRenderer;
	}
	public void setRenderSize(int width, int height, boolean scale){
		if(scale){
			if(Math.abs(screenSize.width-width)>Math.abs(screenSize.height-height)){
				double scaleSize = screenSize.height/(double)height;
				height = screenSize.height;
				width *= scaleSize;
			}else{
				double scaleSize = screenSize.width/(double)width;
				width = screenSize.width;
				height *= scaleSize;
			}
		}
		renderSize.width = width;
		renderSize.height = height;
		x = (screenSize.width-width)/2;
		y = (screenSize.height-height)/2;
		nx = x+renderSize.width;
		ny = y+renderSize.height;
	}
	public void setUserInputListener(UserInputListener listener){
		if(adapter!=null){
			frame.removeMouseListener(adapter);
			frame.removeMouseMotionListener(adapter);
			frame.removeMouseWheelListener(adapter);
			frame.removeKeyListener(adapter);
		}
		adapter = listener;
		if(listener!=null){
			frame.addMouseListener(listener);
			frame.addMouseMotionListener(listener);
			frame.addMouseWheelListener(listener);
			frame.addKeyListener(listener);
		}
	}
}