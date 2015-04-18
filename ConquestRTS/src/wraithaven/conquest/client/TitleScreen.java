package wraithaven.conquest.client;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import wraith.library.WindowUtil.UserInputAdapter;
import wraith.library.WindowUtil.GameWindow.GameRenderer;

public class TitleScreen extends UserInputAdapter implements GameRenderer{
	public void render(Graphics2D g, int x, int y, int width, int height){
	}
	@Override public void keyPressed(KeyEvent e){ if(e.getKeyCode()==KeyEvent.VK_F12)System.exit(0); }
}