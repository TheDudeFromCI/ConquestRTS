package com.wraithavens.conquest.SinglePlayer.Skybox;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Launcher.WraithavensConquest;

public class MountainSkybox{
	private static void prepareRender(int vbo, int ibo){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	private static final int TextureSize = 1024;
	private final int colorTexture;
	private final int frameBuffer;
	private final int renderBuffer;
	private final MountainRenderer renderer;
	public MountainSkybox(MountainRenderer renderer){
		this.renderer = renderer;
		colorTexture = GL11.glGenTextures();
		frameBuffer = GL30.glGenFramebuffers();
		renderBuffer = GL30.glGenRenderbuffers();
		createTextures();
		buildFrameBuffer();
	}
	public void redraw(){
		GL11.glViewport(0, 0, TextureSize, TextureSize);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
		for(int i = 0; i<6; i++){
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
				GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, colorTexture, 0);
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			switch(i){
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
			GL11.glTranslatef(-renderer.getCameraX(), -renderer.getCameraY(), -renderer.getCameraZ());
			renderer.render();
			GL11.glPopMatrix();
		}
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		GL11.glViewport(0, 0, WraithavensConquest.INSTANCE.getScreenWidth(),
			WraithavensConquest.INSTANCE.getScreenHeight());
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
	void render(int vbo, int ibo){
		prepareRender(vbo, ibo);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, colorTexture);
		GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
	}
}
