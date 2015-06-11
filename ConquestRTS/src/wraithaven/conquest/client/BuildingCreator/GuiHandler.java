package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.GameWorld.WindowUtil.FileChooserResponse;
import wraithaven.conquest.client.GameWorld.WindowUtil.FileChooser;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UI;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.UiElement;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.GLGuiComponents;
import wraithaven.conquest.client.GameWorld.GLGuiImage;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

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
		i.w = GuiHandler.CURSOR_SIZE;
		i.h = GuiHandler.CURSOR_SIZE;
		i.x = Loop.screenRes.width/2f-i.w/2f;
		i.y = Loop.screenRes.height/2f-i.h/2f;
		return i;
	}
	private static GLGuiImage createHotbarSelector(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Block Selector.png");
		i.w = GuiHandler.HOTBAR_SLOT;
		i.h = GuiHandler.HOTBAR_SLOT;
		return i;
	}
	private static GLGuiImage createWheelGlass(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Wheel Glass.png");
		i.w = GuiHandler.WHEEL_SIZE;
		i.h = GuiHandler.WHEEL_SIZE;
		i.x = Loop.screenRes.width/2-i.w/2;
		i.y = -i.h/2;
		return i;
	}
	private static GLGuiImage createWheelMain(){
		GLGuiImage i = new GLGuiImage(ClientLauncher.assetFolder, "Wheel Main.png");
		i.w = GuiHandler.WHEEL_SIZE;
		i.h = GuiHandler.WHEEL_SIZE;
		return i;
	}
	private static void drawWires(){
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4f(0, 0, 0, GuiHandler.OUTLINE_INTENSITY);
		GL11.glBegin(GL11.GL_QUADS);
		float sx = -GuiHandler.OUTLINE_BUFFER;
		float sy = -GuiHandler.OUTLINE_BUFFER;
		float sz = -GuiHandler.OUTLINE_BUFFER;
		float ex = 1+GuiHandler.OUTLINE_BUFFER;
		float ey = 1+GuiHandler.OUTLINE_BUFFER;
		float ez = 1+GuiHandler.OUTLINE_BUFFER;
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
		i.w = GuiHandler.HOTBAR_SLOT;
		i.h = GuiHandler.HOTBAR_SLOT;
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
	private boolean pauseMenu;
	private double mouseX, mouseY;
	private final UiElement exitButton, saveButton, loadButton, settingsButton, resumeButton;
	public GuiHandler(){
		components = new GLGuiComponents(Loop.screenRes);
		components2 = new GLGuiComponents(Loop.screenRes);
		components3 = new GLGuiComponents(Loop.screenRes);
		components.addComponent(GuiHandler.createCursor());
		components3.addComponent(GuiHandler.createWheelMain());
		iconManager = new IconManager();
		Texture hotbarPart = Texture.getTexture(ClientLauncher.assetFolder, "Hotbar Slot.png");
		for(int i = 0; i<20; i++)
			components.addComponent(GuiHandler.makeHotBarPart(i, hotbarPart));
		components2.addComponent(GuiHandler.createHotbarSelector());
		components2.addComponent(GuiHandler.createWheelGlass());
		updateHotbarSelector(0);
		displayListId = GL11.glGenLists(1);
		GL11.glNewList(displayListId, GL11.GL_COMPILE);
		GuiHandler.drawWires();
		GL11.glEndList();
		exitButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "Building Creator Exit Button.png"));
		resumeButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "Building Creator Resume Button.png"));
		saveButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "Building Creator Save Button.png"));
		loadButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "Building Creator Load Button.png"));
		settingsButton = new UiElement(Texture.getTexture(ClientLauncher.assetFolder, "Building Creator Settings Button.png"));
		preparePauseButtons();
	}
	private void preparePauseButtons(){
		int width = 200;
		int height = 100;
		exitButton.w = width;
		exitButton.h = height;
		resumeButton.w = width;
		resumeButton.h = height;
		saveButton.w = width;
		saveButton.h = height;
		loadButton.w = width;
		loadButton.h = height;
		settingsButton.w = width;
		settingsButton.h = height;
		final int buttonSpacing = 20;
		float x1 = Loop.screenRes.width/2f-width-buttonSpacing/2f;
		float x2 = Loop.screenRes.width/2f+buttonSpacing/2f;
		float x3 = Loop.screenRes.width/2f-width/2f;
		float y1 = Loop.screenRes.height/2f+buttonSpacing+height/2f;
		float y2 = Loop.screenRes.height/2f-height/2f;
		float y3 = Loop.screenRes.height/2f-height-buttonSpacing-height/2f;
		exitButton.x = x3;
		exitButton.y = y3;
		resumeButton.x = x1;
		resumeButton.y = y1;
		settingsButton.x = x2;
		settingsButton.y = y1;
		saveButton.x = x1;
		saveButton.y = y2;
		loadButton.x = x2;
		loadButton.y = y2;
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
		if(Loop.INSTANCE.getInputController().wireframeMode)return;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		components.render();
		GL11.glPushMatrix();
		GL11.glTranslatef(Loop.screenRes.width/2f, 0, 0);
		GL11.glRotatef(realWheelRotation, 0, 0, 1);
		GL11.glTranslatef(-GuiHandler.WHEEL_SIZE/2, -GuiHandler.WHEEL_SIZE/2, 0);
		components3.render();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		MatrixUtils.setupOrtho(GuiHandler.ROTATION_BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, GuiHandler.ROTATION_BLOCK_ZOOM, -1000, 1000);
		renderRotationIcons();
		MatrixUtils.setupOrtho(BlockIcon.BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, BlockIcon.BLOCK_ZOOM, -1000, 1000);
		iconManager.render();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		components2.render();
		if(pauseMenu){
			UI.renderElement(exitButton);
			UI.renderElement(resumeButton);
			UI.renderElement(saveButton);
			UI.renderElement(loadButton);
			UI.renderElement(settingsButton);
		}
	}
	private void renderRotationIcons(){
		while(goalWheelRotation<0)
			goalWheelRotation += 24;
		goalWheelRotation %= 24;
		if(rotations[goalWheelRotation]==null)return;
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -0.5f*GuiHandler.ROTATION_BLOCK_ZOOM, 0);
		float r;
		for(int i = 0; i<24; i++){
			r = realWheelRotation-(i*15+7.5f);
			if(r>100&&r<260)continue;
			GL11.glPushMatrix();
			GL11.glRotatef(r, 0, 0, 1);
			GL11.glTranslatef(0, (i==goalWheelRotation?0.2f:0)+GuiHandler.WHEEL_SIZE/2f/Loop.screenRes.height*GuiHandler.ROTATION_BLOCK_ZOOM-1.5f, 0);
			GL11.glRotatef(-r, 0, 0, 1);
			GL11.glRotatef(Loop.INSTANCE.getCamera().rx, 1, 0, 0);
			GL11.glRotatef(Loop.INSTANCE.getCamera().ry, 0, 1, 0);
			rotations[i].render(0, 0, 0, -BlockIcon.BLOCK_PITCH, -BlockIcon.BLOCK_YAW);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		MatrixUtils.setupOrtho(GuiHandler.WIREFRAME_BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, GuiHandler.WIREFRAME_BLOCK_ZOOM, -1000, 1000);
		renderWireFrame();
	}
	private void renderWireFrame(){
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (-0.5f+GuiHandler.WHEEL_SIZE*0.175f/Loop.screenRes.height)*GuiHandler.WIREFRAME_BLOCK_ZOOM, 0);
		GL11.glRotatef(Loop.INSTANCE.getCamera().rx, 1, 0, 0);
		GL11.glRotatef(Loop.INSTANCE.getCamera().ry, 0, 1, 0);
		rotations[goalWheelRotation].render(0, 0, 0, -BlockIcon.BLOCK_PITCH, -BlockIcon.BLOCK_YAW);
		GL11.glCallList(displayListId);
		GL11.glPopMatrix();
	}
	public void update(double delta){
		realWheelRotation = (float)(((((((goalWheelRotation*15+7.5f)-realWheelRotation)%360)+540)%360)-180)*delta*GuiHandler.rotationSpeed)+realWheelRotation;
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
		GuiHandler.setHotbarSelectorLocation(components2.getComponent(0), id);
		updateBlockRotations();
	}
	public void togglePauseMenu(){
		pauseMenu = !pauseMenu;
		if(pauseMenu)GLFW.glfwSetInputMode(Loop.INSTANCE.getBuildingCreator().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		else{
			GLFW.glfwSetCursorPos(Loop.INSTANCE.getBuildingCreator().getWindow(), Loop.screenRes.width/2.0, Loop.screenRes.height/2.0);
			GLFW.glfwSetInputMode(Loop.INSTANCE.getBuildingCreator().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
		}
	}
	public boolean isPaused(){
		return pauseMenu;
	}
	public void onMouseDown(double x, double y){
		mouseX = x;
		mouseY = Loop.screenRes.height-y;
		processMouseClick(true);
	}
	public void onMouseMove(double x, double y){
		mouseX = x;
		mouseY = Loop.screenRes.height-y;
	}
	public void onMouseUp(){
		processMouseClick(false);
	}
	private void processMouseClick(boolean begin){
		//TODO Take care of button down events.
		if(begin)return;
		if(mouseX>=exitButton.x
				&&mouseX<exitButton.x+exitButton.w
				&&mouseY>=exitButton.y
				&&mouseY<exitButton.y+exitButton.h){
			GLFW.glfwSetWindowShouldClose(Loop.INSTANCE.getWindow(), GL11.GL_TRUE);
			return;
		}
		if(mouseX>=resumeButton.x
				&&mouseX<resumeButton.x+resumeButton.w
				&&mouseY>=resumeButton.y
				&&mouseY<resumeButton.y+resumeButton.h){
			togglePauseMenu();
			return;
		}
		if(mouseX>=saveButton.x
				&&mouseX<saveButton.x+saveButton.w
				&&mouseY>=saveButton.y
				&&mouseY<saveButton.y+saveButton.h){
			new FileChooser(new FileChooserResponse(){
				public void run(File file){
					SaveSystem.save(file);
				}
			}, true, ".building");
			return;
		}
		if(mouseX>=loadButton.x
				&&mouseX<loadButton.x+loadButton.w
				&&mouseY>=loadButton.y
				&&mouseY<loadButton.y+loadButton.h){
			new FileChooser(new FileChooserResponse(){
				public void run(File file){
					SaveSystem.load(file);
				}
			}, false, ".building");
			return;
		}
	}
}