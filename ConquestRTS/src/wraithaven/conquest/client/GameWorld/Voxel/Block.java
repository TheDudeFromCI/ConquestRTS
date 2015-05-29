package wraithaven.conquest.client.GameWorld.Voxel;

public class Block{
	private static final int[] tempRotations = new int[6];
	public final CubeTextures originalCubeTextures;
	public final BlockRotation rotation;
	public final BlockShape shape;
	public final CubeTextures textures;
	public Block(BlockShape shape, CubeTextures textures, BlockRotation rotation){
		this.shape = shape;
		this.rotation = rotation;
		originalCubeTextures = textures;
		if(rotation==BlockRotation.ROTATION_0) this.textures = textures;
		else{
			this.textures = new CubeTextures();
			rotation.rotateTextures(textures, tempRotations);
			int j, s;
			for(j = 0; j<6; j++){
				s = rotation.rotateSide(j);
				this.textures.setColor(j, textures.colors[s*3], textures.colors[s*3+1], textures.colors[s*3+2]);
				this.textures.setTexture(j, textures.getTexture(s), tempRotations[j]);
			}
		}
	}
	@Override public boolean equals(Object o){
		if(o instanceof Block){
			Block b = (Block)o;
			return b.shape==shape&&b.rotation==rotation&&b.originalCubeTextures==originalCubeTextures;
		}
		return false;
	}
}