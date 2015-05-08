package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.io.File;
import wraithaven.conquest.client.GameWorld.GLGuiImage;
import wraithaven.conquest.client.GameWorld.GLGuiComponents;
import wraithaven.conquest.client.GameWorld.BlockTextures;

public class GuiHandler{
	private IconManager iconManager;
	private GLGuiComponents components;
	private static final float CURSOR_SIZE = 0.03f;
	public GuiHandler(Dimension screenSize){
		components=new GLGuiComponents(screenSize);
		components.addComponent(createCursor(screenSize));
		iconManager=new IconManager();
	}
	public void render(){
		components.render();
		iconManager.render();
	}
	private static GLGuiImage createCursor(Dimension screenSize){
		GLGuiImage i = new GLGuiImage(new File(BlockTextures.getFolder(), "Cursor.png"));
		i.x=0.5f-CURSOR_SIZE/2f;
		i.y=0.5f-(CURSOR_SIZE*screenSize.width/screenSize.height)/2f;
		i.w=CURSOR_SIZE;
		i.h=CURSOR_SIZE*screenSize.width/screenSize.height;
		return i;
	}
}