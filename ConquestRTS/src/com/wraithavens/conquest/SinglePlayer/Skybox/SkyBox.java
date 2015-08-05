package com.wraithavens.conquest.SinglePlayer.Skybox;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class SkyBox{
	private final SkyboxClouds layer0;
	private final Sunbox layer1;
	private final SkyboxClouds layer2;
	private final ShaderProgram shader;
	private final int vboId;
	private final int iboId;
	public SkyBox(SkyboxClouds layer0, Sunbox layer1, SkyboxClouds layer2){
		this.layer0 = layer0;
		this.layer1 = layer1;
		this.layer2 = layer2;
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "SkyboxShader.vert"), null, new File(
				WraithavensConquest.assetFolder, "SkyboxShader.frag"));
		shader.bind();
		shader.loadUniforms("texture");
		shader.setUniform1I(0, 0);
		vboId = GL15.glGenBuffers();
		iboId = GL15.glGenBuffers();
		buildVbo();
	}
	public void render(){
		// ---
		// First we disable depth testing. This way, we can make the sky boxes
		// look infinitly far away without having to scale them and lose detail,
		// or having to clear the depth buffer have each pass. Then update the
		// projection matrix.
		// ---
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()
			/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 0.001f, 5f);
		// ---
		// Now render each layer in order.
		// TODO Make mountains render to sky box as a "layer 3".
		// ---
		shader.bind();
		if(layer0!=null)
			layer0.render(vboId, iboId);
		if(layer1!=null)
			layer1.render();
		if(layer2!=null)
			layer2.render(vboId, iboId);
		// ---
		// Now reable this for rendering with the rest of the game.
		// ---
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	private void buildVbo(){
		ByteBuffer indexData = BufferUtils.createByteBuffer(24);
		indexData.put((byte)1).put((byte)5).put((byte)7).put((byte)3);
		indexData.put((byte)2).put((byte)0).put((byte)4).put((byte)6);
		indexData.put((byte)4).put((byte)5).put((byte)7).put((byte)6);
		indexData.put((byte)0).put((byte)1).put((byte)3).put((byte)2);
		indexData.put((byte)0).put((byte)1).put((byte)5).put((byte)4);
		indexData.put((byte)3).put((byte)2).put((byte)6).put((byte)7);
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(24);
		float n = (float)(1.0/Math.sqrt(3));
		vertexData.put(-n).put(-n).put(-n);
		vertexData.put(n).put(-n).put(-n);
		vertexData.put(-n).put(n).put(-n);
		vertexData.put(n).put(n).put(-n);
		vertexData.put(-n).put(-n).put(n);
		vertexData.put(n).put(-n).put(n);
		vertexData.put(-n).put(n).put(n);
		vertexData.put(n).put(n).put(n);
		vertexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
	}
}
