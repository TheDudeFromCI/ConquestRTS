package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.QuadBatch;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.ShaderProgram;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

public class WorldRenderer{
	private final World world;
	private final Camera camera;
	private ShaderProgram shader;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private final Comparator batchSorter = new Comparator<QuadBatch>(){
		public int compare(QuadBatch a, QuadBatch b){
			return a.getTexture()==b.getTexture()?0:a.getTexture().getId()<b.getTexture().getId()?1:-1;
		}
	};
	public WorldRenderer(World world, Camera camera){
		this.world = world;
		this.camera = camera;
	}
	public void initalize(){
		shader = new ShaderProgram(new File(WraithavensConquest.assetFolder, "Basic Shader.vert"), null, new File(WraithavensConquest.assetFolder, "Basic Shader.frag"));
	}
	public void render(){
		shader.bind();
		Texture t = null;
		for(int i = 0; i<batches.size(); i++){
			if(t==null
					||batches.get(i).getTexture()!=t){
				t = batches.get(i).getTexture();
				t.bind();
			}
			batches.get(i).renderPart();
		}
	}
	public void update(){
		if(world.needsRebuffer()){
			batches.clear();
			for(int i = 0; i<world.getWorldStorage().voxels.size(); i++)
				processVoxel(world.getWorldStorage().voxels.get(i));
			batches.sort(batchSorter);
			world.setRebuffered();
		}
	}
	private void processVoxel(Voxel voxel){
		if(!isVoxelVisible(voxel))return;
		if(voxel.isSolid()){
			if(voxel.getState()!=Voxel.DEFAULT){
				Chunk c = (Chunk)voxel.getState();
				batches.addAll(c.getBatches());
			}
			return;
		}
		for(int i = 0; i<8; i++)
			processVoxel(voxel.getVoxel(i));
	}
	private boolean isVoxelVisible(Voxel voxel){
		if(voxel.getState() instanceof Chunk
				&&!((Chunk)voxel.getState()).containsBlocks())return false;
		return camera.frustum.cubeInFrustum(voxel.x<<Chunk.CHUNK_BITS, voxel.y<<Chunk.CHUNK_BITS, voxel.z<<Chunk.CHUNK_BITS, voxel.size<<Chunk.CHUNK_BITS);
	}
}