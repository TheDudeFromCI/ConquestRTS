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
	private static int groupLocation(int x, int w){
		return x>=0?x/w*w:(x-(w-1))/w*w;
	}
	// ---
	// The amount of vertices used per row/col in the heightmap mesh. Using
	// higher values results in a higher detailed mesh. Use powers of two for
	// most optimal GPU use.
	// ---
	private static final int TextureDetail = 64;
	// ---
	// The actual distance covered by the heightmap segments on 1 axis.
	// ---
	private static final int TextureSize = 8192;
	private static final int Vertices = TextureDetail*TextureDetail;
	private static final int VertexSize = 5;
	private static final int indexCount = (TextureDetail*2+2)*(TextureDetail-1)-2;
	private final HeightMap[] heightmaps = new HeightMap[9];
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
		buildShader();
		// ---
		// And prepare the texture loader/generator.
		// Note: This noise generator must be the exact same as the once that
		// creates the mountains for the blocks, otherwise it won't look right.
		// I use the server hard seed here for debug, but any seed can be used.
		// As long as the same seed is always used on the world save folder.
		// ---
		{
			CosineInterpolation cos = new CosineInterpolation();
			LinearInterpolation lerp = new LinearInterpolation();
			// ---
			// The layout may be slightly similar for the actual game, but this
			// part is more or less a test.
			// ---
			SubNoise worldHeightNoise1 = SubNoise.build(0, 600, 5, cos, 250, 0);
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
			AdvancedNoise humidity = new AdvancedNoise();
			humidity.addSubNoise(humidityNoise1);
			AdvancedNoise tempature = new AdvancedNoise();
			tempature.addSubNoise(tempatureNoise1);
			ColorNoise prairieColor = new ColorNoise(prairieRed, prairieGreen, prairieBlue);
			WorldNoiseMachine machine = new WorldNoiseMachine(worldHeight, humidity, tempature, prairieColor);
			generator = new HeightmapGenerator(TextureDetail, TextureSize/(TextureDetail-1.0f), machine);
		}
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
		for(int i = 0; i<heightmaps.length; i++)
			if(heightmaps[i]!=null)
				heightmaps[i].dispose();
	}
	public void update(float x, float z){
		if(isSafeView(x, z))
			return;
		int a = groupLocation((int)x, TextureSize);
		int b = groupLocation((int)z, TextureSize);
		reorderHeightmaps(a, b);
		fillEmptyHeightmaps(a, b);
	}
	private void buildIndexData(){
		IntBuffer indexData = BufferUtils.createIntBuffer(indexCount);
		final int TriangleRows = TextureDetail-1;
		final int VerticesPerRow = TextureDetail*2;
		int x, y;
		int index;
		for(y = 0; y<TriangleRows; y++){
			if(y>0)
				// ---
				// Double first index.
				// ---
				indexData.put(y*TextureDetail);
			for(x = 0; x<VerticesPerRow; x++){
				index = y*TextureDetail;
				index += x/2;
				if(x%2==1)
					index += TextureDetail;
				indexData.put(index);
			}
			if(y<TriangleRows-1){
				// ---
				// Repeat last index.
				// ---
				index = (y+1)*TextureDetail;
				index += (VerticesPerRow-1)/2;
				indexData.put(index);
			}
		}
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
	private void buildShader(){
		shader.loadUniforms("texture");
		shader.setUniform1I(0, 0);
	}
	private void buildVBOs(){
		buildIndexData();
		buildVertexData();
	}
	private void buildVertexData(){
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(Vertices*VertexSize);
		int x, y;
		float s = TextureSize/(TextureDetail-1.0f);
		for(y = 0; y<TextureDetail; y++)
			for(x = 0; x<TextureDetail; x++){
				vertexData.put(x*s);
				vertexData.put(0.0f);
				vertexData.put(y*s);
				vertexData.put(x/(TextureDetail-1.0f));
				vertexData.put(y/(TextureDetail-1.0f));
			}
		vertexData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
	}
	private HeightMap center(){
		return heightmaps[4];
	}
	private void disposeUnusedHeightmaps(HeightMap[] newHeightmaps){
		int i, j;
		used:for(i = 0; i<9; i++){
			if(heightmaps[i]==null)
				continue;
			for(j = 0; j<9; j++)
				if(newHeightmaps[j]==heightmaps[i])
					continue used;
			heightmaps[i].dispose();
		}
	}
	private void fillEmptyHeightmaps(int x, int z){
		System.out.println("Loading new heightmaps.");
		int subX, subZ;
		for(int i = 0; i<heightmaps.length; i++){
			if(heightmaps[i]!=null)
				continue;
			// ---
			// Offset the x and z to find a actual heightmap locations.
			// ---
			if(i==0||i==3||i==6)
				subX = -TextureSize;
			else if(i==1||i==4||i==7)
				subX = 0;
			else
				subX = TextureSize;
			if(i==0||i==1||i==2)
				subZ = -TextureSize;
			else if(i==3||i==4||i==5)
				subZ = 0;
			else
				subZ = TextureSize;
			subX += x;
			subZ += z;
			heightmaps[i] = new HeightMap(subX, subZ, generator.getHeightmapTexture(subX, subZ));
			System.out.println(i+1+"/9");
		}
		System.out.println("Heightmaps loaded.");
	}
	private HeightMap getHeightmap(int x, int z){
		// ---
		// Pretty self explanatory.
		// ---
		for(int i = 0; i<heightmaps.length; i++)
			if(heightmaps[i]!=null&&heightmaps[i].posX==x&&heightmaps[i].posZ==z)
				return heightmaps[i];
		return null;
	}
	private boolean isSafeView(float x, float z){
		// ---
		// Check for any current gaps. We don't want those. :P
		// ---
		for(int i = 0; i<heightmaps.length; i++)
			if(heightmaps[i]==null)
				return false;
		// ---
		// It's safe to assume that is the center heightmap is correct, then the
		// other 8 must also be correct.
		// ---
		return x>=center().posX&&x<center().posX+TextureSize&&z>=center().posZ&&z<center().posZ+TextureSize;
	}
	private void renderVbo(int index){
		if(heightmaps[index]==null)
			return;
		heightmaps[index].heightmapTexture.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(heightmaps[index].posX, 0, heightmaps[index].posZ);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, indexCount, GL11.GL_UNSIGNED_INT, 0);
		GL11.glPopMatrix();
	}
	private void reorderHeightmaps(int x, int z){
		// ---
		// I'm not going to store this staticlly, because this function isn't
		// called very often. So slight heap is actually better, as the
		// information isn't stored between calls.
		// ---
		HeightMap[] tempStorage = new HeightMap[9];
		// ---
		// Several of these will return null, but that's ok.
		// ---
		tempStorage[0] = getHeightmap(x-TextureSize, z-TextureSize);
		tempStorage[1] = getHeightmap(x, z-TextureSize);
		tempStorage[2] = getHeightmap(x+TextureSize, z-TextureSize);
		tempStorage[3] = getHeightmap(x-TextureSize, z);
		tempStorage[4] = getHeightmap(x, z);
		tempStorage[5] = getHeightmap(x+TextureSize, z);
		tempStorage[6] = getHeightmap(x-TextureSize, z+TextureSize);
		tempStorage[7] = getHeightmap(x, z+TextureSize);
		tempStorage[8] = getHeightmap(x+TextureSize, z+TextureSize);
		// ---
		// Dispose any heightmaps that fell out of range.
		// ---
		disposeUnusedHeightmaps(tempStorage);
		// ---
		// And finally update the list with the new order.
		// ---
		for(int i = 0; i<9; i++)
			heightmaps[i] = tempStorage[i];
	}
	void render(){
		shader.bind();
		for(int i = 0; i<heightmaps.length; i++)
			renderVbo(i);
	}
}
