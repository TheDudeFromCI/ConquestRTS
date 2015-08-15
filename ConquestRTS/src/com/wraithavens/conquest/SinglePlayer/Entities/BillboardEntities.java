package com.wraithavens.conquest.SinglePlayer.Entities;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;

public class BillboardEntities{
	private static final int LayersPerLod = 5;
	private static final int MaxLayerCount = LayersPerLod*4+1;
	private final int vbo;
	private final int ibo;
	private final ShaderProgram shader;
	public BillboardEntities(){
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		{
			{
				// ---
				// Build vertex data.
				// ---
				FloatBuffer vertexData = BufferUtils.createFloatBuffer(MaxLayerCount*36);
				float x;
				for(int i = 0; i<MaxLayerCount; i++){
					x = i*(1f/(MaxLayerCount-1));
					// X Axis
					vertexData.put(x).put(0).put(0);
					vertexData.put(x).put(0).put(1);
					vertexData.put(x).put(1).put(1);
					vertexData.put(x).put(1).put(0);
					// Y Axis
					vertexData.put(0).put(x).put(0);
					vertexData.put(0).put(x).put(1);
					vertexData.put(1).put(x).put(1);
					vertexData.put(1).put(x).put(0);
					// Z Axis
					vertexData.put(0).put(0).put(x);
					vertexData.put(0).put(1).put(x);
					vertexData.put(1).put(1).put(x);
					vertexData.put(1).put(0).put(x);
				}
				vertexData.flip();
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
				GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
			}
			{
				// ---
				// Build index data.
				// ---
				ArrayList<Integer> layerOrdering = new ArrayList();
				for(int i = 0; i<MaxLayerCount; i++)
					layerOrdering.add(i);
				layerOrdering.sort(new Comparator<Integer>(){
					public int compare(Integer a, Integer b){
						int d1 = Math.abs(a-MaxLayerCount/2);
						int d2 = Math.abs(b-MaxLayerCount/2);
						return d1==d2?0:d1<d2?-1:1;
					}
				});
				ShortBuffer indexData = BufferUtils.createShortBuffer(MaxLayerCount*18);
				int x;
				for(int i : layerOrdering){
					x = i*12;
					// X Axis
					indexData.put((short)(x+0)).put((short)(x+1)).put((short)(x+2));
					indexData.put((short)(x+0)).put((short)(x+2)).put((short)(x+3));
					// Y Axis
					x += 4;
					indexData.put((short)(x+0)).put((short)(x+1)).put((short)(x+2));
					indexData.put((short)(x+0)).put((short)(x+2)).put((short)(x+3));
					// Z Axis
					x += 4;
					indexData.put((short)(x+0)).put((short)(x+1)).put((short)(x+2));
					indexData.put((short)(x+0)).put((short)(x+2)).put((short)(x+3));
				}
				indexData.flip();
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
			}
			{
				// ---
				// Load the shader.
				// ---
				shader = new ShaderProgram("Billboard");
				shader.loadUniforms("texture");
				shader.bind();
				shader.setUniform1I(0, 0);
			}
		}
		GlError.dumpError();
	}
	void bind(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
		GL11.glTexCoordPointer(3, GL11.GL_FLOAT, 12, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		shader.bind();
	}
	void dispose(){
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		shader.dispose();
	}
	@SuppressWarnings("static-method")
	void end(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
	void render(int lod){
		GL11.glDrawElements(GL11.GL_TRIANGLES, (MaxLayerCount-LayersPerLod*(lod-1))*18, GL11.GL_UNSIGNED_SHORT,
			0);
	}
}
