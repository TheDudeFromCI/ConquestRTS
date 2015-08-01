package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Noise.AdvancedNoise;
import com.wraithavens.conquest.SinglePlayer.Noise.ColorNoise;
import com.wraithavens.conquest.SinglePlayer.Noise.SubNoise;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.LinearInterpolation;

class WorldHeightmaps{
	/**
	 * This function takes any value, <i>x</i>, and groups it into evenly sized
	 * chunks.
	 *
	 * @param x
	 *            - The location.
	 * @param w
	 *            - The chunk size.
	 * @return The grouped value of x.
	 */
	static int groupLocation(int x, int w){
		return x>=0?x/w*w:(x-(w-1))/w*w;
	}
	public static final int TextureDetail = 1024;
	public static final int VertexCount = 256;
	public static final int ViewDistance = 16384;
	private static final int Vertices = VertexCount*VertexCount;
	private static final int VertexSize = 5;
	private static final int indexCount = (VertexCount*2+2)*(VertexCount-1)-2;
	private final HeightMap heightmap = new HeightMap();
	private final int vbo;
	private final int ibo;
	private final ShaderProgram shader;
	private final HeightmapGenerator generator;
	public WorldHeightmaps(){
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
		// And prepare the texture loader/generator.
		// Note: This noise generator must be the exact same as the once that
		// creates the mountains for the blocks, otherwise it won't look right.
		// I use the server hard seed here for debug, but any seed can be used.
		// As long as the same seed is always used on the world save folder.
		// ---
		CosineInterpolation cos = new CosineInterpolation();
		LinearInterpolation lerp = new LinearInterpolation();
		// ---
		// The layout may be slightly similar for the actual game, but this
		// part is more or less a test.
		// ---
		SubNoise worldHeightNoise1 = SubNoise.build(0, 6000, 6, cos, 40000, 0);
		SubNoise worldHeightNoise2 = SubNoise.build(6, 300, 3, lerp, 800, 0);
		SubNoise humidityNoise1 = SubNoise.build(1, 7500, 10, cos, 1, 0);
		SubNoise tempatureNoise1 = SubNoise.build(2, 20000, 11, cos, 1, 0);
		SubNoise prairieRed = SubNoise.build(3, 120, 2, lerp, 0.15f, 0.25f);
		SubNoise prairieGreen = SubNoise.build(4, 20, 0, lerp, 0.1f, 0);
		SubNoise prairieBlue = SubNoise.build(5, 80, 2, lerp, 0.15f, 0.3f);
		// ---
		// And compiling these together.
		// ---
		AdvancedNoise worldHeight = new AdvancedNoise();
		worldHeight.addSubNoise(worldHeightNoise1);
		worldHeight.addSubNoise(worldHeightNoise2);
		AdvancedNoise humidity = new AdvancedNoise();
		humidity.addSubNoise(humidityNoise1);
		AdvancedNoise tempature = new AdvancedNoise();
		tempature.addSubNoise(tempatureNoise1);
		ColorNoise prairieColor = new ColorNoise(prairieRed, prairieGreen, prairieBlue);
		WorldNoiseMachine machine = new WorldNoiseMachine(worldHeight, humidity, tempature, prairieColor);
		generator = new HeightmapGenerator(machine);
		// ---
		// Now upload all known data.
		// ---
		buildShader((float)machine.getMaxHeight());
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
	public void update(float x, float z){
		if(isSafeView(x, z))
			return;
		updateHeightmaps(groupLocation((int)x, ViewDistance), groupLocation((int)z, ViewDistance));
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
	private void renderVbo(){
		if(heightmap.isNull())
			return;
		heightmap.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(heightmap.getX(), 0, heightmap.getZ());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GL11.glPopMatrix();
	}
	private void updateHeightmaps(int x, int z){
		System.out.println("Loading new heightmap.");
		heightmap.update(x, z, generator.getHeightmapTexture(x, z));
	}
	void render(){
		shader.bind();
		renderVbo();
	}
}
