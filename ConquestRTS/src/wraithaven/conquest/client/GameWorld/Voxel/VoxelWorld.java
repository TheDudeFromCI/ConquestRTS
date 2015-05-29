package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;
import java.util.Comparator;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.IndexManager;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldBounds;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldListener;

public class VoxelWorld{
	private boolean needsRebatch;
	private Chunk chunk;
	private final ArrayList<QuadBatch> tempQuads = new ArrayList();
	private final VoxelWorldListener worldListener;
	private final ChunkStorage chunkStorage;
	final VoxelWorldBounds bounds;
	private final IndexManager indexManager;
	public VoxelWorld(VoxelWorldListener worldListener, VoxelWorldBounds bounds){
		this.worldListener=worldListener;
		this.bounds=bounds;
		if(bounds==null)chunkStorage=new InfiniteWorld();
		else chunkStorage=new FiniteWorld(bounds);
		indexManager=new IndexManager();
	}
	public Chunk loadChunk(int chunkX, int chunkY, int chunkZ){
		if(bounds!=null){
			if(chunkX<bounds.chunkStartX||chunkY<bounds.chunkStartY||chunkZ<bounds.chunkStartZ)return null;
			if(chunkX>bounds.chunkEndX||chunkY>bounds.chunkEndY||chunkZ>bounds.chunkEndZ)return null;
		}
		chunk=new Chunk(this, chunkX, chunkY, chunkZ);
		chunkStorage.addChunk(chunk);
		worldListener.loadChunk(chunk);
		setNeedsRebatch();
		return chunk;
	}
	public void unloadChunk(int chunkX, int chunkY, int chunkZ){
		Chunk c = getChunk(chunkX, chunkY, chunkZ, false);
		if(c==null)return;
		chunkStorage.removeChunk(c);
		worldListener.unloadChunk(c);
		c.dispose();
		setNeedsRebatch();
	}
	public void unloadChunk(Chunk chunk){
		chunkStorage.removeChunk(chunk);
		worldListener.unloadChunk(chunk);
	}
	public Chunk getChunk(int chunkX, int chunkY, int chunkZ, boolean load){
		Chunk chunk = chunkStorage.getChunk(chunkX, chunkY, chunkZ);
		if(chunk!=null)return chunk;
		return load?loadChunk(chunkX, chunkY, chunkZ):null;
	}
	public int getBlock(int x, int y, int z, boolean load){
		Chunk c = getContainingChunk(x, y, z, load);
		if(c==null)return -1;
		return c.getBlock(x, y, z);
	}
	private Comparator batchSorter = new Comparator<QuadBatch>(){
		public int compare(QuadBatch a, QuadBatch b){
			if(a.getTexture().transparent&&b.getTexture().transparent){
				double aDis = dis(a.centerX, a.centerY, a.centerZ);
				double bDis = dis(b.centerX, b.centerY, b.centerZ);
				return aDis==bDis?0:aDis<bDis?1:-1;
			}
			if(a.getTexture().transparent!=b.getTexture().transparent)return a.getTexture().transparent?1:-1;
			return a.getTexture()==b.getTexture()?0:a.getTexture().getId()>b.getTexture().getId()?1:-1;
		}
		private double dis(float x, float y, float z){ return Math.pow(Loop.INSTANCE.getCamera().x-x, 2)+Math.pow(Loop.INSTANCE.getCamera().y-y, 2)+Math.pow(Loop.INSTANCE.getCamera().z-z, 2); }
	};
	public void render(){
		if(needsRebatch){
			tempQuads.clear();
			for(int i = 0; i<chunkStorage.getChunkCount(); i++){
				chunk=chunkStorage.getChunk(i);
				if(worldListener.isChunkVisible(chunk)){
					chunk.clearEmptyBatches();
					if(chunk.needsRebatch())chunk.updateBatches();
					tempQuads.addAll(chunk.batches);
				}
			}
			tempQuads.sort(batchSorter);
			needsRebatch=false;
		}
		Texture bound = null;
		QuadBatch batch;
		trisRendered=0;
		for(int i = 0; i<tempQuads.size(); i++){
			batch=tempQuads.get(i);
			if(bound!=batch.getTexture()){
				bound=batch.getTexture();
				bound.bind();
			}
			batch.renderPart();
		}
	}
	public static int trisRendered;
	public int getBlock(int x, int y, int z){
		chunk=getContainingChunk(x, y, z);
		return chunk==null?-1:chunk.getBlock(x, y, z);
	}
	public void setBlock(int x, int y, int z, short index, boolean rebuild){
		chunk=getContainingChunk(x, y, z);
		if(chunk.setBlock(x, y, z, index)){
			setNeedsRebatch();
			if(rebuild)rebuildAll();
		}
	}
	public void clearAll(){
		if(chunkStorage instanceof FiniteWorld)for(int i = 0; i<chunkStorage.getChunkCount(); i++)unloadChunk(chunkStorage.getChunk(i));
		else for(int i = 0; i<chunkStorage.getChunkCount();)unloadChunk(chunkStorage.getChunk(i));
	}
	public void rebuildAll(){ for(int i = 0; i<chunkStorage.getChunkCount(); i++)chunkStorage.getChunk(i).rebuild(); }
	public IndexManager getIndexManager(){ return indexManager; }
	public Chunk getContainingChunk(int x, int y, int z){ return getChunk(x>>Chunk.CHUNK_BITS, y>>Chunk.CHUNK_BITS, z>>Chunk.CHUNK_BITS, true); }
	public Chunk getContainingChunk(int x, int y, int z, boolean load){ return getChunk(x>>Chunk.CHUNK_BITS, y>>Chunk.CHUNK_BITS, z>>Chunk.CHUNK_BITS, load); }
	public Chunk getChunk(int chunkX, int chunkY, int chunkZ){ return getChunk(chunkX, chunkY, chunkZ, true); }
	public int getChunkCount(){ return chunkStorage.getChunkCount(); }
	public Chunk getChunk(int index){ return chunkStorage.getChunk(index); }
	public void setNeedsRebatch(){ needsRebatch=true; }
	public Block getBlock(int index){ return indexManager.getBlock(index); }
	public short indexOfBlock(BlockShape shape, CubeTextures textures, BlockRotation rotation){ return indexManager.indexOf(shape, textures, rotation); }
}