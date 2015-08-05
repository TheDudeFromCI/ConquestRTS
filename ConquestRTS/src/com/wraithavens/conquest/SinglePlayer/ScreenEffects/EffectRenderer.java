package com.wraithavens.conquest.SinglePlayer.ScreenEffects;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class EffectRenderer{
	private final int frameBufferId;
	private final int renderBufferId;
	private final int textureId;
	private final int vboId;
	private final int iboId;
	private final ShaderProgram shader;
	public EffectRenderer(){
		frameBufferId = GL30.glGenFramebuffers();
		renderBufferId = GL30.glGenRenderbuffers();
		textureId = GL11.glGenTextures();
		vboId = GL15.glGenBuffers();
		iboId = GL15.glGenBuffers();
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "FrameShader.vert"), null, new File(
				WraithavensConquest.assetFolder, "FrameShader.frag"));
		shader.bind();
		shader.loadUniforms("texture");
		shader.setUniform1I(0, 0);
		build();
	}
	public void begin(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBufferId);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBufferId);
	}
	public void end(){
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.bind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 8, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	private void build(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, WraithavensConquest.INSTANCE.getScreenWidth(),
			WraithavensConquest.INSTANCE.getScreenHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
			(ByteBuffer)null);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBufferId);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D,
			textureId, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBufferId);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24,
			WraithavensConquest.INSTANCE.getScreenWidth(), WraithavensConquest.INSTANCE.getScreenHeight());
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
			renderBufferId);
		if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)!=GL30.GL_FRAMEBUFFER_COMPLETE){
			// TODO Look into alternative rendering methods.
			System.out.println("FRAME BUFFER NOT SUPPORTED!!!");
		}
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(8);
		vertexData.put(-1.0f).put(-1.0f);
		vertexData.put(-1.0f).put(1.0f);
		vertexData.put(1.0f).put(1.0f);
		vertexData.put(1.0f).put(-1.0f);
		vertexData.flip();
		ByteBuffer indexData = BufferUtils.createByteBuffer(6);
		indexData.put((byte)0).put((byte)1).put((byte)2);
		indexData.put((byte)0).put((byte)2).put((byte)3);
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
}
