package wraithaven.conquest.client.BuildingCreator;

import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;

public class IconManager{
	private final BlockIcon[] icons = new BlockIcon[20];
	int selectedSlot;
	public int tempHeld = -1;
	public void addIcon(BlockIcon icon, int slot){
		if(icon!=null) icon.itemSlot = slot;
		icons[slot] = icon;
	}
	public void dispose(){
		for(int i = 0; i<icons.length; i++){
			if(icons[i]==null) continue;
			icons[i].dispose();
		}
	}
	public BlockIcon getIcon(int id){
		return icons[id];
	}
	public CubeTextures getSelectedCubeTextures(){
		if(icons[selectedSlot]==null) return null;
		return icons[selectedSlot].block.block.originalCubeTextures;
	}
	public BlockShape getSelectedShape(){
		if(icons[selectedSlot]==null) return null;
		return icons[selectedSlot].block.block.shape;
	}
	public void render(){
		for(int i = 0; i<icons.length; i++){
			if(i==tempHeld) continue;
			if(icons[i]!=null) icons[i].render(BlockIcon.getX(i), BlockIcon.getY(i), 0, 0, 0);
		}
	}
}