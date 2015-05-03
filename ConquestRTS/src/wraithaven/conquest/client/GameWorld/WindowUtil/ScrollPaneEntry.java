package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.awt.Graphics2D;

public interface ScrollPaneEntry{
	public void renderEntry(Graphics2D g, int x, int y, int width, int height);
	public void onEntryClick();
}