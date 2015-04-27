package wraithaven.conquest.client;

import java.util.ArrayList;
import java.util.Comparator;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;

public class CatcheChunkLoader implements VoxelWorldListener{
	private int camX, camY, camZ;
	private int lastCamX, lastCamY=-100, lastCamZ;
	private VoxelWorld world;
	private Camera cam;
	private final ArrayList<VoxelChunkQue> que = new ArrayList();
	public static final int CATCHE_RANGE = 12;
	private static final int CATCHE_RANGE_SQUARED = CATCHE_RANGE*CATCHE_RANGE;
	public static final int CAMERA_RANGE = 7;
	private static final int CAMERA_RANGE_SQUARED = CAMERA_RANGE*CAMERA_RANGE;
	public void setup(VoxelWorld world, Camera cam){
		this.world=world;
		this.cam=cam;
	}
	public void update(int blockCount){
		getPosition();
		if(lastCamX!=camX||lastCamY!=camY||lastCamZ!=camZ){
			lastCamX=camX;
			lastCamY=camY;
			lastCamZ=camZ;
			unloadUneededChunks();
			addNeededChunksToQue();
			sortList();
		}
		for(int i = 0; i<blockCount; i++)if(updateList())return;
	}
	private void getPosition(){
		camX=(int)Math.floor(cam.x)>>4;
		camY=(int)Math.floor(cam.y)>>4;
		camZ=(int)Math.floor(cam.z)>>4;
	}
	private void unloadUneededChunks(){
		VoxelChunk chunk;
		for(int i = 0; i<world.getChunkCount();){
			chunk=world.getChunk(i);
			if(getDistanceSquared(camX, camY, camZ, chunk.chunkX, chunk.chunkY, chunk.chunkZ)>CATCHE_RANGE_SQUARED)world.unloadChunk(chunk);
			else i++;
		}
	}
	private void addNeededChunksToQue(){
		int startX = camX-CAMERA_RANGE;
		int startY = Math.max(camY-CAMERA_RANGE, 0);
		int startZ = camZ-CAMERA_RANGE;
		int endX = camX+CAMERA_RANGE;
		int endY = Math.min(camY+CAMERA_RANGE, WorldGenerator.WORLD_HEIGHT);
		int endZ = camZ+CAMERA_RANGE;
		int x, y, z;
		for(x=startX; x<=endX; x++)for(y=startY; y<=endY; y++)for(z=startZ; z<=endZ; z++)world.getChunk(x, y, z);
	}
	private void sortList(){
		for(VoxelChunkQue q : que)q.tempDistance=getDistanceSquared(camX, camY, camZ, q.chunk.chunkX, q.chunk.chunkY, q.chunk.chunkZ);
		que.sort(new Comparator<VoxelChunkQue>(){
			public int compare(VoxelChunkQue a, VoxelChunkQue b){ return a.tempDistance==b.tempDistance?0:a.tempDistance>b.tempDistance?1:-1; }
		});
	}
	private boolean updateList(){
		if(que.isEmpty()){
			searchForNewChunks();
			return true;
		}
		if(que.get(0).update())que.remove(0);
		return false;
	}
	private void searchForNewChunks(){
		//TODO Ping chunks out side of immediate range.
	}
	public void unloadChunk(VoxelChunk chunk){
		for(int i = 0; i<que.size(); i++){
			if(que.get(i).chunk==chunk){
				que.remove(i);
				return;
			}
		}
	}
	public void loadChunk(VoxelChunk chunk){ que.add(new VoxelChunkQue(world, chunk)); }
	public boolean isChunkVisible(VoxelChunk chunk){ return getDistanceSquared(camX, camY, camZ, chunk.chunkX, chunk.chunkY, chunk.chunkZ)<CAMERA_RANGE_SQUARED; }
	private static double getDistanceSquared(int x1, int y1, int z1, int x2, int y2, int z2){ return Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)+Math.pow(z1-z2, 2); }
}