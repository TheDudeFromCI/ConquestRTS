package com.wraithavens.conquest.Utility;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class WireframeCube{
	public static void build(){
		Instance = new WireframeCube();
		GlError.out("Created Wireframe cube instance.");
	}
	public static void dipose(){
		if(Instance==null)
			return;
		Instance.disposeObject();
	}
	public static WireframeCube intance(){
		return Instance;
	}
	public static void render(){
		if(Instance==null)
			return;
		Instance.renderObject();
	}
	private static WireframeCube Instance;
	private final WireframeCubePool pool = new WireframeCubePool();
	private final ShaderProgram shader;
	private final int ibo;
	private final int vbo;
	private final Vector3f position = new Vector3f();
	private final Vector3f scale = new Vector3f(1, 1, 1);
	private final Vector3f color = new Vector3f(1, 1, 1);
	public WireframeCube(){
		ibo = GL15.glGenBuffers();
		vbo = GL15.glGenBuffers();
		{
			{
				// ---
				// Generate vertex buffer.
				// ---
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(24);
				vertexData.put(0.0f).put(0.0f).put(0.0f);
				vertexData.put(0.0f).put(0.0f).put(1.0f);
				vertexData.put(0.0f).put(1.0f).put(0.0f);
				vertexData.put(0.0f).put(1.0f).put(1.0f);
				vertexData.put(1.0f).put(0.0f).put(0.0f);
				vertexData.put(1.0f).put(0.0f).put(1.0f);
				vertexData.put(1.0f).put(1.0f).put(0.0f);
				vertexData.put(1.0f).put(1.0f).put(1.0f);
				vertexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
			}
			{
				// ---
				// Generate index buffer.
				// ---
				ByteBuffer indexData = BufferUtils.createByteBuffer(36);
				indexData.put((byte)0).put((byte)1).put((byte)4);
				indexData.put((byte)1).put((byte)5).put((byte)4);
				indexData.put((byte)0).put((byte)2).put((byte)3);
				indexData.put((byte)0).put((byte)3).put((byte)1);
				indexData.put((byte)0).put((byte)2).put((byte)6);
				indexData.put((byte)0).put((byte)4).put((byte)6);
				indexData.put((byte)1).put((byte)3).put((byte)7);
				indexData.put((byte)1).put((byte)5).put((byte)7);
				indexData.put((byte)4).put((byte)5).put((byte)7);
				indexData.put((byte)4).put((byte)6).put((byte)7);
				indexData.put((byte)2).put((byte)3).put((byte)6);
				indexData.put((byte)3).put((byte)7).put((byte)6);
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			}
		}
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "WireframeCube.vert"), null, new File(
				WraithavensConquest.assetFolder, "WireframeCube.frag"));
		shader.loadUniforms("color");
		GlError.dumpError();
	}
	public void push(){
		WireframeCubePart part = pool.get();
		part.position.set(position);
		part.scale.set(scale);
		part.color.set(color);
	}
	public void setColor(float r, float g, float b){
		color.set(r, g, b);
	}
	public void setPosition(float x, float y, float z){
		position.set(x, y, z);
	}
	public void setScale(float x, float y, float z){
		scale.set(x, y, z);
	}
	private void disposeObject(){
		Instance = null;
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(vbo);
		pool.dump();
	}
	private void renderObject(){
		shader.bind();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		pool.reset();
		for(int i = 0; i<pool.size(); i++){
			GL11.glPushMatrix();
			pool.get().bind(shader);
			GL11.glDrawElements(GL11.GL_TRIANGLES, 36, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPopMatrix();
		}
		pool.reset();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
}
