package com.wraithavens.conquest.SinglePlayer.Blocks;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Blocks.Octree.Octree;
import com.wraithavens.conquest.SinglePlayer.Blocks.Octree.OctreeTask;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.Utility.Algorithms;

public class World{
	/**
	 * Returns 0 when box is full outside of camera view. Returns 1 when box is
	 * partly outside of camera view. Retusn 2 when box is completely inside
	 * camera view.
	 */
	private static int outside(){
		if(Math.abs(aabb1[0]-aabb2[0])+aabb2[3]<=aabb1[3]&&Math.abs(aabb1[1]-aabb2[1])+aabb2[3]<=aabb1[3]
			&&Math.abs(aabb1[2]-aabb2[2])+aabb2[3]<=aabb1[3])
			return 2;
		int r = aabb1[3]+aabb2[3];
		if(Math.abs(aabb1[0]-aabb2[0])>r||Math.abs(aabb1[1]-aabb2[1])>r||Math.abs(aabb1[2]-aabb2[2])>r)
			return 1;
		return 0;
	}
	private static final int[] aabb1 = new int[4];
	private static final int[] aabb2 = new int[4];
	/**
	 * This value denotes the number of chunks that should be attempted to load
	 * each frame. Turning this higher will allow chunks to load faster, at the
	 * expense of Fps.
	 */
	private static final int ChunksPerFrame = 2;
	private static final int ViewDistance = ViewDistances.View_8.value*16;
	static int SHADER_LOCATION;
	static int SHADER_LOCATION_2;
	private final Camera camera;
	private final ChunkGenerator generator;
	private final ChunkLoader chunkLoader;
	private final ArrayList<ChunkVBO> vbos = new ArrayList();
	private final ShaderProgram shader;
	private final int ibo;
	private final Octree octree;
	private final OctreeTask renderTask;
	private final OctreeTask unloadChunksTask;
	private final ArrayList<ChunkPainter> toRemove = new ArrayList(50);
	public World(WorldNoiseMachine machine, Camera camera){
		GlError.out("Building world.");
		ibo = GL15.glGenBuffers();
		generateIndexBuffer();
		this.camera = camera;
		generator = new ChunkGenerator(machine);
		chunkLoader = new ChunkLoader(generator, ViewDistance);
		GlError.dumpError();
		shader =
			new ShaderProgram(new File(WraithavensConquest.assetFolder, "Basic Shader.vert"), null, new File(
				WraithavensConquest.assetFolder, "Basic Shader.frag"));
		SHADER_LOCATION = shader.getAttributeLocation("shade");
		SHADER_LOCATION_2 = shader.getAttributeLocation("isGrass");
		GlError.dumpError();
		GL20.glEnableVertexAttribArray(SHADER_LOCATION);
		GL20.glEnableVertexAttribArray(SHADER_LOCATION_2);
		GlError.dumpError();
		shader.bind();
		shader.loadUniforms("grassShade");
		shader.setUniform1I(0, 0);
		GlError.dumpError();
		chunkLoader.updateLocation(Algorithms.groupLocation((int)camera.x, 16),
			Algorithms.groupLocation((int)camera.y, 16), Algorithms.groupLocation((int)camera.z, 16));
		octree = new Octree(machine);
		renderTask = new OctreeTask(octree){
			@Override
			public void run(VoxelChunk chunk){
				if(chunk instanceof ChunkPainter)
					((ChunkPainter)chunk).render();
				else if(chunk instanceof VoxelBiome)
					((VoxelBiome)chunk).bind();
			}
			@Override
			public boolean shouldRun(VoxelChunk voxel){
				return camera.getFrustum().cubeInFrustum(voxel.getX(), voxel.getY(), voxel.getZ(),
					voxel.getSize());
			}
		};
		unloadChunksTask = new OctreeTask(octree){
			@Override
			public void run(VoxelChunk chunk){
				if(chunk instanceof ChunkPainter)
					toRemove.add((ChunkPainter)chunk);
			}
			@Override
			public boolean shouldRun(VoxelChunk voxel){
				if(voxel instanceof ChunkPainter)
					return shouldUnload(voxel)==0;
				return shouldUnload(voxel)<2;
			}
		};
		GlError.out("World built.");
	}
	public void dispose(){
		GlError.out("Disposing world.");
		for(int i = 0; i<vbos.size(); i++)
			vbos.get(i).dispose();
		vbos.clear();
		octree.clear();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(ibo);
		GlError.dumpError();
	}
	public int getHeightAt(int x, int z){
		return generator.getHeightAt(x, z);
	}
	public void render(){
		// ---
		// Prepare to render chunks.
		// ---
		shader.bind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		// ---
		// And finally, preform the renders.
		// ---
		renderTask.runTask();
		GlError.dumpError();
	}
	public void unloadAllChunks(){
		GlError.out("Unloading all chunks.");
		octree.clear();
		// ---
		// This part just resets the chunk loader, so new chunks will start
		// generating around the camera again.
		// ---
		chunkLoader.updateLocation(Algorithms.groupLocation((int)camera.x, 16),
			Algorithms.groupLocation((int)camera.y, 16), Algorithms.groupLocation((int)camera.z, 16));
		GlError.dumpError();
	}
	public void update(){
		int x = Algorithms.groupLocation((int)camera.x, 16);
		int y = Algorithms.groupLocation((int)camera.y, 16);
		int z = Algorithms.groupLocation((int)camera.z, 16);
		if(chunkLoader.getX()!=x||chunkLoader.getY()!=y||chunkLoader.getZ()!=z)
			chunkLoader.updateLocation(x, y, z);
		clearEmpties();
		RawChunk raw;
		int max = ChunksPerFrame;
		for(int i = 0; i<max; i++){
			raw = chunkLoader.loadNextChunk(octree);
			if(raw==null)
				break;
			octree.addVoxel(new ChunkPainter(this, raw));
		}
		GlError.dumpError();
	}
	private void clearEmpties(){
		unloadChunksTask.runTask();
		if(!toRemove.isEmpty()){
			for(ChunkPainter chunk : toRemove){
				chunk.dispose();
				octree.removeVoxel(chunk);
			}
			GlError.out("Unloaded "+toRemove.size()+" chunks.");
			toRemove.clear();
		}
		GlError.dumpError();
	}
	private void generateIndexBuffer(){
		GlError.out("Generating index buffer.");
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
		GlError.dumpError();
	}
	private int shouldUnload(VoxelChunk voxel){
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
		GlError.dumpError();
		return v;
	}
}
