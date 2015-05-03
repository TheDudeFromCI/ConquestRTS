package wraithaven.conquest.client.GameWorld;

import java.util.ArrayList;
import java.util.Comparator;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelChunk;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTargetCallback;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorldListener;

public class CatcheChunkLoader implements VoxelWorldListener{
	private int camX, camY, camZ, x, y, z, startX, startY, startZ, endX, endY, endZ;
	private int lastCamX, lastCamY=-100, lastCamZ, tempRange;
	private VoxelWorld world;
	private Camera cam;
	private boolean cameraMoved;
	private CameraTarget cameraTarget;
	private CameraTargetCallback callback;
	private double lastCameraPing;
	private final ArrayList<VoxelChunkQue> que = new ArrayList();
	public static final int CATCHE_RANGE = 10;
	private static final int CATCHE_RANGE_SQUARED = CATCHE_RANGE*CATCHE_RANGE;
	public static final int WORLD_HEIGHT = 15;
	public static final int CHUNK_HEIGHT = WORLD_HEIGHT>>4;
	public static final double CAMERA_PING_SPEED = 0.333f;
	public void setup(VoxelWorld world, Camera cam){
		this.world=world;
		this.cam=cam;
		cameraTarget=new CameraTarget(cam);
	}
	public void update(int blockCount, double time){
		cameraMoved=false;
		if(time-lastCameraPing>=CAMERA_PING_SPEED){
			lastCameraPing=time;
			getPosition();
			if(lastCamX!=camX||lastCamY!=camY||lastCamZ!=camZ){
				cameraMoved=true;
				lastCamX=camX;
				lastCamY=camY;
				lastCamZ=camZ;
				unloadUneededChunks();
				tempRange=0;
			}
		}
		if(tempRange<CATCHE_RANGE&&que.isEmpty()){
			startX=camX-tempRange;
			startY=Math.max(camY-tempRange, 0);
			startZ=camZ-tempRange;
			endX=camX+tempRange;
			endY=Math.min(camY+tempRange, CHUNK_HEIGHT);
			endZ=camZ+tempRange;
			boolean found = false;
			chunkFinder:for(x=startX; x<=endX; x++){
				for(y=startY; y<=endY; y++){
					for(z=startZ; z<=endZ; z++){
						if(x==startX||x==endX||y==startY||y==endY||z==startZ||z==endZ){
							if(world.getChunk(x, y, z, false)==null){
								world.loadChunk(x, y, z);
								found=true;
								break chunkFinder;
							}
						}
					}
				}
			}
			if(!found)tempRange++;
			sortList();
		}
		for(int i = 0; i<blockCount; i++)if(updateList())return;
	}
	private void getPosition(){
		callback=cameraTarget.getTargetBlock(world, 500, false);
		if(callback.block==null){
			camX=(int)Math.floor(cam.x)>>4;
			camY=(int)Math.floor(cam.y)>>4;
			camZ=(int)Math.floor(cam.z)>>4;
		}else{
			camX=callback.block.chunk.chunkX;
			camY=callback.block.chunk.chunkY;
			camZ=callback.block.chunk.chunkZ;
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
		VoxelChunkQue q;
		for(int i = 0; i<que.size(); i++){
			q=que.get(i);
			if(q.tempDistance==0||cameraMoved)q.tempDistance=getDistanceSquared(camX, camY, camZ, q.chunk.chunkX, q.chunk.chunkY, q.chunk.chunkZ);
		}
		que.sort(new Comparator<VoxelChunkQue>(){
			public int compare(VoxelChunkQue a, VoxelChunkQue b){ return a.tempDistance==b.tempDistance?0:a.tempDistance>b.tempDistance?1:-1; }
		});
	}
	private boolean updateList(){
		if(que.isEmpty())return true;
		if(que.get(0).update()){
			que.remove(0);
			return true;
		}
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
	public boolean isChunkVisible(VoxelChunk chunk){ return !chunk.isHidden()&&cam.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, 16); }
	public void loadChunk(VoxelChunk chunk){ que.add(new VoxelChunkQue(world, chunk)); }
	private static double getDistanceSquared(int x1, int y1, int z1, int x2, int y2, int z2){ return Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)+Math.pow(z1-z2, 2); }
}