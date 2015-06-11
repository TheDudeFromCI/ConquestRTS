package wraithaven.conquest.client;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiComponent;
import wraithaven.conquest.client.GameWorld.WindowUtil.GuiLayout;

public class ServerListLayout implements GuiLayout{
	private static final int BACK_BUTTON_EDGE_BUFFER = 20;
	private static final int BACK_BUTTON_HEIGHT = 75;
	private static final int BACK_BUTTON_WIDTH = 300;
	private int tempX,
			tempY,
			tempWidth,
			tempHeight;
	public void setParentDimensions(int x, int y, int width, int height){
		tempX = x;
		tempY = y;
		tempWidth = width;
		tempHeight = height;
	}
	public void validateComponents(ArrayList<GuiComponent> components){
		if(components.isEmpty()) return;
		components.get(0).setSizeAndLocation(tempX, tempY, tempWidth, tempHeight);
		components.get(1).setSizeAndLocation(tempX+ServerListLayout.BACK_BUTTON_EDGE_BUFFER, tempY+tempHeight-ServerListLayout.BACK_BUTTON_HEIGHT-ServerListLayout.BACK_BUTTON_EDGE_BUFFER, ServerListLayout.BACK_BUTTON_WIDTH, ServerListLayout.BACK_BUTTON_HEIGHT);
		components.get(2).setSizeAndLocation((int)(tempWidth*0.05), (int)(tempHeight*0.05), (int)(tempWidth*0.9), (int)(tempHeight*0.8));
		components.get(3).setSizeAndLocation(tempX+tempWidth-ServerListLayout.BACK_BUTTON_WIDTH-ServerListLayout.BACK_BUTTON_EDGE_BUFFER, tempY+tempHeight-ServerListLayout.BACK_BUTTON_HEIGHT-ServerListLayout.BACK_BUTTON_EDGE_BUFFER, ServerListLayout.BACK_BUTTON_WIDTH, ServerListLayout.BACK_BUTTON_HEIGHT);
		components.get(4).setSizeAndLocation(tempX+(tempWidth-ServerListLayout.BACK_BUTTON_WIDTH)/2, tempY+tempHeight-ServerListLayout.BACK_BUTTON_HEIGHT-ServerListLayout.BACK_BUTTON_EDGE_BUFFER, ServerListLayout.BACK_BUTTON_WIDTH, ServerListLayout.BACK_BUTTON_HEIGHT);
	}
}