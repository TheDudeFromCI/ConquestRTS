package wraithaven.conquest.client;

import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Texture;
import wraith.library.LWJGL.Voxel.BlockType;
import wraith.library.LWJGL.Voxel.VoxelWorld;

public class BasicBlock implements BlockType{
	private VoxelWorld world;
	private boolean block_dd, block_d0, block_du, block_0d, block_0u, block_ud, block_u0, block_uu;
	private final CubeTextures textures;
	public static final float shadowIntensity = 0.25f;
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
	public boolean setupShadows(float[] colors, int side, int x, int y, int z){
		if(side==0){
			block_dd=world.getBlock(x+1, y-1, z-1, false)!=null;
			block_d0=world.getBlock(x+1, y-1, z, false)!=null;
			block_du=world.getBlock(x+1, y-1, z+1, false)!=null;
			block_0d=world.getBlock(x+1, y, z-1, false)!=null;
			block_0u=world.getBlock(x+1, y, z+1, false)!=null;
			block_ud=world.getBlock(x+1, y+1, z-1, false)!=null;
			block_u0=world.getBlock(x+1, y+1, z, false)!=null;
			block_uu=world.getBlock(x+1, y+1, z+1, false)!=null;
			if(block_uu||block_0u||block_u0)colors[0]=colors[1]=colors[2]=shadowIntensity;
			else colors[0]=colors[1]=colors[2]=1f;
			if(block_du||block_d0||block_0u)colors[3]=colors[4]=colors[5]=shadowIntensity;
			else colors[3]=colors[4]=colors[5]=1f;
			if(block_dd||block_0d||block_d0)colors[6]=colors[7]=colors[8]=shadowIntensity;
			else colors[6]=colors[7]=colors[8]=1f;
			if(block_ud||block_0d||block_u0)colors[9]=colors[10]=colors[11]=shadowIntensity;
			else colors[9]=colors[10]=colors[11]=1f;
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
			if(block_dd||block_0d||block_d0)colors[0]=colors[1]=colors[2]=shadowIntensity;
			else colors[0]=colors[1]=colors[2]=1f;
			if(block_du||block_d0||block_0u)colors[3]=colors[4]=colors[5]=shadowIntensity;
			else colors[3]=colors[4]=colors[5]=1f;
			if(block_uu||block_0u||block_u0)colors[6]=colors[7]=colors[8]=shadowIntensity;
			else colors[6]=colors[7]=colors[8]=1f;
			if(block_ud||block_0d||block_u0)colors[9]=colors[10]=colors[11]=shadowIntensity;
			else colors[9]=colors[10]=colors[11]=1f;
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
			if(block_dd||block_0d||block_d0)colors[0]=colors[1]=colors[2]=shadowIntensity;
			else colors[0]=colors[1]=colors[2]=1f;
			if(block_du||block_d0||block_0u)colors[3]=colors[4]=colors[5]=shadowIntensity;
			else colors[3]=colors[4]=colors[5]=1f;
			if(block_uu||block_0u||block_u0)colors[6]=colors[7]=colors[8]=shadowIntensity;
			else colors[6]=colors[7]=colors[8]=1f;
			if(block_ud||block_0d||block_u0)colors[9]=colors[10]=colors[11]=shadowIntensity;
			else colors[9]=colors[10]=colors[11]=1f;
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
			if(block_uu||block_u0||block_0u)colors[0]=colors[1]=colors[2]=shadowIntensity;
			else colors[0]=colors[1]=colors[2]=1f;
			if(block_du||block_0u||block_d0)colors[3]=colors[4]=colors[5]=shadowIntensity;
			else colors[3]=colors[4]=colors[5]=1f;
			if(block_dd||block_0d||block_d0)colors[6]=colors[7]=colors[8]=shadowIntensity;
			else colors[6]=colors[7]=colors[8]=1f;
			if(block_ud||block_0d||block_u0)colors[9]=colors[10]=colors[11]=shadowIntensity;
			else colors[9]=colors[10]=colors[11]=1f;
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
			if(block_dd||block_0d||block_d0)colors[6]=colors[7]=colors[8]=shadowIntensity;
			else colors[6]=colors[7]=colors[8]=1f;
			if(block_du||block_d0||block_0u)colors[3]=colors[4]=colors[5]=shadowIntensity;
			else colors[3]=colors[4]=colors[5]=1f;
			if(block_uu||block_0u||block_u0)colors[0]=colors[1]=colors[2]=shadowIntensity;
			else colors[0]=colors[1]=colors[2]=1f;
			if(block_ud||block_0d||block_u0)colors[9]=colors[10]=colors[11]=shadowIntensity;
			else colors[9]=colors[10]=colors[11]=1f;
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
			if(block_dd||block_0d||block_d0)colors[0]=colors[1]=colors[2]=shadowIntensity;
			else colors[0]=colors[1]=colors[2]=1f;
			if(block_du||block_d0||block_0u)colors[3]=colors[4]=colors[5]=shadowIntensity;
			else colors[3]=colors[4]=colors[5]=1f;
			if(block_uu||block_0u||block_u0)colors[6]=colors[7]=colors[8]=shadowIntensity;
			else colors[6]=colors[7]=colors[8]=1f;
			if(block_ud||block_0d||block_u0)colors[9]=colors[10]=colors[11]=shadowIntensity;
			else colors[9]=colors[10]=colors[11]=1f;
		}
		if((block_d0&&block_0d)||(block_u0&&block_0u))colors[12]=colors[13]=colors[14]=shadowIntensity;
		else if(flat())return false;
		else colors[12]=colors[13]=colors[14]=1f;
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