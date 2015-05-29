package wraithaven.conquest.client.BuildingCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.ShapeType;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;

public class SaveSystem{
	public static void save(VoxelWorld world, File file){
		int x, y, z, i;
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			StringBuilder s = new StringBuilder();
			Block block;
			for(i=0; i<world.getIndexManager().getBlockCount(); i++){
				block=world.getIndexManager().getBlock(i);
				s.append(ShapeType.indexOf(block.shape));
				s.append(",");
				s.append(block.rotation.index);
				s.append(",");
				s.append(CubeTextures.encrypt(block.originalCubeTextures));
				s.append("B");
			}
			s.deleteCharAt(s.length()-1);
			s.append("A");
			for(x=0; x<BuildingCreator.WORLD_BOUNDS_SIZE; x++){
				for(y=1; y<BuildingCreator.WORLD_BOUNDS_SIZE; y++){
					for(z=0; z<BuildingCreator.WORLD_BOUNDS_SIZE; z++){
						s.append(world.getBlock(x, y, z));
						s.append(",");
					}
				}
			}
			s.deleteCharAt(s.length()-1);
			out.write(s.toString());
			out.close();
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}
}