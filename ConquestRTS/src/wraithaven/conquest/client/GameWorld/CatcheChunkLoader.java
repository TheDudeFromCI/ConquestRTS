package wraithaven.conquest.client.GameWorld;

import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldListener;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.Voxel.CameraTarget;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;

@SuppressWarnings("unused") public class CatcheChunkLoader implements VoxelWorldListener{
	static final int WORLD_HEIGHT = 15;
	static final int CHUNK_HEIGHT = CatcheChunkLoader.WORLD_HEIGHT>>Chunk.CHUNK_BITS;
	private Camera cam;
	private CameraTarget cameraTarget;
	public boolean isChunkVisible(Chunk chunk){
		return !chunk.isHidden()&&cam.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, Chunk.BLOCKS_PER_CHUNK);
	}
	public void loadChunk(Chunk chunk){
		// que.add(new VoxelChunkQue(world, chunk));
	}
	public void setup(VoxelWorld world, Camera cam){
		this.cam = cam;
		cameraTarget = new CameraTarget(cam);
	}
	public void unloadChunk(Chunk chunk){
		// for(int i = 0; i<que.size(); i++){
		// if(que.get(i).chunk==chunk){
		// que.remove(i);
		// return;
		// }
		// }
	}
	public void update(int blockCount, double time){
		// cameraMoved=false;
		// if(time-lastCameraPing>=CAMERA_PING_SPEED){
		// lastCameraPing=time;
		// getPosition();
		// if(lastCamX!=camX||lastCamY!=camY||lastCamZ!=camZ){
		// cameraMoved=true;
		// lastCamX=camX;
		// lastCamY=camY;
		// lastCamZ=camZ;
		// unloadUneededChunks();
		// tempRange=0;
		// }
		// }
		// if(tempRange<CATCHE_RANGE&&que.isEmpty()){
		// startX=camX-tempRange;
		// startY=Math.max(camY-tempRange, 0);
		// startZ=camZ-tempRange;
		// endX=camX+tempRange;
		// endY=Math.min(camY+tempRange, CHUNK_HEIGHT);
		// endZ=camZ+tempRange;
		// boolean found = false;
		// chunkFinder:for(x=startX; x<=endX; x++){
		// for(y=startY; y<=endY; y++){
		// for(z=startZ; z<=endZ; z++){
		// if(x==startX||x==endX||y==startY||y==endY||z==startZ||z==endZ){
		// if(world.getChunk(x, y, z, false)==null){
		// world.loadChunk(x, y, z);
		// found=true;
		// break chunkFinder;
		// }
		// }
		// }
		// }
		// }
		// if(!found)tempRange++;
		// sortList();
		// }
		// for(int i = 0; i<blockCount; i++)if(updateList())return;
	}
}