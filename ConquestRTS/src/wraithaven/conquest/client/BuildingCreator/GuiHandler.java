package wraithaven.conquest.client.BuildingCreator;

import java.awt.Dimension;
import java.io.File;
import wraith.library.LWJGL.GuiComponents;
import wraith.library.LWJGL.GuiImage;
import wraithaven.conquest.client.GameWorld.BlockTextures;

public class GuiHandler{
	private GuiComponents components;
	private static final float CURSOR_SIZE = 0.02f;
	public GuiHandler(Dimension screenSize){
		components=new GuiComponents(screenSize);
		components.scaleX=screenSize.width/(float)screenSize.height;
		components.addComponent(createCursor());
	}
	public void render(){ components.render(); }
	private static GuiImage createCursor(){
		GuiImage i = new GuiImage(new File(BlockTextures.getFolder(), "Cursor.png"));
		i.x=0.5f-CURSOR_SIZE/2f;
		i.y=0.5f-CURSOR_SIZE/2f;
		i.w=CURSOR_SIZE;
		i.h=CURSOR_SIZE;
		return i;
	}
}