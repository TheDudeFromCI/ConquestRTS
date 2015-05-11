package wraithaven.conquest.client.BuildingCreator;

import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Shape4;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Shape3;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Shape2;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Cube;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.BlockTextures;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Pyramid;

public class IconManager{
	int selectedSlot;
	private final BlockIcon[] icons = new BlockIcon[20];
	{
		CubeTextures textures = new CubeTextures();
		textures.xUp=BlockTextures.sideDirt.getTexture();
		textures.xUpRotation=0;
		textures.xDown=BlockTextures.sideDirt.getTexture();
		textures.xDownRotation=1;
		textures.yUp=BlockTextures.grass.getTexture();
		textures.yUpRotation=3;
		textures.yDown=BlockTextures.dirt.getTexture();
		textures.yDownRotation=0;
		textures.zUp=BlockTextures.sideDirt.getTexture();
		textures.zUpRotation=3;
		textures.zDown=BlockTextures.sideDirt.getTexture();
		textures.zDownRotation=2;
		addIcon(new BlockIcon(new Pyramid(), textures), 0);
		addIcon(new BlockIcon(new Shape2(), textures), 1);
		addIcon(new BlockIcon(new Shape3(), textures), 2);
		addIcon(new BlockIcon(new Shape4(), textures), 3);
		addIcon(new BlockIcon(new Cube(), textures), 4);
	}
	public void addIcon(BlockIcon icon, int slot){
		icon.itemSlot=slot;
		icons[slot]=icon;
	}
	public BlockShape getSelectedShape(){
		if(icons[selectedSlot]==null)return null;
		return icons[selectedSlot].shape;
	}
	public CubeTextures getSelectedCubeTextures(){
		if(icons[selectedSlot]==null)return null;
		return icons[selectedSlot].textures;
	}
	public void rebuildIcon(int id){ if(icons[id]!=null)icons[id].block.rebuild(); }
	public void render(){ for(int i = 0; i<icons.length; i++)if(icons[i]!=null)icons[i].render(); }
	public BlockIcon getIcon(int id){ return icons[id]; }
}