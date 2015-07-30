package com.wraithavens.conquest.SinglePlayer.Heightmap;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.NoiseGenerator;

public class WorldHeightmaps{
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
	// The amount of pixels in the heightmap image. This is set to two lower
	// then the actual value, so that the last pixels are used on multiple
	// height maps, for seemlessness.
	// ---
	static final int TextureDetail = 64;
	// ---
	// The actual distance covered by the heightmap.
	// ---
	private static final int TextureSize = 512;
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
		// I use the seed 0 here for debug, but any seed can be used. As long as
		// the same seed is always used on the world save folder.
		// ---
		NoiseGenerator noise = new NoiseGenerator(0, 200, 5);
		noise.setFunction(new CosineInterpolation());
		generator = new HeightmapGenerator(TextureDetail, TextureSize/(TextureDetail-1.0f), noise, 250);
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
	public void render(){
		shader.bind();
		for(int i = 0; i<heightmaps.length; i++)
			renderVbo(i);
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
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
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
				vertexData.put(x/(float)TextureDetail);
				vertexData.put(y/(float)TextureDetail);
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
			heightmaps[i] = new HeightMap(subX, subZ, generator.getTexture(subX, subZ));
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
		heightmaps[index].texture.bind();
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
}
