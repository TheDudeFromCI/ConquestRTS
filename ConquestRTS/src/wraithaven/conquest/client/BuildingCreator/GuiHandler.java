package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.GLGuiImage;
import wraithaven.conquest.client.GameWorld.GLGuiComponents;

public class GuiHandler{
	private IconManager iconManager;
	private GLGuiComponents components;
	private GLGuiComponents components2;
	private GLGuiComponents components3;
	public int goalWheelRotation;
	private float realWheelRotation;
	private final BlockIcon[] rotations = new BlockIcon[24];
	private final Loop loop;
	private static float CURSOR_SIZE = 50;
	public static float HOTBAR_SLOT = 50;
	public static float WHEEL_SIZE = 400;
	private static final float ROTATION_BLOCK_ZOOM = 40;
	private static final float WIREFRAME_BLOCK_ZOOM = 15;
	private static final float OUTLINE_INTENSITY = 0.5f;
	private static final float OUTLINE_BUFFER = 0.01f;
	public GuiHandler(Loop loop){
		this.loop=loop;
		components=new GLGuiComponents(Loop.screenRes);
		components2=new GLGuiComponents(Loop.screenRes);
		components3=new GLGuiComponents(Loop.screenRes);
		components.addComponent(createCursor());
		components3.addComponent(createWheelMain());
		iconManager=new IconManager();
		Texture hotbarPart = new Texture(new File(ClientLauncher.assetFolder, "Hotbar Slot.png"), 0, null);
		for(int i = 0; i<20; i++)components.addComponent(makeHotBarPart(i, hotbarPart));
		components2.addComponent(createHotbarSelector());
		components2.addComponent(createWheelGlass());
		updateHotbarSelector(0);
	}
	public void render(){
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
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
	}
	private void renderRotationIcons(){
		while(goalWheelRotation<0)goalWheelRotation+=24;
		goalWheelRotation%=24;
		if(rotations[goalWheelRotation]==null)return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -0.5f*ROTATION_BLOCK_ZOOM, 0);
		float r;
		for(int i = 0; i<24; i++){
			r=realWheelRotation-(i*15+7.5f);
			if(r>100&&r<260)continue;
			GL11.glPushMatrix();
			GL11.glRotatef(r, 0, 0, 1);
			GL11.glTranslatef(0, (i==goalWheelRotation?0.2f:0)+WHEEL_SIZE/2f/Loop.screenRes.height*ROTATION_BLOCK_ZOOM-1.5f, 0);
			GL11.glRotatef(-r, 0, 0, 1);
			GL11.glRotatef(loop.getCamera().rx, 1, 0, 0);
			GL11.glRotatef(loop.getCamera().ry, 0, 1, 0);
			rotations[i].render(0, 0, 0, -BlockIcon.BLOCK_PITCH	, -BlockIcon.BLOCK_YAW);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		MatrixUtils.setupOrtho(WIREFRAME_BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, WIREFRAME_BLOCK_ZOOM, -1000, 1000);
		renderWireFrame();
	}
	private void renderWireFrame(){
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (-0.5f+WHEEL_SIZE*0.175f/Loop.screenRes.height)*WIREFRAME_BLOCK_ZOOM, 0);
		GL11.glRotatef(loop.getCamera().rx, 1, 0, 0);
		GL11.glRotatef(loop.getCamera().ry, 0, 1, 0);
		rotations[goalWheelRotation].render(0, 0, 0, -BlockIcon.BLOCK_PITCH	, -BlockIcon.BLOCK_YAW);
		drawWires();
		GL11.glPopMatrix();
	}
	public void updateHotbarSelector(int id){
		iconManager.selectedSlot=id;
		setHotbarSelectorLocation(components2.getComponent(0), id);
		updateBlockRotations();
	}
	private void updateBlockRotations(){
		if(iconManager.getSelectedShape()==null){
			for(int i = 0; i<24; i++)rotations[i]=null;
			return;
		}
		for(int i = 0; i<24; i++)rotations[i]=new BlockIcon(iconManager.getSelectedShape(), iconManager.getSelectedCubeTextures(), BlockRotation.getRotation(i));
	}
	public BlockRotation getSelectedRotation(){ return BlockRotation.getRotation(goalWheelRotation); }
	private static float rotationSpeed = 10;
	public void update(double delta){ realWheelRotation=(float)(((((((goalWheelRotation*15+7.5f)-realWheelRotation)%360)+540)%360)-180)*delta*rotationSpeed)+realWheelRotation; }
	public void addIcon(int id, BlockIcon icon){ iconManager.addIcon(icon, id); }
	public BlockShape getSelectedShape(){ return iconManager.getSelectedShape(); }
	public CubeTextures getSelectedCubeTextures(){ return iconManager.getSelectedCubeTextures(); }
	public int getHotbarSelectorId(){ return iconManager.selectedSlot; }
	public void rebuildBlockIcon(int id){ iconManager.rebuildIcon(id); }
	public IconManager getIconManager(){ return iconManager; }
	private static GLGuiImage createCursor(){
		GLGuiImage i = new GLGuiImage(new File(ClientLauncher.assetFolder, "Cursor.png"));
		i.w=CURSOR_SIZE;
		i.h=CURSOR_SIZE;
		i.x=Loop.screenRes.width/2f-i.w/2f;
		i.y=Loop.screenRes.height/2f-i.h/2f;
		return i;
	}
	private static GLGuiImage makeHotBarPart(int id, Texture texture){
		GLGuiImage i = new GLGuiImage(texture);
		i.w=HOTBAR_SLOT;
		i.h=HOTBAR_SLOT;
		i.y=(Loop.screenRes.height-i.h*10)/2+i.h*(id%10);
		if(id<10)i.x=Loop.screenRes.width-i.w;
		else i.x=0;
		return i;
	}
	private static GLGuiImage createHotbarSelector(){
		GLGuiImage i = new GLGuiImage(new File(ClientLauncher.assetFolder, "Block Selector.png"));
		i.w=HOTBAR_SLOT;
		i.h=HOTBAR_SLOT;
		return i;
	}
	private static void setHotbarSelectorLocation(GLGuiImage i, int id){
		i.y=(Loop.screenRes.height-i.h*10)/2+i.h*(9-id%10);
		if(id<10)i.x=Loop.screenRes.width-i.w;
		else i.x=0;
	}
	private static GLGuiImage createWheelMain(){
		GLGuiImage i = new GLGuiImage(new File(ClientLauncher.assetFolder, "Wheel Main.png"));
		i.w=WHEEL_SIZE;
		i.h=WHEEL_SIZE;
		return i;
	}
	private static GLGuiImage createWheelGlass(){
		GLGuiImage i = new GLGuiImage(new File(ClientLauncher.assetFolder, "Wheel Glass.png"));
		i.w=WHEEL_SIZE;
		i.h=WHEEL_SIZE;
		i.x=Loop.screenRes.width/2-i.w/2;
		i.y=-i.h/2;
		return i;
	}
	private static void drawWires(){
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4f(0, 0, 0, OUTLINE_INTENSITY);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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
}