package wraithaven.conquest.client.GameWorld.Voxel;

public class VoxelBlock{
	public final int x, y, z;
	private boolean xUp, xDown, yUp, yDown, zUp, zDown;
	private boolean hidden;
	final Quad[] quads = new Quad[6];
	public final VoxelChunk chunk;
	public final BlockType type;
	private static final float[] WHITE_COLORS = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	VoxelBlock(VoxelChunk chunk, int x, int y, int z, BlockType type){
		this.x=x;
		this.y=y;
		this.z=z;
		this.chunk=chunk;
		this.type=type;
	}
	private void setHidden(boolean hidden){
		if(this.hidden==hidden)return;
		this.hidden=hidden;
		if(hidden)chunk.addHidden();
		else chunk.removeHidden();
	}
	boolean isSideShown(int side){
		if(side==0)return xUp;
		if(side==1)return xDown;
		if(side==2)return yUp;
		if(side==3)return yDown;
		if(side==4)return zUp;
		if(side==5)return zDown;
		return false;
	}
	void showSide(int side, boolean show){
		if(side==0){
			if(xUp!=show){
				xUp=show;
				updateSideVisibility(side, show);
			}
		}
		if(side==1){
			if(xDown!=show){
				xDown=show;
				updateSideVisibility(side, show);
			}
		}
		if(side==2){
			if(yUp!=show){
				yUp=show;
				updateSideVisibility(side, show);
			}
		}
		if(side==3){
			if(yDown!=show){
				yDown=show;
				updateSideVisibility(side, show);
			}
		}
		if(side==4){
			if(zUp!=show){
				zUp=show;
				updateSideVisibility(side, show);
			}
		}
		if(side==5){
			if(zDown!=show){
				zDown=show;
				updateSideVisibility(side, show);
			}
		}
		if(!xUp&&!xDown&&!yUp&&!yDown&&!zUp&&!zDown)setHidden(true);
		else setHidden(false);
	}
	private void updateSideVisibility(int side, boolean show){
		if(show)quads[side]=Cube.generateQuad(side, x, y, z, type.getRotation(side), WHITE_COLORS);
		else quads[side]=null;
	}
	public Quad getQuad(int side){ return quads[side]; }
	public boolean isHidden(){ return hidden; }
}