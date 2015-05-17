package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.MipmapQuality;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.ShapeType;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;

public class IconManager{
	int selectedSlot;
	private final BlockIcon[] icons = new BlockIcon[20];
	{
		CubeTextures textures = new CubeTextures();
		Texture dirt = new Texture(new File(ClientLauncher.textureFolder, "Light Plank.png"), 4, MipmapQuality.HIGH);
		textures.xUp=dirt;
		textures.xUpRotation=0;
		textures.xDown=dirt;
		textures.xDownRotation=1;
		textures.yUp=dirt;
		textures.yUpRotation=3;
		textures.yDown=dirt;
		textures.yDownRotation=0;
		textures.zUp=dirt;
		textures.zUpRotation=3;
		textures.zDown=dirt;
		textures.zDownRotation=2;
		{
			addIcon(new BlockIcon(ShapeType.SHAPE_0.shape, textures, BlockRotation.ROTATION_0), 0);
			addIcon(new BlockIcon(ShapeType.SHAPE_1.shape, textures, BlockRotation.ROTATION_0), 1);
			addIcon(new BlockIcon(ShapeType.SHAPE_2.shape, textures, BlockRotation.ROTATION_0), 2);
			addIcon(new BlockIcon(ShapeType.SHAPE_3.shape, textures, BlockRotation.ROTATION_0), 3);
			addIcon(new BlockIcon(ShapeType.SHAPE_4.shape, textures, BlockRotation.ROTATION_0), 4);
			addIcon(new BlockIcon(ShapeType.SHAPE_5.shape, textures, BlockRotation.ROTATION_0), 5);
			addIcon(new BlockIcon(ShapeType.SHAPE_6.shape, textures, BlockRotation.ROTATION_0), 6);
			addIcon(new BlockIcon(ShapeType.SHAPE_7.shape, textures, BlockRotation.ROTATION_0), 7);
			addIcon(new BlockIcon(ShapeType.SHAPE_10.shape, textures, BlockRotation.ROTATION_0), 8);
			addIcon(new BlockIcon(ShapeType.SHAPE_9.shape, textures, BlockRotation.ROTATION_0), 9);
		}
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
	public void render(){ for(int i = 0; i<icons.length; i++)if(icons[i]!=null)icons[i].render(BlockIcon.getX(i), BlockIcon.getY(i), 0, 0, 0); }
	public BlockIcon getIcon(int id){ return icons[id]; }
}