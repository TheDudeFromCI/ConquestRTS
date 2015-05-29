package wraithaven.conquest.client.GameWorld.Voxel;

import wraithaven.conquest.client.GameWorld.Plotter;
import wraithaven.conquest.client.GameWorld.Vec3i;

public class CameraTarget{
	private final CameraTargetCallback callback = new CameraTargetCallback();
	private final Camera cam;
	private int chunkX,
			chunkY,
			chunkZ;
	private Chunk lastChunk;
	private final Plotter plotter = new Plotter(0, 0, 0, 1, 1, 1);
	private final Vec3i v = plotter.get();
	public CameraTarget(Camera cam){
		this.cam = cam;
	}
	public CameraTargetCallback getTargetBlock(VoxelWorld world, int range, boolean load){
		lastChunk = null;
		plotter.plot(cam.getPosition(), cam.getDirection(), range);
		while(plotter.next()){
			chunkX = v.x>>Chunk.CHUNK_BITS;
			chunkY = v.y>>Chunk.CHUNK_BITS;
			chunkZ = v.z>>Chunk.CHUNK_BITS;
			if(lastChunk==null||lastChunk.chunkX!=chunkX||lastChunk.chunkY!=chunkY||lastChunk.chunkZ!=chunkZ) lastChunk = world.getChunk(chunkX, chunkY, chunkZ, load);
			if(lastChunk!=null&&(callback.block = lastChunk.getBlock(v.x, v.y, v.z))!=-1){
				callback.x = v.x;
				callback.y = v.y;
				callback.z = v.z;
				callback.side = plotter.getSideHit();
				return callback;
			}
		}
		callback.block = -1;
		callback.side = -1;
		return callback;
	}
}