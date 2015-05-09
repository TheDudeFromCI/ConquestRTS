package wraithaven.conquest.client.BuildingCreator;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.BlockTextures;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.Pyramid;
import wraithaven.conquest.client.BuildingCreator.BlockPalette.BlockIcon;

public class IconManager{
	private final ArrayList<BlockIcon> icons = new ArrayList();
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
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
		addIcon(new BlockIcon(p, textures));
	}
	public void addIcon(BlockIcon icon){
		icon.itemSlot=icons.size();
		icons.add(icon);
	}
	public void render(){ for(int i = 0; i<icons.size(); i++)icons.get(i).render(); }
	public void update(double time){ for(int i = 0; i<icons.size(); i++)icons.get(i).update(time); }
}