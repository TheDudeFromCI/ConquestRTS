package wraithaven.conquest.client.GameWorld.WindowUtil;

import java.util.ArrayList;

public interface GuiLayout{
	public void setParentDimensions(int x, int y, int width, int height);
	public void validateComponents(ArrayList<GuiComponent> components);
}