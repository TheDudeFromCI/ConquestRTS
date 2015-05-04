package wraithaven.conquest.client.GameWorld;

import java.nio.FloatBuffer;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Voxel.BlockType;

public class BasicBlock implements BlockType{
	private VoxelWorld world;
	private boolean block_dd, block_d0, block_du, block_0d, block_0u, block_ud, block_u0, block_uu;
	private final CubeTextures textures;
	private static final float shadowIntensity = 0.65f;
	public BasicBlock(CubeTextures textures, VoxelWorld world){
		this.textures=textures;
		this.world=world;
	}
	public Texture getTexture(int side){
		if(side==0)return textures.xUp;
		if(side==1)return textures.xDown;
		if(side==2)return textures.yUp;
		if(side==3)return textures.yDown;
		if(side==4)return textures.zUp;
		if(side==5)return textures.zDown;
		return null;
	}
	public int getRotation(int side){
		if(side==0)return textures.xUpRotation;
		if(side==1)return textures.xDownRotation;
		if(side==2)return textures.yUpRotation;
		if(side==3)return textures.yDownRotation;
		if(side==4)return textures.zUpRotation;
		if(side==5)return textures.zDownRotation;
		return 0;
	}
	public boolean setupShadows(FloatBuffer data, int side, int x, int y, int z){
		if(side==0){
			block_dd=world.getBlock(x+1, y-1, z-1, false)!=null;
			block_d0=world.getBlock(x+1, y-1, z, false)!=null;
			block_du=world.getBlock(x+1, y-1, z+1, false)!=null;
			block_0d=world.getBlock(x+1, y, z-1, false)!=null;
			block_0u=world.getBlock(x+1, y, z+1, false)!=null;
			block_ud=world.getBlock(x+1, y+1, z-1, false)!=null;
			block_u0=world.getBlock(x+1, y+1, z, false)!=null;
			block_uu=world.getBlock(x+1, y+1, z+1, false)!=null;
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
			block_dd=world.getBlock(x-1, y-1, z-1, false)!=null;
			block_d0=world.getBlock(x-1, y-1, z, false)!=null;
			block_du=world.getBlock(x-1, y-1, z+1, false)!=null;
			block_0d=world.getBlock(x-1, y, z-1, false)!=null;
			block_0u=world.getBlock(x-1, y, z+1, false)!=null;
			block_ud=world.getBlock(x-1, y+1, z-1, false)!=null;
			block_u0=world.getBlock(x-1, y+1, z, false)!=null;
			block_uu=world.getBlock(x-1, y+1, z+1, false)!=null;
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
			block_dd=world.getBlock(x-1, y+1, z-1, false)!=null;
			block_d0=world.getBlock(x-1, y+1, z, false)!=null;
			block_du=world.getBlock(x-1, y+1, z+1, false)!=null;
			block_0d=world.getBlock(x, y+1, z-1, false)!=null;
			block_0u=world.getBlock(x, y+1, z+1, false)!=null;
			block_ud=world.getBlock(x+1, y+1, z-1, false)!=null;
			block_u0=world.getBlock(x+1, y+1, z, false)!=null;
			block_uu=world.getBlock(x+1, y+1, z+1, false)!=null;
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
			block_dd=world.getBlock(x-1, y-1, z-1, false)!=null;
			block_d0=world.getBlock(x-1, y-1, z, false)!=null;
			block_du=world.getBlock(x-1, y-1, z+1, false)!=null;
			block_0d=world.getBlock(x, y-1, z-1, false)!=null;
			block_0u=world.getBlock(x, y-1, z+1, false)!=null;
			block_ud=world.getBlock(x+1, y-1, z-1, false)!=null;
			block_u0=world.getBlock(x+1, y-1, z, false)!=null;
			block_uu=world.getBlock(x+1, y-1, z+1, false)!=null;
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
			block_dd=world.getBlock(x-1, y-1, z+1, false)!=null;
			block_d0=world.getBlock(x-1, y, z+1, false)!=null;
			block_du=world.getBlock(x-1, y+1, z+1, false)!=null;
			block_0d=world.getBlock(x, y-1, z+1, false)!=null;
			block_0u=world.getBlock(x, y+1, z+1, false)!=null;
			block_ud=world.getBlock(x+1, y-1, z+1, false)!=null;
			block_u0=world.getBlock(x+1, y, z+1, false)!=null;
			block_uu=world.getBlock(x+1, y+1, z+1, false)!=null;
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
			block_dd=world.getBlock(x-1, y-1, z-1, false)!=null;
			block_d0=world.getBlock(x-1, y, z-1, false)!=null;
			block_du=world.getBlock(x-1, y+1, z-1, false)!=null;
			block_0d=world.getBlock(x, y-1, z-1, false)!=null;
			block_0u=world.getBlock(x, y+1, z-1, false)!=null;
			block_ud=world.getBlock(x+1, y-1, z-1, false)!=null;
			block_u0=world.getBlock(x+1, y, z-1, false)!=null;
			block_uu=world.getBlock(x+1, y+1, z-1, false)!=null;
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
}