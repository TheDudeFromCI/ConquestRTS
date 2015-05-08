package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.io.File;
import org.lwjgl.opengl.GL11;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.GLGuiImage;
import wraithaven.conquest.client.GameWorld.GLGuiComponents;
import wraithaven.conquest.client.GameWorld.BlockTextures;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Pyramid;

public class PalleteRenderer{
	private boolean mouseDown, mouseOnRed, mouseOnGreen, mouseOnBlue;
	private double xOffset;
	private GemSlider redGem;
	private GemSlider greenGem;
	private GemSlider blueGem;
	private double mouseDownX, mouseDownY;
	private FloatingBlock floatingBlock;
	private GLGuiComponents backgroundGui;
	private static final float MOUSE_SENSITIVITY = 0.5f;
	public PalleteRenderer(){
		floatingBlock=new FloatingBlock();
		floatingBlock.z=-3;
		floatingBlock.x=-0.5f;
		floatingBlock.y=0.7f;
		backgroundGui=new GLGuiComponents(Loop.screenRes);
		redGem=new GemSlider(156, 840, 1055, "Red Gem.png");
		greenGem=new GemSlider(91, 840, 1055, "Green Gem.png");
		blueGem=new GemSlider(23, 840, 1055, "Blue Gem.png");
		backgroundGui.addComponent(makeBackground());
		backgroundGui.addComponent(redGem.gem);
		backgroundGui.addComponent(greenGem.gem);
		backgroundGui.addComponent(blueGem.gem);
		CubeTextures textures = new CubeTextures();
		textures.xUp=BlockTextures.sideDirt.getTexture();
		textures.xUpRotation=0;
		textures.xDown=BlockTextures.sideDirt.getTexture();
		textures.xDownRotation=1;
		textures.yUp=BlockTextures.grass.getTexture();
		textures.yUpRotation=0;
		textures.yDown=BlockTextures.dirt.getTexture();
		textures.yDownRotation=0;
		textures.zUp=BlockTextures.sideDirt.getTexture();
		textures.zUpRotation=3;
		textures.zDown=BlockTextures.sideDirt.getTexture();
		textures.zDownRotation=2;
		floatingBlock.block=new ChunklessBlock(floatingBlock, new Pyramid(), textures);
		floatingBlock.block.build();
		for(int i = 0; i<6; i++)floatingBlock.block.optimizeSide(i);
		floatingBlock.rebuildBatches();
	}
	public void onMouseMove(double x, double y){
		if(mouseDown){
			if(mouseOnRed)redGem.setSliderPosition(x-xOffset);
			else if(mouseOnGreen)greenGem.setSliderPosition(x-xOffset);
			else if(mouseOnBlue)blueGem.setSliderPosition(x-xOffset);
			else{
				floatingBlock.shiftRY+=(x-mouseDownX)*MOUSE_SENSITIVITY;
				floatingBlock.shiftRX+=(y-mouseDownY)*MOUSE_SENSITIVITY;
			}
			mouseDownX=x;
			mouseDownY=y;
		}
	}
	public void onMouseDown(double x, double y){
		this.mouseDownX=x;
		this.mouseDownY=y;
		mouseDown=true;
		y=Loop.screenRes.height-y;
		if(x>=redGem.currentX&&x<redGem.currentX+25&&y>=redGem.yPosition&&y<redGem.yPosition+36){
			mouseOnRed=true;
			xOffset=x-redGem.currentX;
		}
		if(x>=greenGem.currentX&&x<greenGem.currentX+25&&y>=greenGem.yPosition&&y<greenGem.yPosition+36){
			mouseOnGreen=true;
			xOffset=x-greenGem.currentX;
		}
		if(x>=blueGem.currentX&&x<blueGem.currentX+25&&y>=blueGem.yPosition&&y<blueGem.yPosition+36){
			mouseOnBlue=true;
			xOffset=x-blueGem.currentX;
		}
	}
	public void render(){
		GL11.glPushMatrix();
		backgroundGui.render();
		MatrixUtils.setupPerspective(70, Loop.screenRes.width/(float)Loop.screenRes.height, Loop.CAMERA_NEAR_CLIP, 1000);
		GL11.glPopMatrix();
		floatingBlock.render();
	}
	public void onMouseUp(){
		mouseDown=false;
		mouseOnRed=false;
		mouseOnGreen=false;
		mouseOnBlue=false;
	}
	@SuppressWarnings("unused")public void update(double delta, double time){ floatingBlock.update(time); }
	private static GLGuiImage makeBackground(){
		GLGuiImage bg = new GLGuiImage(new File(ClientLauncher.assetFolder, "Block Palette Background.png"));
		double width = 4;
		double height = 3;
		if(Math.abs(Loop.screenRes.width-width)>Math.abs(Loop.screenRes.height-height)){
			double scaleSize = Loop.screenRes.height/height;
			height=Loop.screenRes.height;
			width*=scaleSize;
		}else{
			double scaleSize = Loop.screenRes.width/width;
			width=Loop.screenRes.width;
			height*=scaleSize;
		}
		bg.w=(float)(width/Loop.screenRes.width);
		bg.h=(float)(height/Loop.screenRes.height);
		bg.x=(float)((Loop.screenRes.width-width)/2)/Loop.screenRes.width;
		bg.y=(float)((Loop.screenRes.height-height)/2)/Loop.screenRes.height;
		bg.z=-999;
		return bg;
	}
}