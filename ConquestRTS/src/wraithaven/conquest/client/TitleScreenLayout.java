package wraithaven.conquest.client;

import java.util.ArrayList;
import wraith.library.WindowUtil.GUI.GuiComponent;
import wraith.library.WindowUtil.GUI.GuiLayout;

public class TitleScreenLayout implements GuiLayout{
	private int tempX, tempY, tempWidth, tempHeight;
	public void setParentDimensions(int x, int y, int width, int height){
		tempX=x;
		tempY=y;
		tempWidth=width;
		tempHeight=height;
	}
	public void validateComponents(ArrayList<GuiComponent> components){
		if(components.isEmpty())return;
		int y = (int)(tempHeight*0.7)+tempY;
		components.get(0).setSizeAndLocation(tempX, tempY, tempWidth, tempHeight);
		components.get(1).setSizeAndLocation(getX(0), y, 104, 104);
		components.get(2).setSizeAndLocation(getX(1), y, 104, 104);
		components.get(3).setSizeAndLocation(getX(2), y, 104, 104);
		components.get(4).setSizeAndLocation(getX(3), y, 104, 104);
		components.get(5).setSizeAndLocation(getX(4), y, 104, 104);
	}
	private int getX(int id){
		double w = tempWidth/6.0;
		return (int)(w*(id+0.5)+w/2)-52+tempX;
	}
}