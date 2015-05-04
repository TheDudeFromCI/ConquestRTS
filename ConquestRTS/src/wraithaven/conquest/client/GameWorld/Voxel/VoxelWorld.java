package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;
import java.util.Comparator;
import static org.lwjgl.opengl.GL11.*;

public class VoxelWorld{
	private boolean needsRebatch;
	private VoxelChunk chunk;
	private final ArrayList<QuadBatch> tempQuads = new ArrayList();
	private final VoxelWorldListener worldListener;
	private final ChunkStorage chunkStorage;
	final VoxelWorldBounds bounds;
	public VoxelWorld(VoxelWorldListener worldListener, VoxelWorldBounds bounds){
		this.worldListener=worldListener;
		this.bounds=bounds;
		if(bounds==null)chunkStorage=new InfiniteWorld();
		else chunkStorage=new FiniteWorld(bounds);
	}
	public VoxelChunk loadChunk(int chunkX, int chunkY, int chunkZ){
		if(bounds!=null){
			if(chunkX<bounds.chunkStartX||chunkY<bounds.chunkStartY||chunkZ<bounds.chunkStartZ)return null;
			if(chunkX>bounds.chunkEndX||chunkY>bounds.chunkEndY||chunkZ>bounds.chunkEndZ)return null;
		}
		chunk=new VoxelChunk(this, chunkX, chunkY, chunkZ);
		chunkStorage.addChunk(chunk);
		worldListener.loadChunk(chunk);
		setNeedsRebatch();
		return chunk;
	}
	public void unloadChunk(int chunkX, int chunkY, int chunkZ){
		VoxelChunk c = getChunk(chunkX, chunkY, chunkZ, false);
		if(c==null)return;
		chunkStorage.removeChunk(c);
		worldListener.unloadChunk(c);
		c.dispose();
		setNeedsRebatch();
	}
	public void unloadChunk(VoxelChunk chunk){
		chunkStorage.removeChunk(chunk);
		worldListener.unloadChunk(chunk);
	}
	public VoxelChunk getChunk(int chunkX, int chunkY, int chunkZ, boolean load){
		VoxelChunk chunk = chunkStorage.getChunk(chunkX, chunkY, chunkZ);
		if(chunk!=null)return chunk;
		return load?loadChunk(chunkX, chunkY, chunkZ):null;
	}
	public VoxelBlock getBlock(int x, int y, int z, boolean load){
		VoxelChunk c = getContainingChunk(x, y, z, load);
		if(c==null)return null;
		return c.getBlock(x, y, z);
	}
	public void render(){
		if(needsRebatch){
			tempQuads.clear();
			for(int i = 0; i<chunkStorage.getChunkCount(); i++){
				chunk=chunkStorage.getChunk(i);
				if(worldListener.isChunkVisible(chunk)){
					if(chunk.needsBatchUpdate)chunk.updateBatches();
					tempQuads.addAll(chunk.batches);
				}
			}
			tempQuads.sort(new Comparator<QuadBatch>(){
				public int compare(QuadBatch a, QuadBatch b){ return a.getTexture()==b.getTexture()?0:a.getTexture().getId()>b.getTexture().getId()?1:-1; }
			});
			needsRebatch=false;
		}
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		Texture bound = null;
		for(QuadBatch batch : tempQuads){
			if(bound!=batch.getTexture()){
				if(bound!=null)glEnd();
				bound=batch.getTexture();
				bound.bind();
			}
			batch.renderPart();
		}
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}
	public VoxelBlock getBlock(int x, int y, int z){
		chunk=getContainingChunk(x, y, z);
		return chunk==null?null:chunk.getBlock(x, y, z);
	}
	public VoxelBlock setBlock(int x, int y, int z, BlockType type){
		chunk=getContainingChunk(x, y, z);
		return chunk==null?null:chunk.setBlock(x, y, z, type);
	}
	public VoxelChunk getContainingChunk(int x, int y, int z){ return getChunk(x>>4, y>>4, z>>4, true); }
	public VoxelChunk getContainingChunk(int x, int y, int z, boolean load){ return getChunk(x>>4, y>>4, z>>4, load); }
	public VoxelChunk getChunk(int chunkX, int chunkY, int chunkZ){ return getChunk(chunkX, chunkY, chunkZ, true); }
	public int getChunkCount(){ return chunkStorage.getChunkCount(); }
	public void optimizeAll(){ for(int i = 0; i<chunkStorage.getChunkCount(); i++)chunkStorage.getChunk(i).optimize(); }
	public VoxelChunk getChunk(int index){ return chunkStorage.getChunk(index); }
	public void setNeedsRebatch(){ needsRebatch=true; }
}