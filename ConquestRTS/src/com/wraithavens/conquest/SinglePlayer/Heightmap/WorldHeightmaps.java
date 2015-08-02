package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class WorldHeightmaps{
	static final int TextureDetail = 1024;
	private static final int VertexCount = 256;
	static final int ViewDistance = 16384;
	private static final int Vertices = VertexCount*VertexCount;
	private static final int VertexSize = 5;
	private static final int indexCount = (VertexCount*2+2)*(VertexCount-1)-2;
	private final HeightMap heightmap = new HeightMap();
	private final int vbo;
	private final int ibo;
	private final ShaderProgram shader;
	private final HeightmapGenerator generator;
	public WorldHeightmaps(WorldNoiseMachine machine){
		// ---
		// Prepare the mesh.
		// ---
		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
		buildVBOs();
		// ---
		// Prepare the shader. Dang these names are long. o_O
		// ---
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Heightmap.vert"), null, new File(
				WraithavensConquest.assetFolder, "Heightmap.frag"));
		// ---
		// Now upload all known data.
		// ---
		buildShader((float)machine.getMaxHeight());
		// ---
		// And prepare the texture loader/generator.
		// ---
		generator = new HeightmapGenerator(machine);
		// ---
		// Just to make sure there is some data on the list.
		// ---
		update(0, 0);
	}
	public void dispose(){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		shader.dispose();
		heightmap.dispose();
	}
	public void render(){
		// ---
		// Do nothing if we don't have anything to draw.
		// ---
		if(heightmap.isNull())
			return;
		// ---
		// Prepare the GPU for heightmap information.
		// ---
		shader.bind();
		heightmap.bind();
		// ---
		// Finally, offset the mesh to it's correct location, then render the
		// heightmap mesh. :)
		// ---
		GL11.glPushMatrix();
		GL11.glTranslatef(heightmap.getX(), 0, heightmap.getZ());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GL11.glPopMatrix();
		// ---
		// Clear the depth buffer, so the rest of the game looks like it's
		// fading out into the mountains, rather then clipping into them.
		// ---
		// GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}
	public void update(float x, float z){
		if(isSafeView(x, z))
			return;
		updateHeightmaps(Algorithms.groupLocation((int)x, ViewDistance),
			Algorithms.groupLocation((int)z, ViewDistance));
	}
	private void buildIndexData(){
		IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
		final int TriangleRows = VertexCount-1;
		final int VerticesPerRow = VertexCount*2;
		int x, y;
		int index;
		for(y = 0; y<TriangleRows; y++){
			if(y>0)
				// ---
				// Double first index.
				// ---
				indexData.put(y*VertexCount);
			for(x = 0; x<VerticesPerRow; x++){
				index = y*VertexCount;
				index += x/2;
				if(x%2==1)
					index += VertexCount;
				indexData.put(index);
			}
			if(y<TriangleRows-1){
				// ---
				// Repeat last index.
				// ---
				index = (y+1)*VertexCount;
				index += (VerticesPerRow-1)/2;
				indexData.put(index);
			}
		}
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
	// ---
	// TODO Make light direction change to match the sun.
	// ---
	private void buildShader(float maxHeight){
		shader.loadUniforms("texture", "mountainData", "sunDirection");
		shader.bind();
		shader.setUniform1I(0, 0);
		shader.setUniform3f(1, TextureDetail, 1f/TextureDetail, maxHeight);
		shader.setUniform3f(2, 0.45f, 0.65f, 0.0f);
	}
	private void buildVBOs(){
		buildIndexData();
		buildVertexData();
	}
	private void buildVertexData(){
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(Vertices*VertexSize);
		int x, y;
		float s = ViewDistance*2/(VertexCount-1.0f);
		for(y = 0; y<VertexCount; y++)
			for(x = 0; x<VertexCount; x++){
				vertexData.put(x*s-ViewDistance/2);
				vertexData.put(0.0f);
				vertexData.put(y*s-ViewDistance/2);
				vertexData.put(x/(TextureDetail-1.0f));
				vertexData.put(y/(TextureDetail-1.0f));
			}
		vertexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
	}
	private boolean isSafeView(float x, float z){
		if(heightmap.isNull())
			return false;
		return x>=heightmap.getX()&&x<heightmap.getX()+ViewDistance&&z>=heightmap.getZ()
			&&z<heightmap.getZ()+ViewDistance;
	}
	private void updateHeightmaps(int x, int z){
		System.out.println("Loading new heightmap.");
		heightmap.update(x, z, generator.getHeightmapTexture(x, z));
	}
}
