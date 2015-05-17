package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.io.File;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import com.sun.javafx.geom.Vec3f;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector4f;
import wraithaven.conquest.client.GameWorld.LoopControls.Matrix4f;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.LoopControls.MatrixUtils;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class UI{
	private boolean mouseDown, mouseOnRed, mouseOnGreen, mouseOnBlue, mouseOnTemplate, mouseOnTextures;
	private double xOffset, yOffset;
	private HorizontalGemSlider redGem;
	private HorizontalGemSlider greenGem;
	private HorizontalGemSlider blueGem;
	private HorizontalGemSlider textureGem;
	private VerticalGemSlider templateGem;
	private double mouseDownX, mouseDownY;
	private final CubeTextures cubeTextures = new CubeTextures();
	private final UiElement background;
	private final FloatingBlock floatingBlock;
	private final Shapes shapes;
	private final Textures textures;
	private static final float MOUSE_SENSITIVITY = 0.5f;
	public UI(){
		background=new UiElement(new Texture(new File(ClientLauncher.assetFolder, "Block Palette Background.png"), 0, null));
		floatingBlock=new FloatingBlock();
		shapes=new Shapes();
		floatingBlock.z=-3;
		floatingBlock.x=-0.5f;
		floatingBlock.y=0.7f;
		{
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
			background.w=(float)width;
			background.h=(float)height;
			background.x=(float)((Loop.screenRes.width-width)/2);
			background.y=(float)((Loop.screenRes.height-height)/2);
			redGem=new HorizontalGemSlider(576/768f*(float)height, 668/1024f*(float)width, 888/1024f*(float)width, 888/1024f*(float)width, "Red Gem.png", 25, 36, background.x, background.y);
			greenGem=new HorizontalGemSlider(640/768f*(float)height, 668/1024f*(float)width, 888/1024f*(float)width, 888/1024f*(float)width, "Green Gem.png", 25, 36, background.x, background.y);
			blueGem=new HorizontalGemSlider(708/768f*(float)height, 668/1024f*(float)width, 888/1024f*(float)width, 888/1024f*(float)width, "Blue Gem.png", 25, 36, background.x, background.y);
			templateGem=new VerticalGemSlider(149/1024f*(float)width, 122/768f*(float)height, 382/768f*(float)height, "Template Gem.png", 25, 47, background.x, background.y);
			textureGem=new HorizontalGemSlider(701/768f*(float)height, 121/1024f*(float)width, 298/1024f*(float)width, 121/1024f*(float)width, "Textures Gem.png", 35, 19, background.x, background.y);
			textures=new Textures((float)width, (float)height);
		}
		cubeTextures.xUp=textures.getTexture();
		cubeTextures.xDown=textures.getTexture();
		cubeTextures.yUp=textures.getTexture();
		cubeTextures.yDown=textures.getTexture();
		cubeTextures.zUp=textures.getTexture();
		cubeTextures.zDown=textures.getTexture();
		floatingBlock.block=new ChunklessBlock(floatingBlock, shapes.getCurrentShape(), cubeTextures, BlockRotation.ROTATION_0);
		floatingBlock.block.build();
		for(int i = 0; i<6; i++)floatingBlock.block.optimizeSide(i);
		floatingBlock.rebuildBatches();
	}
	private void updateFloatingBlock(){
		floatingBlock.destroy();
		floatingBlock.block=new ChunklessBlock(floatingBlock, shapes.getCurrentShape(), cubeTextures, BlockRotation.ROTATION_0);
		floatingBlock.block.build();
		for(int i = 0; i<6; i++)floatingBlock.block.optimizeSide(i);
		floatingBlock.rebuildBatches();
	}
	public void render(){
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		MatrixUtils.setupImageOrtho(Loop.screenRes.width, Loop.screenRes.height, -1, 1);
		renderElement(background);
		renderElement(redGem.gem);
		renderElement(greenGem.gem);
		renderElement(blueGem.gem);
		renderElement(templateGem.gem);
		renderElement(textureGem.gem);
		textures.render();
		MatrixUtils.setupOrtho(Shapes.BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, Shapes.BLOCK_ZOOM, -1000, 1000);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shapes.render();
		MatrixUtils.setupOrtho(BLOCK_ZOOM*Loop.screenRes.width/Loop.screenRes.height, BLOCK_ZOOM, -1000, 1000);
		GL11.glTranslatef(-0.6f, 1, -3);
		GL11.glRotatef(shiftPitch, 1, 0, 0);
		GL11.glRotatef(shiftYaw, 0, 1, 0);
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
		{
			modelView.position(0);
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
			modelViewMatrix.load(modelView);
		}
		floatingBlock.render();
	}
	private final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private float shiftYaw, shiftPitch;
	public void update(double time){ shapes.update(time); }
	public void onMouseMove(double x, double y){
		if(mouseDown){
			x-=background.x;
			y=Loop.screenRes.height-(y-background.y);
			if(mouseOnRed)redGem.setSliderPosition(x-xOffset);
			else if(mouseOnGreen)greenGem.setSliderPosition(x-xOffset);
			else if(mouseOnBlue)blueGem.setSliderPosition(x-xOffset);
			else if(mouseOnTemplate){
				templateGem.setSliderPosition(Loop.screenRes.height-(y+yOffset));
				shapes.updateScrollbar(templateGem.sliderPercent);
			}else if(mouseOnTextures){
				textureGem.setSliderPosition(x-xOffset);
				textures.updateSliderPosition(textureGem.sliderPercent);
			}else{
				shiftYaw+=(x-mouseDownX)*MOUSE_SENSITIVITY;
				shiftPitch-=(y-mouseDownY)*MOUSE_SENSITIVITY;
				if(shiftPitch>90)shiftPitch=90;
				if(shiftPitch<-90)shiftPitch=-90;
			}
			mouseDownX=x;
			mouseDownY=y;
		}
		mouseMove=true;
		if(mouseOnRed||mouseOnGreen||mouseOnBlue)textures.updateColor(redGem.sliderPercent, greenGem.sliderPercent, blueGem.sliderPercent);
	}
	private boolean mouseMove;
	private double mouseX, mouseY;
	public void onMouseDown(double x, double y){
		if(textures.mouseClick(x, y))return;
		if(shapes.checkMouseClick(x, y)){
			updateFloatingBlock();
			return;
		}
		mouseMove=false;
		mouseDown=true;
		mouseX=x;
		mouseY=y;
		x-=background.x;
		if(x>=templateGem.xPosition&&x<templateGem.xPosition+templateGem.width&&y>=templateGem.currentY&&y<templateGem.currentY+templateGem.height){
			mouseOnTemplate=true;
			yOffset=y-templateGem.currentY;
		}
		y=Loop.screenRes.height-(y-background.y);
		this.mouseDownX=x;
		this.mouseDownY=y;
		if(x>=redGem.currentX&&x<redGem.currentX+redGem.width&&y>=redGem.yPosition&&y<redGem.yPosition+redGem.height){
			mouseOnRed=true;
			xOffset=x-redGem.currentX;
		}
		if(x>=greenGem.currentX&&x<greenGem.currentX+greenGem.width&&y>=greenGem.yPosition&&y<greenGem.yPosition+greenGem.height){
			mouseOnGreen=true;
			xOffset=x-greenGem.currentX;
		}
		if(x>=blueGem.currentX&&x<blueGem.currentX+blueGem.width&&y>=blueGem.yPosition&&y<blueGem.yPosition+blueGem.height){
			mouseOnBlue=true;
			xOffset=x-blueGem.currentX;
		}
		if(x>=textureGem.currentX&&x<textureGem.currentX+textureGem.width&&y>=textureGem.yPosition&&y<textureGem.yPosition+textureGem.height){
			mouseOnTextures=true;
			xOffset=x-textureGem.currentX;
		}
	}
	public void onMouseUp(int button){
		mouseDown=false;
		mouseOnRed=false;
		mouseOnGreen=false;
		mouseOnBlue=false;
		mouseOnTemplate=false;
		mouseOnTextures=false;
		if(!mouseMove)checkForRaytrace(mouseX, mouseY, button);		
	}
	private void retextureSide(Quad q, int button){
		if(button==GLFW.GLFW_MOUSE_BUTTON_LEFT){
			if(q.side==0)cubeTextures.xUp=textures.getTexture();
			if(q.side==1)cubeTextures.xDown=textures.getTexture();
			if(q.side==2)cubeTextures.yUp=textures.getTexture();
			if(q.side==3)cubeTextures.yDown=textures.getTexture();
			if(q.side==4)cubeTextures.zUp=textures.getTexture();
			if(q.side==5)cubeTextures.zDown=textures.getTexture();
			cubeTextures.colors[q.side*3]=redGem.sliderPercent;
			cubeTextures.colors[q.side*3+1]=greenGem.sliderPercent;
			cubeTextures.colors[q.side*3+2]=blueGem.sliderPercent;
		}else if(button==GLFW.GLFW_MOUSE_BUTTON_RIGHT){
			if(q.side==0)cubeTextures.xUpRotation=(cubeTextures.xUpRotation+1)%4;
			if(q.side==1)cubeTextures.xDownRotation=(cubeTextures.xDownRotation+1)%4;
			if(q.side==2)cubeTextures.yUpRotation=(cubeTextures.yUpRotation+1)%4;
			if(q.side==3)cubeTextures.yDownRotation=(cubeTextures.yDownRotation+1)%4;
			if(q.side==4)cubeTextures.zUpRotation=(cubeTextures.zUpRotation+1)%4;
			if(q.side==5)cubeTextures.zDownRotation=(cubeTextures.zDownRotation+1)%4;
		}
		updateFloatingBlock();
	}
	private static final float BLOCK_ZOOM = 5;
	private final Vec3f pos = new Vec3f();
	private final Vec3f dir = new Vec3f();
	private boolean checkForRaytrace(double x, double y, int button){
		y=Loop.screenRes.height-y;
		x-=Loop.screenRes.width/2.0;
		y-=Loop.screenRes.height/2.0;
		x/=Loop.screenRes.width/2.0;
		y/=Loop.screenRes.height/2.0;
		x*=(BLOCK_ZOOM/2)*Loop.screenRes.width/Loop.screenRes.height;
		y*=(BLOCK_ZOOM/2);
		pos.set((float)x,(float) y, 100);
		dir.set(0, 0, -1);
		int i, j, k;
		Quad q = null;
		float dis = 0;
		float d;
		Vec3f v1 = new Vec3f();
		Vec3f v2 = new Vec3f();
		Vec3f v3 = new Vec3f();
		Quad tempQuad;
		for(i=0; i<floatingBlock.batches.size(); i++){
			for(j=0; j<floatingBlock.batches.get(i).getSize(); j++){
				tempQuad=floatingBlock.batches.get(i).getQuad(j);
				for(k=0; k<2; k++){
					if(k==0){
						v1.set(tempQuad.data.get(25), tempQuad.data.get(26), tempQuad.data.get(27));
						v2.set(tempQuad.data.get(28), tempQuad.data.get(29), tempQuad.data.get(30));
						v3.set(tempQuad.data.get(31), tempQuad.data.get(32), tempQuad.data.get(33));
					}else{
						v1.set(tempQuad.data.get(25), tempQuad.data.get(26), tempQuad.data.get(27));
						v2.set(tempQuad.data.get(31), tempQuad.data.get(32), tempQuad.data.get(33));
						v3.set(tempQuad.data.get(34), tempQuad.data.get(35), tempQuad.data.get(36));
					}
					morph(v1);
					morph(v2);
					morph(v3);
					if((d=rayIntersectsTriangle(v1, v2, v3))<dis||q==null){
						q=tempQuad;
						dis=d;
					}
				}
			}
		}
		if(dis==Float.MAX_VALUE||q==null)return false;
		retextureSide(q, button);
		return true;
	}
	private final Vector4f tempVector = new Vector4f();
	private void morph(Vec3f vec){
		tempVector.set(vec.x, vec.y, vec.z, 1);
		Matrix4f.transform(modelViewMatrix, tempVector, tempVector);
		vec.x=tempVector.x;
		vec.y=tempVector.y;
		vec.z=tempVector.z;
	}
	static void renderElement(UiElement ele){
		GL11.glPushMatrix();
		ele.texture.bind();
		GL11.glTranslatef(ele.x, ele.y, 0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, 0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(ele.w, 0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(ele.w, ele.h, 0);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, ele.h, 0);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	private float rayIntersectsTriangle(Vec3f vertex1, Vec3f vertex2, Vec3f vertex3){
		Vec3f edge1 = new Vec3f(vertex2);
		edge1.sub(vertex1);
		Vec3f edge2 = new Vec3f(vertex3);
		edge2.sub(vertex1);
		Vec3f directionCrossEdge2 = new Vec3f();
		directionCrossEdge2.cross(dir, edge2);
		float determinant = directionCrossEdge2.dot(edge1);
		if(determinant>-0.0000001f&&determinant<0.0000001f)return Float.MAX_VALUE;
		float inverseDeterminant = 1.0f/determinant;
		Vec3f distanceVector = new Vec3f(pos);
		distanceVector.sub(vertex1);
		float triangleU = directionCrossEdge2.dot(distanceVector);
		triangleU*=inverseDeterminant;
		if(triangleU<0||triangleU>1)return Float.MAX_VALUE;
		Vec3f distanceCrossEdge1 = new Vec3f();
		distanceCrossEdge1.cross(distanceVector, edge1);
		float triangleV = dir.dot(distanceCrossEdge1);
		triangleV*=inverseDeterminant;
		if(triangleV<0||triangleU+triangleV>1)return Float.MAX_VALUE;
		float rayDistance = distanceCrossEdge1.dot(edge2);
		rayDistance*=inverseDeterminant;
		if(rayDistance<0)return Float.MAX_VALUE;
		return rayDistance;
	}
}