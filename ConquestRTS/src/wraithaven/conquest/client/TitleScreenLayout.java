package wraithaven.conquest.client;

import java.util.ArrayList;
import wraith.library.WindowUtil.GUI.GuiComponent;
import wraith.library.WindowUtil.GUI.GuiLayout;

public class TitleScreenLayout implements GuiLayout{
	private int tempX, tempY, tempWidth, tempHeight;
	private static final int BUTTON_HEIGHT = 100;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_SEPERATION = 20;
	public void setParentDimensions(int x, int y, int width, int height){
		tempX=x;
		tempY=y;
		tempWidth=width;
		tempHeight=height;
	}
	public void validateComponents(ArrayList<GuiComponent> components){
		if(components.isEmpty())return;
		int startY = tempY+(tempHeight-(BUTTON_HEIGHT*2+BUTTON_SEPERATION))/2;
		int nextY = BUTTON_HEIGHT+BUTTON_SEPERATION;
		components.get(0).setSizeAndLocation(tempX, tempY, tempWidth, tempHeight);
		components.get(1).setSizeAndLocation(tempX+(tempWidth-BUTTON_WIDTH)/2, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
		components.get(2).setSizeAndLocation(tempX+(tempWidth-BUTTON_WIDTH)/2, startY+nextY, BUTTON_WIDTH, BUTTON_HEIGHT);
	}
}