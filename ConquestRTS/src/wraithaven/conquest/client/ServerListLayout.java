package wraithaven.conquest.client;

import java.util.ArrayList;
import wraith.library.WindowUtil.GUI.GuiComponent;
import wraith.library.WindowUtil.GUI.GuiLayout;

public class ServerListLayout implements GuiLayout{
	private int tempX, tempY, tempWidth, tempHeight;
	private static final int BACK_BUTTON_HEIGHT = 75;
	private static final int BACK_BUTTON_WIDTH = 300;
	private static final int BACK_BUTTON_EDGE_BUFFER = 20;
	public void setParentDimensions(int x, int y, int width, int height){
		tempX=x;
		tempY=y;
		tempWidth=width;
		tempHeight=height;
	}
	public void validateComponents(ArrayList<GuiComponent> components){
		if(components.isEmpty())return;
		components.get(0).setSizeAndLocation(tempX, tempY, tempWidth, tempHeight);
		components.get(1).setSizeAndLocation(tempX+BACK_BUTTON_EDGE_BUFFER, tempY+tempHeight-BACK_BUTTON_HEIGHT-BACK_BUTTON_EDGE_BUFFER, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
	}
}