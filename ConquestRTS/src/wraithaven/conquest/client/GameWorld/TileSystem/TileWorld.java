package wraithaven.conquest.client.GameWorld.TileSystem;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;

public class TileWorld{
	private final CubeTextures textures;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private final ArrayList<Tile> tiles = new ArrayList();
	public TileWorld(){
		textures = new CubeTextures();
	}
	public void addTile(Tile t){
		tiles.add(t);
	}
	public void removeTile(Tile t){
		tiles.remove(t);
	}
	public Tile getTile(int x, int z){
		for(Tile t : tiles)
			if(t.x==x
				&&t.z==z)return t;
		return null;
	}
	void addQuad(Quad q, Texture t){
		for(QuadBatch b : batches)
			if(!b.isFull()
					&&b.getTexture()==t){
				b.addQuad(q);
				return;
			}
		QuadBatch b = new QuadBatch(t);
		b.addQuad(q);
		batches.add(b);
	}
	void removeQuad(Quad q){
		for(QuadBatch b : batches)
			if(b.containsQuad(q)){
				b.removeQuad(q);
				return;
			}
	}
	void recompileBuffers(){
		clearEmptyBuffers();
		for(QuadBatch q : batches)
			q.recompileBuffer();
	}
	private void clearEmptyBuffers(){
		for(int i = 0; i<batches.size();)
			if(batches.get(i).getSize()==0){
				batches.get(i).cleanUp();
				batches.remove(i);
			}else i++;
	}
	public CubeTextures getTextures(){
		return textures;
	}
	public void render(){
		Texture t = null;
		for(QuadBatch batch : batches){
			if(t==null
					||t!=batch.getTexture()){
				t = batch.getTexture();
				t.bind();
			}
			batch.renderPart();
		}
	}
	public int getTileCount(){
		return tiles.size();
	}
	public Tile getTile(int index){
		return tiles.get(index);
	}
}