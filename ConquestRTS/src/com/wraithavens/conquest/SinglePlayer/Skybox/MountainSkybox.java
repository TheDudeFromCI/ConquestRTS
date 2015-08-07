package com.wraithavens.conquest.SinglePlayer.Skybox;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Heightmap.HeightMap;

public class MountainSkybox{
	private static void prepareRender(int vbo, int ibo){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	private static int TextureSize;
	private static final int FramesPerSide = 5;
	private final int colorTexture;
	private final int frameBuffer;
	private final int renderBuffer;
	private final MountainRenderer renderer;
	private final Vector3f cameraLocation = new Vector3f();
	private boolean redrawPending = false;
	private int redrawFrame = -1;
	private int step;
	public MountainSkybox(MountainRenderer renderer){
		TextureSize =
			Math.max(WraithavensConquest.INSTANCE.getScreenHeight(),
				WraithavensConquest.INSTANCE.getScreenWidth());
		this.renderer = renderer;
		colorTexture = GL11.glGenTextures();
		frameBuffer = GL30.glGenFramebuffers();
		renderBuffer = GL30.glGenRenderbuffers();
		createTextures();
		buildFrameBuffer();
	}
	private void buildFrameBuffer(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
			GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, colorTexture, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, TextureSize, TextureSize);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
			renderBuffer);
		if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)!=GL30.GL_FRAMEBUFFER_COMPLETE){
			// TODO Look into alternative rendering methods.
			System.out.println("FRAME BUFFER NOT SUPPORTED!!!");
		}
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
	}
	private void createTextures(){
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, colorTexture);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		for(int i = 0; i<6; i++)
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL11.GL_RGBA8, TextureSize, TextureSize,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
	}
	private void redraw(int frame, boolean clear){
		GL11.glViewport(0, 0, TextureSize, TextureSize);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
			GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+frame, colorTexture, 0);
		if(clear)
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		switch(frame){
			case 0:
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glRotatef(180, 0, 0, 1);
				break;
			case 1:
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glRotatef(180, 0, 0, 1);
				break;
			case 2:
				GL11.glRotatef(-90, 1, 0, 0);
				break;
			case 3:
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 4:
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glRotatef(180, 0, 0, 1);
				break;
			case 5:
				GL11.glRotatef(180, 0, 0, 1);
				break;
		}
		GL11.glTranslatef(cameraLocation.x, cameraLocation.y, cameraLocation.z);
		renderer.renderSkybox();
		GL11.glPopMatrix();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		GL11.glViewport(0, 0, WraithavensConquest.INSTANCE.getScreenWidth(),
			WraithavensConquest.INSTANCE.getScreenHeight());
	}
	void dispose(){
		GL11.glDeleteTextures(colorTexture);
		GL30.glDeleteFramebuffers(frameBuffer);
		GL30.glDeleteRenderbuffers(renderBuffer);
	}
	boolean isDrawing(){
		return redrawFrame>-1||redrawPending;
	}
	void redraw(){
		redrawPending = true;
	}
	void render(int vbo, int ibo){
		prepareRender(vbo, ibo);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, colorTexture);
		GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
	}
	void renderMesh(){
		renderer.renderMesh();
	}
	void update(){
		if(redrawFrame>-1){
			int rows = (int)Math.ceil((HeightMap.VertexCount-1.0f)/FramesPerSide);
			int rowOffset = rows*step;
			rows = Math.min(rows, HeightMap.VertexCount-1-rowOffset);
			rows = rows*(HeightMap.VertexCount*2+2)-2;
			rowOffset = rowOffset*(HeightMap.VertexCount*2+2)*4;
			renderer.getHeightmap().setOffset(rows, rowOffset);
			redraw(redrawFrame, step==0);
			step++;
			if(step==FramesPerSide){
				redrawFrame--;
				step = 0;
			}
		}else if(redrawPending){
			redrawPending = false;
			cameraLocation.set(-renderer.getCameraX(), -renderer.getCameraY(), -renderer.getCameraZ());
			redrawFrame = 5;
			step = 0;
		}
	}
}
