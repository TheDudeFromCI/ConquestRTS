package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.BuildingCreator.Loop;

public class Block{
	private boolean block_dd, block_d0, block_du, block_0d, block_0u, block_ud, block_u0, block_uu;
	public final int x, y, z;
	private boolean hidden;
	protected final Quad[] quads = new Quad[6];
	public final Chunk chunk;
	protected static final float[] WHITE_COLORS = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	protected static final float[] TEXTURE_POSITIONS = {0, 1, 0, 1};
	private static final float shadowIntensity = 0.65f;
	Block(Chunk chunk, int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
		this.chunk=chunk;
	}
	protected void setHidden(boolean hidden){
		if(this.hidden==hidden)return;
		this.hidden=hidden;
		if(hidden)chunk.addHidden();
		else chunk.removeHidden();
	}
	void showSide(int side, boolean show){
		if(isSideShown(side)==show)return;
		updateSideVisibility(side, show);
		if(isFullyHidden())setHidden(true);
		else setHidden(false);
	}
	private void updateSideVisibility(int side, boolean show){
		if(show)quads[side]=Cube.generateQuad(side, x, y, z, 0, WHITE_COLORS, 1, TEXTURE_POSITIONS);
		else quads[side]=null;
	}
	public Block getTouchingBlock(int side){
		if(side==0)return chunk.world.getBlock(x+1, y, z, false);
		if(side==1)return chunk.world.getBlock(x-1, y, z, false);
		if(side==2)return chunk.world.getBlock(x, y+1, z, false);
		if(side==3)return chunk.world.getBlock(x, y-1, z, false);
		if(side==4)return chunk.world.getBlock(x, y, z+1, false);
		if(side==5)return chunk.world.getBlock(x, y, z-1, false);
		return null;
	}
	public static Texture getTexture(int side){
		if(side==2)return Texture.getTexture(ClientLauncher.textureFolder, "Grass.png", 4, MipmapQuality.HIGH);
		return Texture.getTexture(ClientLauncher.textureFolder, "Dirt.png", 4, MipmapQuality.HIGH);
	}
	public boolean setupShadows(FloatBuffer data, int side, int x, int y, int z){
		if(side==0){
			if(Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y, z, false)!=null){
				for(int i = 0; i<15; i++)data.put(i, shadowIntensity);
				return false;
			}
		}
		if(side==1){
			if(Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y, z, false)!=null){
				for(int i = 0; i<15; i++)data.put(i, shadowIntensity);
				return false;
			}
		}
		if(side==2){
			if(Loop.INSTANCE.getVoxelWorld().getBlock(x, y+1, z, false)!=null){
				for(int i = 0; i<15; i++)data.put(i, shadowIntensity);
				return false;
			}
		}
		if(side==3){
			if(Loop.INSTANCE.getVoxelWorld().getBlock(x, y-1, z, false)!=null){
				for(int i = 0; i<15; i++)data.put(i, shadowIntensity);
				return false;
			}
		}
		if(side==4){
			if(Loop.INSTANCE.getVoxelWorld().getBlock(x, y, z+1, false)!=null){
				for(int i = 0; i<15; i++)data.put(i, shadowIntensity);
				return false;
			}
		}
		if(side==5){
			if(Loop.INSTANCE.getVoxelWorld().getBlock(x, y, z-1, false)!=null){
				for(int i = 0; i<15; i++)data.put(i, shadowIntensity);
				return false;
			}
		}
		if(side==0){
			block_dd=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z-1, false)!=null;
			block_d0=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z, false)!=null;
			block_du=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z+1, false)!=null;
			block_0d=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y, z-1, false)!=null;
			block_0u=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y, z+1, false)!=null;
			block_ud=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z-1, false)!=null;
			block_u0=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z, false)!=null;
			block_uu=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z+1, false)!=null;
			if(block_uu||block_0u||block_u0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_dd||block_0d||block_d0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==1){
			block_dd=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z-1, false)!=null;
			block_d0=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z, false)!=null;
			block_du=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z+1, false)!=null;
			block_0d=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y, z-1, false)!=null;
			block_0u=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y, z+1, false)!=null;
			block_ud=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z-1, false)!=null;
			block_u0=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z, false)!=null;
			block_uu=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z+1, false)!=null;
			if(block_dd||block_0d||block_d0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_uu||block_0u||block_u0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==2){
			block_dd=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z-1, false)!=null;
			block_d0=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z, false)!=null;
			block_du=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z+1, false)!=null;
			block_0d=Loop.INSTANCE.getVoxelWorld().getBlock(x, y+1, z-1, false)!=null;
			block_0u=Loop.INSTANCE.getVoxelWorld().getBlock(x, y+1, z+1, false)!=null;
			block_ud=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z-1, false)!=null;
			block_u0=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z, false)!=null;
			block_uu=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z+1, false)!=null;
			if(block_dd||block_0d||block_d0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_uu||block_0u||block_u0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==3){
			block_dd=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z-1, false)!=null;
			block_d0=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z, false)!=null;
			block_du=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z+1, false)!=null;
			block_0d=Loop.INSTANCE.getVoxelWorld().getBlock(x, y-1, z-1, false)!=null;
			block_0u=Loop.INSTANCE.getVoxelWorld().getBlock(x, y-1, z+1, false)!=null;
			block_ud=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z-1, false)!=null;
			block_u0=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z, false)!=null;
			block_uu=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z+1, false)!=null;
			if(block_uu||block_u0||block_0u){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_0u||block_d0){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_dd||block_0d||block_d0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==4){
			block_dd=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z+1, false)!=null;
			block_d0=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y, z+1, false)!=null;
			block_du=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z+1, false)!=null;
			block_0d=Loop.INSTANCE.getVoxelWorld().getBlock(x, y-1, z+1, false)!=null;
			block_0u=Loop.INSTANCE.getVoxelWorld().getBlock(x, y+1, z+1, false)!=null;
			block_ud=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z+1, false)!=null;
			block_u0=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y, z+1, false)!=null;
			block_uu=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z+1, false)!=null;
			if(block_uu||block_0u||block_u0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_dd||block_0d||block_d0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if(side==5){
			block_dd=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y-1, z-1, false)!=null;
			block_d0=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y, z-1, false)!=null;
			block_du=Loop.INSTANCE.getVoxelWorld().getBlock(x-1, y+1, z-1, false)!=null;
			block_0d=Loop.INSTANCE.getVoxelWorld().getBlock(x, y-1, z-1, false)!=null;
			block_0u=Loop.INSTANCE.getVoxelWorld().getBlock(x, y+1, z-1, false)!=null;
			block_ud=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y-1, z-1, false)!=null;
			block_u0=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y, z-1, false)!=null;
			block_uu=Loop.INSTANCE.getVoxelWorld().getBlock(x+1, y+1, z-1, false)!=null;
			if(block_dd||block_0d||block_d0){
				data.put(0, shadowIntensity);
				data.put(1, shadowIntensity);
				data.put(2, shadowIntensity);
			}else{
				data.put(0, 1);
				data.put(1, 1);
				data.put(2, 1);
			}
			if(block_du||block_d0||block_0u){
				data.put(3, shadowIntensity);
				data.put(4, shadowIntensity);
				data.put(5, shadowIntensity);
			}else{
				data.put(3, 1);
				data.put(4, 1);
				data.put(5, 1);
			}
			if(block_uu||block_0u||block_u0){
				data.put(6, shadowIntensity);
				data.put(7, shadowIntensity);
				data.put(8, shadowIntensity);
			}else{
				data.put(6, 1);
				data.put(7, 1);
				data.put(8, 1);
			}
			if(block_ud||block_0d||block_u0){
				data.put(9, shadowIntensity);
				data.put(10, shadowIntensity);
				data.put(11, shadowIntensity);
			}else{
				data.put(9, 1);
				data.put(10, 1);
				data.put(11, 1);
			}
		}
		if((block_d0&&block_0d)||(block_u0&&block_0u)){
			data.put(12, shadowIntensity);
			data.put(13, shadowIntensity);
			data.put(14, shadowIntensity);
		}else if(flat())return false;
		else{
			data.put(12, 1);
			data.put(13, 1);
			data.put(14, 1);
		}
		return true;
	}
	private boolean flat(){
		if(block_dd||block_ud||block_du||block_uu){
			if(block_dd){
				if(block_d0||block_0d)return true;
				return false;
			}
			if(block_du){
				if(block_d0||block_0u)return true;
				return false;
			}
			if(block_ud){
				if(block_u0||block_0d)return true;
				return false;
			}
			if(block_uu){
				if(block_u0||block_0u)return true;
				return false;
			}
		}else{
			if(block_d0==block_u0)return true;
			if(block_0d==block_0u)return true;
		}
		return false;
	}
	public Quad getQuad(int side){ return quads[side]; }
	public boolean isHidden(){ return hidden; }
	boolean isSideShown(int side){ return quads[side]!=null; }
	protected boolean isFullyHidden(){ return quads[0]!=null&&quads[1]!=null&&quads[2]!=null&&quads[3]!=null&&quads[4]!=null&&quads[5]!=null; }
}