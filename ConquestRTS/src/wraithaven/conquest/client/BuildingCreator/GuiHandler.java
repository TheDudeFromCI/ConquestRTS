package wraithaven.conquest.client.BuildingCreator;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UiElement;
import wraithaven.conquest.client.GameWorld.GLGuiComponents;
import wraithaven.conquest.client.GameWorld.GLGuiImage;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.WindowUtil.OnScreenText.TextBox;

public class GuiHandler{
	private static float CURSOR_SIZE = 50;
	public static float HOTBAR_SLOT = 50;
	private static final float OUTLINE_BUFFER = 0.01f;
	private static final float OUTLINE_INTENSITY = 0.5f;
	private static final float ROTATION_BLOCK_ZOOM = 40;
	private static float rotationSpeed = 10;
	public static float WHEEL_SIZE = 400;
	private static final float WIREFRAME_BLOCK_ZOOM = 15;
	private static GLGuiImage createCursor(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Cursor.png");
		i.w = CURSOR_SIZE;
		i.h = CURSOR_SIZE;
		i.x = Loop.screenRes.width/2f-i.w/2f;
		i.y = Loop.screenRes.height/2f-i.h/2f;
		return i;
	}
	private static GLGuiImage createHotbarSelector(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Block Selector.png");
		i.w = HOTBAR_SLOT;
		i.h = HOTBAR_SLOT;
		return i;
	}
	private static GLGuiImage createWheelGlass(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Wheel Glass.png");
		i.w = WHEEL_SIZE;
		i.h = WHEEL_SIZE;
		i.x = Loop.screenRes.width/2-i.w/2;
		i.y = -i.h/2;
		return i;
	}
	private static GLGuiImage createWheelMain(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Wheel Main.png");
		i.w = WHEEL_SIZE;
		i.h = WHEEL_SIZE;
		return i;
	}
	private static void drawWires(){
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4f(0, 0, 0, OUTLINE_INTENSITY);
		GL11.glBegin(GL11.GL_QUADS);
		float sx = -OUTLINE_BUFFER;
		float sy = -OUTLINE_BUFFER;
		float sz = -OUTLINE_BUFFER;
		float ex = 1+OUTLINE_BUFFER;
		float ey = 1+OUTLINE_BUFFER;
		float ez = 1+OUTLINE_BUFFER;
		GL11.glVertex3f(sx, sy, sz);
		GL11.glVertex3f(sx, sy, ez);
		GL11.glVertex3f(ex, sy, ez);
		GL11.glVertex3f(ex, sy, sz);
		GL11.glVertex3f(sx, sy, sz);
		GL11.glVertex3f(sx, ey, sz);
		GL11.glVertex3f(ex, ey, sz);
		GL11.glVertex3f(ex, sy, sz);
		GL11.glVertex3f(sx, sy, sz);
		GL11.glVertex3f(sx, sy, ez);
		GL11.glVertex3f(sx, ey, ez);
		GL11.glVertex3f(sx, ey, sz);
		GL11.glVertex3f(sx, ey, sz);
		GL11.glVertex3f(sx, ey, ez);
		GL11.glVertex3f(ex, ey, ez);
		GL11.glVertex3f(ex, ey, sz);
		GL11.glVertex3f(sx, sy, ez);
		GL11.glVertex3f(sx, ey, ez);
		GL11.glVertex3f(ex, ey, ez);
		GL11.glVertex3f(ex, sy, ez);
		GL11.glVertex3f(ex, sy, sz);
		GL11.glVertex3f(ex, sy, ez);
		GL11.glVertex3f(ex, ey, ez);
		GL11.glVertex3f(ex, ey, sz);
		GL11.glEnd();
		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	private static GLGuiImage makeHotBarPart(int id, Texture texture){
		GLGuiImage i = new GLGuiImage(texture);
		i.w = HOTBAR_SLOT;
		i.h = HOTBAR_SLOT;
		i.y = (Loop.screenRes.height-i.h*10)/2+i.h*(id%10);
		if(id<10) i.x = Loop.screenRes.width-i.w;
		else i.x = 0;
		return i;
	}
	private static void setHotbarSelectorLocation(GLGuiImage i, int id){
		i.y = (Loop.screenRes.height-i.h*10)/2+i.h*(9-id%10);
		if(id<10) i.x = Loop.screenRes.width-i.w;
		else i.x = 0;
	}
	private GLGuiComponents components;
	private GLGuiComponents components2;
	private GLGuiComponents components3;
	private final int displayListId;
	public int goalWheelRotation;
	private IconManager iconManager;
	private float realWheelRotation;
	private final BlockIcon[] rotations = new BlockIcon[24];
	private final TextBox textBox;
	private UiElement textEle;
	public GuiHandler(){
		components = new GLGuiComponents(Loop.screenRes);
		components2 = new GLGuiComponents(Loop.screenRes);
		components3 = new GLGuiComponents(Loop.screenRes);
		components.addComponent(createCursor());
		components3.addComponent(createWheelMain());
		iconManager = new IconManager();
		Texture hotbarPart = Texture.getTexture(ClientLauncher.assetFolder, "Hotbar Slot.png");
		for(int i = 0; i<20; i++)
			components.addComponent(makeHotBarPart(i, hotbarPart));
		components2.addComponent(createHotbarSelector());
		components2.addComponent(createWheelGlass());
		updateHotbarSelector(0);
		textBox = new TextBox("Hello", 100, 100, Color.black);
		textEle = new UiElement(textBox.getTexture());
		textEle.x = 0;
		textEle.y = Loop.screenRes.height-100;
		textEle.w = 100;
		textEle.h = 100;
		displayListId = GL11.glGenLists(1);
		GL11.glNewList(displayListId, GL11.GL_COMPILE);
		drawWires();
		GL11.glEndList();
	}
	public void addIcon(int id, BlockIcon icon){
		iconManager.addIcon(icon, id);
	}
	public void cleanUp(){
		iconManager.dispose();
		components.clearComponents();
		components2.clearComponents();
		components3.clearComponents();
		GL11.glDeleteLists(displayListId, 1);
	}
	public int getHotbarSelectorId(){
		return iconManager.selectedSlot;
	}
	public IconManager getIconManager(){
		return iconManager;
	}
	public CubeTextures getSelectedCubeTextures(){
		return iconManager.getSelectedCubeTextures();
	}
	public BlockRotation getSelectedRotation(){
		return BlockRotation.getRotation(goalWheelRotation);
	}
	public BlockShape getSelectedShape(){
		return iconManager.getSelectedShape();
	}
	public void render(){
		if(Loop.INSTANCE.getInputController().wireframeMode) return;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		components.render();
		GL11.glPushMatrix();
		GL11.glTranslatef(Loop.screenRes.width/2f, 0, 0);
		GL11.glRotatef(realWheelRotation, 0, 0, 1);
		GL11.glTranslatef(-WHEEL_SIZE/2, -WHEEL_SIZE/2, 0);
		components3.render();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		MatrixUtils.setupOrtho(ROTATION_BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, ROTATION_BLOCK_ZOOM, -1000, 1000);
		renderRotationIcons();
		MatrixUtils.setupOrtho(BlockIcon.BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, BlockIcon.BLOCK_ZOOM, -1000, 1000);
		iconManager.render();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		components2.render();
	}
	private void renderRotationIcons(){
		while(goalWheelRotation<0)
			goalWheelRotation += 24;
		goalWheelRotation %= 24;
		if(rotations[goalWheelRotation]==null) return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -0.5f*ROTATION_BLOCK_ZOOM, 0);
		float r;
		for(int i = 0; i<24; i++){
			r = realWheelRotation-(i*15+7.5f);
			if(r>100&&r<260) continue;
			GL11.glPushMatrix();
			GL11.glRotatef(r, 0, 0, 1);
			GL11.glTranslatef(0, (i==goalWheelRotation?0.2f:0)+WHEEL_SIZE/2f/Loop.screenRes.height*ROTATION_BLOCK_ZOOM-1.5f, 0);
			GL11.glRotatef(-r, 0, 0, 1);
			GL11.glRotatef(Loop.INSTANCE.getCamera().rx, 1, 0, 0);
			GL11.glRotatef(Loop.INSTANCE.getCamera().ry, 0, 1, 0);
			rotations[i].render(0, 0, 0, -BlockIcon.BLOCK_PITCH, -BlockIcon.BLOCK_YAW);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		MatrixUtils.setupOrtho(WIREFRAME_BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, WIREFRAME_BLOCK_ZOOM, -1000, 1000);
		renderWireFrame();
	}
	private void renderWireFrame(){
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (-0.5f+WHEEL_SIZE*0.175f/Loop.screenRes.height)*WIREFRAME_BLOCK_ZOOM, 0);
		GL11.glRotatef(Loop.INSTANCE.getCamera().rx, 1, 0, 0);
		GL11.glRotatef(Loop.INSTANCE.getCamera().ry, 0, 1, 0);
		rotations[goalWheelRotation].render(0, 0, 0, -BlockIcon.BLOCK_PITCH, -BlockIcon.BLOCK_YAW);
		GL11.glCallList(displayListId);
		GL11.glPopMatrix();
	}
	public void update(double delta){
		realWheelRotation = (float)(((((((goalWheelRotation*15+7.5f)-realWheelRotation)%360)+540)%360)-180)*delta*rotationSpeed)+realWheelRotation;
	}
	public void updateBlockRotations(){
		for(int i = 0; i<24; i++)
			if(rotations[i]!=null) rotations[i].dispose();
		if(iconManager.getSelectedShape()==null){
			for(int i = 0; i<24; i++)
				rotations[i] = null;
			return;
		}
		for(int i = 0; i<24; i++)
			rotations[i] = new BlockIcon(iconManager.getSelectedShape(), iconManager.getSelectedCubeTextures(), BlockRotation.getRotation(i));
	}
	public void updateHotbarSelector(int id){
		iconManager.selectedSlot = id;
		setHotbarSelectorLocation(components2.getComponent(0), id);
		updateBlockRotations();
	}
}