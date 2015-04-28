package wraithaven.conquest.client;

import java.util.ArrayList;
import java.util.Comparator;
import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.CameraTarget;
import wraith.library.LWJGL.Voxel.VoxelBlock;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;

public class CatcheChunkLoader implements VoxelWorldListener{
	private int camX, camY, camZ, x, y, z, startX, startY, startZ, endX, endY, endZ;
	private int lastCamX, lastCamY=-100, lastCamZ, tempRange;
	private VoxelWorld world;
	private Camera cam;
	private VoxelBlock block;
	private final ArrayList<VoxelChunkQue> que = new ArrayList();
	public static final int CATCHE_RANGE = 18;
	private static final int CATCHE_RANGE_SQUARED = CATCHE_RANGE*CATCHE_RANGE;
	public static final int CAMERA_RANGE = 15;
	private static final int CAMERA_RANGE_SQUARED = CAMERA_RANGE*CAMERA_RANGE;
	public static final int WORLD_HEIGHT = 15;
	public static final int CHUNK_HEIGHT = WORLD_HEIGHT>>4;
	public void setup(VoxelWorld world, Camera cam){
		this.world=world;
		this.cam=cam;
		cameraTarget=new CameraTarget(cam);
	}
	public void update(int blockCount){
		getPosition();
		if(lastCamX!=camX||lastCamY!=camY||lastCamZ!=camZ){
			lastCamX=camX;
			lastCamY=camY;
			lastCamZ=camZ;
			unloadUneededChunks();
			tempRange=0;
		}
		if(tempRange<CATCHE_RANGE){
			startX=camX-tempRange;
			startY=Math.max(camY-tempRange, 0);
			startZ=camZ-tempRange;
			endX=camX+tempRange;
			endY=Math.min(camY+tempRange, CHUNK_HEIGHT);
			endZ=camZ+tempRange;
			for(x=startX; x<=endX; x++)for(y=startY; y<=endY; y++)for(z=startZ; z<=endZ; z++)if(x==startX||x==endX||y==startY||y==endY||z==startZ||z==endZ)world.getChunk(x, y, z);
			tempRange++;
			sortList();
		}else tempRange=0;
		for(int i = 0; i<blockCount; i++)if(updateList())return;
	}
	private CameraTarget cameraTarget;
	private void getPosition(){
		if(Test.ISOMETRIC){
			block=cameraTarget.getTargetBlock(world, 500, true);
			if(block==null){
				camX=(int)Math.floor(cam.x)>>4;
				camY=(int)Math.floor(cam.y)>>4;
				camZ=(int)Math.floor(cam.z)>>4;
			}else{
				camX=block.getChunk().chunkX;
				camY=block.getChunk().chunkY;
				camZ=block.getChunk().chunkZ;
			}
		}else{
			camX=(int)Math.floor(cam.x)>>4;
			camY=(int)Math.floor(cam.y)>>4;
			camZ=(int)Math.floor(cam.z)>>4;
		}
	}
	private void unloadUneededChunks(){
		VoxelChunk chunk;
		for(int i = 0; i<world.getChunkCount();){
			chunk=world.getChunk(i);
			if(getDistanceSquared(camX, camY, camZ, chunk.chunkX, chunk.chunkY, chunk.chunkZ)>CATCHE_RANGE_SQUARED)world.unloadChunk(chunk);
			else i++;
		}
	}
	private void sortList(){
		for(VoxelChunkQue q : que){
			q.tempDistance=getDistanceSquared(camX, camY, camZ, q.chunk.chunkX, q.chunk.chunkY, q.chunk.chunkZ);
			if(getDistanceSquared(camX, camY, camZ, q.chunk.chunkX, q.chunk.chunkY, q.chunk.chunkZ)>CAMERA_RANGE_SQUARED||!cam.frustum.cubeInFrustum(q.chunk.startX, q.chunk.startY, q.chunk.startZ, 16))q.tempDistance+=CAMERA_RANGE_SQUARED;
		}
		que.sort(new Comparator<VoxelChunkQue>(){
			public int compare(VoxelChunkQue a, VoxelChunkQue b){ return a.tempDistance==b.tempDistance?0:a.tempDistance>b.tempDistance?1:-1; }
		});
	}
	private boolean updateList(){
		if(que.isEmpty())return true;
		if(que.get(0).update())que.remove(0);
		return false;
	}
	public void unloadChunk(VoxelChunk chunk){
		for(int i = 0; i<que.size(); i++){
			if(que.get(i).chunk==chunk){
				que.remove(i);
				return;
			}
		}
	}
	public boolean isChunkVisible(VoxelChunk chunk){ return !chunk.isHidden()&&getDistanceSquared(camX, camY, camZ, chunk.chunkX, chunk.chunkY, chunk.chunkZ)<CAMERA_RANGE_SQUARED&&cam.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, 16); }
	public void loadChunk(VoxelChunk chunk){ que.add(new VoxelChunkQue(world, chunk)); }
	private static double getDistanceSquared(int x1, int y1, int z1, int x2, int y2, int z2){ return Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)+Math.pow(z1-z2, 2); }
}