package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class UiElement{
	public Texture texture;
	public float x, y, w, h;
	public UiElement(Texture texture){
		this.texture = texture;
	}
	public UiElement(){}
}