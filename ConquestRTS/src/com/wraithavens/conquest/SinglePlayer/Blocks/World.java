package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.Math.MatrixUtils;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class World{
	private static boolean outside(){
		int r = aabb1[3]+aabb2[3];
		return Math.abs(aabb1[0]-aabb2[0])>r&&Math.abs(aabb1[1]-aabb2[1])>r&&Math.abs(aabb1[2]-aabb2[2])>r;
	}
	private static final int[] aabb1 = new int[4];
	private static final int[] aabb2 = new int[4];
	private static final int ViewDistance = ViewDistances.View_8.value*16;
	static int SHADER_LOCATION;
	static int SHADER_LOCATION_2;
	private final Camera camera;
	private final ChunkGenerator generator;
	private final ChunkLoader chunkLoader;
	private final ArrayList<VoxelChunk> voxels = new ArrayList();
	private final ArrayList<ChunkVBO> vbos = new ArrayList();
	private final ShaderProgram shader;
	private int step;
	private final int ibo;
	private final BlockCuller chunkRenderTester;
	public World(WorldNoiseMachine machine, Camera camera){
		ibo = GL15.glGenBuffers();
		generateIndexBuffer();
		this.camera = camera;
		generator = new ChunkGenerator(machine);
		chunkLoader = new ChunkLoader(generator, ViewDistance);
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Basic Shader.vert"), null, new File(
				WraithavensConquest.assetFolder, "Basic Shader.frag"));
		SHADER_LOCATION = shader.getAttributeLocation("shade");
		SHADER_LOCATION_2 = shader.getAttributeLocation("isGrass");
		GL20.glEnableVertexAttribArray(SHADER_LOCATION);
		GL20.glEnableVertexAttribArray(SHADER_LOCATION_2);
		shader.loadUniforms("grassShade");
		shader.bind();
		shader.setUniform1I(0, 0);
		chunkLoader.updateLocation(Algorithms.groupLocation((int)camera.x, 16),
			Algorithms.groupLocation((int)camera.y, 16), Algorithms.groupLocation((int)camera.z, 16));
		chunkRenderTester = new BlockCuller();
	}
	public void dispose(){
		for(int i = 0; i<vbos.size(); i++)
			vbos.get(i).dispose();
		vbos.clear();
		voxels.clear();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(ibo);
	}
	public int getHeightAt(int x, int z){
		return generator.getHeightAt(x, z);
	}
	public void render(){
		// ---
		// This adjusts the clipping plane, so depth testing for blocks and
		// entities are more accurate.
		// ---
		MatrixUtils.setupPerspective(70, WraithavensConquest.INSTANCE.getScreenWidth()
			/(float)WraithavensConquest.INSTANCE.getScreenHeight(), 0.5f, 1000);
		// ---
		// Prepare to render chunks.
		// ---
		shader.bind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		// ---
		// And finally, preform the renders.
		// ---
		for(VoxelChunk v : voxels)
			v.render(chunkRenderTester);
	}
	public void update(){
		int x = Algorithms.groupLocation((int)camera.x, 16);
		int y = Algorithms.groupLocation((int)camera.y, 16);
		int z = Algorithms.groupLocation((int)camera.z, 16);
		if(chunkLoader.getX()!=x||chunkLoader.getY()!=y||chunkLoader.getZ()!=z)
			chunkLoader.updateLocation(x, y, z);
		if(step%1==0){
			clearEmpties();
			RawChunk raw = chunkLoader.loadNextChunk(voxels);
			if(raw!=null){
				// ---
				// TODO Add voxel heirarchy.
				// ---
				voxels.add(new ChunkPainter(this, raw));
			}
		}
		step++;
	}
	private void clearEmpties(){
		for(int i = 0; i<voxels.size();)
			if(shouldUnload(voxels.get(i))){
				voxels.get(i).dispose();
				voxels.remove(i);
			}else
				i++;
	}
	private void generateIndexBuffer(){
		int maxQuads = 16*16*16/2*6;
		ShortBuffer indexData = BufferUtils.createShortBuffer(maxQuads*6);
		short e = 0;
		for(int i = 0; i<maxQuads; i++){
			indexData.put(e).put((short)(e+1)).put((short)(e+2));
			indexData.put(e).put((short)(e+2)).put((short)(e+3));
			e += 4;
		}
		indexData.flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
	}
	private boolean shouldUnload(VoxelChunk voxel){
		// ---
		// Lets test bounding boxes to see which chucks are within view
		// distance. Here I give the camera view distance a 1 extra chunk buffer
		// just to avoid a chunk from attempting to load outside of the camera's
		// view distance somehow. That would be very bad. :P
		// ---
		aabb1[0] = (int)camera.x;
		aabb1[1] = (int)camera.y;
		aabb1[2] = (int)camera.z;
		aabb1[3] = ViewDistance+16;
		// ---
		// Now for the chunk itself. Reordered to save a couple of precious
		// clock cycles, at no cost. ;)
		// ---
		aabb2[3] = voxel.size/2;
		aabb2[0] = voxel.x+aabb2[3];
		aabb2[1] = voxel.y+aabb2[3];
		aabb2[2] = voxel.z+aabb2[3];
		return outside();
	}
	ChunkVBO generateVBO(){
		for(int i = 0; i<vbos.size(); i++)
			if(vbos.get(i).isOpen)
				return vbos.get(i);
		int vbo = GL15.glGenBuffers();
		ChunkVBO v = new ChunkVBO(vbo, 0);
		vbos.add(v);
		return v;
	}
}
