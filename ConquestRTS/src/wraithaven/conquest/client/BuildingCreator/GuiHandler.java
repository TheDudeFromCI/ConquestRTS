package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.BlockIcon;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.GLGuiImage;
import wraithaven.conquest.client.GameWorld.GLGuiComponents;
import wraithaven.conquest.client.GameWorld.BlockTextures;

public class GuiHandler{
	private IconManager iconManager;
	private GLGuiComponents components;
	private static float CURSOR_SIZE = 0.03f;
	public static float HOTBAR_SLOT = 0.04f;
	public GuiHandler(){
		components=new GLGuiComponents(Loop.screenRes);
		components.addComponent(createCursor());
		iconManager=new IconManager();
		Texture hotbarPart = new Texture(new File(ClientLauncher.assetFolder, "Hotbar Slot.png"), 0, null);
		for(int i = 0; i<20; i++)components.addComponent(makeHotBarPart(i, hotbarPart));
	}
	public void render(){
		components.render();
		MatrixUtils.setupOrtho(BlockIcon.BLOCK_ZOOM, BlockIcon.BLOCK_ZOOM, -1000, 1000);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		iconManager.render();
		MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
	}
	public void update(double time){ iconManager.update(time); }
	private static GLGuiImage createCursor(){
		GLGuiImage i = new GLGuiImage(new File(BlockTextures.getFolder(), "Cursor.png"));
		i.x=0.5f-CURSOR_SIZE/2f;
		i.y=0.5f-(CURSOR_SIZE*Loop.screenRes.width/Loop.screenRes.height)/2f;
		i.w=CURSOR_SIZE;
		i.h=CURSOR_SIZE*Loop.screenRes.width/Loop.screenRes.height;
		return i;
	}
	private static GLGuiImage makeHotBarPart(int id, Texture texture){
		GLGuiImage i = new GLGuiImage(texture);
		if(id<10)i.x=1-HOTBAR_SLOT;
		else i.x=0;
		id%=10;
		float totalHeight = 0.5f-((HOTBAR_SLOT*Loop.screenRes.width/Loop.screenRes.height)*10)/2-HOTBAR_SLOT/2f;
		i.w=HOTBAR_SLOT;
		i.h=HOTBAR_SLOT*Loop.screenRes.width/Loop.screenRes.height;
		i.y=totalHeight+i.h*id;
		return i;
	}
}