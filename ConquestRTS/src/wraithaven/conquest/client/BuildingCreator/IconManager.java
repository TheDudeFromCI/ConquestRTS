package wraithaven.conquest.client.BuildingCreator;

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
		Pyramid p = new Pyramid();
		addIcon(new BlockIcon(p, textures), 0);
		addIcon(new BlockIcon(p, textures), 1);
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
	public void render(){ for(int i = 0; i<icons.length; i++)if(icons[i]!=null)icons[i].render(); }
	public void update(double time){ for(int i = 0; i<icons.length; i++)if(icons[i]!=null)icons[i].update(time); }
}