package wraithaven.conquest.client;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiComponent;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiLayout;

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
		int y1 = (int)(tempHeight*0.72)+tempY;
		int y2 = (int)(tempHeight*0.87)+tempY;
		components.get(0).setSizeAndLocation(tempX, tempY, tempWidth, tempHeight);
		components.get(1).setSizeAndLocation(getX(0, 52), y1, 104, 104);
		components.get(2).setSizeAndLocation(getX(1, 52), y1, 104, 104);
		components.get(3).setSizeAndLocation(getX(2, 52), y1, 104, 104);
		components.get(4).setSizeAndLocation(getX(3, 52), y1, 104, 104);
		components.get(5).setSizeAndLocation(getX(4, 52), y1, 104, 104);
		components.get(6).setSizeAndLocation(getX(0, 76), y2, 152, 34);
		components.get(7).setSizeAndLocation(getX(1, 75.5f), y2, 151, 34);
		components.get(8).setSizeAndLocation(getX(2, 47), y2, 94, 34);
		components.get(9).setSizeAndLocation(getX(3, 47.5f), y2, 95, 34);
		components.get(10).setSizeAndLocation(getX(4, 60.5f), y2, 121, 34);
	}
	private int getX(int id, float halfWidth){
		double w = tempWidth/6.0;
		return (int)(w*(id+0.5)+w/2-halfWidth)+tempX;
	}
}