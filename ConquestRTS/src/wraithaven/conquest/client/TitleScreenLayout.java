package wraithaven.conquest.client;

import java.util.ArrayList;
import wraith.library.WindowUtil.GUI.GuiComponent;
import wraith.library.WindowUtil.GUI.GuiLayout;

public class TitleScreenLayout implements GuiLayout{
	private int tempX, tempY, tempWidth, tempHeight;
	private static final int BUTTON_HEIGHT = 100;
	private static final int BUTTON_WIDTH = 300;
	public void setParentDimensions(int x, int y, int width, int height){
		tempX=x;
		tempY=y;
		tempWidth=width;
		tempHeight=height;
	}
	public void validateComponents(ArrayList<GuiComponent> components){
		if(components.size()>=1)components.get(0).setSizeAndLocation(tempX, tempY, tempWidth, tempHeight);
		if(components.size()>=2)components.get(1).setSizeAndLocation(tempX+(tempWidth-BUTTON_WIDTH)/2, tempY+(tempHeight-BUTTON_HEIGHT)/2, BUTTON_WIDTH, BUTTON_HEIGHT);
	}
}